package com.github.sofaid.app.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.sofaid.app.R;
import com.github.sofaid.app.models.enums.Gender;
import com.github.sofaid.app.ui.common.BaseActivity;
import com.github.sofaid.app.ui.signup_seedwords.SignupSeedWordsActivity;
import com.github.sofaid.app.utils.DateTimeUtil;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SignupActivity extends BaseActivity implements SignupView {

    @Inject
    SignupPresenter signupPresenter;
    @BindView(R.id.username_edit_text)
    EditText usernameEditText;
    @BindView(R.id.first_name_edit_text)
    EditText firstNameEditText;
    @BindView(R.id.last_name_edit_text)
    EditText lastNameEditText;
    @BindView(R.id.mobile_phone_edit_text)
    EditText mobilePhoneEditText;
    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.dob_date_picker)
    DatePicker datePicker;
    @BindView(R.id.signup_layout)
    LinearLayout signupLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        signupPresenter.attachView(this);
        datePicker.setMaxDate(new Date().getTime());
    }

    @Override
    protected void onStart() {
        super.onStart();
       // startProperActivity();
    }

    @OnClick(R.id.signup_button)
    public void onSignupButtonClicked() {
        showProgressDialog("Registering", "Creating new account", false);
        SignupForm signupForm = new SignupForm();

        signupForm.setUsername(usernameEditText.getText().toString());
        signupForm.setFirstName(firstNameEditText.getText().toString());
        signupForm.setLastName(lastNameEditText.getText().toString());
        signupForm.setMobileNumber(mobilePhoneEditText.getText().toString());
        signupForm.setEmailAddress(emailEditText.getText().toString());

        String genderStr = (String) genderSpinner.getSelectedItem();
        Gender gender = Gender.fromName(genderStr);
        if (gender != null) {
            signupForm.setGender(gender.getId());
        }


        try {
            Date dob = DateTimeUtil.newDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            Timber.i("Year: " + datePicker.getYear() + " month: " + datePicker.getMonth() + " day: " + datePicker.getDayOfMonth());
            signupForm.setDateOfBirth(dob);
        } catch (Exception e) {
        }

        signupPresenter.signup(signupForm);
    }

    @Override
    public void showSuccessMsg(String message) {
        showSnackSuccess(signupLayout, message);
    }

    @Override
    public void showErrorMsg(String message) {
        showSnackAlert(signupLayout, message);
    }

    @Override
    public void gotoNextActivity() {
        Intent intent = new Intent(this, SignupSeedWordsActivity.class);
        startActivity(intent);
    }

}
