package com.team.study.ch3;

import java.time.Month;

public class BankTransactionIsInFebruaryAndExpensive implements  BankTransactionFilter{
    @Override
    public boolean test(BankTransaction bankTransaction) {
        return (bankTransaction.getDate().getMonth() == Month.FEBRUARY
                && bankTransaction.getAmount() >= 1_000);
    }
}
