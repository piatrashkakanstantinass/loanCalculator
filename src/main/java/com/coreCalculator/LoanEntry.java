package com.coreCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoanEntry {
    private int month;
    private BigDecimal left = BigDecimal.ZERO;
    private BigDecimal interest = BigDecimal.ZERO;
    private BigDecimal credit = BigDecimal.ZERO;

    public void setMonth(int month) {
        this.month = month;
    }

    public void setLeft(BigDecimal left) {
        this.left = left.setScale(2, RoundingMode.HALF_UP);
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest.setScale(2, RoundingMode.HALF_UP);
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit.setScale(2, RoundingMode.HALF_UP);
    }

    public int getMonth() {
        return month;
    }

    public BigDecimal getLeft() {
        return left;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public BigDecimal getTotal() {
        return interest.add(credit);
    }
}
