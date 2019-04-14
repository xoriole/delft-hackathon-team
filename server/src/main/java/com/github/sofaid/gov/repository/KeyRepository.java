package com.github.sofaid.gov.repository;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.data.db.CitizenKeyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface KeyRepository extends JpaRepository<CitizenKeyPair, Long> {

    List<CitizenKeyPair> findByBsn(String bsn);
    CitizenKeyPair findByUserMasterPublicKey(String userMasterPublicKey);

}