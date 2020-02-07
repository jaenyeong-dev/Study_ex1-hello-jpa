# Study_ex1-hello-jpa
## 인프런 자바 ORM 표준 JPA 프로그래밍 - 기본편 (김영한)
* https://www.inflearn.com/course/ORM-JPA-Basic 참조

-----

### [Settings]
#### Java
* zulu jdk 8
#### gradle
* 5.2.1
* 기존 강의는 maven으로 진행되나 익숙하지 않아 gradle로 진행
#### Hibernate
* hibernate-entitymanager 5.3.10.Final
#### H2 Database
* 1.4.199

#### error
* H2 console
  * 
* hibernate.properties not found

-----

### [Contents]
#### JPA 구동 방식
* persistence 클래스가 META-INF/persistence.xml 설정 정보 조회 후 EntityManagerFactory 생성
* EntityManagerFactory가 필요한 entityManager를 생성

#### H2 Database
* H2 DB 콘솔에서 JDBC URL 설정 후 연결
  * persistence.xml 파일 안에 설정한 javax.persistence.jdbc.url 속성 "jdbc:h2:tcp://localhost/~/test" 값 그대로 사용

#### @Entity 어노테이션이 해당 클래스를 못찾는 경우
* persistence.xml 파일 안에 <class>com.jpa.basic.Member</class> 태그 삽입
  * Hibernate만 별도로 사용할 경우 설정
  * Spring을 같이 사용할 경우 엔티티 스캔 기능으로 인하여 따로 설정이 필요 없음
  
#### 주의
* EntityManagerFactory는 하나만 생성해서 앱 전체에서 공유
* EntityManager는 스레드간 공유 하지 않을 것
* JPA의 모든 데이터 변경은 트랜잭션 안에서 실행할 것

#### JPQL
* SQL을 추상화한 객체 지향 쿼리 언어 (특정 DBMS SQL에 의존적이지 않음)
* 엔티티 객체를 대상으로 쿼리 (SQL은 DB 테이블을 대상으로 쿼리)

#### 영속성 컨텍스트
* 엔티티를 영구 저장하는 환경
* 논리적인 개념, 엔티티 매니저를 통해 접근 (EntityManager 안에 PersistenceContext가 눈에 보이지 않게 존재)
* 상태
  * 비영속(new/transient)
    * 새로운 상태
  * 영속(managed)
    * 영속성 컨텍스트에 관리되는 상태
  * 준영속(detached)
    * 영속성 컨텍스트에 저장 되었다가 분리된 상태
  * 삭제(removed)
    * 삭제된(DB에서) 상태
  * 플러시
    * 영속성 컨텍스트의 변경 내용을 DB에 반영(강제 즉시 반영)
    * em.flush()
    * 트랜잭션 커밋, JPQL 쿼리 실행 : 자동 호출
    * 플러시가 실행되도 1차 캐시는 남아 있음 (영속성 컨텍스트를 비우지 않음)
    * 플러시 모드 옵션 (거의 사용하지 않음)
      * em.setFlushMode(AUTO, COMMIT)
  * 준영속
    * em.detach(entity) : 해당 엔티티만 1차 캐시에서 제거
    * em.clear() : 영속성 컨텍스트를 전체 초기화
    * em.close() : 영속성 컨텍스트 종료
* 이점
  * 1차 캐시
    * DB 하나의 트랜잭션 안에서만 살아있음(2차 캐시(App 전체에서 사용하는)가 아니기 때문에 성능적으로 큰 차이는 없음)
    * @Id(컬럼 키 값)가 캐시 키 값이 됨
    * 캐시에 값이 없을 경우 DB에서 가져옴
  * 동일성 보장
    * 1차 캐시로 반복 가능한 읽기 등급의 트랜잭션 격리 수준을 DB가 아닌 App 차원에서 제공
  * 트랜잭션을 지원하는 쓰기 지연
    * 1차 캐시에 저장한 후 쓰기 지연 SQL 저장소에 먼저 SQL을 작성하여 저장
    * 트랜잭션을 커밋하는 시점에 쓰기 지연 SQL 저장소에 있는 쿼리 목록이 실행됨
    * 버퍼링 모아서 한 번에 처리
  * 변경 감지
    * 엔티티 수정 시 (Dirty checking) em.update 또는 em.persist 같은 처리 없이 데이터 동기화
    * 1차 캐시엔 스냅샷이 있음 (최초 영속성 컨텍스트에 들어왔을 당시에 상태를 저장)
      * 스냅샷과 엔티티 값을 비교 후 처리
  * 지연 로딩

