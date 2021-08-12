package com.eden6187.java.actors.impl;

import com.eden6187.java.actors.inter.BankAttributeSelector;
import com.eden6187.java.domain.BankTransaction;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BankTransactionProcessor {
    private final List<BankTransaction> bankTransactions;

    public double calculateTotalAmount(){
        double amount = 0d;
        for (BankTransaction transaction : bankTransactions) { amount += transaction.getAmount(); }
        return amount;
    }

//    아래와 같은 코드는 DRY원칙에 위배되며, OCP에도 위배된다.
//    특정한 조건을 만족시키는 BankTransaction을 찾기 위해서 항상 새로운 메소드를 구현해 주는 것은 코드 중복을 증가시킨다. (DRY 위배)
//    또한, 새로운 기능이 추가 될 때마다 새로운 메소드가 BankTransaction에 추가 되어야 한다. (OCP 위배)

//    public double findMaxBetweenDate(final LocalDate start, final LocalDate end){
//        double max = Double.MIN_VALUE;
//        for (BankTransaction bankTransaction : bankTransactions) {
//            LocalDate date = bankTransaction.getDate();
//            if(date.isAfter(end)) continue;
//            if(date.isBefore(start)) continue;
//            max = Math.max(bankTransaction.getAmount(), max);
//        }
//        return max;
//    }
//
//    public double findMinBetweenDate(final LocalDate start, final LocalDate end){
//        double min = Double.MAX_VALUE;
//        for (BankTransaction bankTransaction : bankTransactions) {
//            LocalDate date = bankTransaction.getDate();
//            if(date.isAfter(end)) continue;
//            if(date.isBefore(start)) continue;
//            min = Math.min(bankTransaction.getAmount(), min);
//        }
//        return min;
//    }


    public List<BankTransaction> findTransactions(Predicate<BankTransaction> bankTransactionFilter){
        return bankTransactions.stream()
                .filter(bankTransactionFilter)
                .collect(Collectors.toList());
    }

    public double summarizeTransactions(Function<BankTransaction, Double> summarizer){
        double summary = 0;
        for (BankTransaction bankTransaction : bankTransactions) summary += summarizer.apply(bankTransaction);
        return summary;
    }

    public Map<String, List<BankTransaction>> generateHist(BankAttributeSelector bankAttributeSelector){
        Map<String, List<BankTransaction>> hist = new HashMap<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            String attribute = bankAttributeSelector.selectAttribute(bankTransaction);
            hist.putIfAbsent(attribute, new ArrayList<>());
            List<BankTransaction> transactions = hist.get(attribute);
            transactions.add(bankTransaction);
            hist.put(attribute,transactions);
        }
        return hist;
    }
}
