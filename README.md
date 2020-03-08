# Study_JPA-Basic
인프런 자바 ORM 표준 JPA 프로그래밍 - 기본편 (김영한)
https://www.inflearn.com/course/ORM-JPA-Basic 참조

-----

### [Settings]
#### Project Name
* Study_ex1-hello-jpa
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
* persistence.xml 파일 안에 <class>com.jpa.basic.entity.Member</class> 태그 삽입
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
      * lombok에서 toString()을 빼고 사용할 것
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
* 예제
  * 배송 추가
    * 주문과 배송은 1:1 (@OneToMany)
    * 상품과 카테고리는 N:M (@ManyToMany)
    * 주문의 배송 필드 포함
    * CATEGORY_ITEM 테이블로 카테고리와 아이템 다대다 매핑
    * Order, Delivery 엔티티는 양방향 매핑
    * Category, Item 엔티티는 양방향 매핑, 매핑 엔티티 없음
* 정리
  * N:M 관계는 1:N, N:1 (중간 매핑 테이블을 이용)
  * 운영상 중간 매핑 테이블은 단순하지 않음
  * @ManyToMany는 운영상 사용하지 않는 것을 권장(필드 추가 X, 엔티티 테이블 불일치)
  * @JoinColumn
    * name : 매핑할 외래키명 (기본값은 )
    * referencedColumnName : 외래키가 참조하는 대상 테이블의 컬럼명 (기본값은 참조 테이블의 기본키-컬럼명)
    * foreignKey(DDL) : 외래키 제약조건 직접 지정 가능, 테이블 생성시만 사용함
    * unique, nullable, insertable, updateable, columnDefinition, table : @Column 어노테이션의 속성과 동일
  * @ManyToOne
    * optional : false 설정시 연관된 엔티티가 항상 존재해야함 (기본값 true)
    * fetch : 글로벌 패치 전략 설정 (즉시 로딩, 지연로딩)
      * @ManyToOne=FetchType.EAGER
      * @OneToMany=FetchType.LAZY
    * cascade : 영속성 전이 기능 사용
    * targetEntity : 연관된 엔티티의 타입 정보 설정. 거의 사용하지 않음 > 컬렉션을 사용해도 제네릭으로 타입 정보를 알 수 있음
    * 다대일 매핑은 항상 연관관계 주인이 되어야 함 (mappedBy 속성이 없음)
  * @OneToMany
    * mappedBy : 연관관계의 주인 필드 선택
    * fetch : 글로벌 패치 전략 설정 (즉시 로딩, 지연로딩)
      * @ManyToOne=FetchType.EAGER
      * @OneToMany=FetchType.LAZY
    * cascade : 영속성 전이 기능 사용
    * targetEntity : 연관된 엔티티의 타입 정보 설정. 거의 사용하지 않음 > 컬렉션을 사용해도 제네릭으로 타입 정보를 알 수 있음
    
#### Drop table error
* error message : Cannot drop "CATEGORY" because "FKJIP0OR3VEMIXCCL6VX0KLUJ03" depends on it; SQL statement:
* 위와 같이 CATEGORY, DELIVERY 등 테이블 삭제시 인덱스에 의해 테이블 삭제 실패 에러 발생
* H2 버전(1.4.200)에 의한 에러로 생각됨 > 1.4.199 버전 사용할 것
* H2 1.4.199버전 포함

#### 상속관계 매핑
* RDB는 상속 관계 없음
* 슈퍼타입, 서브타입 관계라는 모델링 기법이 객체 상속과 유사
* 상속관계 매핑 : 객체 상속 구조와 DB의 슈퍼타입, 서브타입 관계를 매핑
* 슈퍼타입, 서브타입 논리 모델을 실제 물리 모델로 구현하는 방법
  * 각각 테이블로 변환 > 조인 전략
    * 장점
      * 테이블 정규화
      * 외래키 참조 무결성 제약조건 활용가능
      * 저장공간 효율
    * 단점
      * 조회시 조인을 많이 사용하여 성능 저하
      * 조회 쿼리 복잡
      * 데이터 저장시 삽입 쿼리가 2번 호출
  * 통합 테이블로 변환 > 단일 테이블 전략
    * 장점
      * 조인이 필요 없어 일반적으로 조회 성능 빠름
      * 조회 쿼리 단순
    * 단점
      * 자식 엔티티가 매핑한 컬럼은 모두 null 허용
      * 단일 테이블에 저장하기 때문에 테이블이 커질 수 있음 (상황에 따라 조회 성능이 느려질 가능성이 있음)
  * 서브타입 테이블로 변환 > 구현 클래스마다 테이블 전략 (사용하지 않는 것을 권장)
    * 장점
      * 서브 타입을 명확하게 구분해서 처리할 때 효과적
      * not null 제약조건 사용 가능
    * 단점
      * 여러 자식 테이블을 함께 조회할 때 성능이 느림 (UNION)
      * 자식 테이블을 통합해서 쿼리하기 어려움
