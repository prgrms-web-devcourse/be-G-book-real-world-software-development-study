package com.eden6187.java;

import com.eden6187.java.actors.impl.BankTransactionCSVParser;
import com.eden6187.java.domain.BankTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

public class BankTransactionCSVParserTest {
    private final BankTransactionCSVParser statementParser = new BankTransactionCSVParser();

    @Test
    public void shouldParseOneCorrectLine(){
        String row = "30-01-2017,-50,Tesco";
        BankTransaction bankTransaction = new BankTransaction(
                LocalDate.of(2017, Month.JANUARY, 30),
                -50,
                "Tesco"
        );

        BankTransaction parsedRow = statementParser.parseRow(row);

        Assertions.assertEquals(bankTransaction, parsedRow);
    }
}