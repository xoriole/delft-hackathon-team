package com.github.sofaid.app.utils;

import com.github.sofaid.app.exceptions.ApiError;
import com.github.sofaid.app.exceptions.NcrException;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import timber.log.Timber;

/**
 * Created by robik on 6/15/17.
 */

public class ErrorMessageUtil {

    public static final String logExceptionAndGetErrorMessage(Throwable ex) {
        Timber.e(ex, "Error");
        if (ex instanceof NcrException) {
            NcrException ncrException = (NcrException) ex;
            return ncrException.getMessage();
        } else if (ex instanceof HttpException) {
            return getUserReadableApiErrorMsg(ex);
        } else {
            return "Got an Error!";
        }
    }

    public static final String getErrorMessage(Throwable ex) {
        if (ex instanceof NcrException) {
            NcrException ncrException = (NcrException) ex;
            return ncrException.getMessage();
        } else if (ex instanceof HttpException) {
            return getUserReadableApiErrorMsg(ex);
        } else {
            return "Got an Error!";
        }
    }

    public static final String getUserReadableApiErrorMsg(Throwable ex) {
        ApiError apiError = getApiError(ex);
        if (apiError != null && apiError.getUserReadableMessage() != null && apiError.getUserReadableMessage()) {
            if (apiError.getMessages() != null && !apiError.getMessages().isEmpty()) {
                return apiError.getMessages().get(0);
            }
        }
        return "Something went wrong at the server side!";
    }

    public static final ApiError getApiError(Throwable ex) {
        if (ex instanceof HttpException) {
            HttpException httpException = (HttpException) ex;
            try {
                String errorBody = httpException.response().errorBody().string();
                ApiError apiError = GsonUtil.parse(errorBody, ApiError.class);
                return apiError;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
