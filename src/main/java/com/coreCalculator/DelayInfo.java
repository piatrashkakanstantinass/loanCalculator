package com.coreCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DelayInfo {

    int from, duration;
    BigDecimal rate;
    public DelayInfo(int from, int duration, BigDecimal rate) {
        this.from = from;
        this.duration = duration;
        this.rate = rate.setScale(2, RoundingMode.HALF_UP);
        if (from < 0 || duration < 0 || rate.compareTo(BigDecimal.ZERO) < 0)
            throw new NumberFormatException();
    }
}
