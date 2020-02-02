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

-----

### [Contents]
#### JPA 구동 방식ㅎ
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
