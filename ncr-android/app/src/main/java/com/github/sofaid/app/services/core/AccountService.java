package com.github.sofaid.app.services.core;

import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.models.db.Share;
import com.github.sofaid.app.models.remote.BlockchainAccount;
import com.github.sofaid.app.services.remote_api.AccountApiService;
import com.github.sofaid.app.services.repository.DaoService;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sandippandey on 20/06/2017.
 */

public class AccountService {

    private final AccountApiService accountApiService;
    private final PreferencesHelper preferencesHelper;
    private final DaoService daoService;

    @Inject
    public AccountService(AccountApiService accountApiService, PreferencesHelper preferencesHelper,
                          DaoService daoService){
        this.accountApiService = accountApiService;
        this.preferencesHelper = preferencesHelper;
        this.daoService = daoService;
    }

    public rx.Observable<BlockchainAccount> getBlockchainAccount(String address){
        return accountApiService.getBlockchainAccount(address);
    }

    public List<Share> getAllShares(){
        return daoService.getShareDao()
                .queryBuilder().list();
    }

    public void saveShare(Share share){
        daoService.saveShare(share);
    }
}
