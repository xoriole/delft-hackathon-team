package com.github.sofaid.gov.repository;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.data.db.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByBsn(String bsn);
    List<Enrollment> findByDocumentId(String documentId);
    List<Enrollment> findAll();

}