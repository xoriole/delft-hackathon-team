package com.github.sofaid.gov.crypto;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

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
        System.out.println("master:"+ekPrivate.serialize(true));
        System.out.println("first:"+ByteUtils.toHex(ekPrivate.getMaster().getPublic()));


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

    @Test
    public void testUserKeys() throws KeyValidationException {
        String[] xprivs = new String[]{
          "xprv9s21ZrQH143K3RhCf8ZuLMzpRsxbrHtNnES5JnWHtUgGH9Q2ePij4vAkoBNWqBeS8zHEnKrzzd8j5sei3vmYJd9wbMJGeiQ3tV5uxgwf3uE",
          "xprv9s21ZrQH143K2y6F6fVb7PjEV4StSTTgYYgmyAKEUVbUJpUpxEhvtscXf4f867AmWhynfiZd5JTobGgRoJQFQB2Faar3R5cu9sYTGZ4qSkd",
                "xprv9s21ZrQH143K43HC3H44hh7RUAMLZnPhsprGGZsrfZtLjyyz9jTgdASy6CNTNVmRTSaJfStVfgwupN7JM6VMMvy2kTKztg6r6UkMSBTtrYC"

        };

        X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
        String SEED_PREFIX = "SofaID Seed";
        KeyGenerator keyGenerator = new KeyGenerator(curve, SEED_PREFIX);

        System.out.println("0x11D4f65E2d4CD5964e399a0A1Bb558AE38B8E27d");

        for(int i=0; i<xprivs.length; i++){
            KeyGenerator.ExtendedKey extendedKey = keyGenerator.parseExtendedKey(xprivs[i]);
            String address = ByteUtils.toHex(extendedKey.getMaster().getAddress());
            String privateKey = extendedKey.getMaster().getPrivateKey().toString(16);
            System.out.println(privateKey);
            System.out.println(address);
            Credentials cs = Credentials.create(privateKey);
            System.out.println(cs.getAddress());
        }
    }
}
