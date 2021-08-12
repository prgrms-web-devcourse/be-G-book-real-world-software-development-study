# Chapter 2. 입출금 내역 분석기

- 책에서 사용하는 **개념과 원칙**을 소개
- 특정 기술을 다루지는 않고, 다양한 언어와 프레임워크에 적용하는 관습과 원칙을 가르쳐 줄 것





## 2.1 도전과제

- 입출금 내역을 자동으로 분석해 재정 상태를 잘 보여주는 SW 개발





## 2.2 목표

- 좋은 SW 개발의 기반이 무엇인지
- **1개의 클래스로 문제 구현** 후 **유지보수에 대응**하며 **한계 파악**
- 이를 위해 쉽게 유지보수하고, 버그 발생 범위 줄이는 데 도움을 주는 **SRP(단일 책임 원칙)**을 배운다.
  - 응집도와 결합도 특징





## 2.3 입출금 내역 분석기 요구 사항

1. 은행 입출금 내역의 총 수입과 총 지출은 각각 얼마인가? 결과가 양수인가 음수인가?
2. 특정 달엔 몇 건의 입출금 내역이 발생했는가?
3. 지출이 가장 높은 상위 10건은 무엇인가?
4. 돈을 가장 많이 소비하는 항목은 무엇인가?





## 2.4 KISS(Keep It Short and Simple) 원칙

**KISS 원칙 : 디자인에서 간단하고 알기 쉽게 만드는 편이 좋다.* 

