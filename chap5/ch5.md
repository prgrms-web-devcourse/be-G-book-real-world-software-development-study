`#테스트 주도 개발` `#모킹(mocking) 기법` `#지역 변수 형식 추론` `#switch문` `#인터페이스 분리 원칙` `#빌더 패턴` 

## 테스트 주도 개발

- 테스트 코드를 먼저 만든 후, 이에 맞춰 코드를 구현하는 것
- 실제 코드를 구현하기 전에 테스트 코드를 먼저 구현한다.

### TDD를 사용하는 이유

- 테스트를 따로 구현하므로 테스트에 대응하는 요구사항을 한 개 씩 구현할 때마다 필요한 요구 사항에 집중하고 개선할 수 있다.

- 코드를 올바르게 조직할 수 있다. 먼저 테스트를 구현하면서 코드에 어떤 공개 인터페이스를 만들어야 하는지 신중히 검토하게 된다.

- TDD 주기에 따라 요구 사항 구현을 반복하면서 종합적인 테스트 스위트를 완성할 수 있으므로 요구 사항을 만족시켰다는 사실을 조금 더 확신할 수 있으며 버그 발생 범위도 줄일 수 있다.

  > 테스트 스위트
  >
  > - 테스트 케이스(테스트 메서드)들을 하나로 묶은 것
  >
  > - 일정한 순서에 의하여 수행될 개별 테스트들의 집합

- 테스트를 통과하기 위한 코드를 구현하기 때문에 필요하지 않은 테스트를 구현하는 일(오버엔지니어링)을 줄일 수 있다.

### TDD 주기

![TDD주기_feat_리팩터링](img/TDD주기_feat_리팩터링.png)

- 실생활에서는 지속적으로 코드를 리팩터링해야 한다. 위의 프로세스를 따른다면 코드를 바꿨을 때 뭔가 잘못되어도 의지할 수 있는 테스트 스위트를 갖게 된다.



## 모킹

실제 객체를 만들기에는 비용과 시간이 많이 들거나 의존성이 크게 걸쳐져 있어서 테스트 시 제대로 구현하기 어려울 경우 가짜 객체(목 객체)를 만들어서 사용하는 기술

예제에서 BusinessRuleEngine의 run() 메서드, Action의 execute() 메서드 모두 void를 반환하기 때문에 모킹 없이는 검증하기 힘들다. (assertion을 구현할 방법이 없기 때문)

모킹 라이브러리인 모키토(mockito)를 이용하여 테스트를 구현한다.

1. 목(mock) 생성 - mock() : 필요한 목 객체를 만들고 특정 동작이 실행되었는지 확인한다.
2. 메서드가 호출되었는지 확인 - verify() : 특정 메서드가 호출되었는지 확인하는 assertion을 만든다.

```groovy
// build.gradle에 추가
testImplementation "org.mockito:mockito-core:3.+"
```

```java
@Test
public void shouldExecuteOneAction() {
  final BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine();
  // Action의 목 객체를 만든다.
  final Action mockAction = mock(Action.class);

  // when
  businessRuleEngine.addAction(mockAction);
  businessRuleEngine.run();

  // then - assertion
 	// execute() 메서드가 호출되었는지 확인한다.
  verify(mockAction).execute();
}
```

모키토를 이용해 메서드가 몇 번 호출되었는지, 어떤 인수가 제공되었는지 등 조금 더 복잡한 검증 로직도 실행할 수 있다.



## 지역 변수 형식 추론

자바 10은 지역 변수 추론 기능을 지원한다. 형식 추론이 지역 변수까지 확장 적용된다.

> 형식 추론(type inference) : 컴파일러가 정적 형식을 자동으로 추론해 결정하는 기능. 사용자는 더 이상 명시적으로 형식을 지정할 필요가 없다.

- 컴파일 타임에 추론하는 것이기 때문에, 런타임에 추가 연산을 하지 않아 성능에 영향을 주지는 않는다.
- 지역 변수를 선언할 때만 사용할 수 있다. 클래스의 멤버 변수를 선언할 때 사용할 수 없다.

```java
// 명시적 형식으로 지역 변수 선언
Facts env = new Facts();
BusinessRuleEngine businessRuleEngine = new BusinessRuleEngine(env);
```

```java
// 지역 변수 형식 추론
var env = new Facts();
var businessRuleEngine = new BusinessRuleEngine(env);
```

