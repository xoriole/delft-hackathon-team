package com.github.sofaid.app.ui.signup_seedwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.github.sofaid.app.R;
import com.github.sofaid.app.ui.common.BaseActivity;
import com.github.sofaid.app.ui.home.HomeActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SignupSeedWordsActivity extends BaseActivity implements SignupSeedWordsView {

    @Inject
    SignupSeedwordsPresenter presenter;
    @BindView(R.id.seed_words_checkbox)
    CheckBox seedWordsCheckbox;
    @BindView(R.id.finish_button)
    Button finishButton;
    @BindView(R.id.seed_words_layout)
    LinearLayout layout;
    @BindView(R.id.seed_words_text_view)
    AppCompatTextView seedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_seedwords);
        activityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.showSeedWords();
        finishButton.setEnabled(false);
        seedWordsCheckbox.setChecked(false);
    }

    @OnCheckedChanged(R.id.seed_words_checkbox)
    public void onCheckedChanged() {
        if (seedWordsCheckbox.isChecked()) {
            finishButton.setEnabled(true);
        } else {
            finishButton.setEnabled(false);
        }
    }

    @OnClick(R.id.finish_button)
    public void onFinishButtonClicked() {
        presenter.updateActivityToShow();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void showErrorMsg(String message) {
        showSnackAlert(layout, message);
    }

    public void setSeedWords(String seedWords){
        seedTextView.setText(seedWords);
    }

}
