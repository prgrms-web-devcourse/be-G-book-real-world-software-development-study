package com.team.study.ch3;

// BankTransaction의 선택 조건을 결정하는 인터페이스
@FunctionalInterface
public interface BankTransactionFilter {
    boolean test(BankTransaction bankTransaction);
}
