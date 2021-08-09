# 입출금 내역 분석기

## 도전 과제

소비 내역 자동 요약 - 입출금 내역을 자동으로 분석해 재정 상태를 더 잘 보여주는 소프트웨어 개발

## 목표

- 한 개의 클래스로 문제 구현해보고 프로젝트 진행에 따라 바뀌는 요구 사항이나 유지보수에 대응하며 기존 구조의 한계 확인
- 단일 책임 원칙(SRP), 응집도와 결합도의 특징 알기

## 요구사항

- 은행 입출금 내역의 총 수입과 총 지출 (양수, 음수)
- 특정 달 입출금 내역의 건 수
- 지출이 가장 높은 상위 10건
- 돈을 가장 많이 소비하는 항목

## KISS(keep it short and simple) 원칙

소프트웨어 디자인을 간단하고 단순하게 하는 것을 말한다.

- KISS 원칙에 따라 응용프로그램 코드를 한 개의 클래스로 구현한다.

  - 총 수입과 총 지출 구하기

    ```java
    public class BankTransactionAnalyzerSimple {
      private static final String RESOURCES = "src/main/resources/";
    
      public static void main(String[] args) throws IOException {
    
        final Path path = Paths.get(RESOURCES + args[0]);
        final List<String> lines = Files.readAllLines(path);
        double total = 0d;
        for (final String line : lines) { // 각 행
          final String[] columns = line.split(","); // 콤마로 열 분리
          final double amount = Double.parseDouble(columns[1]); // 금액 추출, 금액을 double로 파싱
          total += amount;
        }
        System.out.println("The total for all transactions is " + total);
      }
    }
    ```

  - 특정 달의 입출금 내역 발생 건 수

    ```java
    public class BankTransactionAnalyzerSimple {
      private static final String RESOURCES = "src/main/resources/";
    
      public static void main(String[] args) throws IOException {
    
        final Path path = Paths.get(RESOURCES + args[0]);
        final List<String> lines = Files.readAllLines(path);
        double total = 0d;
        final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (final String line : lines) {
          final String[] columns = line.split(",");
          final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
          if (date.getMonth() == Month.JANUARY) {
            final double amount = Double.parseDouble(columns[1]);
            total += amount;
          }
        }
    
        System.out.println("The total for all transactions in January is " + total);
      }
    }
    ```

    

  ### final 변수

  - final로 정의된 지역 변수나 필드에 값을 재할당할 수 없다.
  - final 사용에 따른 장단점이 존재한다. 
  - 사용 여부는 팀과 프로젝트에 따라 달라진다.
  - 가능한 많은 변수를 final로 표시하면 어떤 객체의 상태가 바뀔 수 있고, 어떤 객체의 상태가 바뀔 수 없는지 명확하게 구분할 수 있다.
  - final 키워드를 적용한다고 해서 객체가 바뀌지 못하는 것은 아니다. final 필드로 가리키는 객체라도 가변 상태(mutable state)를 포함하기 때문이다.
  - final 키워드가 쓸모없는 상황 : 추상 메서드(인터페이스 내)의 메서드 파라미터에 final을 사용하는 상황
    - 실제 구현이 없으므로 final 키워드의 의미가 무력화된다.
  - 자바 10에서 var 키워드 등장 -> final 유용성 감소



## 코드 유지보수성과 안티패턴

- 코드를 구현할 때는 코드 유지보수성을 높이기 위해 노력한다.
  - 특정 기능을 담당하는 코드를 쉽게 찾을 수 있어야 한다.
  - 코드가 어떤 일을 수행하는지 쉽게 이해할 수 있어야 한다.
  - 새로운 기능을 쉽게 추가하거나 기존 기능을 쉽게 제거할 수 있어야 한다.
  - 캡슐화가 잘 되어 있어야 한다.
    - 코드 사용자에게는 세부 구현 내용이 감춰져 있으므로 사용자가 쉽게 코드를 이해하고, 기능을 바꿀 수 있어야 한다.

### 안티패턴

복사, 붙여넣기와 같은 효과적이지 않은 해결 방법을 **안티 패턴**이라고 한다.

