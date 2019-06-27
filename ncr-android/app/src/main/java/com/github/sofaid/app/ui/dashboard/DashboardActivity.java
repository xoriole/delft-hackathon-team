package com.github.sofaid.app.ui.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sofaid.app.R;
import com.github.sofaid.app.crypto.CryptoService;
import com.github.sofaid.app.crypto.KeyGenerator;
import com.github.sofaid.app.ethereum.ContractService;
import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.models.db.Account;
import com.github.sofaid.app.models.internal.Attestation;
import com.github.sofaid.app.services.core.SignupService;
import com.github.sofaid.app.ui.barcode.BarcodeCaptureActivity;
import com.github.sofaid.app.ui.common.BaseActivity;
import com.github.sofaid.app.ui.shares_activities.ShareCoordinatorActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.jboss.aerogear.security.otp.Totp;
import org.web3j.crypto.Credentials;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import jnr.ffi.annotations.In;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DashboardActivity extends BaseActivity {
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final int RC_MASTER_KEY_CAPTURE = 9002;
    private static final String TAG = "DashboardActivity";

    private TextView statusMessage;
    private TextView barcodeValue;
    private boolean autoFocus = true;
    private boolean useFlash = false;


    private Toolbar toolbar;
    private CryptoService cryptoService;
    private KeyGenerator.ExtendedKey masterKey;

    private HomeFragment homeFragment;
    private AttestationFragment attestationFragment;
    private HistoryFragment historyFragment;

    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    SignupService signupService;
    @Inject
    ContractService contractService;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(homeFragment);
                    return true;
                case R.id.navigation_attestations:
                    loadFragment(attestationFragment);
                    return true;
                case R.id.navigation_history:
                    Intent intent = new Intent(DashboardActivity.this,
                            ShareCoordinatorActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        activityComponent().inject(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(" Sofa.ID ");
        setSupportActionBar(toolbar);

        cryptoService = new CryptoService();
        initKeys();

        // initialize the fragments as well
        initFragments();
        loadFragment(homeFragment);
//        ping();
//        testContract();
//        setupMsk();
    }

    public void setupMsk(){
        String msk = getResources().getString(R.string.demo_app_key);
        this.initializeMasterKey(msk);
    }

    public void initKeys(){
        String msk = preferencesHelper.getMsk();
        if(msk.isEmpty()){
            masterKey = cryptoService.generateMasterKeyPair();
            msk = masterKey.serialize(true);
            preferencesHelper.saveMsk(msk);
        }else{
            masterKey = cryptoService.fromXPriv(msk);
        }
        Credentials credentials = Credentials.create(masterKey.getMaster().getPrivateKey().toString(16));
        this.contractService.initContract(credentials);
    }

    public void createNewIdentity(){
        masterKey = cryptoService.generateMasterKeyPair();
        String msk = masterKey.serialize(true);
        preferencesHelper.saveMsk(msk);
        Credentials credentials = Credentials.create(masterKey.getMaster().getPrivateKey().toString(16));
        this.contractService.initContract(credentials);
    }

    public void initializeMasterKey(String msk){
        masterKey = cryptoService.fromXPriv(msk);
        this.preferencesHelper.saveMsk(msk);
        Credentials credentials = Credentials.create(masterKey.getMaster().getPrivateKey().toString(16));
        this.contractService.initContract(credentials);
    }

    public void testContract(){
        contractService.initContract(getMasterKey().getMaster().getPrivateKey().toString(16));
        contractService.getAttestations("0xD14E7706a1A50D7e3eA9A38031F04FE4864A42Ba").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Completed");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Error!");
                    }

                    @Override
                    public void onNext(List list) {
                        System.out.println("List:"+list);
                    }
                });
    }

    protected void initFragments(){
        homeFragment = HomeFragment.newInstance(this.preferencesHelper, this.contractService);
        attestationFragment = AttestationFragment.newInstance(this.contractService);
        historyFragment = new HistoryFragment();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        loadFragment(homeFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_scan:
                Intent intent = new Intent(DashboardActivity.this,
                        BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                break;
            case R.id.action_bsn:
                inputBSN();
                break;
            case R.id.action_refresh_id:
                Toast.makeText(DashboardActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                this.createNewIdentity();
                this.loadFragment(homeFragment);
                homeFragment.updateIdentity();
                break;
            case R.id.action_setup_msk:
                Intent masterKeyIntent = new Intent(DashboardActivity.this,
                        BarcodeCaptureActivity.class);
                masterKeyIntent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus);
                masterKeyIntent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash);
                startActivityForResult(masterKeyIntent, RC_MASTER_KEY_CAPTURE);
                break;
            case R.id.action_balance:
                BigDecimal balance = this.contractService.getBalance();
                String text = String.format("Balance: %.3f", balance);
                Toast.makeText(DashboardActivity.this, text, Toast.LENGTH_SHORT).show();
                break;


        }
        return super.onOptionsItemSelected(item);

    }

    public KeyGenerator.ExtendedKey getMasterKey(){
        if(masterKey == null){
            cryptoService = new CryptoService();
            masterKey = cryptoService.fromXPriv(getResources().getString(R.string.demo_app_key));
        }
        return masterKey;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    statusMessage.setText(R.string.barcode_success);
//                    barcodeValue.setText(barcode.displayValue);
//                    // TODO: do background enrollment
//                    Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
//                    startActivity(intent);
                    Log.d(TAG, "Scanned code:"+barcode.displayValue);
                    Toast.makeText(DashboardActivity.this,barcode.displayValue, Toast.LENGTH_SHORT).show();
                    if(!updateifOtpSecret(barcode.displayValue)){
                        if(!askAttestOnScan(barcode.displayValue)) {
                            login(barcode.displayValue);
                        }
                    }

                } else {
//                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                    Toast.makeText(DashboardActivity.this,"Failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else if (requestCode == RC_MASTER_KEY_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    this.initializeMasterKey(barcode.displayValue);
                    Toast.makeText(DashboardActivity.this,"Initialized new master key", Toast.LENGTH_SHORT).show();
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean updateifOtpSecret(String otpUri){
        if(otpUri.isEmpty() || !otpUri.startsWith("otpauth://totp/")){
            return false;
        }

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        String secret = getSecretFromOtpUri(otpUri);
                        System.out.println("secret:"+secret);
                        preferencesHelper.saveOtpSecret(secret);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Update the OTP secret?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        return true;
    }

    public String getSecretFromOtpUri(String uri){
        int start = uri.indexOf("secret=")+7;
        int end = uri.substring(start).indexOf("&");
        return uri.substring(start, start+end);
    }

    public boolean askAttestOnScan(String uri){
        if(uri.isEmpty() || !uri.startsWith("sofaid://")){
            return false;
        }
        String address = uri.replace("sofaid://", "");
        System.out.println("Asking for attestation");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        attest(address);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        Toast.makeText(DashboardActivity.this, "No Clicked",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to add this address: "+address+"?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        return true;
    }

    public void attest(final String address){
        System.out.println("Actually trying to attest address:"+address);
        contractService.signAttestation(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Attestation Completed");
                        Toast.makeText(DashboardActivity.this, "Signed attestation successful for "+ address, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Error!");
                        Toast.makeText(DashboardActivity.this, "Signed attestation failed for "+ address, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        System.out.println("Attestation in progress");
                        Toast.makeText(DashboardActivity.this, "Signed attestation in progress for "+ address+":"+aBoolean, Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void inputBSN(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input/update BSN");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferencesHelper.saveBSN(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void ping(){
        System.out.println("Testing ping ****");
        signupService.testPing().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Error in ping *****");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("All is well!");
                    }
                });
    }

    public void login(String token){
        Log.i("DashboardActivity", "Logging in with token:"+ token);
        // get the otp token
        String otp = new Totp(preferencesHelper.getOtpSecret()).now();
        System.out.println("otp:"+otp);

        signupService.login(otp, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(DashboardActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(String s) {
                        // this is json value, treat it accordingly
                        Toast.makeText(DashboardActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void alertMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        Toast.makeText(DashboardActivity.this, "Yes Clicked",
                                Toast.LENGTH_LONG).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        Toast.makeText(DashboardActivity.this, "No Clicked",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}
