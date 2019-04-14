package com.github.sofaid.app.utils;

import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Created by robik on 6/17/17.
 */

public class NumberUtils {
    public static MathContext currencyMathContext() {
        return new MathContext(8, RoundingMode.HALF_UP);
    }
}
