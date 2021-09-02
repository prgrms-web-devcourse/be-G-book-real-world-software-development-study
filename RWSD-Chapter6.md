## 6. 트우터

### 6.4 설계 개요

#### 클라이언트-서버 모델

✔︎ 클라이언트 : 서비스를 사용

✔︎ 서버 : 서비스를 제공



#### 통신 방식

✔︎ **풀 기반**

- HTTP 
- 요청을 해야지 응답을 받을 수 있음

✔︎ **푸시 기반**

- Reactive or Event-driven 통신이라고도 부른다.

- Publisher (작성자)가 방출한 이벤트 스트림을 여러 구독자가 수신한다.

- 다른 사용자를 팔로우 즉, 이벤트를 구독하면 사용자가 구독한 관심 트웃을 서버가 클라이언트로 푸시한다.

- 메인 클래스는 이벤트를 수신 + 전송할 수 있어야 한다.

  

### 6.5 이벤트에서 설계까지

#### 6.5.1 통신

✔︎ **웹소캣**

✔︎ **메시지 큐**

- pulisher/subscriber

![img](https://user-images.githubusercontent.com/56511173/131782932-bda73aa6-3baf-47d0-97fa-10df3fc0a756.png)



✔︎ **메시지 전송이나 메시지 큐를 구현하는 오픈 소스**



- [NRISE - 양방향 통신](https://nrise.github.io/posts/using-rabbitmq/)

- [우형 - Websocket](https://techblog.woowahan.com/2547/)



#### 6.5.2 GUI

**UI를 서버의 비즈니스 로직과 분리!**

클라이언트에게 메시지를 전송하고 클라이언트의 메시지를 수신하는 인터페이스가 필요하다.



#### 6.5.3 영구 저장

'영구 저장'이라는 기능이 특정 기술에 종속적이면 안됨.



#### 6.5.4 육각형 설계 ( 포트와 어뎁터 ) 

( 👉 이 파트가 6.5에서 결국 저자가 하고자 하는 말이 아닐까 합니다...😄 )



**특정 기술에 종속된 기능**이나 **통신** 관련 기능은 코어 응용프로그램의 외부 세계로 분류하는 것이 일반적인 관례

**✔︎ 포트**

포트는 바로 인터페이스입니다. 예를 들면 클래스의 메서드 시그니처나 Java의 인터페이스가 바로 포트라고 할 수 있습니다.

**✔︎ 어댑터**

어댑터는, 디자인 패턴에도 있듯이 클라이언트에 제공해야 할 인터페이스를 따르면서도 내부 구현은 서버의 인터페이스로 위임하는 것입니다. 



❓1주차 과제에서 MemoryRepository를 FileRepository로 변경하는 과정도 저는 이러한 육각형 설계의 일종이라고 생각합니다....😅

❓[DTO를 사용해야 하는 이유](https://blog.naver.com/adamdoha/222478760881)

❓어쩌면 우리가 지난주에 토론했었던 얘기들의 연장선...



👉 [LINE 육각형 설계 설명 전문](https://engineering.linecorp.com/ko/blog/port-and-adapter-architecture/                                                                                                                                                                                                                                                                                                          )

👉 [클린아키텍쳐](https://kscory.com/dev/programming_paradigm/bitro_clean_architecture)



### 6.6 작업 순서

**✔︎ Optional**

>**메서드가 반환할 결과값이 ‘없음’을 명백하게 표현할 필요가 있고, `null`을 반환하면 에러를 유발할 가능성이 높은 상황에서 메서드의 반환 타입으로 `Optional`을 사용하자는 것이 `Optional`을 만든 주된 목적이다.** `Optional` 타입의 변수의 값은 절대 `null`이어서는 안 되며, 항상 `Optional` 인스턴스를 가리켜야 한다.

**생각해 봅시다...**

```SenderEndPoint onLogon(...)```  **vs**  ```Optional<SenderEndPoint> onLogon(...)```



- ```SenderEndPoint onLogon(...)``` ⇢ **NullPointerException.**..😹

<img src="https://user-images.githubusercontent.com/56511173/131782796-1bd83e50-7aeb-4798-94e5-10df6b0b4d25.png" alt="Uživatel 프로그래밍_드립 na Twitteru: „그들이 두려워하는 것 NullPointerException...… “" style="zoom:100%;" />



- ```Optional<SenderEndPoint> onLogon(...)``` ⇢ 어쩔 수 없이 Null Checking을 가져 갈 수 밖에 없음 😸

![편안-한 짤 - OP.GG Talk](images/131782870-9aabd692-22fd-4679-9ad8-b51bb9cfb0fd.png)







### 6.7 비밀번호와 보안

✔︎ **일반 텍스트**

- 데이터베이스에 접근한 모든 이가 사용자의 비밀번호를 확인 할 수 있다.

✔︎ **암호화 해시 함수 적용**

👉 [관련해서 간단히 읽어볼 만한 글](https://velog.io/@nameunzz/%EB%8B%A8%EB%B0%A9%ED%96%A5-%ED%95%B4%EC%8B%9C-%ED%95%A8%EC%88%98)  Hashing 👉  O(1) 장점이자 단점❓

✔︎ **솔트** 👉 현유누님이 지난번에 언급 하셨던 것 👍

✔︎ **TLS** 👉 Transport Layer의 보안 



### 6.8 팔로워와 트웃

✔︎ **상향식 기법** : 응용프로그램의 코어 설계에서 시작해서 시스템 전체를 만들어 나가는 방법.

✔︎ **하향식 기법** : 사용자 요구 사항이나 스토리에서 출발해 구현하는 데에 필요한 동작이나 기능을 먼저 개발하고, 데이터나 모델을 추가한다.



#### 6.8.1 오류 모델링

✔︎ **void - 예외**

- 나쁘지 않은 선택 하지만 **예외적인 제어 흐름**에만 예외를 사용하는 것이 일반적이다.
- UI에서의 좋은 선택은 아님



✔︎ **boolean**

- 성공이나 실패 두가지 경우가 있고

- 실패의 **원인도 한가지**일 경우 적절

  

**✔︎ int 상수값**

- 안전한 형식 **x**



✔︎ **Enum**

- 안전한 형식
- 좋은 문서화

- [**Enum 기본 정리**](https://velog.io/@eden6187/Java-Enum)

- [우형 기술블로그](https://techblog.woowahan.com/2527/)



#### 6.8.2 트우팅

#### 6.8.3 목 만들기

**✔︎ 목 객체**

- 원래 객체가 제공하는 메소드와 공개 API를 모두 제공한다.

- 이를 이용해 특정 메서드가 실제 **호출되었는지를 확인** ⇢ 리턴값에 대한 확인이 아니다.

#### 6.8.4 목으로 확인하기

**✔︎ 불변 객체**

⇢ 버그가 발생할 수 있는 범위를 줄인다.

⇢ UI에 전달되는 객체의 값이 변할 수 있다 ❓

⇢ UI는 **표현하는 일이 전부**



#### 6.8.5 모킹 라이브러리

**✔︎ 파워목**

- final 클래스 or static method 모킹 지원

- 권장되지는 않음

**✔︎ 이지목**

- 엄격한 모킹 ⇢ 명시적으로 호출이 발생할 거라 선언하지 않은 상태에서 호출이 발생했을 때 이를 오류로 간주한다.
- 관계없는 동작과 결합❓ 



#### 6.8.6 SenderEndPoint

✔︎ Objects

- null 참조 확인
- hashCode(), equals() 메서드 구현을 제공



### 6.9 Positioon 객체

#### 6.9.1 equals()와 hashCode()

#### 6.9.2 equals()와 hashCode() 메서드 사이의 계약

**✔︎ 좋은 해시코드란?**

⇢ 고르게 퍼져야 한다.

✔︎ **equals() vs hashCode()**

⇢ [관련 포스팅](https://velog.io/@eden6187/equals%EC%99%80-hashCode)

