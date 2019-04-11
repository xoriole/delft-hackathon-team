package com.github.sofaid.gov.service;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.data.db.Enrollment;
import com.github.sofaid.gov.data.dto.enroll.EnrollRequest;
import com.github.sofaid.gov.repository.CitizenRepository;
import com.github.sofaid.gov.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EnrollmentService {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Citizen getCitizenByBSN(String bsn) {
        return citizenRepository.findByBsn(bsn);
    }

    public List<Citizen> getAllCitizens(){
        return citizenRepository.findAll();
    }

    public void register(EnrollRequest request, String bsn, boolean reEnroll) {
        Enrollment enrollment = new Enrollment();
        enrollment.setBsn(bsn);
        enrollment.setDocumentId(request.getDocumentId());
        enrollment.setDocumentType(request.getDocumentType());
        enrollment.setUserMasterPublicKey(request.getUserMasterPublicKey());
        enrollment.setGovMasterPublicKey(request.getGovMasterPublicKey());
        enrollment.setCreatedTs(request.getTimestamp());
        enrollment.setReEnroll(reEnroll);
        enrollmentRepository.save(enrollment);
    }
}