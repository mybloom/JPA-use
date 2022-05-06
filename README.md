# JPA활용1

- 인프런 김영한 JPA활용1을 보고 정리한 내용입니다.

## chap2 프로젝트 환경 설정

### datasource decorator

- ?로 표시되는 쿼리 파라미터를 실제 값이 적용된 상태로 보여주는 datasource decorator는 성능저하의 우려가 있으므로 
실서버 사용시에는 성능테스트 후 사용하거나 개발서버에서만 사용한다.
- 해당 프로젝트에서는 p6spy를 사용.

## chap3 도메인 분석 설계

- p18,p19를 토대로 엔티티 클래스 개발하기 
- Member:Order = `일대다 양방향` 매핑
- [] 일대다 양방향 매핑 다시 보기 
- 외래키 값을 업데이트 치는 것은 누가? 
  - 둘 중에 하나를 주인이라는 개념으로 잡아서 변경 주체 설정

> 외래키 생성여부 
- 시스템마다 상황마다 다르다
- 정확성보다 잘 서비스 되는게 중요하면 외래키 빼고, index만 잘 걸어주면 되고
- 돈과 관련되고 데이터의 정합성이 중요하면 외래키 거는 것을 고민해야 한다.

> getter, setter
- 모두 제공하지 않고 꼭 필요한 별도의 메서드 제공이 이상적
- getter: 실무에서는 엔티티의 데이터는 조회할 일이 너무 많아서 getter는 모두 열어 두는 것이 편리
- setter: 변경지점이 명확하게 변경용 비지니스 메서드를 별도로 제공하는 것이 유지보수 측면에서 좋다.
  - 변경포인트를 많이 두지 않는다.

> 테이블 설계
- 단순히 id로 컬럼을 주면 조인할 때나 사용할 때 명확하지 않아서 `테이블명_id`로 주는 것이 관례상 편하다.

> 주소 값 타입
- 예시) Adderess 객체 
- 값타입은 기본적으로 불변 객체로 설계해야 한다. 
- 그러므로 Getter만 제공한다.
- JPA스펙상 엔티티나 임베디드 타입은 기본생성자를 제공한다.(public 또는 protected로 설정)
- JPA 구현 라이브러리가 객체를 생성할 때 리플렉션,프록시 같은 기술을 사용할 수 있도록 지원해야 하므로

### 엔티티 설계시 주의점

- 즉시로딩(EAGER)은 어떤 SQL이 실행될지 예측이 어렵다. 그리고 JPQL을 실행할 때 연관관계가 즉시로딩으로 되어 있으면 쿼리가 N+1개 수행된다. 
- 컬렉션은 필드에 추가한다. `List<Order> orders = new ArrayList<>();`
  - NPE 체크하지 않아도 되고,
  - 하이버네이트가 엔티티를 영속화할 때 컬렉션을 한번 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다. 
  - 하이버네이트가 원하는 매커니즘으로 동작하게 하기 위해 컬렉션을 가급적 변경하지 않는다. 

> 영속성 전이 cascade
- order 저장하면 orderItems도 같이 persist된다.
- @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) 
- []  cascade는 주인에게 해야 할까? 거울에게 해야할까?

---

## chap4 애플리케이션 구현 준비

> 애플리케이션 아키텍쳐
- 계층형 구조 사용
  - controller, web : 웹계층
  - service : 비즈니스 로직, 트랜잭션 처리
  - repository : JPA를 직접 사용하는 계층, 엔티티 매니저 사용
  - domain : 엔티티가 모여 있는 계층, 모든 계층에서 사용
- 패키지 구조
  - 컨트롤러에서 repository바로 접근 할 수 있는 구조로 갈 예정
  - 방향은 단방향으로 
- **개발순서**
  - 웹 환경을 제외한 계층을 먼저 개발 : service, repository를 먼저 개발 후 test로 검증
  - 웹, Controller는 그 다음 개발

---

## chap5 회원 도메인 개발

- 영속성 컨텍스트에 넣는 시점에 pk가 생성되기 된다. 그러므로 getId()가 항상 있는 것이 보장된다.
```java
public Long join(Member member) {
  //중복회원 검증
  validateDuplicateMember(member);
  memberRepository.save(member);
  //영속성 컨텍스트에 넣는 시점에 pk가 생성되기 때문에 getId()가 항상 있는 것이 보장된다.
  return member.getId(); 
}
```
- @Transactional 
  - jpa모든 로직들은 transaction 안에서 실행되어야 한다. 그래야 lazy로딩 등이 다 된다.
  - 조회시 @Transactional(readOnly = true) 로 해주면 조회시 최적화를 해준다.

> 의존성 주입
- 필드 의존성주입 :  테스트 작성시 mock객체 주입시 해당 객체를 바꿀 수 없다.
```java
@Autowired
private MemberRepository memberRepository;
```
- setter 의존성 주입
  - 애플리케이션 실행하면 의존성 주입을 바꿀일이 없다.
  - 그래서 추천하지 않는 방법이다.
- 생성자 의존성 주입
  - 테스트케이스 작성할 때, new로 객체 생성 시점에 IDE에서 필요한 의존성주입을 알 수 있다.
  - final로 할 경우 컴파일 시점에 생성자 의존성주입을 체크해줘서 잊어버리지 않을 수 있다.

### 회원 기능 테스트

> 테스트 시나리오
- 회원가입 성공
- 회원가입시 같은 이름 있으면 예외 발생

> 테스트 목적
- JPA가 실제 DB까지 실행되는 것을 보는 것이 목적이여서 스프링이랑 integration해서 테스트 예정

> 테스트 코드 설명

- 실행시 insert 쿼리가 없다.
- DB마다 전략이 다르긴한데, 보통은 persist() 할 때 insert문이 실행되지 않는다.
- 왜냐면 commit될 때 flush되면서 db에 저장이 된다.
- 그런데 스프링에서 @Transaction 은 기본 rollback을 한다. 그래서 insert 쿼리가 발생하지 않았다.
- 다른 방법: DB에 쿼리 발생하지만 테스트 이후에 테스트 데이터 ROLLBACK 하도록 처리 
  - entityManager를 주입받아서 em.flush()를 수행한다.
```java
    @Test
    @Transactional(rollback = false)
	void 회원가입() {
		//given
		Member member = new Member();
		member.setName("kkk");

		//when
		Long savedId = memberService.join(member);

		//then
		Assertions.assertThat(member).isEqualTo(memberRepository.findOne(savedId));
	}
```