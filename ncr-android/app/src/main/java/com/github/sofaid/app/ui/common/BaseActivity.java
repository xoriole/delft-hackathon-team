package com.github.sofaid.app.ui.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import com.github.sofaid.app.NcrApplication;
import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.injection.ActivityComponent;
import com.github.sofaid.app.injection.ActivityModule;
import com.github.sofaid.app.injection.ConfigPersistentComponent;
import com.github.sofaid.app.injection.DaggerConfigPersistentComponent;
import com.github.sofaid.app.models.enums.ActivityToShow;
import com.github.sofaid.app.ui.home.HomeActivity;
import com.github.sofaid.app.ui.signup.SignupActivity;
import com.github.sofaid.app.ui.signup_seedwords.SignupSeedWordsActivity;

import timber.log.Timber;


/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> componentsMap = new HashMap<>();

    private ActivityComponent activityComponent;
    private long activityId;
    @Inject
    protected PreferencesHelper preferencesHelper;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!componentsMap.containsKey(activityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", activityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(NcrApplication.get(this).applicationComponent())
                    .build();
            componentsMap.put(activityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", activityId);
            configPersistentComponent = componentsMap.get(activityId);
        }
        activityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, activityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", activityId);
            componentsMap.remove(activityId);
        }
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        dismissProgressDialog();
        super.onPause();
    }

    public ActivityComponent activityComponent() {
        return activityComponent;
    }

    /**
     * @param view
     * @param message
     */
    public Snackbar showSnackAlert(View view, String message) {
        return showSnack(view, message, Snackbar.LENGTH_LONG, Color.DKGRAY, Color.YELLOW, null, null);
    }

    /**
     * @param view
     * @param message
     * @param length
     */
    public Snackbar showSnackAlert(View view, String message, int length) {
        return showSnack(view, message, length, Color.DKGRAY, Color.YELLOW, null, null);
    }

    /**
     * @param view
     * @param message
     * @param length
     * @param action
     * @param onClickListener
     */
    public Snackbar showSnackAlert(View view, String message, int length, String action, View.OnClickListener onClickListener) {
        return showSnack(view, message, length, Color.DKGRAY, Color.YELLOW, action, onClickListener);
    }

    /**
     * @param view
     * @param message
     */
    public Snackbar showSnackSuccess(View view, String message) {
        return showSnack(view, message, Snackbar.LENGTH_LONG, Color.DKGRAY, Color.GREEN, null, null);
    }

    /**
     * @param view
     * @param message
     * @param length
     */
    public Snackbar showSnackSuccess(View view, String message, int length) {
        return showSnack(view, message, length, Color.DKGRAY, Color.GREEN, null, null);
    }

    /**
     * @param view
     * @param message
     * @param length
     * @param action
     * @param onClickListener
     */
    public Snackbar showSnackSuccess(View view, String message, int length, String action, View.OnClickListener onClickListener) {
        return showSnack(view, message, length, Color.DKGRAY, Color.GREEN, action, onClickListener);
    }

    /**
     * @param view
     * @param message
     * @param length
     * @param backgroundColor
     * @param textColor
     * @param action
     * @param onClickListener
     */
    public Snackbar showSnack(View view, String message, int length, int backgroundColor, int textColor, String action, View.OnClickListener onClickListener) {
        Snackbar snackbar = Snackbar.make(view, message, length);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(backgroundColor);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(textColor);
        snackbar.show();
        if (action != null && onClickListener != null) {
            snackbar.setAction(action, onClickListener);
        }
        return snackbar;
    }

    public void showToast(String message, int length) {
        Toast.makeText(this, message, length).show();
    }

    public void showLongToast(String message) {
        showToast(message, Toast.LENGTH_LONG);
    }

    public void showShortToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * Hide keyboard
     */
    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Timber.e(e, "Trying to close keyboard");
        }
    }

    public void showProgressDialog(String title, String message, boolean cancelable) {
        if (progressDialog != null) {
            dismissProgressDialog();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
    }


    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * Starts {@link com.github.sofaid.app.ui.signup.SignupActivity} if user has not signed up yet,
     * Starts {@link com.github.sofaid.app.ui.signup_seedwords.SignupSeedWordsActivity} if user has not confirmed seed words yet
     * Starts {@link com.github.sofaid.app.ui.home.HomeActivity} if user has signed up and confirmed seed words
     */
    protected void startProperActivity() {
        String activityToShowOnLaunchStr = preferencesHelper.getActivityToShowOnLaunch();
        ActivityToShow activityToShow = ActivityToShow.fromName(activityToShowOnLaunchStr);
        Intent intent = null;
        if (activityToShow.equals(ActivityToShow.SIGNUP)) {
            intent = new Intent(this, SignupActivity.class);
        } else if (activityToShow.equals(ActivityToShow.SIGNUP_SEEDWORDS)) {
            intent = new Intent(this, SignupSeedWordsActivity.class);
        } else if (activityToShow.equals(ActivityToShow.HOME)) {
            intent = new Intent(this, HomeActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
