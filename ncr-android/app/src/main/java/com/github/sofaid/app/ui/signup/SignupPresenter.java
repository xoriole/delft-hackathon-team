package com.github.sofaid.app.ui.signup;


import javax.inject.Inject;

import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.injection.ConfigPersistent;
import com.github.sofaid.app.models.db.Account;
import com.github.sofaid.app.models.enums.ActivityToShow;
import com.github.sofaid.app.services.core.SignupService;
import com.github.sofaid.app.ui.common.BasePresenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by sireto on 27-12-16.
 */
@ConfigPersistent
public class SignupPresenter extends BasePresenter<SignupView> {
    private final SignupService signupService;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public SignupPresenter(SignupService signupService, PreferencesHelper preferencesHelper) {
        this.signupService = signupService;
        this.preferencesHelper = preferencesHelper;
    }

    /**
     * Registers the user
     *
     * @param signupForm
     */
    public void signup(SignupForm signupForm) {
        signupService.signup(signupForm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Account>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().dismissProgressDialog();
                        getMvpView().showSuccessMsg("Registration successful!");
                        getMvpView().gotoNextActivity();
                        preferencesHelper.saveActivityToShowOnLaunch(ActivityToShow.SIGNUP_SEEDWORDS.getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().dismissProgressDialog();
                        Timber.e(e, "Error while signing up");
                        getMvpView().showErrorMsg(e.getMessage());
                    }

                    @Override
                    public void onNext(Account account) {

                    }
                });
    }

}
