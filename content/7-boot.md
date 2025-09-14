## 웹 서버와 서블릿 컨테이너 (boot-1)

### 웹 서버와 스프링 부트
- 전통적인 방식 
  - 먼저 서버에 톰캣 같은 WAS를 직접 설치 
  - 서블릿 스펙에 맞춰서 코딩 
  - WAR 파일로 빌드해서 만들고 그걸 또 WAS에 배포 
  - IDE 사용시에도 WAS와 연동하는 설정 해줘야 함
- 최근 방식 (스프링 부트의 혁신)
  - 내장 톰캣이 라이브러리로 들어있음
  - 코드 작성하고 JAR로 빌드, 원하는 위치에서 해당 JAR을 실행하기만 하면 된다
  - 그냥 main() 메서드 실행하면 WAS도 함께 동작

### JAR, WAR
- JAR (Java Archive)
  - JVM 위에서 직접 실행 
  - 예) java -jar abc.jar로 실행
  - 쉽게 이야기해서 클래스와 관련 리소스를 압축한 단순한 파일
  - 필요한 경우 이 파일을 직접 실행할 수도 있고, 다른 곳에서 라이브러리로 사용할 수도 있다
- WAR (Web Application Archive)
  - 웹 애플리케이션 서버(WAS)에 배포할 때 사용하는 파일, 웹 서버 위에서 실행
  - HTML, CSS 등 정적 리소스 + 클래스 파일 모두 포함 
    - 디렉토리 구조를 지켜야 한다
- 빌드와 컴파일의 차이에 대해서 생각해보자

### 서블릿 컨테이너 초기화

#### 순수 서블릿
- ServletContainerInitializer : 서블릿 컨테이너 초기화 인터페이스. 구현하여 사용
  - 서블릿 컨테이너는 실행 시점에 초기화 메서드인 onStartup() 을 호출
- WAS에게 실행할 초기화 클래스를 알려줘야 한다. 정확한 패키지 경로에 파일 생성
- 애플리케이션 초기화 (MyContainerInitV2, 서블릿 컨테이너/애플리케이션 초기화 과정 분리)
  - 서블릿 컨테이너가 시작될 때, 특정 클래스를 지정하여 애플리케이션 초기화 코드를 실행하는 과정
  - 초기화 대상 지정 
    - @HandlesTypes(AppInit.class) 어노테이션을 사용해서 AppInit 인터페이스를 구현한 클래스들을 찾음 
  - 구현 클래스 탐색 및 전달
    - 서블릿 컨테이너는 시작 시점에 @HandlesTypes를 확인, AppInit 인터페이스를 구현한 모든 클래스(AppInitV1Servlet.class 등)를 찾는다
    - 찾아낸 클래스 정보를 ServletContainerInitializer의 파라미터로 전달 (이때는 객체 인스턴스가 아닌, 클래스 설계도 정보만 넘어온다)
  - 객체 생성 (인스턴스화)
    - 전달받은 클래스 정보를 이용해 리플렉션 기술로 실제 객체 생성
  - 초기화 코드 실행
    - 생성된 객체의 onStartup(ctx) 메서드를 호출하여 본격적인 초기화 작업을 시작
    - 이때 파라미터로 서블릿 컨테이너의 정보(ctx)를 넘겨주어, 서블릿을 등록하는 등의 작업을 수행할 수 있게 한다
  - 과정 분리 이점 : 편리, 의존성 분리

#### 스프링 컨테이너 등록 (AppInitV2Spring)
- 스프링 컨테이너 생성
- 스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
- 스프링MVC를 사용하는데 필요한 디스패처 서블릿을 서블릿 컨테이너 등록

#### 스프링 MVC 서블릿 컨테이너 초기화 지원
- 스프링 MVC는 이러한 서블릿 컨테이너 초기화 작업을 이미 만들어 둠
- WebApplicationInitializer 구현 (직접 만든 AppInit 인터페이스 역할)
  - 구현 클래스에서 기존 작업 하면 된다
- SpringServletContainerInitializer (직접 만든 MyContainerV2 역할)
  - spring-web 라이브러리에 등록되어있음

## 내장 톰캣, 스프링 부트 (boot-2)

### WAR 배포 방식
- 톰캣 같은 WAS를 별도로 설치해야 한다
- 개발 환경 설정이 복잡
- 배포 과정이 복잡. WAR를 만들고 이것을 또 WAS에 전달해서 배포
- 톰캣의 버전을 변경하려면 톰캣을 다시 설치해야 한다

### 내장 톰캣 사용 발전시키기
- 내장 톰캣 직접 설정 (EmbedTomcatServletMain)
  - main메소드에서 내장톰캣 생성, 서블릿 등록, 톰캣 시작
