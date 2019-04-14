package com.github.sofaid.app.injection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.sofaid.app.ethereum.ContractService;
import com.github.sofaid.app.services.remote_api.EnrollmentApiService;
import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sofaid.app.constants.API;
import com.github.sofaid.app.helpers.ApplicationStateTracker;
import com.github.sofaid.app.helpers.PreferencesHelper;
import com.github.sofaid.app.services.remote_api.AccountApiService;
import com.github.sofaid.app.services.remote_api.TransactionApiService;
import com.github.sofaid.app.utils.GsonUtil;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application application;
    protected final SharedPreferences pref;
    private final ApplicationStateTracker applicationStateTracker;

    public static final String TOKENIZED_CLIENT = "tokenizedClient";
    public static final String NON_TOKENIZED_CLIENT = "nonTokenizedClient";
    public static final String TOKENIZED_RETROFIT = "tokenizedRetrofit";
    public static final String NON_TOKENIZED_RETROFIT = "nonTokenizedClient";

    public ApplicationModule(Application application) {
        this.application = application;
        pref = this.application.getSharedPreferences(PreferencesHelper.PREF_FILE_NAME, Context.MODE_PRIVATE);
        this.applicationStateTracker = new ApplicationStateTracker();
        application.registerActivityLifecycleCallbacks(applicationStateTracker);
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    @ApiUrl
    String provideApiUrl() {
        return API.BASE_URL;
    }

    @Provides
    @Singleton
    public SharedPreferences sharedPreferences() {
        return pref;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return GsonUtil.buildGson();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    @Named(NON_TOKENIZED_CLIENT)
    OkHttpClient provideNonTokenizedClient(final PreferencesHelper prefs, HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    @Named(NON_TOKENIZED_RETROFIT)
    Retrofit provideNonTokenizedRetrofit(@Named(NON_TOKENIZED_CLIENT) OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ApplicationStateTracker provideApplicationStateTracker() {
        return applicationStateTracker;
    }

    /*
    Remote API services
     */
    @Provides
    @Singleton
    AccountApiService provideAccountApiService(@Named(NON_TOKENIZED_RETROFIT) Retrofit retrofit) {
        return retrofit.create(AccountApiService.class);
    }

    @Provides
    @Singleton
    TransactionApiService provideTransactionApiService(@Named(NON_TOKENIZED_RETROFIT) Retrofit retrofit) {
        return retrofit.create(TransactionApiService.class);
    }

    @Provides
    @Singleton
    EnrollmentApiService provideEnrollmentApiService(@Named(NON_TOKENIZED_RETROFIT) Retrofit retrofit) {
        return retrofit.create(EnrollmentApiService.class);
    }

    @Provides
    @Singleton
    ContractService provideContractService(@Named(NON_TOKENIZED_RETROFIT) Retrofit retrofit) {
        return new ContractService();
    }
}