* JPA
  * @Inheritance(strategy=InheritanceType.XXX)
    * JOINED : 조인 전략
    * SINGLE_TABLE : 단일 테이블 전략
    * TABLE_PER_CLASS : 구현 클래스마다 테이블 전략
  * @DiscriminatorColumn(name="DTYPE")
    * 슈퍼클래스에 태깅 (기본 컬럼명은 DTYPE)
  * @DiscriminatorValue("XXX")
    * 하위클래스에서 태깅 및 값 바인딩 (기본값은 서브클래스명)
* @MappedSuperclass
  * 공통 매핑 정보가 필요할 때 사용(id, name)
  * [주의] 상속관계 매핑이 아님
  * 엔티티가 아님, 테이블과 매핑하지 말 것
  * 슈퍼클래스를 상속 받는 서브클래스에 매핑 정보만 제공
  * 조회, 검색 불가능 (em.find(BaseEntity) 등 불가능)
  * 직접 생성, 사용할 일이 없어 추상 클래스로 생성하길 권장
  * 테이블과 관계 없고, 단순히 엔티티들이 공통으로 사용하는 매핑 정보를 모으는 역할
  * 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통적으로 사용, 적용하는 정보를 모을 때 사용
  * @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래만 상속 가능함

#### 프록시
* 프록시
  * 기초
    * em.find() vs em.getReference()
    * em.find() : DB를 통해서 실제 엔티티 객체 조회
    * em.getReference() : DB 조회를 미루는 가짜 엔티티 객체 조회
    * Proxy 안에 Entity target = null
  * 특징
    * 실제 클래스를 상속하여 생성됨
    * 실제 클래스와 겉 모양이 같음
    * 외부에서 실제 객체와 프록시를 구분하지 않고 사용
    * 프록시 객체는 실제 객체의 참조(target)를 보관
    * 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메소드 호출
    * 프록시 객체는 최초 사용시 한 번만 초기화
    * 프록시 객체 초기화 시 프록시 객체가 실제 엔티티로 바뀌지는 않음, 초기화 후에 프록시 객체를 통해 엔티티 접근 가능
    * 프록시 객체는 원본 엔티티를 상속 받기 때문에 타입 체크시 주의할 것 (== 비교 실패, instance of 사용)
    * 영속성 컨텍스트에 찾는 엔티티가 이미 존재하는 경우 em.getReference()를 호출해도 실제 엔티티 반환
    * 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생
    * 프록시 객체를 먼저 생성하게 되면 em.find() 메소드를 호출하여도 프록시 객체 사용
  * 확인
    * 프록시 인스턴스의 초기화 여부 확인
      * PersistenceUnitUtil.isLoaded(Object entity)
    * 프록시 클래스 확인 방법
      * entity.getClass().getName() 출력 (..javasist.. or HibernateProxy)
    * 프록시 강제 초기화
      * org.hibernate.Hibernate.initialize(entity)
      * JPA 표준은 강제 초기화 없음
* 즉시 로딩, 지연로딩
  * FetchType.LAZY 속성으로 지정시 지연 로딩으로 데이터를 프록시 객체로 가져옴 
    * 프록시 객체안에 필드 등에 접근하거나 사용할 때 초기화됨
  * FetchType.EAGER 속성으로 지정시 즉시 로딩으로 함께 조회
  * 가급적 지연 로딩만 사용하길 권장(운영상)
  * 즉시 로딩 적용시 예상치 못한 SQL 발생 (Join 등으로 인한 성능 저하 등)
  * 즉시 로딩은 JPQL에서 N+1 문제 발생
  * @ManyToOne, @OneToOne은 기본값이 즉시 로딩이기 때문에 LAZY로 속성 변경할 것
  * @OneToMany, @ManyToMany은 기본값이 지연로딩
* 지연 로딩 활용
  * Member와 Team은 자주 함께 사용 > 즉시 로딩
  * Member와 Order는 가끔 함께 사용 > 지연 로딩
  * Order와 Product는 자주 함께 사용 > 즉시 로딩
  * 운영상에선 웬만하면 지연 로딩 사용할 것
  * JPQL fetch join이나, 엔티티 그래프 기능을 사용할 것
  * 즉시 로딩은 상상하지 못한 쿼리가 실행됨
* 영속정 전이 CASCADE
  * 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들고 싶을 때
  * 예 : 부모 엔티티 저장 시 자식 엔티티도 저장
  * 주의
    * 영속성 전이는 연관관계 매핑과 아무 상관 없음
    * 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화는 편리함을 제공
  * 종류
    * ALL : 모두 적용
    * PERSIST : 영속
    * REMOVE : 삭제
    * MERGE : 병합
    * REFRESH : 갱신
    * DETACH : 준영속
