package com.github.sofaid.app.services.core;

import android.util.Log;

import com.github.sofaid.app.constants.SignaturePrefix;
import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.helpers.TypeEncoder;
import com.github.sofaid.app.models.db.Account;
import com.github.sofaid.app.models.enums.AccountCategory;
import com.github.sofaid.app.models.internal.SignatureData;
import com.github.sofaid.app.models.internal.SignedToken;
import com.github.sofaid.app.models.remote.AuthRequest;
import com.github.sofaid.app.models.remote.SignupRequest;
import com.github.sofaid.app.services.crypto.CryptoService;
import com.github.sofaid.app.services.crypto.SignatureService;
import com.github.sofaid.app.services.remote_api.AccountApiService;
import com.github.sofaid.app.services.remote_api.EnrollmentApiService;
import com.github.sofaid.app.services.repository.DaoService;
import com.github.sofaid.app.ui.signup.SignupForm;
import com.github.sofaid.app.utils.ErrorMessageUtil;
import com.github.sofaid.app.utils.DateTimeUtil;
import com.github.sofaid.app.utils.SequentialObservable;
import com.github.sofaid.app.utils.StringUtil;
import com.google.gson.Gson;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by robik on 6/14/17.
 */

public class SignupService {
    private final String TAG = this.getClass().getName();

    private final DaoService daoService;
    private final AccountApiService accountApiService;
    private final EnrollmentApiService enrollmentApiService;

    private final CryptoService cryptoService;
    private final SignatureService signatureService;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public SignupService(DaoService daoService, AccountApiService accountApiService,
                         CryptoService cryptoService, SignatureService signatureService,
                         PreferencesHelper preferencesHelper, EnrollmentApiService enrollmentApiService) {
        this.daoService = daoService;
        this.accountApiService = accountApiService;
        this.cryptoService = cryptoService;
        this.signatureService = signatureService;
        this.preferencesHelper = preferencesHelper;
        this.enrollmentApiService = enrollmentApiService;
    }

    /**
     * Generates an {@link Account} using values from {@link SignupForm} and other fields including ncrAddress,
     * calls remote API to create new {@link Account} and saves it to database
     *
     * @param signupForm
     * @return
     */
    public Observable<Account> signup(final SignupForm signupForm) {
        SignupRequest signupRequest = new SignupRequest();
        populateSignupRequest(signupForm, signupRequest);

        List<Observable> observableList = new ArrayList<>();
        observableList.add(validateSignupFormAndGetObservable(signupForm));
        observableList.add(signup(signupRequest));

        SequentialObservable sequentialObservable = new SequentialObservable();
        return sequentialObservable.observe(observableList);
    }

    public Observable<String> login(String otp, String sessionId){
        final AuthRequest request = new AuthRequest();
        request.setBsn(preferencesHelper.getBSN());
        request.setTotpToken(otp);
        request.setSessionToken(sessionId);

        System.out.println("auth request:"+ request);
        System.out.println(new Gson().toJson(request));

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {

                enrollmentApiService.login(request)
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                                subscriber.onNext("Login successful");
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e, "Could not create account: ");
                                String errorMessage = ErrorMessageUtil.getUserReadableApiErrorMsg(e);
                                subscriber.onError(new RuntimeException(errorMessage));
                            }

                            @Override
                            public void onNext(Void aVoid) {

                            }
                        });
            }
        });
    }

    public Observable<String> testPing(){

        System.out.println("auth ping");

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {

                enrollmentApiService.ping()
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                                subscriber.onNext("Ping successful");
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e, "Could not ping server");
                                String errorMessage = ErrorMessageUtil.getUserReadableApiErrorMsg(e);
                                subscriber.onError(new RuntimeException(errorMessage));
                            }

                            @Override
                            public void onNext(Void aVoid) {

                            }
                        });
            }
        });
    }





    /**
     * Copies fields of {@link SignupForm} to {@link SignupRequest}
     *
     * @param signupForm
     * @param request
     */
    public void populateSignupRequest(SignupForm signupForm, SignupRequest request) {
        // obtain the ncr address from the private key
        String privateKey = generatePrivateKeyIfNotExists();
        Credentials credentials = Credentials.create(privateKey);
        String address = credentials.getAddress();

        request.setNcrAddress(address);
        request.setCategory(AccountCategory.INDIVIDUAL.getId());
        request.setTimestamp(new Date().getTime());
        request.setUsername(signupForm.getUsername());
        request.setFirstName(signupForm.getFirstName());
        request.setLastName(signupForm.getLastName());
        String dobStr = DateTimeUtil.toYYYYMMdd(signupForm.getDateOfBirth());
        request.setDateOfBirth(dobStr);
        request.setGender(signupForm.getGender());
        request.setMobileNumber(signupForm.getMobileNumber());
        request.setEmailAddress(signupForm.getEmailAddress());

        //SignedToken signedToken = populateSignedToken(request);
        SignedToken signedToken = createSignupToken(request, privateKey);
        request.setSignedToken(signedToken);
    }

    public SignedToken createSignupToken(SignupRequest request, String privateKey) {

        try {
            Address address = new Address(request.getNcrAddress());
            Uint256 timestamp = new Uint256(request.getTimestamp());
            String toHash = Numeric.toHexStringNoPrefix(SignaturePrefix.SIGNUP.getBytes())
                    + address.getValue() //.toString(16)
                    + TypeEncoder.encode(timestamp);
            toHash = Hash.sha3(toHash);

            SignatureData signatureData = signatureService.signMessage(toHash, privateKey);

            SignedToken signedToken = new SignedToken();
            signedToken.setToken(SignaturePrefix.SIGNUP);
            signedToken.setTimestamp(request.getTimestamp());
            signedToken.setV(signatureData.getV());
            signedToken.setR(Numeric.toHexString(signatureData.getR()));
            signedToken.setS(Numeric.toHexString(signatureData.getS()));

            return signedToken;
        } catch (Exception ex) {
            Log.e(TAG, "Exception creating signup token",ex);
        }
        return null;
    }