- 형식 추론을 이용하면 자바 코드 구현 시간을 단축할 수 있다.
- 코드를 쉽게 구현하는 것보다 코드를 쉽게 읽을 수 있느냐가 더 중요하다.
- var를 이용한 이득은 주관적이다. var를 사용해도 가독성에 문제가 없다면 var를 사용하고 그렇지 않다면 var를 사용하지 않는 것이 좋다.



## switch문

자바 12에서는 새로운 switch문을 이용해 여러 break문을 사용하지 않고 폴스루(fall-through)를 방지할 수 있다.

> 폴스루(fall-through) : 실행 흐름이 한 case에서 다른 case로 넘어가는 경우. 다음 블록이 의도치 않게 실행되면서 사소한 버그가 발생할 수 있다.

```java
var forcastedAmount = amount * switch (dealStage) {
    case LEAD -> 0.2;
    case EVALUATING -> 0.5;
    case INTERESTED -> 0.8;
    case CLOSED -> 1;
}
```

- 새로운 switch를 이용하면 가독성이 좋아질 뿐만 아니라 모든 가능성을 확인하는 소모 검사(exhaustiveness)도 이루어진다.
- enum에 switch를 사용하면 자바 컴파일러가 모든 enum값을 switch에서 소모했는지 확인한다. (enum 중에 처리하지 않은 게 있으면 오류 발생)



## 인터페이스 분리 원칙(ISP)

클라이언트가 자신이 이용하지 않는 메서드에 의존하지 않아야 한다는 원칙

- 어떤 클래스도 사용하지 않는 메서드에 의존성을 갖지 않아야 한다. 불필요한 결합을 만들기 때문이다.

- 인터페이스 분리 원칙은 설계가 아닌 사용자 인터페이스에 초점을 둔다.

  > 단일 책임 원칙은 한 개의 기능만 의무로 가져야 하며 클래스를 바꾸는 이유 역시 한 가지여야 한다는 설계 가이드라인(설계 관점)

- 인터페이스가 커지면 인터페이스 사용자는 사용하지 않는 기능을 갖게 되며 이는 불필요한 결합도를 만든다.
- 인터페이스 분리 원칙을 따르면 응집도도 높아진다. 인터페이스를 분리하면 도메인과 가까운 이름을 사용할 가능성이 커지기 때문이다.

예제에서는 ConditionalAction 인터페이스의 evaluate(), perfom() -> Condition 인터페이스, Action 인터페이스로 분리시킨다.



## 빌더 패턴

- 단순하게 객체를 만드는 방법을 제공한다.

- 생성자의 파라미터를 분해해서 각각의 파라미터를 받는 여러 메서드로 분리한다. -> 각 메서드는 도메인이 다루는 문제와 비슷한 이름을 갖는다.

  > From 이펙티브 자바
  >
  > 규칙 2. 생성자 인자가 많을 때는 Builder 패턴 적용을 고려하라 (2판)
  >
  > 아이템 2. 생성자에 매개변수가 많다면 빌더를 고려하라 (3판)

```java
public class RuleBuilder {
  private Condition condition;
  private Action action;
  
  public RuleBuilder when(final Condition condition) {
    this.condition = condition;
    return this;
  }
  
  public RuleBuilder then(final Action action) {
    this.action = action;
    return this;
  }
  
  public Rule createRule() {
    return new Rule(condition, action);
  }
}
```

위 코드의 두 가지 문제

- 빈 RuleBuilder 인스턴스화
- createRule() - 객체 생성 메서드 호출

```java
public class RuleBuilder {
  private final Condition condition;

	// 명시적으로 생성자 호출하지 못하도록 private -> API에 다른 진입점 만들기
  private RuleBuilder(final Condition condition) {
    this.condition = condition;
  }

  // 정적 메서드로 만들어 이 메서드를 사용자가 직접 호출하면 예전 생성자를 호출하도록 한다.
  public static RuleBuilder when(final Condition condition) {
    return new RuleBuilder(condition);
  }

  // 객체의 최종 생성
  public Rule then(final Action action) {
    return new Rule(condition, action);
  }
}
```

RuleBuilder 사용 예제

```java
final Rule ruleSendEmailToSalesWhenCEO = RuleBuilder
  .when(facts -> "CEO".equals(facts.getFact("jobTitle")))
  .then(facts -> {
    var name = facts.getFact("name");
    Mailer.sendEmail("sales@company.com", "Relevent customer!!!: " + name);
  });
```

