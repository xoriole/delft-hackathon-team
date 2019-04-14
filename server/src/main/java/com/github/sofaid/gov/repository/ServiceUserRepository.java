package com.github.sofaid.gov.repository;

import com.github.sofaid.gov.data.db.ServiceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ServiceUserRepository extends JpaRepository<ServiceUser, Long> {

    List<ServiceUser> findAllByBsn(String bsn);
    Optional<ServiceUser> findByBsnAndActive(String bsn, boolean active);
    Optional<ServiceUser> findByBlockchainIdAndActive(String blockchainId, boolean active);
    Optional<ServiceUser> findByMasterPublicKey(String masterPublicKey);
    List<ServiceUser> findAll();

}