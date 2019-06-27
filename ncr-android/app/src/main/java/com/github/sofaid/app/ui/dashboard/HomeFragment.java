package com.github.sofaid.app.ui.dashboard;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sofaid.app.R;
import com.github.sofaid.app.crypto.KeyGenerator;
import com.github.sofaid.app.ethereum.ContractService;
import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.secrets.Scheme;
import com.github.sofaid.app.utils.ImageUtil;

import org.jboss.aerogear.security.otp.Totp;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;


//import android.graphics.Bitmap;
//import android.graphics.Point;
//import android.util.Log;
//import android.view.Display;
//import com.matthewtamlin.shamir.standardjavaapi.crypto.Shamir;
//import com.example.myapplication.utils.ImageUtil;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.journeyapps.barcodescanner.BarcodeEncoder;

//import butterknife.BindView;
//import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;

//    @BindView(R.id.id_qr_code) ImageView mIdQrCodeImageView;
    ImageView mIdQrCodeImageView;
    TextView mPublicKeyTextView;
    TextView mBsnTextView;
    TextView mOtpTextView;
    View view;

    private KeyGenerator.ExtendedKey masterKey;
    private PreferencesHelper preferencesHelper;
    private Totp totp;
    private ContractService contractService;

    private String walletAddress="";

    public static HomeFragment newInstance(PreferencesHelper preferencesHelper, ContractService contractService) {
        HomeFragment fragment = new  HomeFragment();
        fragment.setPreferencesHelper(preferencesHelper);
        fragment.setContractService(contractService);
        return fragment;
    }

    private void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    public void setPreferencesHelper(PreferencesHelper preferencesHelper){
        this.preferencesHelper = preferencesHelper;
    }

    //update the otp periodically
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            updateOtpCode();
            mBsnTextView.setText(preferencesHelper.getBSN());
            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
//        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        // TODO: Use the ViewModel
        String userId = "something here";
//        setupQrCode(userId);
        mIdQrCodeImageView = (ImageView)  view.findViewById(R.id.id_qr_code);
        mPublicKeyTextView = (TextView) view.findViewById(R.id.tv_public_key);
        mBsnTextView = (TextView) view.findViewById(R.id.tv_bsn);
        mOtpTextView = (TextView) view.findViewById(R.id.tv_otp);
        updateIdentity();

        mPublicKeyTextView.setOnClickListener(v -> {
            this.shareAddress(walletAddress);
        });
        testShamir();
    }

    public void updateIdentity(){
        masterKey = ((DashboardActivity)getActivity()).getMasterKey();
        //        String publickey = ByteUtils.toHex(masterKey.getMaster().getAddress());
        walletAddress = Credentials.create(masterKey.getMaster().getPrivateKey().toString(16)).getAddress();
        mIdQrCodeImageView.setImageBitmap(ImageUtil.qrCodeLarge("sofaid://"+walletAddress));
        mPublicKeyTextView.setText(walletAddress);
    }

    private void shareAddress(String address) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Sofa ID");
        share.putExtra(Intent.EXTRA_TEXT, address);

        startActivity(Intent.createChooser(share, "Share Identity Address"));
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        timerHandler.postDelayed(timerRunnable, 5000);
    }

    public void updateBsn(){
        mBsnTextView.setText(preferencesHelper.getBSN());
    }

    public void updateOtpCode(){
        String otpSecret = preferencesHelper.getOtpSecret();
        if(!otpSecret.isEmpty()){
            Totp totp = new Totp(otpSecret);
            mOtpTextView.setText(totp.now());
        }
    }



    public void testShamir(){
        final Scheme scheme = new Scheme(new SecureRandom(), 5, 3);
        final byte[] secret = "hello there".getBytes(StandardCharsets.UTF_8);
        final Map<Integer, byte[]> parts = scheme.split(secret);
        final byte[] recovered = scheme.join(parts);
        System.out.println(new String(recovered, StandardCharsets.UTF_8));
    }

}
