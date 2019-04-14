package com.github.sofaid.gov.repository;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.data.db.LoginToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface LoginTokenRepository extends JpaRepository<LoginToken, Long> {

    @Override
    Optional<LoginToken> findById(Long aLong);

    Optional<LoginToken> findByToken(String token);
    List<LoginToken> findAll();

}