#### 엔티티 매핑
* DB 스키마 자동 생성
  * 실무에선 사용하지 않는 것이 좋음 (drop, lock 등 주의할 것)
  * hibernate.hbm2ddl.auto
    * create : 테이블 생성
    * create-drop : 테이블 생성 후 종료 시점에 테이블 드랍
    * update : 변경된 부분만 반영
    * validate : 엔티티와 테이블의 정상 매핑 여부 확인
    * none : 사용하지 않음
  * DDL 생성 기능
    * @Column의 unique, length 속성 등 DDL(DB)에만 영향을 줌
* 객체, 테이블 매핑
  * @Entity : 테이블(엔티티) 지정
    * 해당 어노테이션이 붙은 클래스 = 엔티티
    * 기본 생성자 필수(public, protected)
    * final, enum, interface, inner 클래스는 사용하지 못함
    * 값 저장 필드는 final 사용하지 못함
    * 속성
      * name : JPA에서 사용할 엔티티 이름 지정(같은 클래스명이 없다면 일반적으로 기본 값 그대로 사용)
      * catalog : 카탈로그 매핑
      * schema : 스키마 매핑
      * uniqueConstraints : DDL로 생성시 유니크 제약조건 생성
  * @Table : 엔티티와 매핑할 테이블명
  * @SequenceGenerator : 시퀀스 매핑
    * name : 시퀀스 제네레이터명 (식별자 생성기명) [필수 값]
    * sequenceName : 매핑할 DB 시퀀스명 (기본 값은 hibernate_sequences)
    * initialValue : 초기값 설정
    * allocationSize : 메모리를 통해 할당할 범위 사이즈 (기본 값은 50, [시퀀스 값이 1씩 증가하도록 설정 되어 있으면 이 값을 1로 반드시 설정])
  * @TableGenerator : 키 생성 전용 테이블, 시퀀스를 흉내
    * 모든 DB 사용 가능하나 성능 이슈
    * name : 테이블 제네레이터명 (식별자 생성기명) [필수 값]
    * table : 키 생성 테이블명 (기본 값은 hibernate_sequences)
    * pkColumnName : 시퀀스 컬럼명 (기본값 sequence_name)
    * initialValue : 초기 값 (마지막으로 생성된 값 기준, 기본값은 0)
    * allocationSize : 시퀀스 한 번 호출에 증가하는 수 (기본값은 50) 성능 최적화
    * catalog, schema : DB 카탈로그, 스키마명
    * uniqueConstraint : 유니크 제약조건 지정
* 필드, 컬럼 매핑
  * @Column : 일반 컬럼 지정
    * name : 필드와 매핑할 테이블의 컬럼명
    * insertable, updateable : 등록, 변경 가능 여부
    * nullable : null 허용 여부 (false 설정시 DB에 not null 제약조건)
    * unique : 컬럼에 unique 제약 조건 (제약조건명이 지정할 수 없어 잘 사용하지 않고 @Table 속성에 uniqueConstraints를 사용)
    * columnDefinition : 컬럼 정보를 직접 정의
    * length : String 타입에만 길이 제약조건 정의
    * precision, scale : BigDecimal(BigInteger) 타입에서 사용
      * precision : 소수점을 포함한 전체 자릿수
      * scale : 소수의 자릿수
  * @Enumerated : Enum 속성 매핑. 기본값(EnumType.ORDINAL)보다 EnumType.STRING(Enum 값)를 사용하는게 좋음
    * EnumType.ORDINAL : enum [순서 0, 1, 2 ...]를 DB에 저장
    * EnumType.STRING : enum [이름]을 DB에 저장
  * @Temporal : Date, Calendar 타입 지정
    * java 8 이상시 해당 어노테이션 없이 그냥 LocalDate, LocalDateTime 타입만 사용하면 인식
    * TemporalType.DATE : 날짜, DB date 타입 매핑
    * TemporalType.TIME : 시간, DB time 타입 매핑
    * TemporalType.TIMESTAMP : 날짜와 시간, DB timestamp 타입과 매핑
  * @Lob : BLOB, CLOB(문자)와 매핑 (지정할 속성 없음)
    * BLOB : byte[], java.sql.BLOB
    * CLOB : String, char[], java.sql.CLOB
  * @Transient : 매핑 안함
