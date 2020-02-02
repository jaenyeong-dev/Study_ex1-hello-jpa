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

### [Contents]
#### JPA 구동 방식
* persistence 클래스가 META-INF/persistence.xml 설정 정보 조회 후 EntityManagerFactory 생성
* EntityManagerFactory가 필요한 entityManager를 생성

#### H2 Database
* H2 DB 콘솔에서 JDBC URL 설정 후 연결
  * persistence.xml 파일 안에 설정한 javax.persistence.jdbc.url 속성 "jdbc:h2:tcp://localhost/~/test" 값 그대로 사용
