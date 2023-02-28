package com.loanCalculator;

import java.util.ArrayList;

public class LoanCalculator {

    private final double amount;
    private final int months;
    private final LoanType type;
    private final double yearlyRate;
    public LoanCalculator(double amount, int months, LoanType type, double yearlyRate) {
        if (
                !Double.isFinite(amount) || amount < 0 ||
                !Double.isFinite(yearlyRate) || yearlyRate < 0 ||
                months <= 0
        )
            throw new NumberFormatException();
        this.amount = amount;
        this.months = months;
        this.type = type;
        this.yearlyRate = yearlyRate;
    }

    ArrayList<LoanEntry> generateTable() {
        ArrayList<LoanEntry> data = new ArrayList<>();
        double left = amount;
        for (int month = 1; left > 0; ++month) {
            LoanEntry entry = new LoanEntry();
            entry.left = left;
            entry.month = month;
            if (this.type == LoanType.ANNUITY) {
                double i = yearlyRate / 1200;
                double k = (i * Math.pow(1 + i, months))/(Math.pow(1 + i, months) - 1);
                entry.interest = left * i;
                entry.credit = amount * k - entry.interest;
                if (!Double.isFinite(entry.credit) ||entry.credit < 0)
                    entry.credit = amount / months;
            }
            else {
                entry.credit = amount / months;
                entry.interest = left / 100 * yearlyRate / 12;
            }
            entry.credit = Math.min(entry.credit, left);
            left -= entry.credit;
            data.add(entry);
        }
        return data;
    }
}
