package com.github.sofaid.app.services.remote_api;

import com.github.sofaid.app.models.remote.TransactionRequest;
import com.github.sofaid.app.models.remote.TransactionResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface TransactionApiService {

    @POST("transactions")
    Observable<TransactionResponse> createTransaction(@Body TransactionRequest request);

}
