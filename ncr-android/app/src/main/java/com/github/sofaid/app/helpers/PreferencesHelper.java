package com.github.sofaid.app.helpers;

import android.content.SharedPreferences;

import com.github.sofaid.app.models.enums.ActivityToShow;

import org.web3j.crypto.Credentials;

import javax.inject.Inject;

public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "ncr_pref_file";
    private final SharedPreferences sharedPreferences;
    private static final String PREF_NCR_ADDRESS = "NCR_ADDRESS";
    private static final String PREF_USER_PRIVATE_KEY = "USER_PRIVATE_KEY";
    private static final String PREF_ACTIVITY_TO_SHOW_ON_LAUNCH = "ACTIVITY_TO_SHOW_ON_LAUNCH";
    private static final String PREF_MSK = "PREF_MSK";
    private static final String PREF_BSN = "PREF_BSN";
    private static final String PREF_OTP_SECRET = "PREF_OTP_SECRET";

    @Inject
    public PreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveNcrAddress(String ncrAddress) {
        this.sharedPreferences.edit().putString(PREF_NCR_ADDRESS, ncrAddress).commit();
    }

    public String getPrefNcrAddress() {
        String address = this.sharedPreferences.getString(PREF_NCR_ADDRESS, "default_ncr_address");
        if(address.isEmpty() || !address.startsWith("0x")){
            String privateKey = getUserPrivateKey();
            if(!privateKey.isEmpty()){
                Credentials credentials = Credentials.create(privateKey);
                address = credentials.getAddress();
                saveNcrAddress(address);
            }
        }
        return address;
    }

    public void saveUserPrivateKey(String privateKey) {
        this.sharedPreferences.edit().putString(PREF_USER_PRIVATE_KEY, privateKey).commit();
    }

    public String getUserPrivateKey() {
        return this.sharedPreferences.getString(PREF_USER_PRIVATE_KEY, "");
    }

    public void saveActivityToShowOnLaunch(String activityToShowOnLaunch) {
        this.sharedPreferences.edit().putString(PREF_ACTIVITY_TO_SHOW_ON_LAUNCH, activityToShowOnLaunch).commit();
    }

    public String getActivityToShowOnLaunch() {
        return this.sharedPreferences.getString(PREF_ACTIVITY_TO_SHOW_ON_LAUNCH, ActivityToShow.SIGNUP.getName());
    }

    public void saveBSN(String bsn) {
        this.sharedPreferences.edit().putString(PREF_BSN, bsn).commit();
    }

    public String getBSN() {
        return this.sharedPreferences.getString(PREF_BSN, "000.00.000");
    }

    public void saveOtpSecret(String otp) {
        this.sharedPreferences.edit().putString(PREF_OTP_SECRET, otp).commit();
    }

    public String getOtpSecret() {
        return this.sharedPreferences.getString(PREF_OTP_SECRET, "JBSWY3DPK5XXE3DEJ5TE6QKUJA");
    }

    public void saveMsk(String msk) {
        this.sharedPreferences.edit().putString(PREF_MSK, msk).commit();
    }

    public String getMsk() {
        return this.sharedPreferences.getString(PREF_MSK, "");
    }

}
