package com.eden6187.java.domain;

import lombok.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class BankTransaction {
    private final LocalDate date;
    private final double amount;
    private final String description;

}