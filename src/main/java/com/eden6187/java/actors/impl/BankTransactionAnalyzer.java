package com.eden6187.java.actors.impl;

import com.eden6187.java.actors.inter.BankTransactionParser;
import com.eden6187.java.domain.BankTransaction;
import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BankTransactionAnalyzer {

    private static final String RESOURCES = "src/main/resources/";
    private final String fileName;
    private final BankTransactionParser bankTransactionParser;

    public void analyze() throws IOException{
        final Path path = Paths.get(RESOURCES + this.fileName);
        final List<String> rows = Files.readAllLines(path);
        final List<BankTransaction> bankTransactions = this.bankTransactionParser.parseRows(rows);
        final BankTransactionProcessor bankTransactionProcessor = new BankTransactionProcessor(bankTransactions);

        collectSummaryAndPrint(bankTransactionProcessor);
        findMaxAndMinAmountAndPrintBetween(
                bankTransactionProcessor,
                LocalDate.of(2017,1,1),
                LocalDate.of(2017,12,31)
        );
        generateAndPrintHist(bankTransactionProcessor);
    }

    public void collectSummaryAndPrint(BankTransactionProcessor processor){
        System.out.println(processor.calculateTotalAmount());
    }

    public void findMaxAndMinAmountAndPrintBetween(BankTransactionProcessor processor, LocalDate start, LocalDate end){
        System.out.println(processor.findMaxBetweenDate(start, end));
        System.out.println(processor.findMinBetweenDate(start, end));
    }

    public void generateAndPrintHist(BankTransactionProcessor processor){
        Map<String, List<BankTransaction>> histByMonth = processor.generateHist(bankTransaction -> bankTransaction.getDate().getMonth().toString()); // 월별
        printHist(histByMonth);

        Map<String, List<BankTransaction>> histByDescription = processor.generateHist(BankTransaction::getDescription);
        printHist(histByDescription);
    }

    private void printHist(Map<String, List<BankTransaction>> hist){
        System.out.println("Print Hist");
        hist.forEach((s, bankTransactions) -> bankTransactions.forEach(System.out::println));
        System.out.println();
    }
}
