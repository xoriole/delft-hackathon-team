package com.github.sofaid.gov.repository;

import com.github.sofaid.gov.data.db.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {

    Citizen findByBsn(String bsn);
    Citizen findByFirstnameAndLastnameAndDateOfBirth(String firstname, String lastname, Date date);
    List<Citizen> findAll();

}