1. 갓 클래스 안티 패턴 : 한 클래스로 모든 것을 해결하는 패턴이다. 클래스의 목적이 무엇인지 이해하기 어려워진다.
2. 코드 중복 : 현재 구현은 한 가지 문제만 해결하도록 하드코딩되어 있고, 여러 곳에 이 코드가 중복되어 있어 기존의 기능을 바꾸기 어렵다.

- 코드를 간결하게 유지하는 것도 중요하지만, KISS 원칙을 남용하지 않아야 한다.
- 전체 응용프로그램의 설계를 되돌아보고, 한 문제를 작은 개별 문제로 분리해 더 쉽게 관리할 수 있는지 파악해야 한다.



## 단일 책임 원칙(SRP)

쉽게 관리하고 유지보수하는 코드를 구현하는 데 도움을 주는 포괄적인 소프트웨어 개발 지침

두 가지를 보완하기 위해 SRP를 적용한다.

```
1. 한 클래스는 한 기능만 책임진다.
2. 클래스가 바뀌어야 하는 이유는 오직 하나여야 한다.
```

SRR는 일반적으로 **클래스**와 **메서드**에 적용한다.

SRP를 적용하면 코드가 바뀌어야 하는 이유가 한 가지로 제한되므로 더 튼튼한 코드를 만들 수 있다. 

메인 클래스는 여러 책임을 모두 포함 -> 개별로 분리

```
1. 입력 읽기
2. 주어진 형식의 입력 파싱
3. 결과 처리
4. 결과 요약 리포트
```

1. 다른 문제 구현에 이를 활용할 수 있도록 **CSV 파싱 로직을 새로운 클래스로 분리**한다. 

   ```java
   public class BankStatementCSVParser {
   
     private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyy");
   
     public BankTransaction parseFromCSV(final String line) {
       final String[] columns = line.split(",");
   
       final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
       final double amount = Double.parseDouble(columns[1]);
       final String description = columns[2];
   
       return new BankTransaction(date, amount, description);
     }
   
     public List<BankTransaction> parseLinesFromCSV(final List<String> lines) {
       final List<BankTransaction> bankTransactions = new ArrayList<>();
       for (final String line :
            lines) {
         bankTransactions.add(parseFromCSV());
       }
       return bankTransactions;
     }
   }
   ```

   - parseFromCSV(), parseLinesFromCSV() 라는 BankTransaction 객체를 생성하는 두 메서드(~~클래스~~)를 정의한다.

2. BankTransaction : **도메인 클래스**. 입출금 내역 표현

   - 응용 프로그램의 다른 부분에서 입출금 내역 부분이라는 의미를 공유할 수 있어 매우 유용하다.
   - equals(), hashCode() 메서드 구현을 제공한다.

   ```java
   public class BankTransaction {
     private final LocalDate date;
     private final double amount;
     private final String description;
   
     public BankTransaction(final LocalDate date, final double amount, final String description) {
       this.date = date;
       this.amount = amount;
       this.description = description;
     }
   
     public LocalDate getDate() {
       return date;
     }
   
     public double getAmount() {
       return amount;
     }
   
     public String getDescription() {
       return description;
     }
   
     @Override
     public String toString() {
       return "BankTransaction{" +
         "date=" + date +
         ", amount=" + amount +
         ", description='" + description + '\'' +
         '}';
     }
   
   
     @Override
     public boolean equals(Object o) {
       if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;
       BankTransaction that = (BankTransaction) o;
       return Double.compare(that.amount, amount) == 0 &&
         date.equals(that.date) &&
         description.equals(that.description);
     }
   
     @Override
     public int hashCode() {
       return Objects.hash(date, amount, description);
     }
   }
   ```

