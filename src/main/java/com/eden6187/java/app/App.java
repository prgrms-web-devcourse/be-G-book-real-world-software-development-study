package com.eden6187.java.app;

import com.eden6187.java.actors.impl.BankTransactionAnalyzer;
import com.eden6187.java.actors.impl.BankTransactionCSVParser;

import java.io.IOException;


public class App {
    public static void main(String... args) throws IOException {
        BankTransactionAnalyzer bankTransactionAnalyzer = new BankTransactionAnalyzer(args[0], new BankTransactionCSVParser());
        bankTransactionAnalyzer.analyze();
    }
}
