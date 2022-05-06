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

- 실무에서는, was 띄울 때  메모리 db에 테스트 하는 방법을 사용한다.

> 격리된 환경에서 테스트
- 테스트를 완전히 격리된 환경에서 테스트하는 방법 : 메모리 db 사용
- 스프링 부트에서는 바로 사용가능 하다.  
- main, test로 폴더가 나뉜다. 
- **test가 돌 때는 test폴더가 우선권을 가진다. 그래서 test용 yml파일을 가지면 db셋팅을 memory로 바꾼다.**
- h2를 메모리 모드로 띄우게 설정한다.
  - h2 홈페이지 참고 
  - jdbc:h2:mem:<databaseName>
- 결론 : test용 application.yml파일을 test/resources 디렉토리에 생성 후 db url을 메모리 모드로 띄우게 설정한다.
  - 그런데 스프링 부트는 기본적으로 메모리 db 사용해서 테스트 수행하므로 yml에 db 접속 정보가 없어도 테스트가 실행된다.
  - url jdbc:h2:mem:0e2ce3cf-8edd-462c-a435-59dbb5231ead

---

## chap6 상품 도메인 개발

- `데이터를 가지고 있는 쪽`에서 비지니스 메서드가 있는 것이 응집력이 좋다.
- Item 클래스에 stockQuantity 속성을 가지고 있으므로 , 재고 늘리고/줄이는 비지니스 메서드를 Item 클래스에 생성한다.

---

## chap7 주문 도메인 개발

> 구현 기능
- 상품주문
- 주문 내역 조회
- 주문 취소

> 질문
- [] 생성 메서드는 왜 static으로 만드는걸까?

> 주문 생성
- cascade = CascadeType.ALL을 oderItem과 delivery에서 해줬기 때문에 order만 save해도 연관관계 모두 저장된다.
```java
orderRepository.save(order);
```
> cascade의 범위 고민
- 보통 order같은 경우에 order가 delivery와 orderItem을 관리
- 참조하는 주인이 `private Owner`일 경우에만 사용 : `order만 참조`해서 사용한다
- 감이 오지 않을 때는 cascade를 쓰지 않다가, 나중에 refactoring하는 것도 좋다.

> 주문 취소
- Dirty Checking : 변경 내역 감지
  - 우리는 상태를 변경하거나, 재고수량을 더해준다. 이렇게 객체의 데이터만 변경하면    
    JPA가 변경된 내용을 찾아서 db에 update 쿼리를 날린다.

> 비지니스 로직의 위치 - 도메인 모델 패턴 vs 트랜잭션 스크립트 패턴
- 주문 생성, 취소의 비지니스 로직이 대부분 엔티티에 있다.
- `도메인 모델 패턴`
  - 엔티티가 비지니스 로직을 가지고 객체 지향의 특성을 활용하는 것
  - JPA나 ORM기술 사용하면 도메인 모델 패턴으로 사용하게 된다.
- `트랜잭션 스크립트 패턴`
  - 엔티티에는 비지니스 로직이 없고, 서비스에서 비지니스 로직을 처리하는 것
- 상황에 맞는 패턴을 사용하도록 한다. 한 프로젝트 안에서도 두개의 패턴이 양립하기도 한다.

### 주문 기능 테스트

- 지금 코드에서는 JPA 동작 확인을 위해 DB가 연동된 테스트를 하고 있다.
- 하지만 원래 좋은 테스트는 의존성없이 순수하게 메서드를 단위테스하는 것이 좋다. 

> 상품주문_재고수량초과 테스트
- 원래는 Item.removeStock() 메서드를 단위 테스트하는 것이 중요하다. 

> 주문 취소 테스트 
- 무엇을 테스트 해야할까?
  - 주문을 취소했을 때 재고수량이 원복되는지 확인한다.

> 도메인 모델 패턴의 테스트 
- 엔티티에 핵심 비지니스 로직이 있기 때문에 엔티티에 대해서 테스트를 작성할 수 있다. 
- 그 비지니스 로직을 의미있게 테스트를 작성하는 것이 중요하다.

### 주문 검색 기능 개발

- 동적 쿼리 사용

> 동적쿼리
- parameter값인 status, member이 없으면, `select o from Order o join o.member m`으로 쿼리가 동적으로 생성되어 모든 결과를 가져와야 한다.
- JPA에서는 이런 동적쿼리를 어떻게 처리할까?

1. 문자열로 동적쿼리 생성
```java
return entityManager.createQuery("select o from Order o join o.member m "
			+ "where o.status = :status "
			+ "and m.name like :name", Order.class)
			.setParameter("status", orderSearch.getOrderStatus())
			.setParameter("name", orderSearch.getMemberName())
			.setMaxResults(1000) //최대 1000건
			.getResultList();
```

- JPQL에서 동적쿼리를 생성하는 것은 문자로 처리해야 하기 때문에 번거롭고 에러 발생 확률이 높다.
  - mybatis는 동적쿼리가 편리하게 된다.
```java
public List<Order> findAll(OrderSearch orderSearch) {
		String jpql = "select o from Order o join o.member m";
		boolean isFirstCondition = true;

		//주문 상태 검색
		if(orderSearch.getOrderStatus() != null) {
			if(isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}

		//회원 상태 검색
		if(StringUtils.hasText((CharSequence) orderSearch.getMemberName())) {
			if(isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.name like :name";
		}

		TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class)
			.setMaxResults(1000);  //최대 1000건

		if(orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if(StringUtils.hasText((CharSequence) orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}

		return query.getResultList();
	}
```

2. JPA Criteria - JPA 표준 지원 
 - 사용하기 힘듦. 
 - 치명적인 단점 : 무슨 쿼리가 생성될지 떠오르기 힘듦.

```java
public List<Order> findAllCriteria(OrderSearch orderSearch) {
CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
Root<Order> order = criteriaQuery.from(Order.class); //시작하는 엔티티
Join<Object, Object> member = order.join("member", JoinType.INNER);

		List<Predicate> criteria = new ArrayList<>();

		//주문상태 검색
		if(orderSearch.getOrderStatus() != null ) {
			Predicate status = criteriaBuilder.equal(order.get("status"),
				orderSearch.getOrderStatus());
			criteria.add(status);
		}

		//회원이름 검색
		if(StringUtils.hasText((CharSequence) orderSearch.getMemberName()) ) {
			Predicate name = criteriaBuilder.like(member.get("name"),
				"%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}

		criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[criteria.size()])));
		TypedQuery<Order> query = entityManager.createQuery(criteriaQuery)
			.setMaxResults(1000);
		return query.getResultList();
	}public List<Order> findAllCriteria(OrderSearch orderSearch) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> order = criteriaQuery.from(Order.class); //시작하는 엔티티
		Join<Object, Object> member = order.join("member", JoinType.INNER);

		List<Predicate> criteria = new ArrayList<>();

		//주문상태 검색
		if(orderSearch.getOrderStatus() != null ) {
			Predicate status = criteriaBuilder.equal(order.get("status"),
				orderSearch.getOrderStatus());
			criteria.add(status);
		}

		//회원이름 검색
		if(StringUtils.hasText((CharSequence) orderSearch.getMemberName()) ) {
			Predicate name = criteriaBuilder.like(member.get("name"),
				"%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}

		criteriaQuery.where(criteriaBuilder.and(criteria.toArray(new Predicate[criteria.size()])));
		TypedQuery<Order> query = entityManager.createQuery(criteriaQuery)
			.setMaxResults(1000);
		return query.getResultList();
	}
```

3. QueryDSL 사용