* 고아 객체
  * 고아 객체 제거 : 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
  * orphanRemoval = true
  * 자식 엔티티를 컬렉션에서 제거
    * Parent findParent = em.find(Parent.class, id);
    * findParent.getChildren().remove(0);
  * DELETE FROM CHILD WHERE ID = ?
  * 주의
    * 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능
    * 참조하는 곳이 한곳일 때만 사용할 것
    * 특정 엔티티가 개인 소유시 사용
    * @OneToOne, @OneToMany만 사용 가능
    * 부모를 제거하면 자식은 고아가 됨. 따라서 고아 객체 제거 기능을 활성화하면, 부모 제거시 자식도 함께 제거됨 
      * CascadeType.REMOVE처럼 동작
  * CascadeType.ALL + orphanRemoval = true
    * 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화, em.remove()로 제거
    * 두 옵션을 모두 활성화하면 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있음
* 영속성 전이 + 고아 객체, 생명주기
* 예제 - 연관관계 관리
  * 글로벌 fetch 전략 설정
    * 모든 연관관계를 지연 로딩으로
    * @ManyToOne, @OneToOne은 속성 기본값이 즉시 로딩이므로 지연 로딩으로 변경할 것
  * 영속성 전이 설정
    * Order > Delivery을 영속성 전이 ALL 설정
    * Order > OrderItem을 영속성 전이 ALL 설정

#### 값 타입
* JPA의 데이터 타입 분류
  * 엔티티 타입
    * @Entity 어노테이션으로 정의하는 객체
    * 데이터가 변해도 식별자로 지속해서 추적 가능
    * 예 : 회원 엔티티의 키, 나이 값을 변경해도 식별자로 인식 가능
  * 값 타입
    * int, Integer, String처럼 단순히 값으로 사용하는 기본 값 타입이나 객체
    * 식별자 없이 값만 있으므로 변경시 추적 불가능
    * 예 : 숫자 100을 200으로 변경시 완전히 다른 값으로 대체
* 기본 값 타입
  * int, double, Integer, Long, String 등
  * 생명주기를 엔티티의 의존
    * 예 : 회원을 삭제하면 이름, 나이 필드도 함께 삭제
  * 값 타입은 공유하면 안됨
    * 예 : 회원 이름 변경시 다른 회원의 이름도 함께 변경되면 안됨
  * 자바의 기본 타입은 절대 공유되지 않음
    * 자바는 값 참조 > 기본 타입은 항상 값을 복사함
    * Integer같은 래퍼 클래스, String같은 특수한 클래스는 공유 가능한 객체이지만 변경 못함
* 임베디드 타입(복합 값 타입)
  * 예 : x, y 좌표 값을 가지고 있는 Position 클래스
  * 새로운 값 타입을 직접 정의할 수 있음
  * JPA는 임베디드 타입이라 칭함
  * 주로 기본값 타입을 모아 만들기 때문에 복합값 타입이라고도 함
  * int, String과 같은 값 타입
  * 사용법
    * @Embeddable : 값 타입을 정의하는 곳에 표시
    * @Embedded : 값 타입을 사용하는 곳에 표시
    * 기본 생성자 필수
  * 장점
    * 재사용
    * 높은 응집도
    * Period.isWork() 메소드처럼 해당 값 타입만 사용하는 의미있는 메소드 생성 가능
    * 임베디드 타입을 포함한 모든 값 타입은, 값 타입을 소유한 엔티티에 생명주기를 의존
  * 임베디드 타입과 테이블 매핑
    * 임베디드 타입은 엔티티의 값일뿐
    * 엠베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같음
    * 객체와 테이블을 아주 세밀하게 매핑하는 것이 가능
    * 잘 설계된 ORM 앱은 매핑한 테이블의 수보다 클래스의 수가 더 많음
  * 임베디드 타입과 연관관계
    * 임베디드는 임베디드를 포함할 수 있음
    * 임베디드는 엔티티를 포함할 수 있음 (FK만 가지고 있으면 됨)
  * @AttributeOverride 속성
    * 한 엔티티에서 같은 값 타입을 사용? > 컬럼명이 중복
    * @AttributeOverrides @AttributeOverride를 사용하여 컬럼명 속성 재정의
      * 예 : @AttributeOverrides({
                @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
                @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
                @AttributeOverride(name = "zipCode", column = @Column(name = "WORK_ZIPCODE"))
        	})
  * 임베디드 타입 값이 NULL이면 매핑한 컬럼 값은 모두 NULL
* 컬렉션 값 타입
  * 기본 값, 임베디드 타입을 배열로 가진 타입
