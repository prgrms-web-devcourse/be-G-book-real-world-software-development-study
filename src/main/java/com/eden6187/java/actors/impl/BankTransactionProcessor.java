package com.eden6187.java.actors.impl;

import com.eden6187.java.actors.inter.BankAttributeSelector;
import com.eden6187.java.domain.BankTransaction;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
public class BankTransactionProcessor {
    private final List<BankTransaction> bankTransactions;

    public double calculateTotalAmount(){
        double amount = 0d;
        for (BankTransaction transaction : bankTransactions) { amount += transaction.getAmount(); }
        return amount;
    }

    public double findMaxBetweenDate(final LocalDate start, final LocalDate end){
        double max = Double.MIN_VALUE;
        for (BankTransaction bankTransaction : bankTransactions) {
            LocalDate date = bankTransaction.getDate();
            if(date.isAfter(end)) continue;
            if(date.isBefore(start)) continue;
            max = Math.max(bankTransaction.getAmount(), max);
        }
        return max;
    }

    public double findMinBetweenDate(final LocalDate start, final LocalDate end){
        double min = Double.MAX_VALUE;
        for (BankTransaction bankTransaction : bankTransactions) {
            LocalDate date = bankTransaction.getDate();
            if(date.isAfter(end)) continue;
            if(date.isBefore(start)) continue;
            min = Math.min(bankTransaction.getAmount(), min);
        }
        return min;
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