- 스프링 연동 까지 (EmbedTomcatSpringMain)
  - main메소드에서 스프링 컨테이너, 디스패처 서블릿 작업
- 부트 클래스 (@MySpringBootApplication 사용)
- application.run 메소드 하나로 편리하게 동작
  - 내장 톰캣 실행, 스프링 컨테이너 생성, 디스패처 서블릿, 컴포넌트 스캔까지 모든 기능이 한번에

### Jar과 빌드,배포
- ide 도움 받지 않고 직접 빌드, 배포
  - JAR 안에 META-INF/MANIFEST.MF 파일에 실행할 main() 메서드의 클래스를 지정해주어야 한다
  - Gradle의 도움을 받으면 이 과정을 쉽게 진행할 수 있다
- JAR를 푼 결과를 보면 스프링 라이브러리나 내장 톰캣 라이브러리가 전혀 보이지 않는다
  - JAR 파일은 JAR파일을 포함할 수 없다.
  - 대안이 있긴 하지만 너무 복잡하고 권장x
- FatJar
  - 수 많은 JAR라이브러리를 풀어서 나오는 class들을 새로 만드는 JAR에 포함시키는 방식
  - 하나의 jar 파일에 필요한 라이브러리들을 내장할 수 있게 된다 (톰캣 등) -> 웹 서버 설치, 실행 배포 단순화
  - 모두 class 로 풀려있으니 어떤 라이브러리가 사용되고 있는지 추적하기 어렵다
    - 파일명 중복 해결 불가

### 스프링 부트와 웹 서버
- spring-boot-starter-web 를 사용하면 내부에서 내장 톰캣을 사용
- SpringApplication.run() 으로 수많은 일 처리
  - 핵심은 두가지
  - 스프링 컨테이너 생성 (ServletWebServerApplicationContextFactory)
  - was 생성 (.TomcatServletWebServerFactory)
- 스프링 부트 직접 빌드, 배포
  - 실행 가능 JAR 사용
  - jar 내부에 jar를 포함할 수 있는 특별한 구조의 jar를 생성, 동시에 만든 jar를 내부 jar를 포함
  - JarLauncher 제공
    - jar 내부에 jar를 읽어들이는 기능, 특별한 구조에 맞게 클래스 정보도 읽어들이도록 처리해줌

## 라이브러리 버전 관리

### 라이브러리 직접 관리의 한계
- 프로젝트를 수동으로 구성 시
  - 필요한 라이브러리와 버전을 일일이 선택
  - 라이브러리 간 호환성 검증이 불가피해져 초기 설정에 과도한 시간과 비용이 소요

### 스프링 부트 의존성 관리 플러그인(`io.spring.dependency-management`)
- 플러그인을 사용하면 spring-boot-dependencies 에 있는 다음 bom 정보를 참고, 현재 부트 버전에 맞는 라이브러리 제공
  - spring-boot-dependencies 는 스프링 부트 gradle 플러그인에서 사용하기 때문에 개발자의 눈에 의존 관계로 보이지는 않는다
- 스프링 부트가 관리하지 않는 외부 라이브러리 -> 직접 버전 명시

### 스프링 부트 스타터(`spring-boot-starter-*`)
- 목적에 따라 필요한 라이브러리를 골라 놓음 -> 일괄적으로 편하게 사용하게 도와줌
- 일반적인 기능 세트를 하나의 의존성으로 묶어, 필요한 라이브러리를 일괄 가져오게 함

### 버전 커스터마이징
- 스프링 부트가 관리하는 라이브러리의 특정 버전을 변경하고 싶다면 (거의 x)
  - ext['tomcat.version'] = '10.1.4'

## 자동 구성 (Auto Configuration)

### 스프링 부트의 자동 구성
- 일반적으로 자주 사용하는 수 많은 빈들을 자동으로 등록
- spring-boot-autoconfigure 라는 프로젝트 안에서 수 많은 자동 구성을 제공
  - JdbcTemplateAutoConfiguration : JdbcTemplate
  - DataSourceAutoConfiguration : DataSource
  - DataSourceTransactionManagerAutoConfiguration : TransactionManager
- @AutoConfiguration: 자동 구성 활성화 
  - after = DataSourceAutoConfiguration.class: 실행 순서 지정 
  - @Conditional : 특정 조건에 맞을 때 설정이 동작하도록
  - @Import: 설정 추가

