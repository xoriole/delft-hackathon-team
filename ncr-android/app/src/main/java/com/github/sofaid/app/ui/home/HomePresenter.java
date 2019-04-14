package com.github.sofaid.app.ui.home;


import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.injection.ConfigPersistent;
import com.github.sofaid.app.models.remote.BlockchainAccount;
import com.github.sofaid.app.services.core.AccountService;
import com.github.sofaid.app.ui.common.BasePresenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by robik
 */
@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeView> {
    private final AccountService accountService;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public HomePresenter(AccountService accountService, PreferencesHelper preferencesHelper) {
        this.accountService = accountService;
        this.preferencesHelper = preferencesHelper;
    }

    public void showBlockchainAccount() {
        String ncrAddress = preferencesHelper.getPrefNcrAddress();
        accountService.getBlockchainAccount(ncrAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BlockchainAccount>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        HomePresenter.this.getMvpView().showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(BlockchainAccount blockchainAccount) {
                        HomePresenter.this.getMvpView().showBlockchainAccountInfo(blockchainAccount);
                    }
                });
    }
}
