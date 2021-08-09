package real;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BankStatementCSVParser implements BankStatementParser {
    //CSV 파싱 로직 담당

    final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");// 날짜 포맷

//    private BankTransaction parseFromCSV(final String line) {
//        final String[] columns = line.split(","); //, 로 열 분리(30-01-2017 | -100 | Deliveroo)
//
//        final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);// 주어진 월 선택하기
//        final double amount = Double.parseDouble(columns[1]); //금액 가져오기
//        final String description = columns[2];//항목
//
//        return new BankTransaction(date, amount, description);
//    }
//
//    public List<BankTransaction> parseLinesFromCSV(final List<String> lines) {
//        final List<BankTransaction> bankTransactions = new ArrayList<>();
//        for (final String line: lines) {
//            bankTransactions.add(parseFromCSV(line));
//        }
//        return bankTransactions;
//    }

    @Override
    public BankTransaction parseFrom(String line) {
        final String[] columns = line.split(","); //, 로 열 분리(30-01-2017 | -100 | Deliveroo)

        final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);// 주어진 월 선택하기
        final double amount = Double.parseDouble(columns[1]); //금액 가져오기
        return new BankTransaction(date, amount, columns[2]);
    }

    @Override
    public List<BankTransaction> parseLinesFrom(List<String> lines) {
        return lines.stream().map(this::parseFrom).collect(toList());
    }
}