### @Conditional
- 특정 조건일 때만 해당 기능이 활성화 되도록
- 이 기능을 사용하려면 먼저 Condition 인터페이스를 구현
  - matches() 메서드가 true 를 반환하면 조건에 만족해서 동작 
  - ConditionContext : 스프링 컨테이너, 환경 정보등을 담고 있다. 
  - AnnotatedTypeMetadata : 애노테이션 메타 정보를 담고 있다.
- @ConditionalOnXxx
  - 클래스 / 빈 / 환경 정보 / 리소스 / 웹 환경 등 만족할 경우

### @AutoConfiguration

#### 순수 라이브러리 만들기 (boot-library)
- @AutoConfiguration 
  - 스프링 부트가 제공하는 자동 구성 기능을 적용할 때 사용하는 애노테이션
- 자동 구성 대상 지정 
  - 스프링 부트 자동 구성을 적용하려면, 다음 파일에 자동 구성 대상을 꼭 지정해주어야 한다. 
  - 폴더 위치와 파일 이름이 길기 때문에 주의 
    - 파일 생성
    - src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
    - 앞서 만든 자동구성 클래스 패키지 경로 입력

#### 자동 구성 라이브러리 적용 (boot-autoconfigure)
- libs 폴더에 빌드한 라이브러리 JAR파일 복사
- Gradle dependencies에 적용
- 조건에 맞게 실행시(@Conditional 조건) 적용된다
- 라이브러리 사용자 입장에서 편해진다
  - 내부 구조 알 필요 x
  - 빈 하나하나 직접 등록 x
  - 라이브러리 추가만으로 자동으로 모든 구성 완료
  - 조건 불만족시 기능 비활성화 -> 불필요한 빈 로드 x

### 스프링 부트 @SpringBootApplication, @EnableAutoConfiguration
- @Import 에 설정 정보를 추가하는 방법은 2가지가 있다
  - 정적인 방법: @Import (클래스) 이것은 정적
    - 설정으로 사용할 대상을 동적으로 변경할 수 없다 
  - 동적인 방법: @Import ( ImportSelector ) 코드로 프로그래밍해서 설정으로 사용할 대상을 동적으로 선택
    - 스프링은 설정 정보 대상을 동적으로 선택할 수 있는 ImportSelector 인터페이스를 제공
    - 스프링은 HelloImportSelector 를 실행하고, "autoconfigure.bootautoconfigure.selector.HelloConfig" 라는 문자를 반환
    - 스프링은 이 문자에 맞는 대상을 설정 정보로 사용. 따라서 hello.selector.HelloConfig 이 설정 정보로 사용
- @EnableAutoConfiguration 동작 방식
  - @SpringBootApplication
  - @EnableAutoConfiguration
  - @Import(AutoConfigurationImportSelector.class)
  - resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 파일을 열어서 설정 정보 선택
  - 해당 파일의 설정 정보가 스프링 컨테이너에 등록되고 사용

#### 스프링 부트의 자동 구성을 직접 만들어서 사용할 시
- @AutoConfiguration 에 자동 구성의 순서를 지정할 수 있다. 
- @AutoConfiguration 도 설정 파일이다. 내부에 @Configuration 이 있는 것을 확인할 수 있다. 
  - 하지만 일반 스프링 설정과 라이프사이클이 다르기 때문에 컴포넌트 스캔의 대상이 되면 안된다. 파일에 지정해서 사용해야 한다. 
  - resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 
  - 따라서 스프링 부트가 제공하는 컴포넌트 스캔에서는 @AutoConfiguration 을 제외하는 AutoConfigurationExcludeFilter 필터가 포함되어 있다.
- 라이브러리를 만들어서 제공할 때 사용, 그 외에는 거의 사용할 일 없다
  - 특정 빈들이 어떻게 등록된 것인지 확인이 필요할 때가 있다

## 외부 설정, 프로필 (boot-profile)

- 각각의 환경에 따라서 서로 다른 설정값이 존재한다는 점
- 가장 단순한 방법은 다음과 같이 각각의 환경에 맞게 애플리케이션을 빌드 -> 불편
- 그래서 보통 빌드는 한번만 하고 각 환경에 맞추어 실행 시점에 외부 설정값을 주입

### 외부 설정 방법 4가지
- OS 환경 변수: OS에서 지원하는 외부 설정, 해당 OS를 사용하는 모든 프로세스에서 사용
  - 모든 프로그램에서 공유 가능 
  - 전역 변수 성격으로 특정 애플리케이션만을 위한 설정에는 부적합
- 자바 시스템 속성: 자바에서 지원하는 외부 설정, 해당 JVM안에서 사용
  - 해당 JVM 안에서만 접근 가능 
  - -D 옵션이 -jar 옵션보다 앞에 위치해야 함 
  - Java가 내부적으로 사용하는 기본 속성들도 포함