* 값 타입과 불변 객체
  * 값 타입은 단순화 하려고 만든 개념
  * 값 타입 공유 참조
    * 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험
    * 사이드 이펙트 발생
  * 값 타입 복사
    * 값 타입의 실제 인스턴스인 값을 공유하면 위험하기 때문에 값(인스턴스)을 복사해서 사용
  * 객체 타입의 한계
    * 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있음
    * 문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본 타입이 아니라 객체 타입
    * 자바 기본 타입에 값을 대입하면 값을 복사
    * 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없음
    * 객체의 공유 참조는 피할 수 없음
    * 기본 타입은 값을 복사
      * int a = 10;
      * int b = a;
      * b = 4;
    * 객체 타입은 참조를 전달
      * Address a = new Address("Old");
      * Address b = a;
      * b.setCity("New);
  * 불변 객체(생성 시점 이후 값을 절대 변경할 수 없는 객체)
    * 객체 타입을 수정할 수 없게 작성(부작용 원천 차단)
    * 값 타입은 불변 객체로 설계할 것
    * 생성자 값 설정 이외에 Setter(수정자)를 생성하지 않음
    * Integer, String 등은 자바가 제공하는 대표적인 불변 객체
* 값 타입 비교
  * 인스턴스가 달라도 그 안에 값이 같으면 같은 것으로 인식해야 함
    * int a = 10; int b = 10; a == b;
  * 동일성, 동등성을 구분해서 사용
    * 동일성(Identity) : 인스턴스의 참조 값을 비교 (== 사용)
    * 동등성(Equivalence) : 인스턴스의 값을 비교 (equals() 사용)
    * 값 타입은 a.equals(b)를 사용하여 동등성 비교
    * equals() 메소드를 적절하게 재정의(일반적으로 모든 필드 적용)
* 값 타입 컬렉션
  * 값 타입을 하나 이상 저장할 때 사용
  * @ElementCollection, @CollectionTable 사용
  * DB는 컬렉션을 같은 테이블에 저장할 수 없음
  * 컬렉션 저장을 위한 별도 테이블 필요
  * 영속성 cascade + 고아 객체 제거 기능을 필수로 가지고 있음
  * 제약사항
    * 값 타입은 엔티티와 다르게 식별자 개념이 없음
    * 값은 변경하면 추적이 어려움
    * 값 타입 컬렉션에 변경 사항이 발생시
      * 주인 엔티티와 연관된 모든 데이터를 삭제 후 현재 값 타입 컬렉션에 있는 값을 모두 다시 저장
    * 값 타입 컬렉션을 매핑하는 테이블은 모두 컬럼을 묶어서 기본키를 구성해야 함 (null 입력 안됨, 중복 저장 안됨)
  * 대안
    * 값 타입 컬렉션 대신 일대다 관계를 고려
    * 일대다 관계를 위한 엔티티 생성, 여기에 값 타입을 사용
    * 영속성 cascade + 고아 객체 제거를 사용, 값 타입 컬렉션 처럼 사용
* 정리 (엔티티 타입과 값 타입 특징)
  * 엔티티 타입 특징
    * 식별자 있음
    * 생명주기 관리
    * 공유
  * 값 타입 특징
    * 식별자 없음
    * 생명주기를 엔티티의 의존
    * 공유하지 않는 것이 안전(복사해서 사용)
    * 불변 객체로 만드는 것이 안전
  * 값 타입은 정말 값 타입이라 판단될 때만 사용
  * 엔티티와 값 타입을 혼동하지 말 것
  * 식별자가 필요, 지속해서 값을 추적하여 변경해야 한다면 엔티티를 사용해야 함
* 실전 예제
  * equals(), hashCode() 메소드 생성
    * 메소드 생성시 필드에 직접 접근하지 않고 getter를 사용하여 필드의 접근하게 생성할 것 (프록시일때 계산을 위해서)
    
#### 객체지향 쿼리 언어 [기본 문법]
* JPA가 지원하는 쿼리 방법
  * JPQL
    * 가장 단순한 조회 방법
    * EntityManager.find()
    * 객체 그래프 탐색(a.getB().getC())
    * JPA 사용하면 엔티티 객체를 중심으로 개발
    * 검색 쿼리 등이 문제
    * 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
    * 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
    * 필요한 데이터만 DB에서 불러오려면 검색 조건이 포함된 SQL이 필요
    * JPA는 SQL을 추상화한 JPQL이라는 객체지향 쿼리 언어를 제공
    * SQL 문법과 유사 (SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 등 지원)
    * JPQL은 엔티티 객체를 대상으로 쿼리함
    * SQL은 테이블을 대상으로 쿼리함
    * 테이블이 아닌 객체를 대상으로 실행하는 객체지향 쿼리
    * SQL을 추상화해서 특정 DB 벤더에 의존하지 않음
    * JPQL은 객체지향  SQL
  * JPA Criteria
    * JPQL 빌더 역할. JPA 공식 기능
    * JPQL은 String 문자열을 쿼리로 작성하는 것이기 때문에 동적 쿼리를 작성하기 어려움
    * 자바 코드처럼 코딩(메소드 호출 등)하여 컴파일 레벨에서 오타 등을 잡아줌
    * 위와 같은 이유로 동적 쿼리 작성(생성)이 쉬움
    * SQL 같지가 않은 것이 단점
    * 운영상 거의 사용하지 않음 (가독성이 좋지 않음 - 너무 복잡하고 실용성이 없음)
    * 대신에 QueryDSL 사용을 권장
  * QueryDSL (오픈소스 라이브러리)
    * gradle 설정 실패.. (추후 확인할 것) 
    * 자바 코드로 JPQL을 작성할 수 있음
    * JPQL의 빌더 역할
    * 컴파일 시점에 문법 오류를 찾을 수 있음
    * 동적 쿼리 작성(생성) 쉬움
    * 운영상 사용 권장
    * JPQL을 익힌 후 사용할 것
  * 네이티브 SQL
    * JPA가 제공하는 SQL을 직접 사용하는 기능
    * JPQL로 해결할 수 없는 특정 DB 벤더에 의존적인 기능을 사용할 때
      * 예 : 오라클 Connect by, 특정 DB 벤더만 사용하는 SQL 힌트 등
  * JDBC API 직접 사용, MyBatis, SpringJdbcTemplate 함께 사용
    * 영속성 컨텍스트를 적절한 시점에 강제로 플러시 할 필요가 있음
      * 예 : JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트 수동 플러시
      
#### JPQL
* Java Persistence Query Language
* 객체지향 쿼리 언어 > 테이블이 아닌 엔티티 객체를 대상으로 쿼리 (처음에 영속성 컨텍스트에서 가져옴)
  * SQL을 추상화해서 특정 DB 벤더 SQL에 의존하지 않음
* 문법 (select m from Member as m where m.age > 18)
  * 키워드
    * SELECT
    * FROM
    * WHERE
    * GROUPBY
    * HAVING
    * ORDERBY
    * UPDATE
    * DELETE
  * 엔티티와 속성은 대소문자를 구분함
  * JPQL 키워드는 대소문자를 구분하지 않음
  * 엔티티 이름 사용, 테이블명이 아님 (@Entity(name="") 속성을 써야함)
  * 별칭 필수(m) as는 생략 가능하나 가급적 써줌
  * 일반적인 집합, 정렬 함수 사용 가능
  * TypeQuery
    * 반환 타입이 명확할 때 사용
  * Query
    * 반환 타입이 명확하지 않을 때 사용
  * 결과 조회 API
    * query.getResult() : 결과가 하나 이상(리스트) 반환 (결과가 없으면 빈 리스트 반환)
    * query.getSingleResult() : 결과가 정확히 한개일 때 단일 객체 반환
      * 결과가 없으면 javax.persistence.NoResultException
      * 결과가 둘 이상이면 javax.persistence.NonUniqueResultException
  * 파라미터 바인딩
    * 이름 기준
    * 위치 기준 : 변경될 가능성때문에 가급적 사용하지 말 것
* 프로젝션
  * SELECT 절에 조회할 대상을 지정하는 것
    * 엔티티
      * SELECT m FROM Member m
      * SELECT m.team FROM Member m
    * 임베디드
      * SELECT m.address FROM Member m
    * 스칼라 타입(숫자, 문자 등 기본 데이터 타입)
      * SELECT m.userName, m.age FROM Member m
    * DISTINCT로 중복 제거
  * 여러 값 조회
    * Query 타입으로 조회
    * Query[] 타입으로 조회
    * new 명령어로 조회
      * 단순 값을 DTO로 바로 조회
      * SELECT new jpabook.jpql.UserDTO(m.userName, m.age) FROM Member m
      * 패키지명을 포함한 전체 클래스명 입력
      * 순서, 타입이 일치하는 생성자 필요
* 페이징 API (아래 두 API로 추상화)
  * setFirstResult(int startPosition) : 조회 시작 위치(0부터 시작)
  * setMaxResult(int maxResult) : 조회할 데이터 수
* 조인
  * 내부 조인
    * SELECT m FROM Member m [INNER] JOIN m.team t
  * 외부 조인
    * SELECT m FROM Member m LEFT [OUTER] JOIN m.team t
  * 세타 조인
    * SELECT count(m) FROM Member m, Team t where m.userName = t.name
  * ON 절 (JPA 2.1부터 지원)
    * 조인대상 필터링
      * JPQL : SELECT m, t FROM Member m LEFT JOIN m.team t ON t.name = 'A'
      * SQL : SELECT m.\*, t.\* FROM Member m LEFT JOIN TEAM t ON m.TEAM_ID = t.ID AND t.NAME = 'A'
    * 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1부터 지원)
      * JPQL : SELECT m, t FROM Member m LEFT JOIN Team t ON m.userName = t.name
      * SQL : SELECT m.\*, t.\* FROM Member m LEFT JOIN TEAM t ON m.USER_NAME = t.NAME
