package com.github.sofaid.app.ui.send_ncr;


import com.github.sofaid.app.injection.ConfigPersistent;
import com.github.sofaid.app.models.db.Transaction;
import com.github.sofaid.app.models.enums.Status;
import com.github.sofaid.app.models.enums.TransactionRequestType;
import com.github.sofaid.app.models.internal.TransactionAmount;
import com.github.sofaid.app.services.core.TransactionService;
import com.github.sofaid.app.ui.common.BasePresenter;
import com.github.sofaid.app.utils.ErrorMessageUtil;

import java.math.BigDecimal;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by robik
 */
@ConfigPersistent
public class SendNcrPresenter extends BasePresenter<SendNcrView> {

    private final TransactionService transactionService;

    @Inject
    public SendNcrPresenter(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public TransactionAmount calculateTransactionAmount(BigDecimal amount) {
        return transactionService.buildTransactionAmount(amount, TransactionRequestType.FUND_TRANSFER);
    }

    public String getFeeAndTotalText(TransactionAmount transactionAmount) {
        return "(" +
                "Network Fee: " + transactionAmount.getNetworkFeePercent() + "% " +
                "Service Fee: " + transactionAmount.getServiceFeePercent() + "% " +
                "Total: " + transactionAmount.getTotalAmount() + " NCR" +
                ")";
    }

    public void sendNcr(SendNcrForm form) {
        transactionService.sendNcr(form)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Transaction>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMsg = ErrorMessageUtil.logExceptionAndGetErrorMessage(e);
                        getMvpView().showErrorMsg(errorMsg);
                    }

                    @Override
                    public void onNext(Transaction transaction) {
                        Status status = Status.fromId(transaction.getStatus());
                        getMvpView().showSuccessMsg("The transaction "+status.getClause());
                    }
                });
    }
}