- 자바 커맨드 라인 인수: 커맨드 라인에서 전달하는 외부 설정, 실행시 main(args) 메서드에서 사용
  - 공백(space)으로 구분 
  - 개발자가 직접 파싱 필요
  - 커맨드 라인 옵션 인수 (스프링 표준) : --key=value 형식
  - 스프링 부트는 커맨드 라인을 포함해서 커맨드 라인 옵션 인수를 활용할 수 있는 ApplicationArguments 를 스프링 빈으로 등록, 커맨드 라인을 저장
  - 해당 빈을 주입 받으면 커맨드 라인으로 입력한 값을 어디서든 사용할 수 있다.
- 설정 데이터: 프로그램에서 파일을 직접 읽어서 사용

#### 스프링의 외부 설정 통합 Environment와 PropertySource로 추상화
- PropertySource: 각각의 외부 설정 조회 구현체
- Environment: 모든 외부 설정을 통합하여 일관된 접근 제공

### 설정 데이터 (외부 파일, 내부 파일)
- 설정 파일을 외부에 관리하는 것은 상당히 번거로운 일
  - 서버 개수만큼 설정 파일 관리 필요 
  - 설정값 변경 이력 추적 어려움 
  - 배포와 설정 관리가 분리되어 복잡함
- 내부 파일로 관리
  - 설정 파일을 프로젝트 내부에 포함해서 관리 빌드 시점에 함께 빌드
  - application-{profile}.properties
  - 하나의 properties파일에서 #---로 구분
- 우선순위 체계 
  - 기본 우선순위 원칙
  - 2가지 핵심 원칙:
    - 더 유연한 것이 우선권: 실행 시 변경 가능한 것이 파일보다 우선 
    - 범위가 좁은 것이 우선권: 접근 범위가 제한적인 것이 전역적인 것보다 우선 
- 자주 사용하는 우선순위 (아래에 위치할 수록 우선권)
  - 설정 데이터 (application.properties)
  - OS 환경변수 
  - 자바 시스템 속성 
  - 커맨드 라인 옵션 인수 
  - @TestPropertySource (테스트 전용)
- 설정 데이터 내 우선순위 (아래에 위치할 수록 우선권))
  - jar 내부 application.properties 
  - jar 내부 프로필 적용 파일 application-{profile}.properties 
  - jar 외부 application.properties 
  - jar 외부 프로필 적용 파일 application-{profile}.properties
- 설정 데이터 적용 순서와 기본값 
  - 적용 메커니즘
    - 위에서 아래로 순차 처리 : 문서를 순서대로 읽으며 값 설정 
    - 덮어쓰기 방식 : 기존 값이 있으면 새로운 값으로 교체 
    - 프로필 조건부 적용 : spring.config.activate.on-profile 조건 만족 시만 적용 
    - 부분 적용 가능 : 일부 속성만 변경하고 나머지는 기존값 유지 
  - 프로필 미지정 시
    - 기본값만 적용됨
    - 스프링은 자동으로 default 프로필 활성화
    - 프로필 지정 없이도 로컬 개발 환경에서 편리하게 사용 가능 
  - 새로운 키 : 기존 설정에 추가 
  - 기존 키 : 기존 값을 덮어씀

### 설정 조회 방법
Spring의 외부 설정 조회 방법
- Environment : 직접적인 설정값 조회 
- @Value : 값 주입 방식 
- @ConfigurationProperties : 타입 안전한 설정 속성

#### Environment
- Environment.getProperty(key, Type) 호출 시 타입 정보를 주면 해당 타입으로 자동 변환
- 직접 값을 꺼내는 과정을 반복해야 한다

#### @Value
- 외부 설정값을 편리하게 주입받는다.
- 내부적으로는 Environment를 사용
- 필드 혹은 파라미터를 사용, 설정 값 주입 받을 수 있음
- 기본 값 지정 가능

#### @ConfigurationProperties (@ConfigurationPropertiesV1)
- 타입 안전한 설정 속성, 객체 사용
- 자바빈 프로퍼티 방식으로 주입 받음 -> getter, setter 필요
- 표기법 변환이 일어난다 (케밥 -> 카멜)
- 타입 안맞을 시 오류 발생
- setter사용하기 때문에 조금 불안할 수 있음

#### @ConfigurationProperties (@ConfigurationPropertiesV3)
- 생성자를 만들어 두어 이를 통해 주입, 바인딩
- setter 사용x -> 변경 x

### @Profile
- 각 환경마다 서로 다른 빈을 등록 필요 시
- 내부적으로 @Conditional 사용
