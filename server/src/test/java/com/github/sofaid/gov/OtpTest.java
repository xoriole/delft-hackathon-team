package com.github.sofaid.gov;

import org.jboss.aerogear.security.otp.Totp;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class OtpTest {

    @Test
    public void testOtp(){
//        String secret = "somesecret here";
        String secret = "whatever is here is okay";
        Totp totp = new Totp(secret);
        String now = totp.now();
        assertThat(totp.verify(now)).isEqualTo(true);
    }

    @Test
    public void testOtpUri() throws MalformedURLException {
        String uri = "otpauth://totp/2FA-Demo:EXAMPLE110.00.110BSN?secret=VS2UL743KA5KQN6Q&issuer=2FA-Demo";

        int start = uri.indexOf("secret=")+7;
        int end = uri.substring(start).indexOf("&");
        System.out.println("start:"+ start+"; end:"+end);
        String secret = uri.substring(start, start+end);
        System.out.println("secret:" + secret);
//        URL url = new URL(uri);
//        String query = url.getQuery();
//        System.out.println("query:" + query);

    }
}
