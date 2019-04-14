package com.github.sofaid.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.sofaid.app.R;
import com.github.sofaid.app.models.remote.BlockchainAccount;
import com.github.sofaid.app.ui.common.BaseActivity;
import com.github.sofaid.app.ui.send_ncr.SendNcrActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomeView {

    @Inject
    HomePresenter presenter;
    @BindView(R.id.home_layout)
    LinearLayout layout;

    @BindView(R.id.address_textview)
    AppCompatTextView address;
    @BindView(R.id.balance_textview)
    AppCompatTextView balance;
    @BindView(R.id.enabled_checkbox)
    CheckBox enabledCheckbox;
    @BindView(R.id.verified_checkbox)
    CheckBox verifiedCheckbox;

    @OnClick(R.id.send_ncr_button)
    public void onSendNcrButtonClicked() {
        Intent intent = new Intent(this, SendNcrActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.showBlockchainAccount();
    }


    @Override
    public void showBlockchainAccountInfo(BlockchainAccount blockchainAccount) {
        address.setText(blockchainAccount.getAddress());
        balance.setText(blockchainAccount.getBalance().toString()+" NCR");
        if(blockchainAccount.isEnabled()){
            enabledCheckbox.setChecked(true);
        }
        if(blockchainAccount.isVerified()){
            verifiedCheckbox.setChecked(true);
        }
    }

    @Override
    public void showErrorMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT);
    }
}
