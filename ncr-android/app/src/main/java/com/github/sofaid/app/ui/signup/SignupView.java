package com.github.sofaid.app.ui.signup;


import com.github.sofaid.app.ui.common.MvpView;

/**
 * Created by sireto on 27-12-16.
 */

public interface SignupView extends MvpView {

    void showSuccessMsg(String message);

    void showErrorMsg(String message);

    void showProgressDialog(String title, String message, boolean cancellable);

    void dismissProgressDialog();

    void gotoNextActivity();

}
