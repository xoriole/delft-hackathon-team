package com.github.sofaid.app.ethereum;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import java8.util.concurrent.CompletableFuture;
import java8.util.function.Consumer;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ContractService {
    static final BigInteger GAS_PRICE = BigInteger.valueOf(20_000L);
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    static final BigInteger INITIAL_WEI_VALUE = BigInteger.valueOf(4_300_000);

//    static final String CONTRACT_ADDRESS = "0x05418b77db326a0286c61023f6d71a80d3592de3"; // ganache
//    static final String CONTRACT_ADDRESS = "0x824d5d5b46f92094eaadb708a3ba350c56fa415d"; // ganache
//    static final String CONTRACT_ADDRESS = "0xee9c644ad94f5785797d561ad984e7a9905269e9"; // rinkeby
    static final String CONTRACT_ADDRESS = "0xad5fc729aa1bebea0adab4b38fe2dc3453fbcfbd"; // ganache - june 27
//    static final String CONTRACT_ADDRESS = "0x7780c86cBE38B63730EB2E5Bd5109327170FF126";
    static final String WEB3_URL = "https://ganache.offlinepass.com";
//    static final String WEB3_URL = "http://node.crypteli.com:9998";
//    static final String WEB3_URL = "https://ropsten.infura.io/v3/b8c6bd5ce2da4453bf3bda071d2baa1a";
    //    static final String PRIVATE_KEY = "6d333c75332974e7d7fb90fcff68629feafc1edb40c1a8f25e555ed5fc4523fc";
    static final String PRIVATE_KEY = "29041e47bf115e3ec9685ced4d46c1ee914f528955be3f01f9e51077e824b260";
    static final String ADDRESS = "0xD14E7706a1A50D7e3eA9A38031F04FE4864A42Ba";

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

    Web3j web3j;
    SofaIdAttestation contract;
    Credentials credentials;

    public ContractService(){
        web3j = Web3j.build(new HttpService(WEB3_URL));
    }

    public void initContract(String privateKey){
        Credentials NODE = Credentials.create(privateKey);
        initContract(NODE);
    }

    public void initContract(Credentials credentials){
        System.out.println("contract deployer address:"+credentials.getAddress());
        this.credentials = credentials;
        contract = SofaIdAttestation.load(CONTRACT_ADDRESS, web3j, credentials, GAS_PRICE, GAS_LIMIT);
    }

    public Credentials getCredentials(){
        return credentials;
    }

    public BigDecimal getBalance(){
        return getBalance(credentials.getAddress());
    }

    public BigDecimal getBalance(String address){
        EthGetBalance ethGetBalance = null;
        try {
            ethGetBalance = web3j
                    .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();

            return Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;

    }

    public Observable<List> getAttestations(String address){
        return rx.Observable.create(new rx.Observable.OnSubscribe<List>() {
            @Override
            public void call(final Subscriber<? super List> subscriber) {
                try {
                    contract.getAttestations(address).sendAsync().thenAccept(new Consumer<List>() {
                        @Override
                        public void accept(List list) {
                            subscriber.onNext(list);
                            subscriber.onCompleted();
                        }
                    });
                }catch (Exception ex){
                    subscriber.onError(ex);
                }
            }
        });
    }

//
    public Observable<Boolean> signAttestation(String address){
        return rx.Observable.create(new rx.Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                try {
                    contract.signAttestation(address).sendAsync().thenAccept(new Consumer<TransactionReceipt>() {
                        @Override
                        public void accept(TransactionReceipt transactionReceipt) {
                            System.out.println("attestation transaction completed!");
                            System.out.println("tx hash:"+transactionReceipt.getBlockHash());
                            subscriber.onNext(true);
                            subscriber.onCompleted();
                        }
                    });
                }catch (Exception ex){
                    ex.printStackTrace();
                    subscriber.onError(ex);
                }
            }
        });
    }

//    public Observable<Boolean> signAttestation2(String address){
//        CompletableFuture<TransactionReceipt> txFuture = contract.signAttestation(address).sendAsync();
//        RemoteCall<TransactionReceipt> remoteCall = contract.signAttestation(address);
//        return Observable.from(txFuture, Schedulers.io())
//                .doOnSubscribe(disposable -> txFuture.run())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<TransactionReceipt>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onNext(TransactionReceipt transactionReceipt) {
//                        System.out.println("attestation transaction completed!");
////                        subscriber.onNext(true);
////                        subscriber.onCompleted();
//                    }
//                });
//        return rx.Observable.create(new rx.Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(final Subscriber<? super Boolean> subscriber) {
//                try {
//                    System.out.println("Calling sign attestation for address:"+ address);
//                    contract.signAttestation(address).sendAsync().completeAsync(() -> {
//                        System.out.println("attestation transaction completed!");
//                        subscriber.onNext(true);
//                        subscriber.onCompleted();
//                    });
//                    contract.signAttestation(address).sendAsync().thenAccept(new Consumer<TransactionReceipt>() {
//                        @Override
//                        public void accept(TransactionReceipt transactionReceipt) {
//                            System.out.println("attestation transaction completed!");
//                            subscriber.onNext(true);
//                            subscriber.onCompleted();
//                        }
//                    });
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                    subscriber.onError(ex);
//                }
//            }
//        });
//    }

}
