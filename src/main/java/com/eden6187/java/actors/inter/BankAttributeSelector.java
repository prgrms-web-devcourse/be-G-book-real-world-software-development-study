package com.eden6187.java.actors.inter;

import com.eden6187.java.domain.BankTransaction;

@FunctionalInterface
public interface BankAttributeSelector {
    String selectAttribute(BankTransaction bankTransaction);
}