3. BankStatementCSVParser의 parseLinesFromCSV() 메서드를 사용해 기존 코드를 리팩터링한다.

   ```java
   // 입출금 내역 CSV 파서 사용하기
   
   final BankStatementCSVParser bankStatementParser = new BankStatementCSVParser();
   
   final String fileName = args[0];
   final Path path = Paths.get(RESOURCES + fileName);
   final List<String> lines = Files.readAllLines(path);
   
   final List<BankTransaction> bankTransactions = bankStatementParser.parseLinesFromCSV(lines);
   
   System.out.println("The total for all transactions is " + calculateTotalAmount(bankTransactions));
   System.out.println("Transctions in January " + selectInMonth(BankTransaction, Month.JANUARY));
   ```

   - 구현 코드의 BankTransaction 객체에서 직접 정보를 추출하기 때문에 내부 파싱 방법을 알 필요가 없어졌다.

   - calculateTotalAmount(), selectInMonth() 메서드

     - 입출금 내역 목록 처리 - 거래 목록을 처리해 결과를 반환한다.

     ```java
     public static Double calculateTotalAmount(final List<BankTransaction> bankTransactions) {
       double total = 0d;
       for (final BankTransaction bankTransaction : bankTransactions) {
         total += bankTransaction.getAmount();
       }
       return total;
     }
     
     public static List<BankTransaction> selectInMonth(final List<BankTransaction> bankTransactions, final Month month) {
       final List<BankTransaction> bankTransactionsInMonth = new ArrayList<>();
       for (final BankTransaction bankTransaction : bankTransactions) {
         if (bankTransaction.getDate().getMonth() == month) {
           bankTransactionsInMonth.add(bankTransaction);
         }
       }
       return bankTransactionsInMonth;
     }
     ```

   - 리팩터링 덕분에 메인 응용프로그램에서 파싱 로직을 구현하는 부분이 사라졌다.
   - 파싱 기능을 **다른 클래스와 메서드에 위임**했고, 이 기능을 **독립적으로 구현**했다.
   - 새 요구 사항이 들어오면 BankStatementCSVParser 클래스로 캡슐화된 기능을 재사용해 구현한다.
   - 파싱 알고리즘 동작 방식을 바꿔야 하는 일이 생겨도 한 곳의 코드만 바꾸면 된다.
   - BankTransaction 클래스 덕분에 **다른 코드가 특정 데이터 형식에 의존하지 않게 되었다.**

   

   메서드를 구현할 떄는 **놀람 최소화 원칙**을 따라야 한다.

   - 메서드가 수행하는 일을 바로 이해할 수 있도록 자체 문서화를 제공하는 메서드명을 사용한다.
   - 코드의 다른 부분이 파라미터의 상태에 의존할 수 있으므로 파라미터의 상태를 바꾸지 않는다.



## 응집도

**서로 어떻게 관련되어 있는지**를 가리킨다. 정확히는, 클래스나 메서드의 책임이 서로 얼마나 강하게 연결되어 있는지를 측정한다.

- 소프트웨어의 복잡성을 유추하는 데 도움을 준다.
- 응집도 개념은 클래스(클래스 수준 응집도)에 즉용하지만, 이를 메서드(메서드 수준 응집도)에도 적용할 수 있다.

예제의 BankStatementAnalyzer 클래스는 응용프로램의 다양한 부분을 연결한다. -> 응집도가 떨어진다.

계산 연산을 BankStatementProcessor 라는 별도의 클래스로 추출한다. 

- 메서드 시그니처는 훨씬 단순해지고 BankStatementProcessor의 응집도도 개선된다.
- 응용프로그램의 다른 부분에서 BankStatementAnalyzer 전체 클래스를 의존하지 않고도 BankStatementProcessor의 메서드를 재사용할 수 있다.

```java
// 계산 연산 그룹화
public class BankStatementProcessor {

  private final List<BankTransaction> bankTransactions;

  public BankStatementProcessor(final List<BankTransaction> bankTransactions) {
    this.bankTransactions = bankTransactions;
  }

  public double calculateTotalAmount() {
    double total = 0;
    for (final BankTransaction bankTransaction : bankTransactions) {
      total += bankTransaction.getAmount();
    }
    return total;
  }

  public double calculateTotalInMonth(final Month month) {
    double total = 0;
    for (final BankTransaction bankTransaction : bankTransactions) {
      if (bankTransaction.getDate().getMonth() == month) {
        total += bankTransaction.getAmount();
      }
    }
    return total;
  }

  public double calculateTotalForCategory(final String category) {
    double total = 0;
    for (final BankTransaction bankTransaction : bankTransactions) {
      if (bankTransaction.getDescription().equals(category)) {
        total += bankTransaction.getAmount();
      }
    }
    return total;
  }
}
```