* 서브 쿼리
  * 나이가 평균보다 많은 회원
    * SELECT m FROM Member m WHERE m.age > (SELECT avg(m2.age) FROM Member m2)
  * 한 건이라도 주문한 고객
    * SELECT m FROM Member m WHERE (SELECT COUNT(o) FROM Order o WHERE m = o.member) > 0
  * 지원 함수
    * [NOT] EXISTS (subquery) : 서브쿼리에 결과가 존재하면 참
      * {ALL | ANY | SOME} (subquery)
      * ALL 모두 만족하면 참
      * ANY, SOME : 같은 의미, 조건을 하나라도 만족하면 참
    * [NOT] IN (subquery) : 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참
  * 예제
    * 팀 A 소속인 회원
      * SELECT m FROM Member m WHERE EXISTS (SELECT t FROM m.team WHERE t.name = '팀A')
    * 전체 상품 각각의 재고보다 주문량이 많은 주문
      * SELECT o FROM Order o WHERE o.orderAmount > ALL (SELECT p.stockAmount FROM Product p)
    * 어떤 팀이든 팀에 소속된 회원
      * SELECT m FROM Member m WHERE EXISTS (SELECT t FROM m.team WHERE t.name = '팀A')
  * 한계
    * JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
    * SELECT 절 또한 가능(하이버네이트에서 지원)
    * FROM 절 서브쿼리는 현재 JPQL에서 사용 불가능
      * 조인으로 풀 수 있으면 풀어서 해결할 것
