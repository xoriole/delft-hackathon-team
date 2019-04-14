package com.github.sofaid.app.crypto;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;

public class CryptoService {

    private KeyGenerator keyGenerator;

    public CryptoService(){
        X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
        String SEED_PREFIX = "SofaID Seed";
        this.keyGenerator = new KeyGenerator(curve, SEED_PREFIX);
    }

    public KeyGenerator.ExtendedKey generateMasterKeyPair(){
        return this.keyGenerator.createExtendedKey();
    }

    public KeyGenerator.ExtendedKey fromXPriv(String xpriv){
        try {
            return this.keyGenerator.parseExtendedKey(xpriv);
        } catch (KeyValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyGenerator.ExtendedKey fromXPub(String xpub){
        try {
            return this.keyGenerator.parseExtendedKey(xpub);
        } catch (KeyValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyGenerator.ECKeyPair getKey(String xpub, int index){
        try {
            KeyGenerator.ExtendedKey extendedKey = this.keyGenerator.parseExtendedKey(xpub);
            return extendedKey.getKey(index);
        } catch (KeyValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String signMessage(KeyGenerator.ECKeyPair keyPair, String message){
        try {
            return ByteUtils.toHex(keyPair.sign(message.getBytes()));
        } catch (KeyValidationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isValidSignature(String xpub, int index, String message, String signature){
        KeyGenerator.ECKeyPair key = this.getKey(xpub, index);
        if(key == null)
            return false;
        return key.verify(message.getBytes(), signature.getBytes());
    }

}
