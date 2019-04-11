package com.github.sofaid.gov.service;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.repository.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CitizenService {

    @Autowired
    private CitizenRepository citizenRepository;

    public Citizen getCitizenByBSN(String bsn) {
        return citizenRepository.findByBsn(bsn);
    }

    public List<Citizen> getAllCitizens(){
        return citizenRepository.findAll();
    }

}