* JPQL 타입 표현
  * 문자 : 'HELLO', 'She'
  * 숫자 : 10L (Long), 10D (Double), 10F (Float)
  * Boolean : TRUE, FALSE
  * ENUM : com.jpabook.jpashop.domain.OrderStatus (패키지명 포함)
  * entity type : TYPE(m) = Member (상속 관계에서 사용)
  * 기타
    * SQL과 문법이 같은 식
    * EXISTS, IN
    * AND, OR, NOT
    * =, >, >=, <, <=, <>
    * BETWEEN, LIKE, IS NULL
* 조건식 - CASE
  * 기본 CASE
    * SELECT  
        CASE WHEN m.age <= 10 THEN '학생요금'  
             WHEN m.age >= 60 THEN '경로요금'  
             ELSE '일반요금'  
        END  
       FROM Member m
  * 단순 CASE
    * SELECT  
        CASE t.name
             WHEN '팀A' THEN '인센티브110%'  
             WHEN '팀B' THEN '인센티브120%'  
             ELSE '인센티브105%'  
        END  
       FROM Team t
  * COALESCE : 하나씩 조회해서 NULL이 아니면 반환
    * SELECT COALESCE(m.userName, '이름 없는 회원') FROM Member m
    * 사용자 이름이 없으면 '이름 없는 회원'을 반환
  * NULLIF : 두 값이 같으면 NULL 반환, 다르면 첫번째 값 반환
    * SELECT NULLIF(m.userName, '관리자') FROM Member m
    * 사용자 이름이 '관리자'면 NULL을 반환하고 나머지는 본인의 이름을 반환
* 기본함수 (JPQL이 제공하는 표준 함수)
  * CONCAT
  * SUBSTRING
  * TRIM
  * LOWER, UPPER
  * LENGTH
  * LOCATE
  * ABS, SQRT, MOD
  * SIZE : 컬렉션의 크기를 돌려주는 함수, INDEX(JPA 용도)
* 사용자 정의 함수
  * 하이버네이트는 사용전 방언에 추가해야 함
    * 사용하는 DB 방언을 상속 받고, 사용자 정의 함수를 등록
    * SELECT FUNCTION('group_concat', i.name) FROM Item i
    * Dialect를 생성 (com.jpql.dialect.MyH2Dialect)

