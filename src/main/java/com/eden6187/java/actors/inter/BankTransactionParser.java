package com.eden6187.java.actors.inter;

import com.eden6187.java.domain.BankTransaction;

import java.util.List;

public interface BankTransactionParser{
    BankTransaction parseRow(String row);
    List<BankTransaction> parseRows(List<String> rows);
}
