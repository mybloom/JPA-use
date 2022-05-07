# JPA 활용2 - API 개발과 성능 최적화


REST API 개발

성능 최적화
JPA 극한의 성능 최적화 노하우
복잡한 예제를 6단계로 나누어 성능 튜닝

---

## chap1 API 개발 기본

템플릿엔진보다 SPA - viewJS, 리액트
- MSA
    - API끼리 통신 상황이 많이 발생
> 학습목표
- `JPA 엔티티 개념이 있기 때문에 API 만들 때의 올바른 방향 정리`

> 패키지 구조
- API controller와 템플릿 엔진용 controller 분리
- 공통으로 예외처리할 때 패키지 단위로 보통 하는데, API와 템플릿 엔진용의 공통 관점이 차이가 있어서 그렇게 한다.

### 회원 등록 API

- @Valid 하면 Member 객체에 맞게 자동으로 validation 한다.
`public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) `

> v2 리팩토링
- 프리젠테이션 계층을 위한 검증 로직이 엔티티에 들어 있다.
  - Member 엔티티에 @NotEmpty를 넣은 상황
  - 엔티티는 바뀔 확률이 높은데, 엔티티가 바뀔 때 API 스펙이 바뀌는 상황이다.
  - API 요청 스펙에 맞춰 요청/응답 DTO를 만든다.
  - 그래서 DTO만 확인하면, API 스펙을 확인 할 수 있다.