#### 객체지향 쿼리 언어 [중급 문법]
* JPQL 경로 표현식
  * 점을 찍어 객체 그래프를 탐색하는 것
    * SELECT m.userName > 상태 필드  
        FROM Member m  
        JOIN m.team t > 단일 값 연관 필드  
        join m.orders o > 컬렉션 값 연관 필드  
       WHERE t.name = '팀A'
  * 용어 정리
    * 상태 필드(state field) : 단순히 값을 저장하기 위한 필드 (예 : m.userName)
    * 연관 필드(association field) : 연관관계를 위한 필드
      * 단일 값 연관 필드 : @ManyToOne, @OneToOne, 대상이 엔티티(예 : m.team)
      * 컬렉션 값 연관 필드 : @OneToMany, @ManyToMany, 대상이 컬렉션(예 : m.orders)
  * 특징
    * 상태 필드 : 경로 탐색의 끝, 탐색이 안됨
    * 단일 값 연관 경로 : 묵시적 내부 조인 발생, 탐색 안됨
    * 컬렉션 값 연관 경로 : 묵시적 내부 조인 발생, 탐색 안됨
      * FROM 절에서 명시적 조인을 통해 별칭을 얻으면 그 별칭을 통해서 탐색 가능
    * 묵시적 조인이 발생하게 설계를 하거나 쿼리를 사용하지 않는 것을 권장(튜닝하기 어려움)
* 조인
  * 명시적 조인 : JOIN 키워드 직접 사용 (outer join 사용하려면 명시적 조인을 사용)
    * SELECT m FROM Member m JOIN m.team t
  * 묵시적 조인 : 경로 표현식에 의해 묵시적으로 SQL 조인 발생 (inner join만 가능)
    * SELECT m.team FROM Member m
* 예제
  * SELECT o.member.team FROM Order o (성공)
  * SELECT t.members FROM Team (성공)
  * SELECT t.members.userName FROM Team t (실패)
  * SELECT m.username FROM Team t join t.members m (성공)
* 경로 탐색을 사용한 묵시적 조인시 주의사항
  * 항상 내부 조인
  * 컬렉션은 경로 탐색의 끝, 명시적 조인을 통해 별칭을 얻어야함
  * 경로 탐색은 SELECT, WHERE 절에서만 사용하지만 묵시적 조인으로 인해 SQL의 FROM(JOIN) 절에 영향을 줌
* 운영
  * 가급적 묵시적 조인보다 명시적 조인 사용
  * 조인은 SQL 튜닝에 중요한 포인트
  * 묵시적 조인은 일어나는 상황을 한눈에 파악(확인)하기 어려움
* fetch join
  * 운영상 매우 중요함
  * fetch join이란
    * SQL 조인 종류가 아님
    * JPQL에서 성능 최적화를 위해 제공하는 기능
    * 연관된 엔티티 컬렉션을 SQL 한 번에 함께 조회하는 기능
    * join fetch 명령어를 사용
    * 페치 조인 ::= [LEFT [OUTER] | INNER] JOIN FETCH 조인 경로
  * 엔티티 페치 조인
    * 회원을 조회하면서 연관된 팀도 함께 조회(SQL 한 번에)
      * Member와 Team을 함께 SELECT 해옴
    * JPQL : SELECT m FROM Member m join fetch m.team
    * SQL : SELECT M.\*, T.\* FROM MEMBER M INNER JOIN TEAM T ON M.TEAM_ID=T.ID
  * 컬렉션 페치 조인
    * 일대다 관계
    * JPQL : SELECT t FROM Team JOIN FETCH t.members WHERE t.name = '팀A'
    * SQL : SELECT T.\*, M.\* FROM TEAM T INNER JOIN MEMBER M ON T.ID = M.TEAM_ID WHERE T.NAME = '팀A'
  * DISTINCT
    * SQL의 DISTINCT는 중복된 결과를 제거하는 명령
    * JPQL의 DISTINCT 2가지 기능 제공
      * SQL의 DISTINCT 추가
      * 앱에서 엔티티 중복 제거
    * 일대다는 중복이 나올 수 있으나 (개수가 여러개 나옴) 다대일은 중복 없음
  * 페치 조인과 일반 조인의 차이
    * 일반 조인 실행시 연관 엔티티를 함께 조회하지 않음
    * JPQL : SELECT t FROM Team t JOIN t.members m WHERE t.name = '팀A'
    * SQL : SELECT T.\* FROM TEAM T INNER JOIN MEMBER M ON T.ID = M.TEAM_ID WHERE T.NAME = '팀A'
    * JPQL은 결과를 반환할 때 연관관계 고려하지 않음
    * 단지 SELECT 절에 지정한 엔티티만 조회할 뿐
    * 여기 일반 조인 예제에서는 팀 엔티티만 조회, 회원 엔티티는 조회하지 않음
    * 페치 조인을 사용할 때만 연관된 엔티티도 함께 조회(즉시로딩)됨
    * 페치 조인은 객체 그래프를 SQL 한 번에 조회하는 개념
  * 특징과 한계
    * 특징
      * 연관된 엔티티들을 SQL 한 번으로 조회 (성능 최적화)
      * 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선
        * @OneToMany(fetch = FetchType.LAZY) // 글로벌 로딩 전략
      * 운영(실무)에서 글로벌 로딩 전략은 모두 지연 로딩
      * 최적화가 필요한 곳은 페치 조인 적용
    * 한계
      * 페치 조인 대상에는 별칭을 줄 수 없음 (하이버네이트는 가능하나 가급적 사용을 권장하지 않음)
        * 예 : SELECT t FROM Team t JOIN FETCH t.members as m WHERE m.age > 10
        * 특정 조건들을 걸러내는건 JPA 설계 의도(객체 그래프 설계)와 맞지 않음 
        * FETCH JOIN은 기본적으로 연관된 모든 것들을 가져오게 설계되어 있기 때문에 가급적이면 별칭을 주어 조건을 걸러 내어 사용하지 말 것
      * 둘 이상의 컬렉션은 페치 조인할 수 없음
        * 다대다로 되어 (곱하기) 데이터가 너무 많거나 정합성이 안맞을 수 있음 (할 수 있어도 하지 말것)
      * 컬렉션을 페치 조인하면 페이징 API를 사용할 수 없음 (setFirstResult, setMaxResult)
        * 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
        * 하이버네이트는 경고 로그를 남기고 메모리에서 페이징 (매우 위험)
        * 예 : Team에 Member가 2명인데 페이징을 1로 설정했을 때 Member 데이터를 1개만 가져와 문제 발생함
      * SELECT t FROM Team t 쿼리로 fetch join을 하지 않고 사용
        * Team 클래스에 List<Member> members 필드에 @BatchSize(size = 100) 어노테이션 태깅
      * 쿼리를 직접 작성하여 DTO로 출력
  * 정리
    * 모든 것을 페치 조인으로 해결할 수 없음
    * 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
    * 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면,  
      페치 조인보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적