*(출처: https://ko.wikipedia.org/wiki/KISS_%EC%9B%90%EC%B9%99)*

- 한 개의 클래스로 구현
- 예외처리 X



- [예제 2-1] 실행 시 아래와 같은 문제가 발생할 수 있기 때문에 어떻게 처리할지 고려해야 한다.

  ```
  public class BankTransactionAnalyzerSimple {
      private static final String RESOURCES = "src/main/resources/";
  
      public static void main(String[] args) throws IOException {
          final Path path = Paths.get(RESOURCES + args[0]); // 시스템 경로
          final List<String> lines = Files.readAllLines(path); // 파일의 모든 행 목록
          double total = 0d;//총합
          for (final String line: lines) { // 모든 행 돌면서
              final String[] columns = line.split(","); //, 로 열 분리(30-01-2017 | -100 | Deliveroo)
              final double amount = Double.parseDouble(columns[1]); //금액 가져오기
              total += amount;
          }
  
          System.out.println("The total for all transactions is " + total);
      }
  }
  ```

  - 파일이 비어 있을 때
  - 데이터에 문제가 있어 금액 파싱이 안될 때
  - 행의 데이터가 완벽하지 않을 때

- 위와 같이 ***<u>언제나 발생할 수 있는 문제에 관한 질문을 하는 습관을 가지는 것이 좋다.</u>***



#### 데이터에 문제가 있어 금액 파싱이 안될 때

- 이전 코드 복사+붙여넣기 후 주어진 월을 선택하도록 바꾸기



**2.4.1 final 변수**

- 해당 변수에 값 재할당 불가능
- 사용 여부는 팀과 프로젝트에 따라 달라짐
- 많은 변수를 final 표시하면 **어떤** **객체의 상태가 바뀌고 안바뀌는지 명확하게 구분**할 수 있다.

- final 필드로 가리키는 객체도 **가변 상태를 포함**하기 때문에 객체가 **무조건 변경 불가능한 것은 아니다**.
  - 메서드 파라미터에 final 필드를 포함시켜 해당 변수들이 지역 변수가 아니며, 다시 할당할 수 없음을 명시

- 추상 메서드 내 메서드 파라미터에 final을 사용하는 경우 실제 구현이 없기 때문에 무의미하다.
- 자바 10에서 var 키워드가 등장하면서 유용성이 감소되었다.

* var : 자료형이 필요없는 타입*





## 2.5 코드 유지보수성과 안티 패턴

- 개발 시 복사 + 붙여넣기 보다는 다음과 같은 **코드 유지보수성**을 높이기 위해 노력해야 한다.
  - 특정 **기능**을 담당하는 **코드를 쉽게 찾을 수 있어야** 한다.
  - **코드**가 **어떤 일을 수행하는지** **쉽게 이해**할 수 있어야 한다.
  - 새로운 **기능을 쉽게 추가**하거나 기존 기능을 **쉽게 제거**할 수 있어야 한다.
  - **캡슐화!** 사용자에게 세부 구현 내용이 감추어져 있어 **쉽게 코드를 이해**하고, **기능을 바꿀 수 있어야** 한다.



- 개발자는 개발하고 있는 프로그램의 복잡성을 관리하는 것이 목표다.
- 하지만, 이 과정에서 새로운 요구 사항이 생길 때마다 복사 + 붙여넣기로 해결하는 방법(안티 패턴)은 효과적이지 않다.
  - **갓 클래스(god class)** 때문에 코드이해가 어렵다.
  - **코드 중복**때문에 코드가 **불안정**하고 **변화에 쉽게 망가**진다.



**2.5.1 갓 클래스**

- 모든 코드가 구현되어 있는 클래스
- 한 곳에서 모든 일을 수행하기 때문에 클래스의 목적 이해가 힘듦
- 이러한 문제를 **갓 클래스 안티 패턴**이라고 부른다.
- 이런 패턴으로 구현하면 안되며, 이를 보완해주는 SRP(단일 책임 원칙)이 필요하다.



**2.5.2 코드 중복**

- 여러 곳에 **코드가 중복되어 있으면** 기능 수정 시 **모든 코드를 수정**해야 한다.
- 그 과정에서 **새로운 버그가 발생**할 가능성이 커진다.

- KISS 원칙 남용하면 안된다.
- 전체 프로그램의 설계를 돌아보면서, **한 문제를** 작은 개별 문제로 **분리해** 더 쉽게 **관리할 수 있는지 파악**해야 한다.





## 2.6 단일 책임 원칙(SRP, Single Responsibility Principle)

- 쉽게 관리하고 유지보수하는 코드를 구현하는 데 도움을 주는 SW 개발 지침
- *<u>한 클래스는 한 기능만 책임진다.</u>*
- *<u>클래스가 바뀌어야 하는 이유는 오직 하나여야 한다.</u>*



- SRP는 클래스와 메소드에 적용한다.
- SRP를 적용하면 **코드가 바뀌어야 하는 이유**가 **한 가지로 제한**된다.
  - 만약 이유가 다양하면 여러 장소에서 코드 변경이 발생해 유지보수가 어려워진다.



- 해당 코드에서는 다음을 개별로 분리해야 한다.
  1. 입력 읽기
  2. 주어진 형식의 입력 파싱
  3. 결과 처리
  4. 결과 요약 리포트



**BankTransactionAnalyzer** 

```java
public class BankTransactionAnalyzer {
    private static final String RESOURCES = "src/main/resources/";

    public static void main(String[] args) throws IOException {
        final BankStatementCSVParser bankStatementParser = new BankStatementCSVParser();

        final String fileName = args[0];//파일 이름
        final Path path = Paths.get(RESOURCES + fileName); // 시스템 경로
        final List<String> lines = Files.readAllLines(path); // 파일의 모든 행 목록

        final List<BankTransaction> bankTransactions = bankStatementParser.parseLinesFromCSV(lines);//파싱 결과값 가져오기

        System.out.println("The total for all transactions in January is " + calculateTotalAmount(bankTransactions));//총액 계산
        System.out.println("Transactions in January " + selectInMonth(BankTransaction, Month.JANUARY));//
    }

    private static double calculateTotalAmount(List<BankTransaction> bankTransactions) {
        double total = 0d;
        for (final BankTransaction bankTransaction: bankTransactions) {
            total += bankTransaction.getAmount();
        }
        return total;
    }
    
    private static List<BankTransaction> selectInMonth(final List<BankTransaction> bankTransactions, final Month month) {
        final List<BankTransaction> bankTransactionsInMonth = new ArrayList<>();
        for (final BankTransaction bankTransaction: bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {
                bankTransactionsInMonth.add(bankTransaction);//달이 같으면 정보 담기
            }
        }
        return bankTransactionsInMonth;
    }
}
```

- 메인에서 파싱 로직 부분을 다른 클래스와 메소드에 위임해 독립적으로 구현함.
- 새 요구 사항이 들어오면 **BankStatementCSVParser 클래스**로 **캡슐화된 기능을 재사용**해 구현한다.
- 파싱 동작 방식을 바꿔야 하는 일이 생겨도 한 곳의 코드만 바꾸면 된다.

- BankStatementCSVParser : 파싱 로직 담당 클래스
- BankTransaction : 입출금 내역 도메인 클래스
  - equals(), hashCode() 구현
  - 다른 코드가 특정 데이터 형식에 의존하지 않게 됨



- 메소드를 구현할 때, 코드를 보고 **무슨 일이 일어나는지 명확히 이해**하기 위해 **놀람 최소화 원칙**을 따라야 한다.
  - *놀람 최소화 원칙 : 일관성을 유지하는 범위에서 코드를 구현할 것을 강조하는 원칙*
    - 메소드가 수행하는 일이 무엇인지 바로 이해할 수 있도록 자체 문서화를 제공하는 메소드명 사용
    - 파라미터의 상태를 바꾸지 않는다.





## 2.7 응집도

- **서로 어떻게 관련되어 있는지**를 가리킴(**클래스나 메소드의 책임**이 얼마나 강하게 연결되어 있는지)
- 코드 유지보수성을 결정
- SW의 복잡성을 유추하는 데 도움울 준다.
- 누구나 쉽게 코드를 찾고, 이해하고, 사용할 수 있도록 **높은 응집도**가 **개발자의 목표**다.
- BankStatementCSVParser(파싱 로직 담당 클래스)는 데이터 파싱 관련 메소드들을 한 그룹으로 묶어 응집도가 높다.
- 응집도 개념은 클래스와 메소드에 적용이 가능하다.



- **BankSatetementAnalyzer(진입점)** 클래스는 프로그램의 다양한 부분을 연결하지만, **계산 작업**은 파싱이나 결과 전송과 **직접적인 관련이 없어** **응집도가 떨어진다**.

-> BankStatementProcessor 클래스로 추출

- 모든 연산에서 입출금 내역 목록 공유해 이를 클래스 필드로 만듦
- **BankStatementProcessor**

```java
public class BankStatementProcessor {
    private final List<BankTransaction> bankTransactions;

    public BankStatementProcessor(final List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public double calculateTotalAmount() {
        //총합 구하기
        double total = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            total += bankTransaction.getAmount();
        }
        return total;
    }

    public double calculateTotalInMonth(final Month month) {
        //월에 따른 총합 구하기
        double total = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {
                total += bankTransaction.getAmount();
            }
        }
        return total;
    }

    public double calculateTotalForCategory(final String category) {
        //항목에 따른 총합 구하기
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





**2.7.1 클래스 수준 응집도**

- 기능, 정보, 유틸리티, 논리, 순차, 시간 으로 그룹화
- 그룹화하는 메소드의 **관련성이 약하면** **응집도가 낮아진다**.



1. 기능
   - BankStatementCSVParser 클래스는 기능이 비슷한 메소드가 그룹화되어 있다.
   - parseLinesFromCSV는 parseFromsCSV 메소드를 사용한다.
   - 함께 사용하는 메소드를 구룹화하면 찾기 쉽고 이해하기 쉬워서 응집도를 높인다.
   - 한 개의 메소드를 갖는 클래스를 너무 많이 만들어 코드가 장황해지고 복잡해질 수 있다.



2. 정보

   - 같은 데이터나 도메인 객체를 처리하는 메소드를 그룹화하는 방법

   - 여러 기능을 그룹화하면서, 필요한 일부 기능을 포함하는 클래스 전체를 의존성으로(Dependency) 추가한다는 약점이 있다.



3. 유틸리티

   - 만능!
   - 관련성 없는 메소드를 한 클래스로 포함시켜야 하는 경우와 같이 **메소드**를 **어디에 두어야 결정할지 모르는 경우** **유틸리티 클래스에 추가**하기도 한다.
   - 특징을 찾기 어려워 **코드를 찾기 어렵고** **사용방법을 이해하기 어렵다.**

   - 낮은 응집도로 이어지기 때문에 자제해야 한다.



4. 논리

   ```java
   public class BankTransactionParser {
   	public BankTransaction parseFromCSV(final String line) {}
   	public BankTransaction parseFromJSON(final String line) {}
   	public BankTransaction parseFromXML(final String line) {}
   }
   ```

   - 위 메소드들은 '파싱'이라는 논리로 그룹화되었다.
   - 한 클래스가 4가지 책임을 갖게되어 SRP를 위배한다.



5. 순차

   - 파일 **'읽기 -> 파싱 -> 처리 -> 정보 저장'** 과 같이 순차적으로 흐르는 것을 순차 응집이라고 한다.

   - 여러 동작이 어떻게 함께 수행되는지 쉽게 이해된다.
   - 하지만, 이 방법은 **클래스를 복잡**하게 만들어 **SRP를 위배**한다.
   - **각 책임**을 개별적으로 **응집된 클래스로 분리**하는 것이 더 좋다.



6. 시간

   - 시간 관련 연산을 그룹화
   - ex) 작업 시작 전/뒤의 **초기화**, **뒷정리 작업**을 담당하는 메소드를 **포함하는 클래스**

   - **초기화**는 다른 작업과 연관은 없지만, **먼저 실행**되어야 한다.



| 응집도 수준           | 장점                        | 단점                               |
| --------------------- | --------------------------- | ---------------------------------- |
| 기능(높은 응집도)     | 이해하기 쉬움               | 너무 단순한 클래스 생성            |
| 정보(중간 응집도)     | 유지보수하기 쉬움           | 불필요한 디펜던시                  |
| 순차(중간 응집도)     | 관련 동작을 찾기 쉬움       | SRP를 위배할 수 있음               |
| 논리(중간 응집도)     | 높은 수준의 카테고리화 제공 | SRP를 위배할 수 있음               |
| 유틸리티(낮은 응집도) | 간단히 추가 가능            | 클래스의 책임을 파악하기 어려움    |
| 시간(낮은 응집도)     | 판단 불가                   | 각 동작을 이해하고 사용하기 어려움 |



**2.7.2 메소드 수준 응집도**

- 응집도 원칙 메소드에도 적용 가능
- 메소드도 연관 없는 일을 처리하면 응집도가 낮아짐 -> 여러 책임 포함해 각 책임들 테스트가 어려움
- ex) **if/else 블록이 여러 개 포함**되어 있다면 더 작은 조각으로 **메소드를 분리**해 **응집도를 높여야 한다!**





## 2.8 결합도

- 코드가 서로 **어떻게 의존하는지**와 관련 있는 척도
- 한 기능이 **다른 클래스에 얼마나 의존하고 있는지** 가늠
- *"어떤 클래스를 구현하는 데 얼마나 많은 클래스를 참조했는가"* -> 기능 변경 시 참조한만큼 유연성이 떨어진다.
- ex) 시계
  - 사람은 내부 구조에 의존하지 않아, 동작원리를 몰라도 시간을 알 수 있다.
  - 이는 인터페이스와 구현이 서로 결합되지 않았기 때문!

- 현재 BankStatementAnalyzer는 BankStatementCSVParser에 의존한다. 그래서 CSV가 아니라 다른 파일 타입으로 변경하면 수정 작업이 필요하다. **인터페이스**를 이용하면 요구사항이 변경되어도 **유연성을 유지**할 수 있다.

- 두 클래스 사이에 BankStatementParser 인터페이스를 사용해 결합을 제거한다.
  - 인터페이스라는 중간다리를 거쳐 결합도가 낮아진다.





## 2.9 테스트



**2.9.1 테스트 자동화**

1. 확신
   - 규격 사양과 일치하며 동작하는지 테스트해 고객의 **요구 사항을 충족**하고 있다는 사실을 더욱 **확신**할 수 있음
   - 테스트가 고객의 사양이 된다.



2. 변화에도 튼튼함 유지
   - 코드가 많으면 문제를 찾기 어렵다.
   - 자동화된 테스트 스위트가 있다면 동료가 코드를 수정했을 때 버그 발생 유무를 확인할 수 있다.



3. 프로그램 이해도
   - 테스트는 다양한 컴포넌트의 의존도와 어떻게 상호작용하는지 드러내 **전체 개요**를 **빨리 파악**할 수 있다.



**2.9.2 제이유닛(JUnit) 사용하기**

- *JUnit : 단위 테스트를 위한 테스트용 프레임워크*

* 유닛 테스트 : 메소드, 클래스처럼 작은 단위 테스트

  

* BankStatementCSVParser 테스트 하기

  1. 테스트 메소드 정의하기

     - 기본 규칙 : "src/main/java"에 코드 저장, "src/test/java"에 테스트 클래스 저장

     - JUnit 라이브러리 디펜던시 추가

     - 클래스명에 Test라는 접미어를 붙이자.

     - @Test 어노테이션을 붙여 실행 대상임을 지정한다.

     - 무엇을 테스트하는지 쉽게 알 수 있는 이름을 붙이자.

       

  2. Assert 구문

     - 특정 조건을 테스트하는 역할

     - 연산의 결과와 예상한 결과를 비교할 수 있다.

       ```java
       public void shouldParseOneCorrectLine() throws Exception {
           //  Assert.fail("Not yet implemented");
           
           String line = "30-01-2017,-50,Tesco";
           
           BankTransaction result = statementParser.parseFrom(line);//파싱
           BankTransaction expected = new BankTransaction(LocalDate.of(2017, Month.JANUARY, 30), -50, "Tesco");//test
           
           Assert.assertEquals(expected.getDate(), result.getDate());
           Assert.assertEquals(expected.getAmount(), result.getAmount(), 0.0d);
           Assert.assertEquals(expected.getDescription(), result.getDescription());
       }
       ```

       (1) 테스트의 콘텍스트 설정(파싱할 행 설정)

       (2) 동작 실행.(입력 행 파싱)

       (3) 예상 결과 Assertion 지정(날짜, 금액, 설명 제대로 파싱되었는지 확인

     - 위와 같은 패턴은 "Given-When-Then" 공식 -> 테스트가 무엇을 수행하는지 쉽게 이해 가능

       | 어서션 구문                                  | 용도                                                        |
       | -------------------------------------------- | ----------------------------------------------------------- |
       | Assert.fail(message)                         | 메소드 실행 결과를 실패로 만듦.                             |
       | Assert.assetEquals(exptected, actual)        | 두 값이 같은지 테스트                                       |
       | Asset.assertEquals(exptedted, actual, delta) | 두 float이나 double이 delta(0.0d) 범위 내에서 같은지 테스트 |
       | Assert.assertNotNull(object)                 | 객체가 null이 아닌지 테스트                                 |

  

**2.9.3 코드 커버리지**

- *코드 커버리지 : 테스트 집합이 SW의 코드를 얼마나 테스트 했는지 가리키는 척도*
- 높을수록 버그 발생 확률 낮아짐
- 보통 70%~90%를 목표로 정해야 한다.
- 자바에서는 자코코(JaCoCo), 에마(Emma), 코베르투라(Cobertura) 많이 사용



- *구문 커버리지 : 얼마나 많은 구문의 코드를 커버했는지 나타낸다.*

- 여러개의 경로가 가능한 분기분을 한 구문으로 취급하는 약점이 있음 -> *분기 커버리지(각 분기문 확인)* 사용하는 것이 좋다.





## 2.10 총정리

- 갓 클래스와 코드 중복은 코드를 추론하고 유지보수하기 어렵게 만드는 요인
- SRP(단일 책임 원칙)은 관리하고 유지보수하기 쉬운 코드를 구현하는데 도움을 준다.
- 응집도 : 클래스나 메소드의 책임이 얼마나 강하게 연관되어 있는지 나타낸다.
- 결합도 : 클래스가 다른 클래스에 얼마나 의존하고 있는지 나타낸다.
- 쉬운 유지보수를 위해 높은 응집도와 낮은 결합도를 가져야 한다.
- 자동화된 테스트 스위트 : SW가 **올바르게 동작**하는지, 코드를 수정해도 **잘 동작하는지 확신**을 주고, 프로그램을 **쉽게 이해**할 수 있도록 한다.
- JUnit(제이유닛)을 활용해 테스트하는 유닛 테스트를 만든다.
- 유닛 테스트는 Given-When-Then 세 부분으로 분리해 쉽게 이해할 수 있도록 한다.