* 기본키 매핑
  * @Id : 직접 할당
  * @GeneratedValue (strategy = "아이덴티티, 시퀀스, 테이블 등 아래 값", generator = "@SequenceGenerator 에서 지정한 시퀀스 제네레이터명")
    * GenerationType.IDENTITY : MySQL 등 기본키 생성을 DB에 위임
      * 영속성 컨텍스트에서 관리하려면 PK가 필요한데 IDENTITY는 DB에 insert해야 Key 값을 알 수 있음
      * 그래서 이 경우에만 em.persist() 했을 때 바로 DB에 insert함 (일반적으로 commit 했을 때만 DB에 처리됨)
    * GenerationType.SEQUENCE : Oracle 등 시퀀스 오브젝트 사용
      * @SequenceGenerator 필요
    * GenerationType.TABLE : 키 생성용 테이블 사용 (벤더의 의존하지 않음)
      * @TableGenerator 필요
    * GenerationType.AUTO : 벤더에 따라 자동 지정 (위 세개중 하나)
  * IDENTITY 특징
  * 기본키 매핑
    * @Column의 name 속성 대소문자는 개발 룰에 따를 것
* 연관관계 매핑
  * @ManyToOne
  * @JoinColumn
    
#### H2 DB 생성
* jdbc:h2:tcp://localhost/~/jpashop 으로 연결 시 자동 생성
* 자동 생성이 되지 않을 경우 jdbc:h2:~/jpashop 와 같이 persistence.xml을 수정한 후 시도

#### 연관관계
* 방향, 다중성, 연관관계의 주인
* 예제 시나리오
  * 회원과 팀이 존재
  * 회원은 하나의 팀에만 소속
  * 회원과 팀은 다대일 관계
* 양방향 연관관계
  * 객체의 양방향 관계는 단방향 관계 2개
  * 양방향 매핑 규칙
    * 두 객체중 하나를 연관관계의 주인으로 지정
    * 연관관계의 주인만이 외래키를 관리(등록, 수정)
    * 주인이 아닌 쪽은 읽기만 가능
    * 주인은 mappedBy 속성을 사용하지 않음
    * 주인이 아닌 경우 mappedBy 속성으로 주인 지정
    * 외래키가 있는 곳을 주인으로 지정할 것
    * 양쪽에 값을 다 세팅해줘야 함
      * 1. em.flush(), em.clear()를 하지 않을 경우 직접 양쪽에 세팅하여 1차 캐시에 저장 해야함
      * 2. 테스트 케이스 작성을 위하여
      * 연관관계 메소드를 생성하여 사용하는 것을 추천
        * 일반적인 setter 메소드보다 새로운 메소드를 생성하여 로직을 처리하는 것을 추천
    * 양방향 매핑 시 무한 루프 주의할 것 (toString(), lombok, JSON 등)
      * lombok에서 toString 을 빼고 사용할 것
      * JSON 라이브러리 > 컨트롤러에서는 entity를 반환하지 말 것 (무한루프, 엔티티 변경으로 인한 API 변경)