* 다형성 쿼리
  * 조회 대상을 특정 자식 타입으로 한정 지을 수 있음
    * 예 : Item 중에 Book, Movie를 조회
    * JPQL : SELECT i FROM Item i WHERE type(i) IN (Book, Movie)
    * SQL : SELECT I.\* FROM ITEM I WHERE I.DTYPE IN ('B', 'M')
  * TREAT(JPA 2.1)
    * 자바의 타입 캐스팅과 유사함
    * 상속 구조에서 부모 타입을 특정 자식 타입으로 다룰 때 사용함
    * FROM, WHERE, SELECT(하이버네이트 지원) 사용
    * 예 : 부모인 Item과 자식 Book이 있음
    * JPQL : SELECT i FROM Item i WHERE TREAT(i as Book).author = 'kim'
    * SQL : SELECT I.\* FROM ITEM I WHERE I.DTYPE = 'B' and I.auther = 'kim' (싱글 테이블 전략인 경우)
* 엔티티 직접 사용
  * 기본키 값
    * JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본키 값을 사용
    * JPQL : 
      * SELECT COUNT(m.id) FROM Member m // 엔티티의 아이디를 사용
      * SELECT COUNT(m) FROM Member m // 엔티티를 직접 사용
    * SQL : SELECT COUNT(M.ID) as cnt FROM MEMBER M // JPQL 둘다 같은 SQL 실행됨
  * 외래키 값
    * 마찬가지
* Named 쿼리
  * 정적 쿼리만 가능 (동적 쿼리는 불가능하지만 파라미터 바인딩 쿼리로 작성 가능)
    * 미리 정의해서 이름을 부여한 후 사용하는 JPQL
    * 어노테이션 또는 XML에 정의
    * 앱 로딩 시점에 초기화 후 재사용 (정적 쿼리라 변하지 않음 > SQL로 파싱하여 미리 캐싱처리)
    * 앱 로딩 시점에 쿼리를 검증
  * 환경 설정
    * XML이 우선권을 가짐
    * 앱 운영 환경에 따라 다른 XML을 배포할 수 있음
* 벌크 연산 (UPDATE, DELETE - 특정 조건 1개 데이터가 아닌 경우)
  * 재고가 10개 미만인 모든 상품의 가격을 10% 상승 하려면?
  * JPA 변경 감지 기능으로 실행하려면 너무 많은 SQL 실행
    * (1) 재고가 10개 미만인 상품을 리스트로 조회
    * (2) 상품 엔티티의 가격을 10% 증가
    * (3) 트랜잭션 커밋 시점에 변경 감지 동작
  * 변경된 데이터가 100건이라면 100번의 UPDATE SQL이 실행됨
  * 예제
    * 쿼리 한 번으로 여러 테이블 로우 변경(엔티티)
    * executeUpdate()의 결과는 영향받은 엔티티 수 반환
    * UPDATE, DELETE 지원
    * INSERT(INSERT INTO .. SELECT, 하이버네이트 지원)
  * 주의
    * 벌크 연산은 영속성 컨텍스트를 무시하고 DB에 직접 쿼리 실행
      * 벌크 연산 먼저 실행
      * 벌크 연산 수행 후 영속성 컨텍스트 초기화
