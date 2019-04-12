package com.github.sofaid.gov.controller;

import com.github.sofaid.gov.crypto.ByteUtils;
import com.github.sofaid.gov.crypto.KeyGenerator;
import com.github.sofaid.gov.crypto.KeyValidationException;
import com.github.sofaid.gov.data.db.LoginToken;
import com.github.sofaid.gov.data.db.ServiceUser;
import com.github.sofaid.gov.data.dto.enroll.*;
import com.github.sofaid.gov.data.enums.EnrollStatus;
import com.github.sofaid.gov.service.CryptoService;
import com.github.sofaid.gov.service.TrustService;
import com.github.sofaid.gov.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Hash;

import javax.xml.ws.Service;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthorityController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityController.class.getName());

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private TrustService trustService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<ServiceUser> listUsers(){
        return userService.findAll();
    }

    @PostMapping("/register")
    public RegisterResponse enroll(@RequestBody RegisterRequest request) {
        // Check that the signature is valid
        String message = request.getBlockchainId() + ":" + request.getBsn() + ":" + request.getTimestamp();
        boolean signatureValid = cryptoService.isValidSignature(request.getMasterPublicKey(), 0, message, request.getSignature());
        if (signatureValid) {
            return RegisterResponse.invalidParameters("Invalid request; Signature is not valid");
        }
        // Get trust from the blockchain
        int trustLevel = trustService.getTrustLevel(request.getBlockchainId());
        if (trustLevel < 50) {
            return RegisterResponse.invalidParameters("Insufficient reputation to register");
        }

        // Register the user
        try {
            userService.registerUser(request);
            return new RegisterResponse(EnrollStatus.Success, "OK", "N/A");
        } catch (Exception ex) {
            LOGGER.error("Failed to register the user with ID:{}", request.getBlockchainId());
            ex.printStackTrace();
            return RegisterResponse.invalidParameters("Failed to register the user");
        }
    }

    @GetMapping("/login")
    public String login(){
        // create a login token and send it to the user interface
        return userService.createLoginToken();
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        // Check that the token is valid
        Optional<LoginToken> loginTokenOpt = userService.findLoginToken(request.getLoginToken());
        if(!loginTokenOpt.isPresent()){
            return LoginResponse.invalidParameters("Invalid login token");
        }

        // get logger public key from logger id
        Optional<ServiceUser> loggerOpt = userService.findByActiveUserByBlockchainId(request.getLoggerId());
        if(!loggerOpt.isPresent()){
            return LoginResponse.invalidParameters("Invalid logger id");
        }
        ServiceUser loggingUser = loggerOpt.get();

        // check if logger signature is correct
        String message = request.getLoginToken()+request.getTimestamp();
        String messageHashStr = Hash.sha3String(message);
        System.out.println("verification hash in login:"+messageHashStr);
        KeyGenerator.ExtendedKey loggerKey = cryptoService.fromXPub(loggingUser.getMasterPublicKey());
        try {
            System.out.println("pub key:"+ByteUtils.toHex(loggerKey.getKey(0).getPublic()));
            boolean verify = loggerKey.getKey(0).verify(messageHashStr.getBytes(), ByteUtils.fromHex(request.getLoggerSignature()));
            if(!verify){
                return LoginResponse.invalidParameters("Logger signature is not valid");
            }

            // get delegator public key from delegator id
            Optional<ServiceUser> delegatorOpt = userService.findByActiveUserByBlockchainId(request.getDelegatorId());
            if(!delegatorOpt.isPresent()){
                return LoginResponse.invalidParameters("Invalid logger Id");
            }
            ServiceUser delegatorUser = delegatorOpt.get();
            KeyGenerator.ExtendedKey delegatorKey = cryptoService.fromXPub(delegatorUser.getMasterPublicKey());

            // check if delegator signature is correct
            String delegatorMessage = ByteUtils.toHex(loggerKey.getKey(0).getPublic())+request.getDelegatorKeyIndex();
            String delegatorMessageHashStr = Hash.sha3String(delegatorMessage);
            boolean verify2 = delegatorKey.getKey(request.getDelegatorKeyIndex())
                    .verify(delegatorMessageHashStr.getBytes(), ByteUtils.fromHex(request.getDelegatorSignature()));
            if(!verify2){
                return LoginResponse.invalidParameters("Delegator signature is not valid");
            }

            return new LoginResponse("OK", "Login successful!");
        } catch (KeyValidationException e) {
            e.printStackTrace();
        }
        return new LoginResponse("FAILED", "Login failed for unknown reason");
    }
}
