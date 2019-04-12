package com.github.sofaid.gov;

import org.jboss.aerogear.security.otp.Totp;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OtpTest {

    @Test
    public void testOtp(){
        String secret = "somesecret here";
        Totp totp = new Totp(secret);
        String now = totp.now();
        assertThat(totp.verify(now)).isEqualTo(true);
    }
}
