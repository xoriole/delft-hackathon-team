package com.github.sofaid.app.ui.home;


import com.github.sofaid.app.models.remote.BlockchainAccount;
import com.github.sofaid.app.ui.common.MvpView;

/**
 * Created by robik
 */

public interface HomeView extends MvpView {

    void showBlockchainAccountInfo(BlockchainAccount blockchainAccount);

    void showErrorMessage(String message);

}
