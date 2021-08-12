package com.programmers.java.chapter3;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BankStatementProcessor {

    private final List<BankTransaction> bankTransactions;

    public BankStatementProcessor(final List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public double summarizeTransactions(final BankTransactionSummarizer bankTransactionSummarizer) {
        double result = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            result = bankTransactionSummarizer.summarize(result, bankTransaction);
        }

        return result;
    }

    public double calculateTotalAmount() {
        double total = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            total += bankTransaction.getAmount();
        }
        return total;
    }

    public double calculateTotalInMonth(final Month month) {
        double total = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month)
                total += bankTransaction.getAmount();
        }
        return total;
    }

    public double calculateTotalForCategory(final String category) {
        double total = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDescription().equals(category))
                total += bankTransaction.getAmount();
        }
        return total;
    }

    // 개방/폐쇄 원칙을 적용한 후 유연해진 findTransactions() 메서드
    public List<BankTransaction> findTransactions(final BankTransactionFilter bankTransactionFilter) {
        final List<BankTransaction> result = new ArrayList<>();

        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransactionFilter.test(bankTransaction)) {
                result.add(bankTransaction);
            }
        }

        return result;
    }

    public List<BankTransaction> findTransactionsGreaterThanEqual(final int amount) {
        /** case 1.
         * 특정 BankTransactionFilter 구현(class BankTransactionIsInFebruaryAndExpensive)으로 findTransactions () 호출
         *
         * final List<BankTransaction> transactions = findTransactions(new BankTransactionIsInFebruaryAndExpensive());
         */
        /** case 2.
         *  람다 표현식으로 BankTransactionFilter 구현하기
         *
         *  final List<BankTransaction> transactions = findTransactions(bankTransaction ->
         *      bankTransaction.getDate().getMont() == Month.FEBRUARY
         *      && bankTransaction.getAmount() >= 1_000);
         *      );
         */
        return findTransactions(bankTransaction -> bankTransaction.getAmount() >= amount);
    }
}
