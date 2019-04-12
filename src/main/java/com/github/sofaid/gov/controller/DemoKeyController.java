package com.github.sofaid.gov.controller;

import com.github.sofaid.gov.crypto.ByteUtils;
import com.github.sofaid.gov.crypto.KeyGenerator;
import com.github.sofaid.gov.crypto.KeyValidationException;
import com.github.sofaid.gov.data.dto.enroll.LoginRequest;
import com.github.sofaid.gov.data.dto.enroll.LoginResponse;
import com.github.sofaid.gov.data.dto.enroll.RegisterRequest;
import com.github.sofaid.gov.service.CryptoService;
import com.github.sofaid.gov.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Hash;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/demo")
public class DemoKeyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoKeyController.class.getName());
    private static final String[] BLOCKCHAIN_IDS = new String[]{
            "BlockchainID1",
            "BlockchainID2"
    };
    private static final String[] BSN_IDS = new String[]{
            "1100.00.101",
            "1100.00.102"
    };
    private static final String[] DEMO_KEYS = new String[]{
            "xprv9s21ZrQH143K3RhCf8ZuLMzpRsxbrHtNnES5JnWHtUgGH9Q2ePij4vAkoBNWqBeS8zHEnKrzzd8j5sei3vmYJd9wbMJGeiQ3tV5uxgwf3uE",
            "xprv9s21ZrQH143K29wk7drtEY3iW6dofN3x7MyBDm5TDZyFLZpy6oqg6remKVq2kfdznwwemKagchUTsiUQe7BuVpAa64BhZ8fn2DepmQHePKa"
    };

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private UserService userService;

    @GetMapping("/gen")
    public String generateKeyPair() {
        return this.cryptoService.generateMasterKeyPair().serialize(true);
    }

    @GetMapping("/gen/public")
    public String generatePublicKeyPair() {
        return this.cryptoService.generateMasterKeyPair().getReadOnly().serialize(true);
    }

    @GetMapping("/gen/users")
    public String generateUsers(){
//        RegisterRequest registerRequest = genRegisterRequest();
//        userService.registerUser(registerRequest);
        return "ok";
    }

    @GetMapping("/gen/register")
    public List<RegisterRequest> genRegisterRequest() {
        List<RegisterRequest> requests = new ArrayList<>();
        for(int i=0;i<2;i++) {
            String blockchainId = BLOCKCHAIN_IDS[i];
            String bsn = BSN_IDS[i];
            Long timestamp = System.currentTimeMillis();

            RegisterRequest request = new RegisterRequest();
            request.setBlockchainId(blockchainId);
            request.setBsn(bsn);
            request.setTimestamp(timestamp);

            try {
//                KeyGenerator.ExtendedKey rootKey = cryptoService.generateMasterKeyPair();
//            String xpriv = "xprv9s21ZrQH143K4GG2rEJQat2pBjmeKKoprvWKih73Jwv5zTzGcFNrg33fD1CuVckTbZuWb1aJN2oXPNwkJaSY5BZtHnoKwF8JoHLL9DMdqZy";
                KeyGenerator.ExtendedKey rootKey = cryptoService.fromXPriv(DEMO_KEYS[i]);
                System.out.println("root key:" + rootKey.serialize(true));

                KeyGenerator.ExtendedKey extendedKey = rootKey.generateKey(1);
                System.out.println("user registration master private key:" + extendedKey.serialize(true));
                request.setMasterPublicKey(extendedKey.getReadOnly().serialize(true));
                System.out.println("user registration master public key:" + extendedKey.getReadOnly().serialize(true));

                KeyGenerator.ECKeyPair key = extendedKey.getKey(0);
                String message = blockchainId + bsn + timestamp;
                String hash = Hash.sha3String(message);

                byte[] signature = key.sign(hash.getBytes());
                String sigHex = ByteUtils.toHex(signature);
                request.setSignature(ByteUtils.toHex(signature));

                // verify that it is ok
                String rootKeyStr = extendedKey.serialize(true);
                KeyGenerator.ExtendedKey extendedKey1 = cryptoService.fromXPriv(rootKeyStr);
                KeyGenerator.ECKeyPair signingKey = extendedKey1.getKey(0);
                boolean verify = signingKey.verify(hash.getBytes(), ByteUtils.fromHex(sigHex));
                System.out.println("verify:" + verify);


            } catch (KeyValidationException e) {
                e.printStackTrace();
            }
            requests.add(request);
            userService.registerUser(request);
        }
        return requests;
    }

    @PostMapping("/gen/verify")
    public String verifyRequest(@RequestBody RegisterRequest request, String xpriv){
        String message = request.getBlockchainId() + request.getBsn() + request.getTimestamp();
        String hash = Hash.sha3String(message);
        byte[] sig = ByteUtils.fromHex(request.getSignature());

        KeyGenerator.ExtendedKey rootKey = cryptoService.fromXPriv(xpriv);
        try {
            KeyGenerator.ECKeyPair signingKey = rootKey.getKey(0);
            boolean verify = signingKey.verify(hash.getBytes(), sig);
            return "ok";
        } catch (KeyValidationException e) {
            e.printStackTrace();
        }
        System.out.println("Not verified");
        return "Not verified";
    }

    @GetMapping("/gen/login")
    public LoginRequest genLoginRequest(){

        String loggerId = BLOCKCHAIN_IDS[0];
        String delegatorId = BLOCKCHAIN_IDS[1];
        Long timestamp = System.currentTimeMillis();
        String loginToken = "LOGIN_TOKEN_1234";

        try {
            KeyGenerator.ExtendedKey loggerRootKey = cryptoService.fromXPriv(DEMO_KEYS[0]).generateKey(1);
            KeyGenerator.ExtendedKey delegatorRootKey = cryptoService.fromXPriv(DEMO_KEYS[1]).generateKey(1);

            KeyGenerator.ECKeyPair loggerKey = loggerRootKey.getKey(0);
            System.out.println("logger pub key:"+ByteUtils.toHex(loggerKey.getPublic()));

            int delegatorIndex = 1;
            KeyGenerator.ECKeyPair delegatorKey = delegatorRootKey.getKey(1);
            System.out.println("delegator pub key:"+ByteUtils.toHex(delegatorKey.getPublic()));

            LoginRequest request = new LoginRequest();

            String message = loginToken+timestamp;
            String messageHashStr = Hash.sha3String(message);
            System.out.println("messageStr:"+messageHashStr);
            byte[] loggerSig = loggerKey.sign(messageHashStr.getBytes());

            String delegatorMessage = ByteUtils.toHex(loggerKey.getPublic())+delegatorIndex;
            String delegatorMessageHashStr = Hash.sha3String(delegatorMessage);
            byte[] delegatorSig = delegatorKey.sign(delegatorMessageHashStr.getBytes());

            request.setLoggerId(loggerId);
            request.setDelegatorId(delegatorId);
            request.setDelegatorKeyIndex(delegatorIndex);
            request.setLoggerSignature(ByteUtils.toHex(loggerSig));
            request.setDelegatorSignature(ByteUtils.toHex(delegatorSig));
            request.setLoginToken(loginToken);
            request.setTimestamp(timestamp);

            return request;
        } catch (KeyValidationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