```java
// BankStatementProcessor 클래스를 이용해 입출금 내역 목록 처리
public class BankStatementAnalyzer {
    private static final String RESOURCES = "src/main/resources/";
    private static final BankStatementCSVParser bankStatementParser = new BankStatementCSVParser();

    public static void main(String[] args) throws IOException {

        final String fileName = args[0];
        final Path path = Paths.get(RESOURCES + fileName);
        final List<String> lines = Files.readAllLines(path);

        final List<BankTransaction> bankTransactions = bankStatementParser.parseLinesFromCSV(lines);
        final BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);

        collectSummary(bankStatementProcessor);
    }

    private static void collectSummary(final BankStatementProcessor bankStatementProcessor) {
        System.out.println("The total for all transactions is " + bankStatementProcessor.calculateTotalAmount());
        System.out.println("The total for in January is " + bankStatementProcessor.calculateTotalInMonth(Month.JANUARY));
        System.out.println("The total salary received is " + bankStatementProcessor.calculateTotalForCategory("Salary"));
    }
}
```

### 클래스 수준 응집도

실무에서는 일반적으로 여섯 가지 방법으로 그룹화한다.

- 기능
  - 기능이 비슷한 메서드를 그룹화한다.
  - 함께 사용하는 메서드를 그룹화하면 찾기도 쉽고 이해하기도 쉬우므로 응집도를 높인다.
  - 약점 : 한 개의 메서드를 갖는 클래스를 너무 과도하게 만들려는 경향이 발생할 수 있다. -> 코드 장황해지고 복잡해진다.

- 정보

  - 같은 데이터나 도메인 객체를 처리하는 메서드를 그룹화한다.

  - 테이블이나 특정 도메인 객체를 저장하는 데이터베이스와 상호작용할 때 흔히 볼 수 있다.

  - 약점 : 여러 기능으 그룹화하면서, 필요한 일부 기능을 포함하는 클래스 전체를 디펜던시로 추가해야한다.

  - 예) BankTransaction 객체 - 4개의 다른 메서드를 관련 정보로 응집하는 클래스 구현

    ```java
    public class BankTransactionDAO {
    
      public BankTransaction create(final LocalDate date, final double amount, final String description) {
        // ...
        throw new UnsupportedOperationException();
      }
    
      public BankTransaction read(final long id) {
        // ...
        throw new UnsupportedOperationException();
      }
    
      public BankTransaction update(final long id) {
        // ...
        throw new UnsupportedOperationException();
      }
    
      public void delete(final BankTransaction bankTransaction) {
        // ...
        throw new UnsupportedOperationException();
      }
    }
    ```

- 유틸리티

  - 관련성이 없는 여러 메서드를 명확하지 않는 기준으로 그룹화한다.
  - 유틸리티 클래스 사용은 낮은 응집도로 이어지므로 자제해야 한다.
  - 클래스 전체의 기능을 추론하기가 어렵다.
  - 특징을 찾기가 어렵다.

- 논리

  - 본질적으로 다르고 관련이 없는 여러 개의 메서드를 한 가지 논리로 그룹화한다.

  - 클래스가 여러 책임을 갖게 되면서 SRP를 위배한다. (권장되지 않음)

  - 예) '파싱'이라는 논리로 그룹화

    ```java
    public class BankTransactionParser {
      public BankTransaction parseFromCSV(final String line) {
        // ...
        throw new UnsupportedOperationException();
      }
    
      public BankTransaction parseFromJSON(final String line) {
        // ...
        throw new UnsupportedOperationException();
      }
    
      public BankTransaction parseFromXML(final String line) {
        // ...
        throw new UnsupportedOperationException();
      }
    }
    ```

- 순차

  - 파일을 읽고, 파싱하고, 처리하고, 정보를 저장하는 메서서드들을 한 클래스로 그룹화한다.
  - 입출력이 순차적으로 흐르는 것을 순차 응집이라 부른다.
  - 실전에서 순차 응집을 적용하면 한 클래스를 바꿔야 할 여러 이유가 존재하므로 SRP를 위배한다.
  - 클래스를 순식간에 복잡하게 만든다.

- 시간
  - 여러 연산 중 시간과 관련된 연산을 그룹화한다.

