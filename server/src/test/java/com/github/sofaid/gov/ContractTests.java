package com.github.sofaid.gov;

import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ContractTests {

    static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000L);
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    static final BigInteger INITIAL_WEI_VALUE = BigInteger.valueOf(4_300_000);

//    static final String CONTRACT_ADDRESS = "0x7780c86cBE38B63730EB2E5Bd5109327170FF126";
//    static final String WEB3_URL = "https://ropsten.infura.io/v3/b8c6bd5ce2da4453bf3bda071d2baa1a";
//    static final String PRIVATE_KEY = "6d333c75332974e7d7fb90fcff68629feafc1edb40c1a8f25e555ed5fc4523fc";
////    static final String PRIVATE_KEY = "29041e47bf115e3ec9685ced4d46c1ee914f528955be3f01f9e51077e824b260";
//    static final String ADDRESS = "0xD14E7706a1A50D7e3eA9A38031F04FE4864A42Ba";


 static final String CONTRACT_ADDRESS = "0x05418b77db326a0286c61023f6d71a80d3592de3";
    static final String WEB3_URL = "https://ganache.offlinepass.com";
    static final String PRIVATE_KEY = "89222F175DAA3C525B274DC1F98ECE855FCFD110DBA0587E1749C9F79E028DDB";
//    static final String ADDRESS = "0x5a37A4c1f8026a6729883baB7E655dcb565702CC";

    static String[] TEST_USERS = {
            "0x3fE953beb765522c87B8D21505a75601bd97DFBd",
            "0x5fb8c6c324c10968212807a3cbdf2f00f2dee40b",
            "0x69f48d03b6d2bacde12cdb9be803c863aba2360b",
            "0x9fac8957f0e36a9f6af94f4e2c22f8a787fab0d2",
            "0xb540d6a499c386d02807c2ac0ee9ee210191b708",
            "0xb95183b975112feef2fd4c9402c9a2a4810f1768",
            "0xa5281b9bc2b21ffa9539c53f14051f8005b8a0c4",
            "0xa706e27560fc15440e54b861e9066a8a8b05f18a",
            "0x9fd8db741f55f99e4c32196ced309fc71c9071fa"
    };



    public static void main(String[] args) throws Exception {

        String bankPrivateKey = "d54ceaf8cd688b915918cb7df0bd6d2157f96470539022790f87342e07241833";
        String publicKey = "";
        String WALLET_PASSWORD = "";

        String alicePrivateKey = "3e0d3f9bc6eac9a671be4334284818c84da714bbca32789e77f73ab4dbbf00a3";
        String bobPrivateKey = "f9491803f33d167660b43f8494f8fb2226a7e3049d7d70e018fa94f9165c44c1";
        String charliePrivateKey = "91842078efdac485fb38c81e0b7841e5897ba7d17042644764fce43147c4c4ae";

        Credentials BANK = Credentials.create(bankPrivateKey);

        Credentials ALICE = Credentials.create(alicePrivateKey);
        Credentials BOB = Credentials.create(bobPrivateKey);
        Credentials CHARLIE = Credentials.create(charliePrivateKey);

        Web3j web3j = Web3j.build(new HttpService(WEB3_URL));
        Credentials NODE = Credentials.create(PRIVATE_KEY);

        SofaIdAttestation contract = SofaIdAttestation.load(CONTRACT_ADDRESS, web3j, NODE, GAS_PRICE, GAS_LIMIT);

        List send = contract.getAttestations("0x9fd8db741f55f99e4c32196ced309fc71c9071fa").send();
        System.out.println(send);

//        for(int i=0; i<TEST_USERS.length; i++) {
//            try {
//                RemoteCall<TransactionReceipt> transactionReceiptRemoteCall = contract.signAttestation(TEST_USERS[i]);
//                TransactionReceipt transactionReceipt = transactionReceiptRemoteCall.send();
//                System.out.println("tx hash:" + transactionReceipt.getBlockHash());
//            }catch (Exception ex){
//                ex.printStackTrace();
//            }
//        }
//
//        List send2 = contract.getAttestations("0x5fb8c6c324c10968212807a3cbdf2f00f2dee40b").send();
//        System.out.println(send2);
    }
}