* 정리
  * 단방향 매핑만으로도 이미 연관관계 매핑 완료
  * 양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색) 기능이 추가된 것 뿐
  * JPQL에서 역방향으로 탐색할 일이 많음
  * 단방향 매핑 후, 양방향은 필요할때만 추가 (테이블에 영향을 주지 않음)
  * 연관관계 주인을 정하는 기준
    * 비즈니스 로직을 기준으로 선정하면 안됨
    * 연관관계 주인은 외래키의 위치를 기준으로 선정
  * 가급적 단반향으로 설계할 것
  * jpashop 패키지 안에 Member의 Order가 있는 건 일반적으로 좋지 않은 설계

#### 연관관계 매핑시 고려사항 3가지
* 다중성
  * 다대일 @ManyToOne
    * 가장 많이 사용 (ex : member > team)
    * 다대일 양방향 
      * 외래키가 있는 쪽이 연관관계 주인
      * 양쪽을 서로 참조하도록 개발
  * 일대다 @OneToMany
    * 권장하지 않음
    * 일대다 단방향
      * 이 경우엔 일(1) 쪽이 연관관계 주인
      * 테이블 일대다 관계는 항상 다(N) 쪽에 외래키가 있음
      * 객체, 테이블 패러다임 차이 때문에 반대편 테이블의 외래키를 관리하게 되는 구조
      * @JoinColumn 어노테이션을 반드시 사용해야 함 (사용하지 않을 경우 조인 테이블 방식을 사용함 - 중간에 테이블 하나 추가)
    * 일대다 양방향
      * 공식 스펙 아님
      * @JoinColumn(insertable = false, updatable = false)
      * 읽기 전용 필드 사용하여 양방향처럼 사용하는 방법
      * 가급적 다대일 양방향을 사용하는 것을 권장
  * 일대일 @OneToOne
    * 반대 관계도 일대일
    * 주 테이블과 대상 테이블 중에 외래키 선택 가능
      * 주 테이블에 외래키 (Member)
        * 객체 지향 개발자 선호
        * JPA 매핑 편리
        * 주 테이블만 조회(핸들링)해도 대상 테이블에 데이터 유무 확인 가능
      * 대상 테이블에 외래키 (Locker)
        * 정통적 DB 위주 개발자 선호
        * 일대일 > 일대다 변경시 테이블, 객체 관계 구조 유지
        * 프록시 기능의 한계로, 지연 로딩 설정해도 항상 즉시 로딩됨
          * Member 테이블 데이터 조회시 Locker 테이블의 값을 같이 가져와서 바인딩하기 때문에
    * 외래키에 DB 유니크 제약 조건 추가
    * 다대일과 유사
      * 다대일 양방향 매핑처럼 외래키가 있는 곳이 연관관계 주인
      * 반대 쪽은 mappedBy 적용
  * 다대다 @ManyToMany (사용하지 않는 것이 좋음)
    * RDB에서는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없음
    * 디비는 중간 연결 테이블을 추가해서 관계를 풀어야 함
    * 객체는 컬렉을 사용하여 다대다 관계 표현 가능
    * @ManyToMany 사용
    * @JoinTable로 연결 테이블 지정
    * 단방향, 양방향 가능
    * 연결 테이블이 단순히 연결만 하고 끝나는 경우가 없음
    * 주문시간, 수량 데이터 등이 들어올 수 있음
    * 연결 테이블용 엔티티 추가하여 사용 (@OneToMany, )
* 단방향, 양방향
  * 테이블
    * 외래키 하나로 양쪽 조인 가능
    * 사실 방향이라는 개념이 없음
  * 객체
    * 참조용 필드가 있는 쪽으로만 참조 가능
    * 한쪽만 참조하면 단방향, 양쪽이 서로 참조하면 양방향
* 연관관계 주인
  * 테이블은 외래키 하나로 두 테이블이 연관관계를 맺음
  * 객체 양방향 관계는 참조가 2군데
  * 2군데 중 외래키를 관리할 곳을 지정
  * 연관관계 주인 : 외래키를 관리하는 참조
  * 주인 반대편 : 외래키에 영향을 주지 않음, 단순 조회

