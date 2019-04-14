package com.github.sofaid.gov;

import com.codahale.shamir.Scheme;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;

public class ShamirTests {

    @Test
    public void shamirTests(){
        final Scheme scheme = new Scheme(new SecureRandom(), 5, 3);
        final byte[] secret = "hello there".getBytes(StandardCharsets.UTF_8);
        final Map<Integer, byte[]> parts = scheme.split(secret);

        final byte[] recovered = scheme.join(parts);
        System.out.println(new String(recovered, StandardCharsets.UTF_8));
    }
}
