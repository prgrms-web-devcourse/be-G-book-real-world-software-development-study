package com.programmers.java.chapter3;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        final BankTransactionAnalyzerSimple bankTransactionAnalyzerSimple
                = new BankTransactionAnalyzerSimple();

        final BankStatementParser bankStatementParser
                = new BankStatementCSVParser();

        bankTransactionAnalyzerSimple.analyze(args[0], bankStatementParser);

    }
}
