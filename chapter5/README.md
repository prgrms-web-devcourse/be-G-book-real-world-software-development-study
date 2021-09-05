# 5장

5장에서는 무엇을 배우는가?

- **비즈니스 규칙 엔진**을 직접 구현
- **TDD** 기법으로 새로운 설계 문제를 풀어나가는 방법
- 유닛 테스트를 구현하는 데 유용한 **모킹** 기법
- **최신** 자바 기능 (지역 변수 형식 추론, switch문 등)
- **빌더 패턴**
- **인터페이스 분리 원칙**으로 사용자 친화적인 API 개발 방법

비즈니스 규칙 엔진?

- 비즈니스 규칙 엔진을 완성하면 비즈니스 팀에서 **직접 원하는 규칙을 만들 수 있으므로** <u>생선성을 높이고</u> 새로운 규칙을 <u>구현하는 시간을 단축</u>

## 비지니스 규칙 엔진 요구 사항

그래서 이 장에선 무엇을 달성할 것 인가?

- 개발자가 아닌 사람도 자신의 워크플로에 비지니스 로직을 추가하거나 바꿀 수 있는 기능을 만든다.

무엇을 제공하려 하는가?

- 비지니스 규칙 엔진(*규칙 기반 관리 시스템 - RBMS*)으로 간단한 맞춤 언어를 사용하여 한 개 이상의 **비지니스 규칙**을 실행하는 소프트웨어로 다양한 컴포넌트를 동시에 지원하자

비지니스 규칙들?

- **팩트** : 규칙이 확인할 수 있는 정보
- **액션** : 수행하려는 동작
- **조건** : 액션을 언제 발생시킬지 지정
- **규칙** : 실행하려는 비지니스 규칙을 지정. 보통 팩트, 액션, 조건을 한 그룹으로 묶어 규칙으로 만듦.

비즈니스 규칙 엔진이 생산성과 관련된 좋은점?

- 규칙이 응용프로그램과는 **독립된 곳**에서 실행, 유지보수, 테스트할 수 있다는 점

## 테스트 주도 개발(TDD)

TDD 철학이란?

- 테스트 코드를 먼저 만든 후, 이에 맞춰 코드를 구현하는 것
- 즉, 실제 코드를 구현하기 전에 테스트 코드를 먼저 구현

### 1. TDD를 사용하는 이유

귀찮지만 어마어마한 **보상**과 **장점**이 있다.

- 테스트를 따로 구현하므로 테스트에 대응하는 요구 사항을 한 개씩 구현할 때마다 필요한 요구 사항에 집중하고 개선할 수 있다.
- 코드를 올바르게 조작할 수 있다. 예를 들어 먼저 테스트를 구현하면서 코드에 어떤 공개 인터페이스를 만들어야 하는지 신중히 검토하게 된다.
- TDD 주기에 따라 요구 사항구현을 반복하면서 종합적인 테스트 스위트를 완성할 수 있으므로 요구사항을 만족시켰다는 사실을 조금 더 확신할 수 있으며 버그 발생 범위도 줄일 수 있다.
- 테스트를 통과하기 위한 코드를 구현하기 때문에 필요하지 않은 테스트를 구현하는 일(오버엔지니어링)을 줄일 수 있다.

