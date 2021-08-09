package com.team.study;

import com.team.study.ch2.BankStatementCSVParser;
import com.team.study.ch2.BankStatementParser;
import com.team.study.ch2.BankTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

/**
 *  1. 테스트의 콘텍스트를 설정한다. 예제에서는 파싱할 행을 설정한다.
 *  2. 동작을 실행한다. 예제에서는 입력 행을 파싱한다.
 *  3. 예상된 결과를 어서션으로 지정한다. 예제에서는 날짜, 금액, 설명이 제대로 파싱되었는지 확인한다.
 *
 *  → Given-When-Then 공식
 */
public class BankStatementCSVParserTest {
    private final BankStatementParser statementParser = new BankStatementCSVParser();

    @Test
    public void shouldParseOneCorrectLine() throws Exception {

        final String line = "30-01-2017,-50,Tesco";

        final BankTransaction result = statementParser.parseFrom(line);

        final BankTransaction expected = new BankTransaction(LocalDate.of(2017, Month.JANUARY, 30), -50, "Tesco");
        final double tolerance = 0.0d;

        Assertions.assertEquals(expected.getDate(), result.getDate());
        Assertions.assertEquals(expected.getAmount(), result.getAmount(), tolerance);
        Assertions.assertEquals(expected.getDescription(), result.getDescription());

    }
}
