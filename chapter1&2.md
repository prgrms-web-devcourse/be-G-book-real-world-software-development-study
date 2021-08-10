# chapter 1 여행의 시작 && chapter 2 입출금 내역 분석기

발표일: August 9, 2021
발표자: 임현유

  

[1. SRP : Single Responsibility Principle, 단일 책임 원칙](#1-srp--single-responsibility-principle-단일-책임-원칙)

​		[코드 유지보수성 Code Maintainability](#코드-유지보수성-code-maintainability)

​		[안티패턴 : Anti-pattern](#안티패턴--anti-pattern)		

[2. 응집도 & 결합도](#2-응집도--결합도)

​		[응집도 Cohesion](#응집도-cohesion)

​		[결합도 Coupling](#결합도-coupling)

[3. 테스트](#3-테스트)

​		[테스트 자동화](#테스트-자동화)

​		[유닛 테스트](#유닛-테스트)

​		[Given - When - Then 공식](#given---when---then-공식)

​		[코드 커버리지 Code Coverage](#코드-커버리지-code-coverage)

  
<br/>

<br/>  

# 1. SRP : Single Responsibility Principle, 단일 책임 원칙

목적 : 쉬운 유지보수, 가독성 높이기, 버그 발생 범위 좁히기

<br/>

<details>
  <summary>cf) DRY, KISS, 놀람최소화 원칙</summary>



- **DRY : Don`t Repaet Yourself**

- **KISS : Keep It Short and SImple**

  <details>
    <summary markdown="span"> 놀람 최소화 원칙 : Principle of Least Surprise</summary>


  - 메서드의 동작을 바로 이해할 수 있는 메서드명 사용  
  - 파라미터의 상태를 바꾸지 않는다. 코드가 파라미터의 상태에 의존할 수 있으므로 
    </details>

</details>

<details>
  <summary> cf) final 변수</summary>



- 어떤 객체 상태의 변화 가능 여부를 구분 가능
- 주의! final 필드로 가리키는 객체라도 가변상태(mutable state)를 포함할 수 있다 (4장 참고)
- var  키워드의 등장으로 final의 유용성이 크게 감소되었다 (5장 참고)  

</details>

<br/>  

### 코드 유지보수성 Code Maintainability

- 특정 기능을 담당하는 코드를 <u>쉽게 찾을</u> 수 있어야 함
- 코드의 수행 작업을 <u>쉽게 이해</u>할 수 있어야 함
- 새로운 기능을 <u>쉽게 추가</u>하거나, 기존 기능을 <u>쉽게 제거</u>할 수 있어야 함
- **캡슐화(Encapsulation)**가 잘 되어 사용자가 (세부 구현 내용을 읽을 필요 없이) <u>쉽게 기능 변경</u>을 할 수 있어야 함

<br/>  

### 안티패턴 : Anti-pattern

- **갓 클래스(God Class)**

    하나의 거대한 클래스가 모든 일을 수행 

- **코드 중복 (Code Duplication)**

    여러 곳에 동일한 코드가 존재

→ 유지보수가 어렵다 

→ **SRP** 원칙 필요 && **KISS** 원칙을 남용하지 않아야

<br/>  

<br/>  

<br/>  

# 2. 응집도 & 결합도

<br/>  

## 응집도 Cohesion

- 코드의 동작과 책임이 서로 **연결**된 정도

- 코드를 어떻게 그룹화 하는가


  
    #### 다양한 응집도 수준과 장단점
    
    | **응집화** **방법** | **응집도** **수준** | **장점**                    | **단점**                           |
    | ------------------- | ------------------- | --------------------------- | ---------------------------------- |
    | **기능**            | 높음                | 이해하기 쉬움               | 너무 단순한 클래스 생성            |
    | **정보**            | 중간                | 유지보수하기 쉬움           | 불필요한 디펜던시                  |
    | **순차**            | 중간                | 관련 동작을 찾기 쉬움       | SRP 위배 가능성                    |
    | **논리**            | 중간                | 높은 수준의 카테고리화 제공 | SRP 위배 가능성                    |
    | **유틸리티**        | 낮음                | 간단히 추가 가능            | 클래스의 책임을 파악하기 어려움    |
    | **시간**            | 낮음                | -                           | 각 동작을 이해하고 사용하기 어려움 |
  
<br/>

## 결합도 Coupling

- 코드가 서로 **의존**하는 정도

    ex) 시계의 인터페이스와 구현

    사람은 시계의 내부구조를 몰라도 시계를 읽을 수 있다. 

    → 시계 내부구조는 시계의 인터페이스에 영향을 주지 않는다

<br/>  

<br/>  

<br/>  

# 3. 테스트

<br/>

### **테스트 자동화**

- 사람의 조작 없이 여러 테스트가 포함된 스위트(Suite)가 자동으로 실행됨
- 코드가 제대로 동작할거라고 조금 더 확신을 가질 수 있다
- 장점
    - 확신이 높아짐
    - 변화에도 튼튼함 유지
    - 프로그램 이해도

<br/>  

### **유닛 테스트**

유닛 Unit : 메서드나 작은 클래스처럼 작고 고립된 단위

- 테스트 클래스명 뒤에 Test를 붙이는게 관례
- 테스트 메서드명은 동작을 최대한 서술적으로 작성
- @Test 어노테이션을 테스트 메서드에 추가(제이유닛)
- Assert구문으로 예상된 결과를 설정! p.46 참고

<br/>  

### **Given - When - Then 공식**

- **Given :** 테스트의 Context를 설정
- **When :** 동작을 실행
- **Then :** 예상된 결과를 assertion으로 설정

<br/>

### **코드 커버리지 Code Coverage**

테스트 집합이 소프트웨어의 소스코드를 얼마나 테스트했는가를 가리키는 척도

- **구문 커버리지 (Line Coverage)**

    추천X. 분기문(if, for, while)을 하나의 구문으로 처리한다

- **분기 커버리지 (Branch Coverage)**

    추천O

<br/>  

<br/>  

<br/>  

# 질문!

<br/>

- Q. 아래 문구가 무슨뜻인가요?
    - 코드의 다른 부분이 파라미터의 상태에 의존할 수 있으므로 파라미터의 상태를 바꾸지 않는다.
    - final 키워드를 적용한다고 해서 객체가 바뀌지 못하도록 강요하는 것은 아님.

    A. 코드예시

    ```java
    
    public class DevSchool {
    
    	public class Student() {
    		String alias;
    		String health;
    		...
    	}
    	
    	public void main() {
    	
    		Student student = new Student();
    	
    		// 메소드명을 보고 여기서 student 객체의 health 상태를 바꿀 것이라 예측할 수 없음
    		enroll(student);
    		
    		if (student.alias.equals("백둥이")) {
    			student.health = "Good";
    		} else {
    			student.health = "Bad";
    		}
    		...
    	}
    	
    	public void enroll(final Student student) {
    	
    		// student = otherStudent;   <- final 키워드로 인해 불가능	
    		student.alias = "앞둥이"; //   <- 가능은 하지만, 놀람 최소화 원칙에 위배됨
    		...
    	}
    }
    
    ```

<br/>      
    
- Q. 결합도를 낮출 수 있는 다른 방법?

    A. factory 패턴! 결합도를 낮추는 대표적인 방법 중 하나

<br/>      

- Q. 결합도를 낮춰야 하는 이유?

    A. 한객체가 다른 객체를 변경할 수 있는 행위를 막는 것만으로 결합도를 낮출 수 있음

<br/>      

- Q. 의존성 주입을 통해서 결합도를 낮출 수 있는지?

    A. IoC의 Bean, DI, AOP

<br/>

  
