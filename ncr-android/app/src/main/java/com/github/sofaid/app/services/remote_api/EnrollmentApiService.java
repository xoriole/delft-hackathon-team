package com.github.sofaid.app.services.remote_api;

import com.github.sofaid.app.models.remote.AccountPublicInfo;
import com.github.sofaid.app.models.remote.AuthRequest;
import com.github.sofaid.app.models.remote.BlockchainAccount;
import com.github.sofaid.app.models.remote.SignupRequest;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface EnrollmentApiService {
    @POST("/register")
    Observable<Void> register(@Body SignupRequest request);

    @GET("/keymaster/ping")
    Observable<Void> ping();

    @POST("/keymaster/userqr/session_validator")
    Observable<Void> login(@Body AuthRequest request);

    @GET("/accounts/blockchain")
    Observable<BlockchainAccount> getBlockchainAccount(@Query("address") String ncrAddress);

}
