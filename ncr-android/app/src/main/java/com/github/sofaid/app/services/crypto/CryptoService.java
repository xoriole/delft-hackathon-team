package com.github.sofaid.app.services.crypto;

import com.github.sofaid.app.exceptions.NcrException;

//import org.bitcoinj.crypto.MnemonicCode;
//import org.bitcoinj.crypto.MnemonicException;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sandippandey on 18/06/2017.
 */

public class CryptoService {
    private static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    private static final String RANDOM_NUMBER_ALGORITHM_PROVIDER = "SUN";

    @Inject
    public CryptoService(){}

    public String newPrivateKey(){
        SecureRandom secureRandom;
        try {
            secureRandom
                    = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM, RANDOM_NUMBER_ALGORITHM_PROVIDER);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            secureRandom = new SecureRandom();
        }

        // create a random private key
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String privateKey = Numeric.toHexString(randomBytes);
        return privateKey;
    }

    public String getPrivateKey(Credentials credentials){
        return credentials.getEcKeyPair().getPrivateKey().toString(16);
    }

    public Credentials fromSeedWords(List<String> seeds) throws NcrException{
//        try {
//            byte[] privateEntropy = MnemonicCode.INSTANCE.toEntropy(seeds);
//            String privateKey = Numeric.toHexString(privateEntropy);
//            return Credentials.create(privateKey);
//        } catch (MnemonicException.MnemonicLengthException | MnemonicException.MnemonicWordException | MnemonicException.MnemonicChecksumException ex) {
//            throw new NcrException("Could not generate private key from seed", ex.getMessage());
//        }
        return null;
    }

    public List<String> getSeedWords(String privateKey) throws NcrException{
//        try {
//            byte[] privateEntropy = Numeric.hexStringToByteArray(privateKey);
//            List<String> seedwords = MnemonicCode.INSTANCE.toMnemonic(privateEntropy);
//            return seedwords;
//        } catch (MnemonicException.MnemonicLengthException ex) {
//            throw new NcrException("Could not generate seed words", ex.getMessage());
//        }
        return new ArrayList<>();
    }

    public List<String> getSeedWords(Credentials credentials) throws NcrException {
        return getSeedWords(getPrivateKey(credentials));
    }
}
