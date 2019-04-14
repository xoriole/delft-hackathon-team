package com.github.sofaid.app.services.core;

import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.models.db.Transaction;
import com.github.sofaid.app.models.enums.AccountIdType;
import com.github.sofaid.app.models.enums.TransactionRequestType;
import com.github.sofaid.app.models.internal.SignedToken;
import com.github.sofaid.app.models.internal.TransactionAmount;
import com.github.sofaid.app.models.remote.AccountPublicInfo;
import com.github.sofaid.app.models.remote.TransactionRequest;
import com.github.sofaid.app.models.remote.TransactionResponse;
import com.github.sofaid.app.services.remote_api.AccountApiService;
import com.github.sofaid.app.services.remote_api.TransactionApiService;
import com.github.sofaid.app.services.repository.DaoService;
import com.github.sofaid.app.ui.send_ncr.SendNcrForm;
import com.github.sofaid.app.utils.NumberUtils;
import com.github.sofaid.app.models.enums.Status;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by robik on 6/17/17.
 */
public class TransactionService {
    private final PreferencesHelper preferencesHelper;
    private final AccountApiService accountApiService;
    private final TransactionApiService transactionApiService;
    private final DaoService daoService;
    public static final BigDecimal FUND_TRANSFER_NETWORK_FEE_PERCENT = new BigDecimal("0.1");
    public static final BigDecimal FUND_TRANSFER_SERVICE_FEE_PERCENT = new BigDecimal("0");

    @Inject
    public TransactionService(PreferencesHelper preferencesHelper, AccountApiService accountApiService,
                              TransactionApiService transactionApiService, DaoService daoService) {
        this.preferencesHelper = preferencesHelper;
        this.accountApiService = accountApiService;
        this.transactionApiService = transactionApiService;
        this.daoService = daoService;
    }