> **Test Suite**
>
> 소프트웨어 개발에서 테스트 스위트는 덜 일반적으로 유효성 검사 스위트로 알려져 있으며 소프트웨어 프로그램을 테스트하여 특정 동작 세트가 있음을 보여주기 위한 테스트 케이스 모음입니다. [위키백과(영어)](https://en.wikipedia.org/wiki/Test_suite)

### 2. TDD 주기

리팩터링을 추가한 TDD 주기

- 실패하는 테스트 구현
- 모든 테스트 실행
- 기능이 동작하도록 코드 구현
- 모든 테스트 실행

하지만 실생활에서는 코드를 항상 **리팩터링**해야 하기 때문에 아래와 같은 모습을 보인다.

![](./images/tdd_lifecycle.png)

결과를 반환하지 않는 동작(행위)를 테스트 할려면?

- **`모킹`**이라는 새로운 기술이 필요하다.

## 모킹

모킹이란?

- `run()`과 같은 행위 메서드가 실행되었을 때 이를 확인하는 기법
- 그래서 구현하려는 비즈니스 규칙에 액션을 추가할 때마다 `run()`이 실행되었는지 확인한다.

어떻게 모킹 기술을 쓸 것 인가?

- 자바의 유명한 모킹 라이브러리인 **`모키토(Mockito)`**를 이용

How to use Mocking?

1. Mock 생성
2. 메서드가 호출되었는지 확인

<!--TODO: Mock Stub Spy Fake Dummy 좀 더 알아 오세용-->

Code

1. 라이브러리 임포트
2. 정적 메서드 `mock()` 로 필요한 목 객체를 만듦
3. `verify()` 로 특정 동작(메서드)이 실행되었는지 확인하는 어서션을 만든다.

```java
// 1. import library
import static org.mockito.Mockito.*;

public class ActionTest {
  // ...
  @Test
  void shouldExecuteOneAction() {
    final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine();
    // 2. make mock object
    final Action mockAction = mock(Action.class); // given
    
    businessRuleEngine.addAction(mockAction); // when
    businessRuleEngine.run(); // when
    
    // 3. verify method
    verify(mockAction).perform(); // then
  }
}

```

<!--TODO: What is static import?-->

모키토를 사용해서 뭐가 나아졌는데?

- 비즈니스 규칙 엔진이 실행될 때 Action 객체의 perform() 메서드가 호출되었는지 확인할 수 있다.
- 메서드가 몇 번 호출되었는지, 어떤 인수가 제공되었는지 등 조금 더 복잡한 검증 로직도 실행할 수 있다.

## 조건 추가하기

특정 **`조건`**을 만족하면 

- 액션을 수행하도록 설정할 수 있어야 한다. 
- 이 조건은 <u>**어떤 팩트에 의존**</u>하는데 예를 들어 "**잠재 고객의 직함이 CEO** 면 알림 주기" 같은거다.

### 1. 상태 모델링

```java
final Customer customer = new Customer("Mark", "CEO");

businessRuleEgine.addAction(new Action() {
  @Override
  public void perform() {
    if("CEO".equals(customer.getJobTitle())) {
      Mailer.sendEmail("sales@company.com", "Relevant cusomer:" + customer);
    }
  }
})
```

위의 코드에선 문제가 두 가지가 있다.

1. 액션을 어떻게 테스트할 것 인가?
   - `customer` 객체가 하드코딩된 디펜던시("Mark, "CEO")를 가지기 때문에 기능 코드가 독립적이지 않다.
2. `customer` 객체는 여러 곳에 공유된 외부 상태이므로 의무(책임)가 혼란스럽게 엉틴다.
   - `customer` 객체는 액션과 그룹화되어 있지 않다.
   - <!--TODO: 그룹화 되어 있지 않다는 표현이 무엇?-->

해결 방법

- 비지니스 규칙 엔진 내의 액션에서 사용할 수 있는 상태로 캡슐화해야 한다. -> `Facts` 라는 클래스로 정의

이르케 상태를 모델링하는 `Facts` 클래스를 따로 만들면

- 공개 API로 사용자에게 제공할 기능을 조절할 수 있으며
- 클래스의 동작을 유닛 테스트할 수 있다.

### 2. 지역 변수 형식 추론

자바 10의 기능

- 지역 변수 **`형식 추론`** 기능을 지원

**`형식 추론`**이란

- 컴파일러가 정적 형식을 자동으로 추론해 결정하는 기능
- 사용자는 더 이상 명시적으로 형식을 지정할 필요가 없음.

```java
Map<String, String> facts = new HashMap<>(); // 이 기능은 자바 7에서 추가된 '다이아몬드 연산자'라는 기능.
```

자바 10 부터는 형식 추론이 **지역 변수까지** 확장 적용 됨.

```java
var env = new Facts(); // Facts env = new Facts();
var businessRuleEngine = new BusinessRuleEngine(env); // BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(env);
```

<!--TODO: var 키워드란 무엇일까?-->

항상 이 기능을 사용해야 할까?

- 노노, 개발자는 코드를 구현하는 것보다 읽는 데 더 많은 시간을 소비한다.
- `var` 이걸 써도 읽히면 이걸 써도 됨 ㅋㅋㅋ

### 3. switch 문

여러 거래를 저장하고 싶어!

- 상태를 `enum` 으로 지정하자.

  ```java
  public enum Stage {
    LEAD, INTERESTED, EVALUATING, CLOSED
  }
  ```

- <!--TODO: java enum 에 대해 깊숙히 알아보자.-->

**자바 12**에서는 

- 새로운 `switch` 문을 이용해서 여러 `break` 문을 사용하지 않고도 **<u>폴스루</u>**를 방지할 수 있다.
- 폴스루(fall-through)란, 다음 블록이 의도치 않게 실행되는 것. 그래서 사소한 버그가 발생할 수 있다.

그래서 새로운 switch 문을 쓰면

- 가독성이 좋아
- 모든 가능성을 확인하는 **소모 검사**(exhaustiveness)도 이뤄짐.
- <u>자바 컴파일러</u>가 모든 enum 값을 switch에서 소모했는지 확인함!

### 4. 인터페이스 분리 원칙(ISP)

클래스의 기능을 수행과 함께 <u>수행 조건을 검사</u>하는 기능도 추가하고 싶다면?

- 따로 조건을 평가하는 기능을 내장하도록 새 인터페이스를 구현한다.

인터페이스 분리 원칙이란?

- 어떤 클래스도 사용하지 않는 메서드에 의존성을 갖지 않아야 한다. -> 불필요한 결합을 만들기 때문

ISP가 SRP과 비슷하지만 관점이 다른데,,

- ISP는 설계가 아닌 사용자 인터페이스(UI)에 초저을 둔다
- 즉, 인터페이스가 커지면 인터페이스 사용자는 결국 사용하지 않는 기능을 갖게 되며 이는 불필요한 결합도를 만든다.

그래서 ISP를 잘 따르려면?

- <u>현재의 개념</u>을 **독자적인 작은 개념**으로 쪼개야 한다. -> 이 원칙은 `응집도`도 높아진다.

## 플루언트 API 설계

개선된 switch문은 그리 친숙한 문법이 아닌데 어떻게,,?

- 비즈니스 사용자의 도메인에 맞춰 단순하게 규칙을 추가하는 기능을 제공하려함.

여기서 배울건?

- 빌더 패턴
- 플루언트 API

### 1. 플루언트 API란

플루언트 API란

- 특정 문제를 더 직관적으로 해결할 수 있도록 **<u>특정 도메인</u>**에 맞춰진 API를 가리킨다.
- 메서드 체이닝(method chaining)을 이용하면 더 복잡한 연산도 저장할 수 있음.

많이 접해봤을 껀데

- 자바 스트림 API <!--TODO: java stream  -->
- Spring Integration <!--TODO: Spring Integration -->
- jOOQ  <!--TODO: jOOQ?? -->

### 2. 도메인 모델링

우리가 만들 애플리케이션의 <u>편의성을 어떻게 개선</u>할 것 인가?

- 어떤 조건이 주어졌을 때(when)
- 이런 작업을 한다(then)

위와 같은 간단한 조합을 규칙으로 지정할 수 있게 한다.

만들 애플리케이션의 도메인에는 세 가지 개념이 등장하는데

- 조건 : 어떤 패트에 적용할 조건
- 액션 : 실행할 연산이나 코드 집합
- 규칙 : 조건과 액션을 합친 것. 조건이 참일 때만 액션을 실행한다.

> 좋은 이름은 코드가 어떤 문제를 해결하는지 이해하는 데 도움을 주므로 프로그래밍에서 좋은 이름은 아주 중요하다.

```java
// Condtion interface
@FunctionalInterface
public interface Condition {
  boolean evalute(Facts facts);
}

// Rule interface
@FunctionalInterface
public interface Rule {
  void perform(Facts facts);
}

// DefaultRule implemented Rule
public class DefaultRule implements Rule {
  private final Condition condition;
  private final Action action;
  
  public Rule(final Condtion condition, final Action action) { // TODO: 아니 생성자는 DefaultRule 아님?
    this.condition = condition;
    this.action = action;
  }
  
  public void perform(final Facts facts) {
    if (condition.evalute(facts))
      action.execute(facts);
  }
}

// 동작
public class Main {
  public static void main(String[] args) {
    // ...
    final Condtion condition = (Facts facts) -> "CEO".equals(facts.getFact("jobTitle"));
    final Action action = (Facts facts) -> {
      var name = facts.getFact("name");
      Mailer.sendEmail("sales@company.com", "Relevant customer!!" + name);
    }
    
  } 
}
```

### 3. 빌더 패턴

위의 코드는 여전히 <u>수동적</u>인데 어떻게 개선할 것?

- 사용자가 각 객체를 인스턴스화한 다음, 한데로 모아야 한다.

- **`빌드 패턴`**으로 Rule 객체와 필요한 조건, 액션을 만드는 과정을 개선해보자.

개선 과정

- 빌던 패턴
  - 단순하게 객체를 만드는 방법을 제공한다. 
  - 생성자의 파라미터를 분해해서 각각의 파라미터를 받는 여러 메서드로 분리한다.
- 덕분에 각 메서드는 도메인이 다루는 문제와 비슷한 이름을 갖는다.

```java
public class RuleBuilder {
  private Condtion condtion;
  private final Action action;
  
  public RuleBuilder when(final Condtion condition) { 
    this.condition = condition;
		return this; // 현재 인스턴스를 반환
  }
  
  public RuleBuilder then(final Action action) { 
    this.action = action;
		return this; // 현재 인스턴스를 반환 -> 이렇게 하면 메서드를 연쇄적으로 연결한다.
  }
  
  public Rule createRule() {
    return new DefaultRule(condtion, action);
  }
}
```

```java
Rule rule = new RuleBuilder()
  .when(facts -> "CEO".equals(facts.getFact("jobTitle")))
  .then(facts -> {
    var name = facts.getFact("name");
    Mailer.sendEmail("sales@company.com", "Relevant customer!!" + name);
  })
  .createRule();
```

이렇게하면

- 쿼리와 비슷한 형태로 보이며
- 규칙의 개념, when(), then() 등 도메인 용어를 내장 생성자로 활영한다.

그래도 두 가지 문제가 남아 있다.

- 빈 RuleBuilder 인스턴스화
- createRule() 메서드 호출

세 가지 방법으로 API를 개선해보자.

1. 사용자가 명시적으로 생성자를 호출하지 못하도록 생성자를 비공개로 설정한다. 그러려면 API에 다른 진입점을 만들어야 한다.
2. when() 메서드를 정적 메서드로 만들어 이 메서드를 사용자가 직접 호출하면 예전 생성자를 호출하도록 한다. 게다가 정적 메서드를 제공하므로 Rule 객체를 설정하려면 어떤 메서드를 이용해야 하는지 쉽게 알 수 있으므로 발견성도 개선된다.
3. then() 메서드가 DefaultRule 객체의 최종 생성을 책임진다.

```java
public class RuleBuilder {
  private final Condtion condtion;
  
  private RuleBuilder(final Condtion condtion) {
    this.condition = condtion;
  }
   
  public static RuleBuilder when(final Condtion condition) { 
		return new RuleBuilder(condtion); // 현재 인스턴스를 반환
  }
  
  public RuleBuilder then(final Action action) { 
    return new DefaultRule(condtion, action);
  }
  
  public Rule createRule() {
    return new DefaultRule(condtion, action);
  }
}

// 사용 (in Main.class)
final Rule ruleSendEmailToSalesWhenCEO = RuleBuilder
  .when(facts -> "CEO".equals(facts.getFact("jobTitle")))
  .then(facts -> {
    var name = facts.getFact("name");
    Mailer.sendEmail("sales@company.com", "Relevant customer!!" + name);
  });

```

## 정리

- TDD
- 모킹
- switch문, 형식 추론
- 빌더 패턴
- 인터페이스 분리 원칙