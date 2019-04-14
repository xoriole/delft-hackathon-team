package com.github.sofaid.app.ui.signup_seedwords;


import com.github.sofaid.app.ui.common.MvpView;

/**
 * Created by robik
 */

public interface SignupSeedWordsView extends MvpView {

    void setSeedWords(String seedWords);

    void showErrorMsg(String message);

}
