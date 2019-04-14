package com.github.sofaid.app.ui.send_ncr;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.sofaid.app.R;
import com.github.sofaid.app.models.enums.AccountIdType;
import com.github.sofaid.app.models.internal.TransactionAmount;
import com.github.sofaid.app.ui.common.BaseActivity;
import com.github.sofaid.app.utils.StringUtil;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SendNcrActivity extends BaseActivity implements SendNcrView {

    @Inject
    SendNcrPresenter presenter;
    @BindView(R.id.to_id_type_spinner)
    Spinner toIdTypeSpinner;
    @BindView(R.id.recepient_id_edit_text)
    EditText recepientIdEditText;
    @BindView(R.id.amount_edit_text)
    EditText amountEditText;
    @BindView(R.id.fee_and_total_text_view)
    TextView feeAndTotalTextView;
    @BindView(R.id.remarks_edit_text)
    EditText remarksEditText;
    @BindView(R.id.send_button)
    Button sendButton;
    @BindView(R.id.send_ncr_layout)
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_ncr);
        activityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        sendButton.setEnabled(false);
    }

    @OnTextChanged(R.id.amount_edit_text)
    public void onAmountTextChanged() {
        refreshFeeAndTotalTextView();
        enableOrDisableSendButton();
    }

    private void refreshFeeAndTotalTextView() {
        BigDecimal amount = new BigDecimal(amountEditText.getText().toString());
        TransactionAmount transactionAmount = presenter.calculateTransactionAmount(amount);
        String feeAndTotalText = presenter.getFeeAndTotalText(transactionAmount);
        feeAndTotalTextView.setText(feeAndTotalText);
    }

    @OnTextChanged(R.id.recepient_id_edit_text)
    public void onRecepientIdTextChanged() {
        enableOrDisableSendButton();
    }

    private void enableOrDisableSendButton() {
        if (StringUtil.isEmpty(recepientIdEditText.getText().toString()) ||
                StringUtil.isEmpty(amountEditText.getText().toString())) {
            sendButton.setEnabled(false);
        } else {
            sendButton.setEnabled(true);
        }
    }

    @OnClick(R.id.send_button)
    public void onSendButtonClicked() {
        SendNcrForm form = new SendNcrForm();

        form.setRecepientId(recepientIdEditText.getText().toString());

        AccountIdType accountIdType = AccountIdType.fromName((String) toIdTypeSpinner.getSelectedItem());
        form.setToIdType(accountIdType.getId());

        BigDecimal amount = new BigDecimal(amountEditText.getText().toString());
        TransactionAmount transactionAmount = presenter.calculateTransactionAmount(amount);
        form.setTransactionAmount(transactionAmount);

        form.setRemarks(remarksEditText.getText().toString());
        presenter.sendNcr(form);
    }

    @Override
    public void showErrorMsg(String message) {
        showSnackAlert(layout, message);
    }

    @Override
    public void showSuccessMsg(String s) {
        showSnackSuccess(layout, s);
    }

}
