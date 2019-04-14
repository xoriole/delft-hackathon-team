package com.github.sofaid.app.services.remote_api;

import com.github.sofaid.app.models.remote.AccountPublicInfo;
import com.github.sofaid.app.models.remote.BlockchainAccount;
import com.github.sofaid.app.models.remote.SignupRequest;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface AccountApiService {
    String API_PREFIX = "/api/v1/";

    @POST(API_PREFIX+"accounts")
    Observable<Void> signup(@Body SignupRequest request);

    @GET(API_PREFIX+"accounts/public_info")
    Observable<AccountPublicInfo> getAccountPublicInfo(@Query("id") String id,
                                                       @Query("id_type") String idType);
    @GET(API_PREFIX+"accounts/blockchain")
    Observable<BlockchainAccount> getBlockchainAccount(@Query("address") String ncrAddress);

}
