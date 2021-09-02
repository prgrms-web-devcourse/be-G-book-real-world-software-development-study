## 클라이언트 서버 모델

다양한 환경의 사용자들이 서로 통신 -> **클라이언트 서버 모델**

### 풀 기반

- 클라이언트가 서버로 정보를 요청한다.
- 점대점(point-to-point), 요청 응답 통신 형식
- 대부분의 웹에서 이 통신 형식을 사용한다.

### 푸시 기반

- 리액티브(reactive), 이벤트 주도(event-driven) 통신
- 작성자가 방출한 이벤트 스트림을 여러 구독자가 수신한다.
- 일대일 통신뿐만 아니라 일대다 통신도 지원한다.
- 여러 컴포넌트 간에 다양한 이벤트의 의사소통이 발생하는 상황에서 유용하다.



## 육각형 아키텍처 - 포트와 어댑터

구성 요소 간의 종속성이 도메인 개체 안쪽(코어 비즈니스)으로 향한다는 것

- 코어 비즈니스 로직과 분리하려는 특정 기술이 있다면 **포트**를 이용한다. 외부 이벤트는 **포트**를 통해 코어 비즈니스 로직으로 전달한다.
- **어댑터**는 포트로 연결하는 특정 기술을 이용한 구현 코드다.

아키텍처를 이용하려면 포트와 어댑터를 추상화해야 한다. 

어떤 기능을 포트로 지정하고 어떤 기능을 도메인으로 분리해야 하는지?

비즈니스 문제를 해결하는 데 꼭 필요한 기능을 응용프로그램의 코어로 분류, 나머지 특정 기술에 족속된 기능이나 통신 관련 기능은 코어 응용프로그램의 외부 세계로 분류
=> 비즈니스 로직은 코어 도메인 / 저장 기능, UI를 이용한 이벤트 주도 통신 기능은 포트로 추상화

포트와 어댑터의 목표 : 응용 프로그램의 코어와 특정 어댑터 구현의 결합을 제거하는 것 - 인터페이스로 다양한 어댑터를 추상화해야 한다.

> [지속 가능한 소프트웨어 설계 패턴: 포트와 어댑터 아키텍처 적용하기](https://engineering.linecorp.com/ko/blog/port-and-adapter-architecture/)



## Optional

값이 있거나 없는 상황을 표현한다.

반환 형식으로 Optional을 사용해 메서드 실행 결과가 실패했음을 명시적으로 표현할 수 있다.

'결과 없음'을 타입으로 강제하기 위해 존재한다. (null은 결과 없음을 의미하지 않는다. 그냥 null 값이라는 특수한 비트열일 뿐)

메서드가 반환할 결과값이 ‘없음’을 명백하게 표현할 필요가 있고, `null`을 반환하면 에러를 유발할 가능성이 높은 상황에서 메서드의 반환 타입으로 `Optional`을 사용하자는 것이 `Optional`을 만든 주된 목적이다.

1. `isPresent()-get()` 대신 `orElse()/orElseGet()/orElseThrow()`
2. `orElse(new ...)` 대신 `orElseGet(() -> new ...)`
3. 단지 값을 얻을 목적이라면 `Optional` 대신 `null` 비교
4. `Optional` 대신 비어있는 컬렉션 반환
5. `Optional`을 필드로 사용 금지
6. `Optional`을 생성자나 메서드 인자로 사용 금지
7. `Optional`을 컬렉션의 원소로 사용 금지
8. `of()`, `ofNullable()` 혼동 주의
9. `Optional<T>` 대신 `OptionalInt`, `OptionalLong`, `OptionalDouble`

[Optional 바르게 쓰기](https://homoefficio.github.io/2019/10/03/Java-Optional-%EB%B0%94%EB%A5%B4%EA%B2%8C-%EC%93%B0%EA%B8%B0/)



## 비밀번호와 보안

비밀번호에 암호화 해시 함수를 적용 - 임의의 길이의 문자열을 입력받아 **다이제스트(digest)**라는 출력으로 변환하는 기능

> digest : Hash 함수를 통과 하기전의 원본 데이터를 메세지(message)라고 부르고, 통과된 이후의 데이터를 다이제스트(digest)라고 부른다.

바운시 캐슬(Bouncy Castle) 라이브러리 : 경량화된 암호화 API(기본 암호화 알고리즘을 구현하는 API set) 제공

Scrypt 해싱 함수 : 다이제스트를 생성할 때 메모리 오버헤드를 갖도록 설계되어, 억지 기법 공격(brute-force attack)을 시도할 때 병렬화 처리가 매우 어렵다.

솔트(salt) : 암호 해싱 함수에 적용하는 임의로 생성된 추가 입력. 사용자가 입력하지 않은 임의의 값을 비밀번호에 추가해 누군가 해싱을 되돌리는 기능을 만들지 못하게 막는다.

전송 계층 보안(Transport Layer Security) : 연결된 네트워크로 전달되는 데이터의 프라이버시와 무결성을 제공하는 암호화된 프로토콜



## 오류 모델링

예외가 아닌 다른 방식

- 불리언
  - 실패의 원인이 한 가지뿐이라면 좋은 선택
  - 여러 가지 이유로 동작이 실패할 수 있는 상황에서 **왜 이 동작이 실패했는지** 알려줄 수 없다는 것이 단점
- int 상숫값
  - 안전한 형식을 제공하지 못함
  - 가독성과 유지보수성이 낮아짐

=> **enum 형식** - 안전한 형식과 좋은 문서화를 제공할 수 있다.



## equals(), hashCode() 메서드

참조되는 값으로 두 객체가 같은지 판단하는 메서드

equals()와 hashCode() 메서드 사이의 계약 -> equals() 메서드를 오버라이드하면 hashCode() 메서드도 오버라이드해야 한다.

equals() 메서드로 같다고 판단했을 때 hashCode() 메서드 역시 같은 값을 반환해야 한다. 