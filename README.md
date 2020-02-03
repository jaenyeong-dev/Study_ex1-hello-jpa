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