|          | 응집도 수준 | 장점                        | 단점                               |
| -------- | ----------- | --------------------------- | ---------------------------------- |
| 기능     | 높은 응집도 | 이해하기 쉬움               | 너무 단순한 클래스 생성            |
| 정보     | 중간 응집도 | 유지보수하기 쉬움           | 불필요한 디펜던시                  |
| 순차     | 중간 응집도 | 관련 동작을 찾기 쉬움       | SRP를 위배할 수 있음               |
| 논리     | 중간 응집도 | 높은 수준의 카테고리화 제공 | SRP를 위배할 수 있음               |
| 유틸리티 | 낮은 응집도 | 간단히 추가 기능            | 클래스의 책임을 파악하기 어려움    |
| 시간     | 낮은 응집도 | 판단 불가                   | 각 동작을 이해하고 사용하기 어려움 |



### 메서드 수준 응집도

- 메서드가 연관이 없는 여러 일을 처리한다면 응집도가 낮아진다.
- 응집도가 낮은 메서드는 여러 책임을 포함하기 때문에 각 책임을 테스트하기가 어렵고, 메서드의 책임도 테스트하기 어렵다.



## 결합도

한 기능이 다른 클래스에 얼마나 의존하고 있는지를 가늠한다. 코드가 서로 **어떻게 의존하는지**와 관련이 있는 척도다.

어떤 클래스를 구현하는 데 얼마나 많은 지식(다른 클래스)을 참조했는가로 설명할 수 있다.

- 인터페이스를 이용해 여러 컴포넌트의 결합도를 제거할 수 있다.
- 인터페이스를 이용하면 요구사항이 바뀌더라도 유연성을 유지할 수 있다.

코드를 구현할 때는 결합도를 낮춰야 한다. (코드의 다양한 컴포넌트가 내부와 세부 구현에 의존하지 않아야 함을 의미)

높은 결합도는 무조건 피해야 한다.

입출금 내역 파서를 어떻게 사용하는지 정의하는 인터페이스를 만든다.

```java
// 입출금 내역을 파싱하는 인터페이스 정의
public interface BankStatementParser {
    BankTransaction parseFrom(String line);

    List<BankTransaction> parseLinesFrom(List<String> lines);
}
```

BankStatementCSVParser : 위에서 정의한 인터페이스를 구현한다

```java
public class BankStatementCSVParser implements BankStatementParser {
 // ... 
}
```

BankStatementAnalyzer와 특정 BankStatementCSVParser 구현의 결합 제거 -> 인터페이스를 사용한다.

BankTransactionParser를 인수로 받는 analyze() 메서드를 새로 만들어 특정 구현에 종속되지 않도록 클래스를 개선한다.

```java
// 특정 파서와의 결합 제거
public class BankStatementAnalyzer {
  private static final String RESOURCES = "src/main/resources/";

  public void anaylze(final String fileName, final BankStatementParser bankStatementParser) throws IOException {

    final Path path = Paths.get(RESOURCES + fileName);
    final List<String> lines = Files.readAllLines(path);

    final List<BankTransaction> bankTransactions = bankStatementParser.parseLinesFrom(lines);
    final BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);

    collectSummary(bankStatementProcessor);
  }

  private static void collectSummary(final BankStatementProcessor bankStatementProcessor) {
    System.out.println("The total for all transactions is " + bankStatementProcessor.calculateTotalAmount());
    System.out.println("The total for in January is " + bankStatementProcessor.calculateTotalInMonth(Month.JANUARY));
    System.out.println("The total salary received is " + bankStatementProcessor.calculateTotalForCategory("Salary"));
  }
}
```

메인 응용프로그램에서 지금까지 구현한 코드를 사용한다.

```java
public class MainApplication {
  public static void main(String[] args) throws IOException {

    final BankStatementAnalyzer bankStatementAnalyzer = new BankStatementAnalyzer();

    final BankStatementParser bankStatementParser = new BankStatementCSVParser();

    bankStatementAnalyzer.anaylze(args[0], bankStatementParser);
  }
}
```



## 테스트

### 테스트 자동화

- 자동화된 테스트에서는 사람의 조작 없이 여러 테스트가 포함된 스위트(suite)가 자동으로 실행된다.
- 코드를 바꿨을 때, 지정된 테스트가 빠르게 실행되므로 소프트웨어가 예상하지 못한 문제를 일으키지 않고 제대로 동작할 거라는 확신을 조금 더 가질 수 있다.

#### 테스트 자동화의 장점

