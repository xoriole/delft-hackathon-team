package com.github.sofaid.app.ui.signup_seedwords;


//import com.google.common.base.Joiner;
import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.injection.ConfigPersistent;
import com.github.sofaid.app.models.enums.ActivityToShow;
import com.github.sofaid.app.services.crypto.CryptoService;
import com.github.sofaid.app.ui.common.BasePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by robik
 */
@ConfigPersistent
public class SignupSeedwordsPresenter extends BasePresenter<SignupSeedWordsView> {

    private final PreferencesHelper preferencesHelper;
    private final CryptoService cryptoService;

    @Inject
    public SignupSeedwordsPresenter(PreferencesHelper preferencesHelper, CryptoService cryptoService) {
        this.preferencesHelper = preferencesHelper;
        this.cryptoService = cryptoService;
    }

    public void updateActivityToShow() {
        preferencesHelper.saveActivityToShowOnLaunch(ActivityToShow.HOME.getName());
    }

    public void showSeedWords() {
        String privateKey = preferencesHelper.getUserPrivateKey();
        List<String> seedWordsList = cryptoService.getSeedWords(privateKey);
        String seedwords = "";//Joiner.on(" ").join(seedWordsList);
        this.getMvpView().setSeedWords(seedwords);
    }
}
