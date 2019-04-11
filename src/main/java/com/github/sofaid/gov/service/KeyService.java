package com.github.sofaid.gov.service;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.data.db.GovKeyPair;
import com.github.sofaid.gov.repository.CitizenRepository;
import com.github.sofaid.gov.repository.GovKeyPairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class KeyService {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private GovKeyPairRepository govKeyPairRepository;

    public Citizen getCitizenByBSN(String bsn) {
        return citizenRepository.findByBsn(bsn);
    }

    public List<Citizen> getAllCitizens(){
        return citizenRepository.findAll();
    }

    public GovKeyPair getGovKeyPair(String govMasterPublicKey){
        return govKeyPairRepository.findByGovMasterPublicKey(govMasterPublicKey);
    }

}