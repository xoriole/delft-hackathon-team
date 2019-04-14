package com.github.sofaid.gov.repository;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.data.db.CitizenDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface DocumentRepository extends JpaRepository<CitizenDocument, Long> {

    List<CitizenDocument> findAllByBsn(String bsn);
    CitizenDocument findByDocumentId(String documentId);

}