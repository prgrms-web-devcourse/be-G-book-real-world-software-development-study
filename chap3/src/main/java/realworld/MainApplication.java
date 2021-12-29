package realworld;

import java.io.IOException;

public class MainApplication {
    public static void main(String[] args) throws IOException {

        final BankStatementAnalyzer bankStatementAnalyzer = new BankStatementAnalyzer();

        final BankStatementParser bankStatementParser = new BankStatementCSVParser();

        bankStatementAnalyzer.anaylze(args[0], bankStatementParser);
    }
}
