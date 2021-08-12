package real;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;

//import static org.junit.jupiter.api.Assertions.*;

public class BankStatementCSVParserTest {

    @Test
    public void test() {
//        Assert.fail("Not yet");

        String line = "30-01-2017,-50,Tesco";

        BankTransaction result = statementParser.parseFrom(line);

        BankTransaction expected = new BankTransaction(LocalDate.of(2017, Month.JANUARY, 30), -50, "Tesco");
        Assert.assertEquals(expected.getDate(), result.getDate());
        Assert.assertEquals(expected.getAmount(), result.getAmount(), 0.0d);
        Assert.assertEquals(expected.getDescription(), result.getDescription());
    }
}