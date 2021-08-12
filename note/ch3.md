# chapter 3 입출금 내역 분석기 확장판

### 📌 `개방 폐쇄 원칙(OCP)` `예외 처리` `빌드 도구`

---

<br/>

---

#### 들어가기 전,
- 특정 월이나 금액 범위 내 입출금 내역 검색 기능 추가
- 2장에서 구현했던 `BankStatementProcessor` 클래스 → 비즈니스 로직 + 반복 로직
```java
    public List<BankTransaction> findTransactionsInMonthAndGreater(final Month month, final int amount) {
        final List<BankTransaction> result = new ArrayList<>();
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month && bankTransaction.getAmount() >= amount) {
                result.add(bankTransaction);
            }
        }
        return result;
    }
```
→ 개방/폐쇄 원칙 적용하면 코드를 직접 바꾸지 않고 해당 메서드나 클래스의 동작을 바꿀 수 ⭕️

<br/>

## # 개방 폐쇄 원칙(OCP : Open-Closed Principle)
> 소프트웨어 구성 요소(컴포넌트, 클래스, 모듈, 함수)는 확장에 대해서는 개방되어야 하지만 변경에 대해서는 폐쇄되어야 한다.

### # 개방 폐쇄 원칙의 장점
- 기존 코드를 바꾸지 않으므로 기존 코드가 잘못될 가능성이 줄어든다.
- 코드가 중복되지 않으므로 기존 코드의 재사용성이 높아진다.
- 결합도가 낮아지므로 코드 유지보수성이 좋아진다.

```java
    @FunctionalInterface
    public interface BankTransactionFilter {
        boolean test(BankTransaction bankTransaction);  // + 함수형 인터페이스 
    }
```
```java   
    public List<BankTransaction> findTransactions(final BankTransactionFilter bankTransactionFilter) {
        final List<BankTransaction> result = new ArrayList<>();
            for (final BankTransaction bankTransaction : bankTransactions) {
                if (bankTransactionFilter.test(bankTransaction)) {  
                    result.add(bankTransaction);
                }
            }
            return result;
        }
    }
```

- `findTransactions()` 메서드는 더 이상 특정 필터(월, 금액) 구현에 **의존**하지 않는다.
- 기존 메서드의 바디를 바꿀 필요 없이 새로운 구현을 인수로 전달하기 때문!


<details markdown="1">
<summary> + </summary>

#### # 함수형 인터페이스
> 함수형 인터페이스는 단 하나의 추상 메소드만이 선언된 인터페이스이다.

1. 변경되는 부분과 변경되지 않는 부분의 코드를 분리한다.
2. 변경되는 부분을 인터페이스로 추출한다.
3. 인터페이스에 대한 구현체를 익명 클래스로 구현해 메소드의 인자로 전달한다.

#### # 람다식
> (매개변수1, 매개변수2, ...) -> { 실행문 } 함

- 함수를 간략하면서 명확한 식으로 표현 가능
- 메서드의 이름과 반환 값이 없어지므로 `익명 함수` 이라고도 함
</details>

<br/>

---

### # 인터페이스의 문제
- 많은 메서드를 포함하는 **갓 인터페이스**는 복잡도와 결합도를 높인다.
    <details markdown="1">
    <summary> 갓 인터페이스 </summary>
        - 구현 클래스는 인터페이스에서 정의한 모든 연산의 구현 코드를 제공해야 한다. 따라서 인터페이스를 바꾸면 이를 구현한 코드도 바뀐 내용을 지원하도록 갱신되어야한다.
        <br/>
        - 인터페이스는 도메인 객체의 특정 접근자에 종속 되어서는 안된다. 도메인 객체의 세부 내용이 바뀌게 되면 인터페이스도 바뀌어야 하며 결국엔 구현 코드도 바뀌어야 한다.
    </details>
- 너무 세밀한 메서드를 포함하는 인터페이스는 응집도를 낮춘다.

### # 명시적 API vs 암묵적 API
- API의 가독성을 높이고 쉽게 이해할 수 있도록 메서드 이름을 서술적으로 만들어야 한다.


### # 도메인 클래스 vs 원싯값
- 원싯값으로는 다양한 결과를 반환할 수 없어 유연성이 떨어진다.
- **원싯값 포장**을 하면 도메인의 다양한 개념간의 결합을 줄이고, 요구 사항이 바뀔 때 연쇄적으로 코드가 바뀌는 일도 최소화할 수 있다.

### # 인터페이스의 나쁜 예(void 반환 형식)
- void 반환 형식은 아무 도움이 되지 않고, 기능을 파악하기도 어렵다.
  - 메서드가 무엇을 반환하는 지 알 수 없기 때문..
  - 인터페이스로부터 얻을 수 있는 정보가 아무것도 없다.
- 연산 결과로 void를 반환하면 동작을 테스트하기 어렵다.

<br/>

---
## # 예외 처리

`확인된 예외`
- 회복해야 하는 대상의 예외
- 메서드가 던질 수 있는 확인된 예외 목록을 선언 or 해당 예외를 _try/catch_ 로 처리
- _Exception_

`미확인 예외`
- 프로그램을 실행하면서 발생할 수 있는 종류의 예외
- 메서드 시그니처에 명시적으로 오류를 선언하지 않으면 호출자도 이를 꼭 처리할 필요가 없음
- _Error, RuntimeException_

→ 대다수의 예외를 미확인 예외로 지정하고 꼭 필요한 상황에서만 확인된 예외로 지정헤 불필요한 코드를 줄여야 한다 !!

`노티피케이션 패턴 `
- 너무 많은 미확인 예외를 사용하는 상황에 적합한 해결책 제공
- 도메인 클래스로 오류를 수집하여 해당 클래스 객체에 오류 메시지를 추가

<br/>

---
## # 빌드 도구
    - 프로젝트에서 필요한 xml,jar 파일들을 자동으로 인식하여 빌드해주는 도구
    - 소스 코드를 컴파일, 테스트 등을 하여 실행가능한 앱으로 빌드
    - 응용 프로그램 빌드, 테스트, 배포 등의 작업 자동  
    - 외부 라이브러리를 참조하여 자동으로 다운로드 및 업데이트의 관리 

### # _Maven vs. Gradle_
- Gradle에 비해 Maven이 점유율이 더 높은 상황(점차 Gradle 점유울 오르는 중)
- Gradle에 비해 Maven의 성능이 떨어짐
- Maven에 비해 Gradle이 대규모 프로젝트에서의 성능이 좋음
- Maven : pom.xml | Gradle : build.gradle
- Gradle은 설치 없이 사용할 수 있음

<br/>

#### # `함수형 인터페이스` `람다식` `갓 인터페이스` 