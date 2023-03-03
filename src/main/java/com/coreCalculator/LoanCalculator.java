package com.coreCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class LoanCalculator {

    private final BigDecimal amount;
    private final int months;
    private final LoanType type;
    private final BigDecimal yearlyRate;
    private DelayInfo delayInfo;

    public LoanCalculator(BigDecimal amount, int months, LoanType type, BigDecimal yearlyRate, DelayInfo delayInfo) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        int delayDuration = 0;
        if (delayInfo != null)
            delayDuration = delayInfo.duration;
        this.months = months + delayDuration;
        this.type = type;
        this.yearlyRate = yearlyRate.setScale(2, RoundingMode.HALF_UP);
        this.delayInfo = delayInfo;
        if (
                this.amount.compareTo(BigDecimal.ZERO) < 0 ||
                this.yearlyRate.compareTo(BigDecimal.ZERO) < 0 ||
                months <= 0
        )
            throw new NumberFormatException();
    }

    public ArrayList<LoanEntry> generateTable(LoanType type) {
        /*ArrayList<LoanEntry> data = new ArrayList<>();
        BigDecimal left = amount;
        for (int month = 1; left.compareTo(BigDecimal.ZERO) > 0; ++month) {
            LoanEntry entry = new LoanEntry();
            entry.setLeft(left);
            entry.setMonth(month);
            // TODO: FIX LOGIC
            if (type == LoanType.ANNUITY) {
                BigDecimal i = yearlyRate.divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP);
                BigDecimal i1 = i.add(BigDecimal.ONE);
                try {
                    BigDecimal k = i.multiply(i1.pow(months)).divide(i1.pow(months).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
                    entry.setCredit(amount.multiply(k).subtract(entry.getInterest()));
                    // (i * Math.pow(1 + i, months))/(Math.pow(1 + i, months) - 1);
                } catch (ArithmeticException e) {
                    entry.setCredit(amount.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP));
                }
                entry.setInterest(left.multiply(i));
                //entry.credit = amount * k - entry.interest;

                /*if (!Double.isFinite(entry.credit) ||entry.credit < 0)
                    entry.credit = amount / months;
            }
            else {
                entry.setCredit(amount.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP));
                entry.setInterest(left.multiply(yearlyRate).divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP));
            }
            entry.setCredit(entry.getCredit().min(left));
            left = left.subtract(entry.getCredit());
            data.add(entry);
        }
        return data;*/
        ArrayList<LoanEntry> data = new ArrayList<>();
        BigDecimal left = amount;
        int creditMonths = months;
        if (delayInfo != null)
            creditMonths -= delayInfo.duration;
        for (int month = 1; month <= months; ++month) {
            BigDecimal rate = yearlyRate;
            boolean countCredit = true;
            if (delayInfo != null && delayInfo.from <= month && month < delayInfo.duration + delayInfo.from) {
                countCredit = false;
                rate = delayInfo.rate;
            }
            LoanEntry entry = new LoanEntry();
            entry.setLeft(left);
            entry.setMonth(month);

            if (type == LoanType.LINEAR) {
                if (month == months)
                    entry.setCredit(left);
                else
                    entry.setCredit(amount.divide(BigDecimal.valueOf(creditMonths), 2, RoundingMode.HALF_DOWN));
                entry.setInterest(left.multiply(rate).divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP));
            }
            else {

            }

            entry.setCredit(entry.getCredit().min(left));
            if (!countCredit)
                entry.setCredit(BigDecimal.ZERO);
            left = left.subtract(entry.getCredit());
            data.add(entry);
        }
        return data;
    }

    public ArrayList<LoanEntry> generateTable() {
        return generateTable(type);
    }
}
