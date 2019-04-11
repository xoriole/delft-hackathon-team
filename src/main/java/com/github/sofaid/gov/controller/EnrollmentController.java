package com.github.sofaid.gov.controller;

import com.github.sofaid.gov.data.db.CitizenDocument;
import com.github.sofaid.gov.data.db.GovKeyPair;
import com.github.sofaid.gov.data.dto.enroll.AcknowledgeRequest;
import com.github.sofaid.gov.data.dto.enroll.EnrollRequest;
import com.github.sofaid.gov.data.dto.enroll.EnrollResponse;
import com.github.sofaid.gov.data.enums.EnrollStatus;
import com.github.sofaid.gov.service.CryptoService;
import com.github.sofaid.gov.service.DocumentService;
import com.github.sofaid.gov.service.EnrollmentService;
import com.github.sofaid.gov.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enroll")
public class EnrollmentController {
    public static final long FIVE_MINS = 5 * 60 * 1000L;

    @Autowired
    private KeyService keyService;

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private DocumentService documentService;


    @PostMapping("/begin")
    public EnrollResponse begin(@RequestBody EnrollRequest request){
        // Check if the request contains the valid document identity
        CitizenDocument document = documentService.findByDocumentId(request.getDocumentId());
        if(document == null){
            return EnrollResponse.invalidParameters("Invalid request; Document Id is invalid");
        }

        // Check if timestamp in request is recent
        long currentTime = System.currentTimeMillis();
        if(currentTime - request.getTimestamp() > FIVE_MINS){
            return EnrollResponse.invalidParameters("Invalid request; Request timestamp is too old");
        }

        // Check signature if it is valid
        String message = request.getDocumentType()+":"+request.getDocumentId()+":"+request.getTimestamp();
        boolean signatureValid = cryptoService.isValidSignature(request.getUserMasterPublicKey(), 0, message, request.getSignature());
        if(signatureValid){
            return EnrollResponse.invalidParameters("Invalid request; Signature is not valid");
        }

        // Check if government public key is correct if it exists
        boolean reEnroll = false;
        String govMasterPublicKey = request.getGovMasterPublicKey();
        if(govMasterPublicKey != null && !govMasterPublicKey.isEmpty()){
            reEnroll = true;
            GovKeyPair govKeyPair = keyService.getGovKeyPair(govMasterPublicKey);
            if(govKeyPair == null){
                return EnrollResponse.invalidParameters("Invalid request; Government master public key is invalid");
            }
        }

        // Register the enrollment
        enrollmentService.register(request, document.getBsn(), reEnroll);

        // Send the enroll response with the challenge
        EnrollResponse successResponse = new EnrollResponse();
        successResponse.setStatus(EnrollStatus.Success);
        if(reEnroll)
            successResponse.setMessage("Please sign and submit the challenge");
        else
            successResponse.setMessage("Please go to your resident municipality and scan the QR code");
        successResponse.setChallenge("Challenge");
        return successResponse;
    }

    @GetMapping("/scan")
    public EnrollResponse scan(@RequestParam String bsn){
        return null;
    }

    public EnrollResponse acknowledge(@RequestBody AcknowledgeRequest _request){
        return null;
    }

    public void reEnroll(){}
    public void disable(){}
}
