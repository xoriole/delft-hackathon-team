package com.github.sofaid.app.models.remote;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String bsn;
    @SerializedName("session_token")
    private String sessionToken;
    @SerializedName("totp_token")
    private String totpToken;
}