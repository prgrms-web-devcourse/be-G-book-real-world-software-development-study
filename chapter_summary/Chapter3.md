## 3.4 Open-Closed Principal

### 요구받은 구현 사항

>1. 특정 **금액** 이상의 입출금 내역 찾기 
>
>2. 특정 **월**의 입출금 내역 찾기

BankProcessor에 아래의 2가지 기능을 추가해보자.

만약, 각각의 기능을 **메소드로 따로 분리하여 구현**한다면❓❓❓

**[ 특정 금액 이상의 입출금 내역 조회 ]**

```java
public List<BankTransaction> findTransactionsGreaterThanEqual(final int amount){
  final List<BankTransaction> result = new ArrayList();
  for(BankTransaction bt : this.banktransactions){
    if(bankTransaction.getAmount() >= amout){
      result.add(bankTransaction);
    }
  }
  return result;
}
```

**[ 특정 월의 입출금 내역 조회 ]**

```java
public List<BankTransaction> findTransactionsInMonth(final Month month){
  final List<BankTransaction> result = new ArrayList();
  for(BankTransaction bt : this.banktransactions){
    if(bankTransaction.getMonth().equals(month)){
      result.add(bankTransaction);
    }
  }
  return result;
}
```

🛑 위와 같은 구현의 문제점은 

>1. **코드의 중복** ( **DRY 위배** )
>2. 반복 로직 + 비즈니스 로직이 결합
>3. **확장성이 낮다.** ( **OCP 위배** )

만약,  BankTransaction의 속성들을 이용하여 새로운 조회 기능을 만들고자 할 경우 **계속해서 새로운 메소드를 만들어야 할 것**이고 **코드의 중복성은 더 심해진다.** 🤑 

Ex) 

- **( 속성의 조합 )특정 월,금액 이상의 입출금 내역** 조회 기능을 추가한다고 했을 경우 ⇢ `findTransactionsInMonthAndGreaterThanEqual(BankTransaction)(...)`
- **( 새로운 속성 )특정 descpiton을 가진 내역**을 조회 하고자 하는 경우 ⇢`findTransactionsHasDescription(BankTransaction)(...)`

- 속성이 추가 되고 기능이 늘어나면 끝도없이 메소드가 늘어난다......😂



따라서, 앞의 2장에서 배웠듯 **비즈니스 로직** ( **BankTransaction의 속성을 이용 하여 List에 넣을지를 판단** )을 **인터페이스로 분리**한다.

>1. 인터페이스로 **분리**
>2. 해당 인터페이스를 **구현하느 클래스 정의**
>3. 2에서 정의한 클래스를 **인스턴스화**

그리고, 위의 3가지 과정을 거쳐서 사용 할 수 있다. 하지만 이러한 과정은 **불필요한 클래스를 너무 많이 만들어 내기 때문에** **익명클래스 (람다 표현식)를 이용한다.**

**[ BankProcessor의 내부 ]**

```java
public findTransactions(BankTransactionFilter filter){
  List<BankTransaction> filteredTransactions = new ArrayList<>();
  (BankTransaction bt : this.bankTransactions){
    if(filter.(bt)) filteredTransactions.add(bt);
  }
  return filteredTransactions;
}
```

**[ BankProcessor의 클라이언트(사용하는 쪽) ]**

```java
// 특정 금액 이상의 입출금 내역 조회
List<BankTransaction> list1 = bankProcessor.filter(bankTransaction -> bankTrnasction.getAmount() >= 1_000d)
  
// 특정 월의 입출금 내역 조회
List<BankTransaction> list2 = bankProcessor.filter(bankTransaction -> bankTrnasction.getMont().equals(Month.FEBURARY))
  
// 특정 월의 특정 금액 이상의 입출금 내역 조회
List<BankTransaction> list3 = bankProcessor.filter(bankTransaction ->
{
    return bankTrnasction.getMonth().equals(Month.FEBURARY)
            && bankTransaction.getAmount() >= 1)000d;
})
// BankTransaction의 속성을 이용해서 무언가 찾고자 한다면 자유롭게 확장 할 수 있다...
```

>1. 기존 코드를 수정하지 않고도 **기능을 확장 할 수 있다.** ⇢ BankTransactionProcessor에 새로운 method가 계속 추가되지 않는다.
>
>2. 코드가 **중복되지 않는다.** 
>
>3. **결합도**가 낮아진다. ⇢ Method가 BankTransaction라는 도메인의 **속성**에 종속 되지 않는다. 



## 3.5 인터페이스 문제

### 갓 인터페이스

BankTransaction들에 대한 **연산 기능을 제공하는 API성 Interface**인 BankTransactionProcessor를 인터페이스로 정의하는 것이 옳을까? 

이러한 형태의 인터페이스 설계는 **지양 해야 한다.** 

인터페이스가 도메인의 **특정한 속성에 종속되는 것은 지양해야 한다.**

또한, 확장 해야 할 가능성이 거의 없는 경우 굳이 인터페이스로 새로 정의하여 추상화 구조를 더 복잡하게 만들 이유도 없다.

### 지나친 세밀함

하지만, 그렇다고 지나치게 세분화해서 인터페이스를 나누는 것도 지양 해야 한다.

앞에서 배웠듯이 **응집도**를 생각해보았을 때 **유사한 기능들은 한 곳에 응집되어 있는 곳이 좋기 때문이다.**



👉 3.5 파트는 일종의 문제 제기용 파트인듯 ❓❓❓ 합니다.

##  3.6 명시적 API vs 암묵적 API

3.5에서 살펴 보았듯이 그렇다면, 어느 정도까지 도메인에 종속적이어야 할까?

### 명시적 API

> ```findTransactionsInMonth(final Month month)``` 과 같이 **도메인의 특정 속성에 종속적**인 API

📈**장점**

- 직관적이다.
- 가독성이 좋다.

📉**단점**

- 도메인의 특정 속성에 종속 되어 있기 때문에, 확장성이 좋지 못하다. (이해가 가지 않는다면 3.4를 잘 읽어보자!)



### 암묵적 API

> `public findTransactions(BankTransactionFilter filter)`과 같이 도메인의 특정 속성에 종석적이지 않은 API

📈**장점**

- 확장성이 좋다.

📉**단점**

- 직관성이 떨어진다 ⇢ 처음 사용하기가 어렵기 때문에, **문서화를 잘 해 놓는것이 무엇보다 중요하다.**



### 명시적 API와 암묵적 API의 절충점 찾기

> - 확장성을 위해 암묵적 API를 고려하자.
> - 단, 흔히 사용하는 연산이라면 사용자가 이해하기 쉽게 이를 명식적 API로 만들자.



### 도메인 클래스 값 vs 원시 값

**원시값**으로 다양한 결과를 반환하게 되면 **유연성이 떨어진다.** 따라서, **새로운 도메인 클래스를 정의**하고 **그 안에 원시 값을 담아서 반환**하는 것이 좋다.



## 3.7 다양한 형식으로 내보내기 - 도메인 객체

**숫자**

- 유연한 대처가 떨어진다.

**컬렉션**

- 컬렉션**'만'** 반환 할 수 있다.

**특별한 도메인 객체**, **일반적인 도메인 객체**❓❓

- **생산하는 부분과 소비하는 부분이 서로 결합하지 않는다.**



## 3.8 예외처리

- 노티피케이션 패턴
- **예외 대안 기능 - Optional**

- 예외처리 관련 글은 따로 아티클로 정리 할 예정입니다!

## 3.9 빌드 도구

- Maven
- Gradle

