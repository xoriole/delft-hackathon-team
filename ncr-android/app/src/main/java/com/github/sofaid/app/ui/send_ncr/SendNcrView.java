package com.github.sofaid.app.ui.send_ncr;


import com.github.sofaid.app.ui.common.MvpView;

/**
 * Created by robik
 */

public interface SendNcrView extends MvpView {

    void showErrorMsg(String message);

    void showSuccessMsg(String s);
}
