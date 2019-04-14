package com.github.sofaid.gov.repository;

import com.github.sofaid.gov.data.db.CitizenKeyPair;
import com.github.sofaid.gov.data.db.GovKeyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GovKeyPairRepository extends JpaRepository<GovKeyPair, Long> {

    List<GovKeyPair> findByBsn(String bsn);
    GovKeyPair findByGovMasterPublicKey(String govMasterPublicKey);

}