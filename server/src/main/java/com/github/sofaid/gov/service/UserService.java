package com.github.sofaid.gov.service;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.data.db.LoginToken;
import com.github.sofaid.gov.data.db.ServiceUser;
import com.github.sofaid.gov.data.dto.enroll.RegisterRequest;
import com.github.sofaid.gov.data.dto.enroll.RegisterResponse;
import com.github.sofaid.gov.repository.CitizenRepository;
import com.github.sofaid.gov.repository.LoginTokenRepository;
import com.github.sofaid.gov.repository.ServiceUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    @Autowired
    private ServiceUserRepository serviceUserRepository;
    @Autowired
    private LoginTokenRepository loginTokenRepository;

    public Optional<ServiceUser> findByActiveUserByBsn(String bsn) {
        return serviceUserRepository.findByBsnAndActive(bsn, true);
    }

    public Optional<ServiceUser> findByActiveUserByBlockchainId(String blockchainId) {
        return serviceUserRepository.findByBlockchainIdAndActive(blockchainId, true);
    }

    public ServiceUser registerUser(RegisterRequest request){
        // check if the user already exists
        Optional<ServiceUser> existingUserOpt = serviceUserRepository.findByBsnAndActive(request.getBsn(), true);
        if(existingUserOpt.isPresent()){
            ServiceUser existingUser = existingUserOpt.get();
            existingUser.setActive(false);
            serviceUserRepository.save(existingUser);
        }

        // Register as new user and make it active
        ServiceUser user = new ServiceUser();
        user.setBlockchainId(request.getBlockchainId());
        user.setBsn(request.getBsn());
        user.setMasterPublicKey(request.getMasterPublicKey());
        user.setRegisterTs(System.currentTimeMillis());
        user.setSignature(request.getSignature());
        user.setActive(true);
        serviceUserRepository.save(user);
        return user;
    }

    public List<ServiceUser> findAll() {
        return serviceUserRepository.findAll();
    }

    public Optional<LoginToken> findLoginToken(String token){
        return loginTokenRepository.findByToken(token);
    }

    public String createLoginToken(){
        String token = UUID.randomUUID().toString();
        LoginToken loginToken = new LoginToken();
        loginToken.setToken(token);
        loginToken.setTimestamp(System.currentTimeMillis());
        loginToken.setUserId("");
        loginTokenRepository.save(loginToken);
        return token;
    }
}