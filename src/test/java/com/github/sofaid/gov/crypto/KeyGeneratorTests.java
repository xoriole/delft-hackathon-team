package com.github.sofaid.gov.crypto;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class KeyGeneratorTests {
    private final SecureRandom random = new SecureRandom();

    @BeforeClass
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void testGenerator() throws KeyValidationException {
        X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
        String BITCOIN_SEED = "Bitcoin seed";
        KeyGenerator keyGenerator = new KeyGenerator(curve, BITCOIN_SEED);

        KeyGenerator.ExtendedKey ekPrivate = keyGenerator.createExtendedKey();
        KeyGenerator.ExtendedKey ekPublic = keyGenerator.publicExtendedKey(ekPrivate);


        for (int j = 0; j < 20; j++) {

            KeyGenerator.ECKeyPair fullControl = ekPrivate.getKey(j);
            KeyGenerator.ECKeyPair readOnly = ekPublic.getKey(j);

            assertTrue(Arrays.equals(fullControl.getPublic(), readOnly.getPublic()));
            assertTrue(Arrays.equals(fullControl.getAddress(), readOnly.getAddress()));

            byte[] toSign = new byte[100];
            random.nextBytes(toSign);

            byte[] signature = fullControl.sign(toSign);
            assertTrue(readOnly.verify(toSign, signature));

        }
    }

    @Test
    public void testECDSA() throws KeyValidationException {
        X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
        String BITCOIN_SEED = "Bitcoin seed";
        KeyGenerator keyGenerator = new KeyGenerator(curve, BITCOIN_SEED);

        KeyGenerator.ECKeyPair key = keyGenerator.createECKeyPair(true);
        byte[] data = new byte[32];
        random.nextBytes(data);
        byte[] signature = key.sign(data);
        for (int i = 0; i < 100; ++i) {
            assertTrue(key.verify(data, signature));
        }
    }
}