- 확신
  - 소프트웨어가 규격 사양과 일치하며 동작하는지를 테스트해 고객의 요구 사항을 충족하고 있다는 사실을 더욱 확신할 수 있다.
  - 테스트 규격 사양과 결과를 증거로 제공할 수 있다.
- 변화에도 튼튼함 유지
  - 바꾼 코드로 인해 새로운 버그가 발생하지 않았음을 확인하는 데 큰 도움이 된다.
- 프로그램 이해도
  - 소스코드의 프로젝트에서 다양한 컴포넌트가 어떻게 동작하는지 이해하는 데 도움을 준다.
  - 테스트는 다양한 컴포넌트의 디펜던시와 이들이 어떻게 상호작용하는지를 명확하게 드러낸다. -> 소프트웨어의 전체 개요를 빨리 파악할 수 있다.

### JUnit 사용하기

자바 테스트 프레임워크인 JUnit으로 유닛 테스트(메서드나 작은 클래스처럼 작고 고립된 단위를 테스트)를 구현한다.

#### 테스트 메서드 정의하기

- src/test/java 에 테스트 클래스를 저장하는 것이 기본 규칙이다.
- 프로젝트에 JUnit 라이브러리를 디펜던시로 추가해야 한다.

BankStatementCSVParser 테스트하는 간단한 코드

```java
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class BankStatementCSVParserTest {

  private final BankStatementParser statementParser = new BankStatementCSVParser();

  @Test
  public void shouldParseOneCorrectLine() throws Exception {
    Assert.fail("Not yet implemented");
  }
}
```

- 테스트 클래스명에는 Test라는 접미어를 붙이는 것이 관습이다.
- 테스트 메서드 명은 구현 코드를 보지 않고도 무엇을 테스트하는지 쉽게 알 수 있도록 서술적인 이름을 붙이는 것이 좋다.
- JUnit의 애너테이션 @Test를 테스트 메서드에 추가하여 해당 메서드가 유닛 테스트의 실행 대상임을 지정한다.

#### Assert 구문

JUnit은 특정 조건을 테스트하는 다양한 Assert 구문을 제공한다.

| Assertion 구문                               | 용도                                                         |
| -------------------------------------------- | ------------------------------------------------------------ |
| Assert.fail(message)                         | 메서드 실행 결과를 실패로 만듦. 테스트 코드를 구현하기 전에 placeholder로 유용하게 활용 가능 |
| Assert.aasertEquals(expected, actual)        | 두 값이 같은지 테스트                                        |
| Assert.assertEquals(expected, actual, delta) | 두 float이나 double이 delta 범위 내에서 같은지 테스트        |
| Assert.assertNotNull(object)                 | 객체가 null이 아닌지 테스트                                  |

```java
public class BankStatementCSVParserTest {

  private final BankStatementParser statementParser = new BankStatementCSVParser();

  @Test
  public void shouldParseOneCorrectLine() throws Exception {
    final String line = "30-01-2017,-50,Tesco";

    final BankTransaction result = statementParser.parseFrom(line);

    final BankTransaction expected = new BankTransaction(LocalDate.of(2017, Month.JANUARY, 30), -50, "Tesco");
    final double tolerance = 0.0d;

    Assert.assertEquals(expected.getDate(), result.getDate());
    Assert.assertEquals(expected.getAmount(), result.getAmount(), tolerance);
    Assert.assertEquals(expected.getDescription(), result.getDescription());
  }
}
```

1. 테스트의 context를 설정한다.
2. 동작을 실행한다.
3. 예상된 결과를 Assertion으로 지정한다.

이와 같은 유닛 테스트 설정의 세 단계 패턴을 Given-When-Then 공식이라고 부른다.

### 코드 커버리지

테스트 집합이 소프트웨어의 소스코드를 얼마나 테스트했는가를 가리키는 척도이다.

커버리지가 높을수록 예상하지 못한 버그가 발생할 확률이 낮아지므로 커버리지를 높이는 것을 목표로 삼아야 한다. (70~90%)

코드 커버리지는 테스트하지 않은 부분이 남아 있음을 알려주는 역할이다. 테스트 품질과는 아무 관련이 없다.

구문 커버리지보다 각 분기문(if, while, for)을 확인하는 분기 커버리지를 사용하는 것이 좋다.