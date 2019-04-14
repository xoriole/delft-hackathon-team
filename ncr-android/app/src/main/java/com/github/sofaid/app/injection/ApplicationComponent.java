package com.github.sofaid.app.injection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.sofaid.app.ethereum.ContractService;
import com.github.sofaid.app.services.remote_api.EnrollmentApiService;
import com.google.gson.Gson;
import com.github.sofaid.app.services.remote_api.AccountApiService;
import com.github.sofaid.app.services.remote_api.TransactionApiService;
import com.github.sofaid.app.services.repository.DaoService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    Gson gson();

    SharedPreferences sharedPreferences();

    DaoService daoService();

    AccountApiService accountApiService();

    TransactionApiService transactionApiService();

    EnrollmentApiService enrollmentApiService();

    ContractService contractService();
}
