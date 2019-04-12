package com.github.sofaid.gov.service;

import com.github.sofaid.gov.crypto.ByteUtils;
import com.github.sofaid.gov.crypto.KeyGenerator;
import com.github.sofaid.gov.crypto.KeyValidationException;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.springframework.stereotype.Service;

@Service
public class TrustService {

    public int getTrustLevel(String publicKey){
        // replace with blockchain call and compute
        return 100;
    }

}
