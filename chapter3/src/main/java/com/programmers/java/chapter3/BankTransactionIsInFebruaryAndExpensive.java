package com.programmers.java.chapter3;

import java.time.Month;

public class BankTransactionIsInFebruaryAndExpensive implements BankTransactionFilter {

    @Override
    public boolean test(BankTransaction bankTransaction) {
        /** Stream API를 사용할줄 안다면 이를 이용해 수많은 연산 패턴을 쉽게 구현할 수 있다.
         * bankTransactions.stream().filter(bankTransaction -> bankTransaction.getAmount() >= 1_000).collect(Collectors.toList())
         */
        return bankTransaction.getDate().getMonth() == Month.FEBRUARY && bankTransaction.getAmount() >= 1_000;
    }
}