    /**
     * Returns an {@link Observable} which when subscribed: Saves the transaction request into database and calls remote API for transaction
     *
     * @param form
     * @return
     */
    public Observable<Transaction> sendNcr(final SendNcrForm form) {
        return Observable.create(new Observable.OnSubscribe<Transaction>() {
            @Override
            public void call(final Subscriber<? super Transaction> subscriber) {
                getAccountPublicInfo(form)
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AccountPublicInfo>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(AccountPublicInfo accountPublicInfo) {
                                TransactionRequest transactionRequest
                                        = buildTransactionRequest(form, accountPublicInfo);
                                saveTransactionAndSendRequest(transactionRequest)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Subscriber<Transaction>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                subscriber.onError(e);
                                            }

                                            @Override
                                            public void onNext(Transaction transaction) {
                                                subscriber.onNext(transaction);
                                                subscriber.onCompleted();
                                            }
                                        });
                            }
                        });
            }
        });

    }

    /**
     * Returns an {@link Observable} which when subscribed, saves {@link Transaction} to local db, calls remote API to create the transaction
     * <p>
     * If the request fails then status of {@link Transaction} is updated to {@link Status#FAILED}
     * If the request succeeds then the status of {@link Transaction} is updated to {@link Status#SUCCESS}.
     * <p>
     * Since transaction may take a long time in the future and UI thread may be killed by that time,it is the responsibility of notification receiver to update the status to proper value when server sends a notification about the transaction.
     *
     * @param transactionRequest
     * @return
     */
    public Observable<Transaction> saveTransactionAndSendRequest(final TransactionRequest transactionRequest) {
        return Observable.create(new Observable.OnSubscribe<Transaction>() {
            @Override
            public void call(final Subscriber<? super Transaction> subscriber) {
                final Transaction transaction = buildTransaction(transactionRequest);
                try {
                    daoService.saveTransaction(transaction);
                    transactionApiService.createTransaction(transactionRequest)
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<TransactionResponse>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    markAsFailed(transaction);
                                    subscriber.onError(e);
                                }

                                @Override
                                public void onNext(TransactionResponse transactionResponse) {
                                    markAsSuccess(transaction, transactionResponse);
                                    subscriber.onNext(transaction);
                                    subscriber.onCompleted();
                                }

                            });
                } catch (Exception e) {
                    subscriber.onError(e);
                }

            }
        });
    }

    /**
     * Sets status to {@link Status#SUCCESS} and sets the transaction hash too
     *
     * @param transaction
     * @param transactionResponse
     */
    public void markAsSuccess(Transaction transaction, TransactionResponse transactionResponse) {
        transaction.setStatus(Status.SUCCESS.getId());
        transaction.setTransactionHash(transactionResponse.getTransactionHash());
        daoService.saveTransaction(transaction);
    }

    /**
     * Sets status to {@link Status#FAILED}
     *
     * @param transaction
     */
    public void markAsFailed(Transaction transaction) {
        transaction.setStatus(Status.FAILED.getId());
        daoService.saveTransaction(transaction);
    }

    /**
     * Returns an {@link Observable} which when subscribed calls remote API to get {@link AccountPublicInfo} for given account id/idType
     *
     * @param form
     * @return
     */
    public Observable<AccountPublicInfo> getAccountPublicInfo(final SendNcrForm form) {
        return Observable.create(new Observable.OnSubscribe<AccountPublicInfo>() {
            @Override
            public void call(final Subscriber<? super AccountPublicInfo> subscriber) {
                AccountIdType toIdType = AccountIdType.fromId(form.getToIdType());
                accountApiService.getAccountPublicInfo(form.getRecepientId(),
                        toIdType.getKeyword())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AccountPublicInfo>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(AccountPublicInfo accountPublicInfo) {
                                subscriber.onNext(accountPublicInfo);
                                subscriber.onCompleted();
                            }
                        });
            }
        });
    }

    /**
     * Builds {@link TransactionRequest} which can be sent to remote API to perform the transaction
     *
     * @param form
     * @param accountPublicInfo
     * @return
     */
    private TransactionRequest buildTransactionRequest(SendNcrForm form, AccountPublicInfo accountPublicInfo) {
        TransactionRequest request = new TransactionRequest();
        String referenceId = UUID.randomUUID().toString();
        request.setReferenceId(referenceId);
        request.setFromAddress(preferencesHelper.getPrefNcrAddress());
        request.setToAddress(accountPublicInfo.getNcrAddress());
        request.setToIdType(form.getToIdType());
        request.setType(TransactionRequestType.FUND_TRANSFER.getId());
        request.setTransactionAmount(form.getTransactionAmount());

        SignedToken signedToken = buildSignedToken(form, accountPublicInfo);
        request.setSignedToken(signedToken);
        return request;
    }

    /**
     * Builds {@link SignedToken} for transaction
     *
     * @param form
     * @param accountPublicInfo
     * @return
     */
    private SignedToken buildSignedToken(SendNcrForm form, AccountPublicInfo accountPublicInfo) {
        SignedToken signedToken = new SignedToken();
        signedToken.setToken("tx: " + accountPublicInfo.getNcrAddress());
        signedToken.setV((byte)1);
        signedToken.setR("r:" + accountPublicInfo.getNcrAddress());
        signedToken.setS("s:" + accountPublicInfo.getNcrAddress());
        return signedToken;
    }

    /**
     * Builds {@link Transaction} with id same as {@link TransactionRequest#referenceId}
     *
     * @param transactionRequest
     * @return
     */
    private Transaction buildTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionRequest.getReferenceId());
        transaction.setFromAddress(transactionRequest.getFromAddress());
        transaction.setToAddress(transactionRequest.getToAddress());
        transaction.setToIdType(transactionRequest.getToIdType());
        transaction.setType(transactionRequest.getType());

        TransactionAmount transactionAmount = transactionRequest.getTransactionAmount();
        transaction.setAmount(transactionAmount.getAmount().doubleValue());
        transaction.setNetworkFeePercent(transactionAmount.getNetworkFeePercent().doubleValue());
        transaction.setNetworkFeeAmount(transactionAmount.getNetworkFeeAmount().doubleValue());
        transaction.setServiceFeePercent(transactionAmount.getServiceFeePercent().doubleValue());
        transaction.setServiceFeeAmount(transactionAmount.getServiceFeeAmount().doubleValue());
        transaction.setTotalAmount(transactionAmount.getTotalAmount().doubleValue());

        transaction.setRemarks(transactionRequest.getRemarks());
        transaction.setCreatedOn(new Date());
        transaction.setStatus(Status.PENDING.getId());
        return transaction;
    }

    /**
     * Calculates values in {@link TransactionAmount} applying fees on the requested amount
     * TODO: Gather actual fee rates from remote API or local db
     *
     * @param amount
     * @param transactionRequestType
     * @return
     */
    public TransactionAmount buildTransactionAmount(BigDecimal amount, TransactionRequestType transactionRequestType) {
        if (transactionRequestType.equals(TransactionRequestType.FUND_TRANSFER)) {
            return buildTransactionAmount(amount, FUND_TRANSFER_NETWORK_FEE_PERCENT, FUND_TRANSFER_SERVICE_FEE_PERCENT);
        } else {
            return buildTransactionAmount(amount, new BigDecimal("0.2"), new BigDecimal("0.1"));
        }
    }

    /**
     * Calculates values in {@link TransactionAmount} with given fees
     *
     * @param amount
     * @param networkFeePercent
     * @param serviceFeePercent
     * @return
     */
    public TransactionAmount buildTransactionAmount(BigDecimal amount, BigDecimal networkFeePercent, BigDecimal serviceFeePercent) {

        BigDecimal networkFeeAmount = networkFeePercent.multiply(amount, NumberUtils.currencyMathContext())
                .divide(new BigDecimal("100"));
        BigDecimal serviceFeeAmount = serviceFeePercent.multiply(amount, NumberUtils.currencyMathContext())
                .divide(new BigDecimal("100"));
        BigDecimal totalAmount = amount.add(serviceFeeAmount).add(networkFeeAmount);

        TransactionAmount transactionAmount = new TransactionAmount();
        transactionAmount.setAmount(amount);
        transactionAmount.setNetworkFeePercent(networkFeePercent);
        transactionAmount.setNetworkFeeAmount(networkFeeAmount);
        transactionAmount.setServiceFeePercent(serviceFeePercent);
        transactionAmount.setServiceFeeAmount(serviceFeeAmount);
        transactionAmount.setTotalAmount(totalAmount);
        return transactionAmount;
    }
}
