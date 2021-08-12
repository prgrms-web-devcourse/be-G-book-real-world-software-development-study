package com.eden6187.java.actors.impl;

import com.eden6187.java.actors.inter.BankTransactionParser;
import com.eden6187.java.domain.BankTransaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BankTransactionCSVParser implements BankTransactionParser {
    private static final DateTimeFormatter DATE_PATTERN =  DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public BankTransaction parseRow(final String line){
        final String[] columns = line.split(",");

        final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
        final double amount = Double.parseDouble(columns[1]);
        final String description = columns[2];

        return new BankTransaction(date, amount, description);
    }
    public List<BankTransaction> parseRows(final List<String> rows){
        final List<BankTransaction> bankTransactions = new ArrayList<>();
        rows.forEach((row) -> {bankTransactions.add(parseRow(row));});
        return bankTransactions;
    }
}
