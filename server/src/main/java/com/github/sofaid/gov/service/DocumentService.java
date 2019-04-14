package com.github.sofaid.gov.service;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.data.db.CitizenDocument;
import com.github.sofaid.gov.repository.CitizenRepository;
import com.github.sofaid.gov.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;


    public CitizenDocument findByDocumentId(String documentId) {
        return documentRepository.findByDocumentId(documentId);
    }
}