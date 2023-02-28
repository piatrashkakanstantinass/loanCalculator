package com.loanCalculator;

public class LoanEntry {
    int month;
    double left;
    double interest;
    double credit;

    public int getMonth() {
        return month;
    }

    public double getLeft() {
        return left;
    }

    public double getInterest() {
        return interest;
    }

    public double getCredit() {
        return credit;
    }

    public double getTotal() {
        return interest + credit;
    }
}