//    /**
//     * TODO: Implement this function!
//     * Populates {@link SignedToken} object
//     *
//     * @param request
//     */
//    public SignedToken populateSignedToken(SignupRequest request) {
//        SignedToken signedToken = new SignedToken();
//        signedToken.setToken("token:" + request.getNcrAddress());
//        signedToken.setV("v:" + request.getNcrAddress());
//        signedToken.setR("r");
//        signedToken.setS("s");
//        return signedToken;
//    }

    /**
     * Gets a {@link Observable} which when subscribed will validate the {@link SignupForm}
     *
     * @param signupForm
     * @return
     */
    public Observable<Void> validateSignupFormAndGetObservable(final SignupForm signupForm) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                try {
                    validateSignupForm(signupForm);
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * Validates the {@link SignupForm}
     *
     * @param signupForm
     * @throws {@link RuntimeException}
     */
    public void validateSignupForm(final SignupForm signupForm) {
        if (StringUtil.isEmpty(signupForm.getUsername())) {
            throw new RuntimeException("Please specify a username!");
        }
        if (signupForm.getUsername().length() < 4) {
            throw new RuntimeException("Username must have at least 4 characters!");
        }
        if (StringUtil.isEmpty(signupForm.getFirstName())) {
            throw new RuntimeException("Please specify your first name!");
        }
        if (StringUtil.isEmpty(signupForm.getLastName())) {
            throw new RuntimeException("Please specify your last name!");
        }
        if (StringUtil.isEmpty(signupForm.getMobileNumber())) {
            throw new RuntimeException("Please specify your mobile number!");
        }
        if (StringUtil.isEmpty(signupForm.getEmailAddress())) {
            throw new RuntimeException("Please specify your email address!");
        }
        if (signupForm.getDateOfBirth() == null) {
            throw new RuntimeException("Please specify your date of birth!");
        }
    }

    /**
     * Calls remote API to register the user and stores details in db if that call is successful
     *
     * @param request
     * @return
     */
    public Observable<Account> signup(final SignupRequest request) {
        return Observable.create(new Observable.OnSubscribe<Account>() {
            @Override
            public void call(final Subscriber<? super Account> subscriber) {

                accountApiService.signup(request)
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {
                                Account account = new Account();

                                copyIntoAccount(request, account);

                                daoService.saveAccount(account);
                                subscriber.onNext(account);
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e, "Could not create account: ");
                                String errorMessage = ErrorMessageUtil.getUserReadableApiErrorMsg(e);
                                subscriber.onError(new RuntimeException(errorMessage));
                            }

                            @Override
                            public void onNext(Void aVoid) {

                            }
                        });
            }
        });
    }

    /**
     * Copies fields from {@link SignupRequest} to {@link Account}
     *
     * @param request
     * @param account
     */
    private void copyIntoAccount(SignupRequest request, Account account) {
        account.setNcrAddress(request.getNcrAddress());
        account.setUsername(request.getUsername());
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setCategory(request.getCategory());
        Date dob = DateTimeUtil.fromYYYYMMdd(request.getDateOfBirth());
        account.setDateOfBirth(dob);
        account.setGender(request.getGender());
        account.setTimestamp(request.getTimestamp());
        account.setMobileNumber(request.getMobileNumber());
        account.setEmailAddress(request.getEmailAddress());
        account.setVerified(false);
    }


    public String generatePrivateKeyIfNotExists(){
        if(preferencesHelper.getUserPrivateKey().isEmpty()) {
            String privateKey = cryptoService.newPrivateKey();
            preferencesHelper.saveUserPrivateKey(privateKey);
        }
        return preferencesHelper.getUserPrivateKey();
    }

}
