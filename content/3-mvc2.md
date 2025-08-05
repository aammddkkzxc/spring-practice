### 메세지, 국제화
- 여러 화면에 보이는 상품명, 가격, 수량 등, label 에 있는 단어를 변경하려면 다음 화면들을 다 찾아가면서 모두 변경 해야 한다
- 다양한 메시지를 한 곳에서 관리하도록 하는 기능을 메시지 기능
- 메시지 관리 기능을 사용하려면 스프링이 제공하는 MessageSource를 스프링 빈으로 등록 (스프링 부트 사용시 MessageSource 자동 등록)
- properties파일 나눠서 국제화 편리하게 가능
- 스프링도 Locale 정보를 알아야 언어를 선택할 수 있는데, 스프링은 언어 선택시 기본으로 AcceptLanguage 헤더의 값을 사용

### Validation
- 컨트롤러의 중요한 역할중 하나는 HTTP 요청이 정상인지 검증
- 클라이언트 검증, 서버 검증
    - 클라이언트 검증은 조작할 수 있으므로 보안에 취약
    - 서버만으로 검증하면, 즉각적인 고객 사용성이 부족
    - 둘을 적절히 섞어서 사용하되, 최종적으로 서버 검증은 필수

#### 스프링의 검증 지원 기능 (BindingResult)
- 필드에 오류가 있으면 FieldError 객체를 생성해서 bindingResult 에 담아 둔다
- 특정 필드를 넘어서는 오류가 있으면 ObjectError 객체를 생성해서 bindingResult 에 담아두면 된다
- BindingResult 가 있으면 @ModelAttribute 에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출
    - 없으면 -> 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동
    - 있으면 -> 오류 정보(FieldError) 를 BindingResult 에 담아서 컨트롤러를 정상 호출
- FieldError 생성자 파라미터에 사용자의 거절된 값(rejectedValue)도 유지, 각 오류별 메시지와 값 보관 가능
- errors.properties 로 에러 메시지 관리 -> 스프링 부트가 해당 메시지 파일을 인식할 수 있게 설정을 추가 해야 한다
- rejectValue(), reject()로 단순화 가능
- MessageCodesResolver, 오류 메시지 관리 전략 (구체적인 것에서 덜 구체적인 순서대로)
    - Level1: 필드+객체별 메시지 required.item.itemName
    - Level2: 필드별 메시지 required.itemName
    - Level3: 타입별 메시지 required.java.lang.String
    - Level4: 코드별 범용 메시지 required
- Validator 분리(검증기 클래스화, 스프링이 제공하는 Validator인터페이스 구현)
    - 컨트롤러에서 검증기 직접 주입 받아서 사용도 가능은 함
    - @InitBinder 사용, WebDataBinder 에 검증기를 추가하면 해당 컨트롤러에서 검증기를 자동으로 적용할 수 있다.
        - @Validated 를 사용해서 검증기 실행.
        - 글로벌 적용도 가능

### Bean Validation
- 검증(Validation)은 매번 코드를 작성하기 번거롭고, 대부분의 검증 요구사항(빈값, 범위 등)은 공통적
- Bean Validation은 검증을 표준화(JSR-380)한 자바 표준. 주요 구현체는 Hibernate Validator
    - 면 스프링 부트는 Bean Validator를 글로벌 Validator 로 등록 (직접 글로벌 Validator 등록시 동작 안해버림)
- @ModelAttribute -> 각각의 필드 타입 변환시도 -> 변환에 성공한 필드만 BeanValidation 적용
    - 실패하면 typeMismatch 로 FieldError 추가
    - 바인딩에 성공한 필드만 Bean Validation 적용
- BeanValidation 메시지 찾는 순서
    - 생성된 메시지 코드 순서대로 messageSource 에서 메시지 찾기
    - 애노테이션의 message 속성 사용 @NotBlank(message = "공백! {0}")
    - 라이브러리가 제공하는 기본 값 사용 (공백일 수 없습니다.)
- 오브젝트 오류 -> 별도로 처리
- 생성/수정 -> 검증 요구사항(등록 시에는 id값이 없음, 다른 비지니스 요구사항)이 다르다 -> 별도의 객체를 사용하는 방법이 실용적(groups 잘 안 씀)
    - @ModelAttribute 모델 명칭 주의
- @ModelAttribute vs @RequestBody
    - @ModelAttribute 는 필드 단위로 정교하게 바인딩이 적용. 특정 필드가 바인딩 되지 않아도 나머지 필드는 정상 바인딩 되고, Validator를 사용한 검증도 적용할 수 있다
    - @RequestBody 는 HttpMessageConverter 단계에서 JSON 데이터를 객체로 변경하지 못하면 이후 단계 자체가 진행되지 않고 예외가 발생. 컨트롤러도 호출되지 않고, Validator도 적용할 수 없다

### 로그인 - 쿠키, 세션

- 쿠키에는 영속 쿠키와 세션 쿠키가 있다.
    - 영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지
    - 세션 쿠키: 만료 날짜를 생략하면 브라우저 종료시 까지만 유지
    - Cookie객체 생성 시 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
    - @CookieValue로 쿠키 쉽게 읽음
- 쿠키 보안문제
    - 쿠키 값 조작/탈취 가능 -> 다른 사용자로 변조 가능, 정보 유출
    - 쿠키엔 랜덤 토큰만 저장, 실제 값/정보는 서버(세션)에 저장
- 세션
    - 로그인 후 랜덤 세션ID 생성 (UUID 등)
    - 서버에 세션저장소(sessionId:회원 정보) 보관 (기본적으로 서버 메모리 사용)
    - 세션ID만 쿠키(mySessionId)로 응답, 클라이언트 보관
    - 모든 요청에 세션ID 쿠키가 실려서 서버가 사용자 식별 가능
    - 직접 만들기 : 로그인 -> createSession, 로그 아웃 -> expire, 홈 -> getSession회원 정보 조회
- 서블릿 HTTP 세션 사용
    - HttpSession 사용
    - JSESSIONID: 서블릿 컨테이너가 발급하는 기본 세션 쿠키명
    - 스프링 @SessionAttribute: 세션조회 자동 바인딩
-  jsessionid URL 노출 대응
- URL 전달 방식을 끄고 항상 쿠키를 통해서만 세션유지 강제 가능
- URL에 지속적으로 사용하기도 가능 -> 별로
- 세션 타임아웃
    - 세션은 사용자가 로그아웃을 직접 호출해서 session.invalidate() 가 호출 되는 경우에 삭제
    - 그런데 대부분 의 사용자는 로그아웃을 선택하지 않고, 그냥 웹 브라우저를 종료
    - 문제는 HTTP가 비 연결성(ConnectionLess)이므로 서버 입장에서는 해당 사용자가 웹 브라우저를 종료한 것인지 아닌지를 인식할 수 없음
    - 따라서 서버에서 세션 데이터를 언제 삭제해야 하는지 판단하기가 어렵다
    - 무한정 보관할 경우
        - JSESSIONID 탈취 당하여 악의적 요청
        - 메모리 문제
    - 서버에 최근에 요청한 시간을 기준으로 30분 정도를 유지해주는 것이 괜찮은 대안(HttpSession은 이 방식 사용)

### 로그인 - 필터, 인터셉터

#### 서블릿 필터
- filter의 경우 Spring의 context 바깥에서 실행.
    - 컨트롤러와 타임리프 렌더링이 끝난 뷰가 응답에 실려 나오고 난 다음 finally 내 코드가 실행
- 실무에서 HTTP 요청시 같은 요청의 로그에 모두 같은 식별자를 자동으로 남기는 방법은 logback mdc로 검색
    - 여기선 간단히 uuid로 HTTP 요청을 구분
- chain.doFilter(request, response)
    - 다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출
    - 만약 이 로직을 호출하지 않으면 다음 단계로 진행 X
- 스프링 부트를 사용한다면 FilterRegistrationBean 을 사용해서 등록
- httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
    - 미인증 사용자일 경우 더이상 진행 않고 로그인 화면으로 리다이렉트 (필터는 물론 서블릿, 컨트롤러 더는 호출 X)
    - 로그인 이후에 원래 화면으로 들어가는 것이 좋다
    - 컨트롤러에서 로그인 성공시 해당 경로로 이동하는 기능 필요

#### 스프링 인터셉터
- request.setAttribute(LOG_ID, uuid)
    - 서블릿 필터의 경우 지역변수로 해결이 가능 하지만, 스프링 인터셉터는 호출 시점이 완전히 분리되어 있음.
    - 따라서 preHandle 에서 지정한 값을 postHandle , afterCompletion 에서 함께 사용하려면 어딘 가에 담아두어야 한다
    - LogInterceptor 도 싱글톤 처럼 사용되기 때문에 맴버변수를 사용하면 위험 -> request 에 담아둔다
- HandlerMethod
    - 핸들러 정보는 어떤 핸들러 매핑을 사용하는가에 따라 달라진다
    - @Controller , @RequestMapping 을 활용한 핸들러 매핑을 사용하는데, 이 경우 핸들러 정보로 HandlerMethod 가 넘어옴
    - /resources/static 와 같은 정적 리소스가 호출 되는 경우 ResourceHttpRequestHandler 가 핸들러 정보로 넘어오기 때문에 타입에 따라서 처리가 필요
-  예외가 발생한 경우 postHandle 가 호출되지 않음. afterCompletion은 예외가 발생해도 호출 되는 것을 보장
- 필터와 비교해보면 인터셉터는 addPathPatterns , excludePathPatterns 로 매우 정밀하게 URL 패턴을 지정 할 수 있다.

#### 필터, 인터셉터
- 인터셉터는 스프링 MVC 구조에 특화된 필터 기능을 제공(생명주기에 따른 세밀한 제어).
- 스프링 MVC를 사용하고, 특별히 필터를 꼭 사용해야 하는 상황이 아니라면 인터셉터를 사용하는 것이 더 편리
- 필터에는 스프링 인터셉터는 제공하지 않는, 아주 강력한 기능이 있다
    - chain.doFilter(request, response); 를 호출해서 다음 필터 또는 서블릿을 호출할 때 request , response 를 다른 객체로 바꿀 수 있다.
    - ServletRequest , ServletResponse 를 구현한 다른 객체를 만들어서 넘기면 해당 객체가 다음 필터 또는 서블릿에서 사용
    - 잘 사용하는 기능은 아님. 참고만

#### ArgumentResolver
-  @Login 애노테이션 정의, HandlerMethodArgumentResolver 구현
- 자동으로 세션에 있는 로그인 회원을 찾아주고, 만약 세션에 없다면 null 을 반환하도록 개발
- 결과는 동일하지만, 더 편리하게 로그인 회원 정보를 조회

### 예외 처리와 오류 페이지
- 순수 서블릿은 다음 2가지 방식으로 예외 처리
    - Exception (예외)
        - 자바의 main() 메서드 넘어서 예외 던져지면 정보 남긴후 해당 스레드 종료
        - 웹 어플리케이션 -> 톰캣 같은 WAS 까지 예외가 전달
            - 서버 내부에서 처리할 수 없는 오류가 발생한 것으로 생각해서 HTTP 상태 코드 500을 반환, 오류 페이지 제공
    - response.sendError(HTTP 상태 코드, 오류 메시지)
        - 호출 한다고 당장 예외가 발생하는 것은 아니지만, 서블릿 컨테이너에게 오류가 발생했다는 점을 전달, 정보 제공
        - 서블릿 컨테이너는 고객에게 응답 전에 response 에 sendError() 가 호출되었는지 확인. 그리고 호출 되었다면 설정한 오류 코드에 맞추어 기본 오류 페이지를 보여준다
    - WebServerCustomizer 를 만들고, 예외 종류에 따라서 ErrorPage 를 추가, 예외 처리용 컨트롤러 ErrorPageController 를 만듬 -> 오류 페이지 설정

#### 오류 페이지 작동 원리
- 예외가 발생해서 WAS까지 전파
    - WAS는 오류 페이지 경로를 찾아서 내부에서 오류 페이지를 호출. 이때 오류 페이지 경로로 필터, 서블릿, 인터셉터, 컨트롤러가 모두 다시 호출
    - 매우 비효율적
- 서블릿 필터는 이런 경우를 위해서 dispatcherTypes 라는 옵션을 제공
    - 서블릿 스펙은 실제 고객이 요청한 것인지, 서버가 내부에서 오류 페이지를 요청하는 것인지 DispatcherType 으로 구분할 수 있는 방법을 제공
    - 필터 등록 시 기본값 사용하면 클라이언트 요청인 경우에만 필터 적용
    - filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR); -> 오류 페이지 요청에서도 호출
- 스프링 인터셉터
    - excludePathPatterns에 오류 페이지 경로 추가

#### 스프링 부트와 오류 페이지
- ErrorPage 를 자동으로 등록한다. 이때 /error 라는 경로로 기본 오류 페이지를 설정
- BasicErrorController 라는 스프링 컨트롤러를 자동으로 등록 (ErrorPage 에서 등록한 /error 를 매핑해서 처리)
- BasicErrorController 컨트롤러는 여러 정보를 model에 담아서 뷰에 전달. 뷰 템플릿은 이 값을 활용해서 출력 가능
    - 오류 관련 내부 정보들을 고객에게 노출하는 것은 좋지 않다. 혼란만 더해지고, 보안상 문제
    - application.properties로 설정 가능
- 에러 공통 처리 컨트롤러의 기능을 변경하고 싶으면 ErrorController 인터페이스를 상속 받아서 구현하거나 BasicErrorController 상속 받아서 기능을 추가


### 예외 처리와 API
- 오류 페이지는 단순히 고객에게 오류 화면을 보여주고 끝이지만, API는 각 오류 상황에 맞는 오류 응답 스펙을 정하고, JSON으로 데이터를 내려주어야 한다
- BasicErrorController 코드 -> /error 동일한 경로를 처리하는 두 메서드
    - errorHtml() : produces = MediaType.TEXT_HTML_VALUE -> 클라이언트 요청의 Accept 해더 값이 text/html 인 경우에는 errorHtml() 을 호출해서 view를 제공
    - error() : 그외 경우에 호출되고 ResponseEntity 로 HTTP Body에 JSON 데이터를 반환

#### HandlerExceptionResolver
- 스프링 MVC는 컨트롤러(핸들러) 밖으로 예외가 던져진 경우 예외를 해결하고, 동작을 새로 정의할 수 있는 방법을 제공
    - 컨트롤러 밖으로 던져진 예외를 해결하고, 동작 방식을 변경하고 싶으면 HandlerExceptionResolver 를 사용하면 된다. 줄여서 ExceptionResolver
    - ExceptionResolver 로 예외를 해결해도 postHandle() 은 호출 X
- HandlerExceptionResolver 의 반환 값에 따른 DispatcherServlet 의 동작 방식
    - 빈 ModelAndView: new ModelAndView()처럼 빈 ModelAndView 를 반환하면 뷰 렌더링 X, 정상 흐름으로 서블릿이 리턴
    - ModelAndView 지정: ModelAndView 에 View , Model 등의 정보를 지정해서 반환하면 뷰를 렌더링
    - null: 다음 ExceptionResolver 를 찾아서 실행. 만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다
- ExceptionResolver 를 사용하면 컨트롤러에서 예외가 발생해도 ExceptionResolver 에서 예외를 처리
    - 결과적으로 WAS 입장에서는 정상 처리

#### 스프링이 제공하는 ExceptionResolver
- 스프링 부트 HandlerExceptionResolverComposite 에 다음 순서로 등록
    1. ExceptionHandlerExceptionResolver
    2. ResponseStatusExceptionResolver
    3. DefaultHandlerExceptionResolver <- 우선순위 가장 낮음
- ResponseStatusExceptionResolver
    - 예외에 따라서 HTTP 상태 코드를 지정해주는 역할
    - @ResponseStatus 가 달려있는 예외, ResponseStatusException 예외 처리
- DefaultHandlerExceptionResolver
    - 파라미터 바인딩 시점에 타입이 맞지 않으면 내부에서 TypeMismatchException 이 발생, 이 경우 예외가 발생했기 때문에 그냥 두면 서블릿 컨테이너까지 오류가 올라감, 결과적으로 500 오류가 발생.
    - 파라미터 바인딩은 대부분 클라이언트가 HTTP 요청 정보를 잘못 호출해서 발생하는 문제 -> HTTP 에서는 이런 경우 HTTP 상태 코드 400을 사용하도록 되어 있다.
    - DefaultHandlerExceptionResolver 는 이것을 500 오류가 아니라 HTTP 상태 코드 400 오류로 변경 (내부적으로 sendError 사용 ->  WAS에서 다시 오류 페이지(/error)를 내부 요청)

#### API 예외 처리 - @ExceptionHandler (ExceptionHandlerExceptionResolver)
- HandlerExceptionResolver직접 구현 불편함
    - ModelAndView 를 반환해야 했다. 이것은 API 응답에 필요하지 않음
    - API 응답을 위해서 HttpServletResponse 에 직접 응답 데이터를 넣어주었다. 매우 불편
    - 특정 컨트롤러에서만 발생하는 예외를 별도로 처리하기 어렵다
- @ExceptionHandler 애노테이션을 선언하고, 해당 컨트롤러에서 처리하고 싶은 예외를 지정해주면 된다
    - 지정한 예외 또는 그 예외의 자식 클래스는 모두 잡을 수 있다
    - 자식예외 가 발생하면 부모 예외처리() , 자식예외처리() 둘다 호출 대상
    - 둘 중 더 자세한 것이 우선권을 가지므로 자식예외처리()가 호출
    - 부모예외 가 호출되면 부모예외처리() 만 호출 대상이 되므로 부모예외처리() 가 호출
    - 다양한 예외를 한번에 처리 가능
    - 어노테이션에 예외 선언 생략 시 메서드 파라미터의 예외가 지장됨
- @ControllerAdvice
    - @ControllerAdvice 는 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler , @InitBinder 기능을 부여해주는 역할
    - @ControllerAdvice 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌 적용)
    - 어노테이션, 패키지경로, 인터페이스, 클래스 지정 가능

### 스프링 타입 컨버터

#### 컨버터 - Converter
- 스프링의 타입 변환 적용 예
    - 스프링 MVC 요청 파라미터
    - @RequestParam , @ModelAttribute , @PathVariable
    - @Value 등으로 YML 정보 읽기
    - XML에 넣은 스프링 빈 정보를 변환
    - 뷰를 렌더링 할 때
- 스프링은 확장 가능한 Converter 인터페이스를 제공 -> 추가적인 타입 변환 필요시 구현해서 등록
    - Converter , ConverterFactory , GenericConverter 등 수많은 컨버터 제공
- ConversionService 구현체 -> 등록과 사용 관심사 분리
- 스프링은 내부에서 ConversionService 를 제공. WebMvcConfigurer 가 제공하는 addFormatters() 를 사용해서 추가하고 싶은 컨버터 등록
- @RequestParam -> RequestParamMethodArgumentResolver 에서 ConversionService 를 사용해서 타입을 변환
- 타임리프 ${{...}} 를 사용하 -> ConversionService를 이용한 변환 적용됨. 그냥 ${...} 는 toString호출

#### 포맷터 - Formatter
- Formatter 는 문자에 특화(객체 문자, 문자 객체) + 현지화(Locale)
- 포맷터를 지원하는 컨버전 서비스
- 포맷터를 지원하는 컨버전 서비스를 사용하면 컨버전 서비스에 포맷터를 추가 가능
    - 내부에서 어댑터 패턴을 사용해서 Formatter 가 Converter 처럼 동작하도록 지원
    - DefaultFormattingConversionService (FormattingConversionService)
- 포맷터 -> 기본형식이 지정 해야함 -> 각 필드마다 다양하게 사용하기 어려움
    - 애노테이션 기반 포맷터로 쉽게 가능
    - @NumberFormat, @DateTimeFormat
- 주의
    - Converter가 Formatter보다 적용 우선순위가 높다
    - 메시지 컨버터(HttpMessageConverter)에는 컨버전 서비스가 적용되지 않는다
        - HttpMessageConverter 의 역할은 HTTP 메시지 바디의 내용을 객체로 변환하거나 객체를 HTTP 메시지 바디에 입력하는 것
        - 전혀 상관 없음

### 파일 업로드

- HTML Form 데이터 전송 방식
    - application/x-www-form-urlencoded
        - 폼 데이터를 기본 문자로 전송
        - 파일 업로드엔 부적합(파일=바이너리 데이터)
    - multipart/form-data
        - Form 태그에 enctype="multipart/form-data"
        - 여러 종류(문자+바이너리)를 하나의 요청에 담아 전송(각 항목=Part)
        - 업로드 파일의 이름, Content-Type 정보도 포함
- 파일 저장시 고려사항
    - 유저마다 같은 파일 이름을 쓸 수 있다. -> 디스크에 같은 파일 이름으로 하면 충돌 일어날 수 있음
        - 고객이 관리하는 파일명 / 서버에서 관리하는 파일명(uuid로 안겹치게) 따로 관리
        - MultipartFile 로 받아와서 UploadFile로 저장
    - 확장자는 uuid로 변환하지 않고 별도로 추출후 그대로 저장하면 편리할 수 있다.
    - 파일 자체를 데이터베이스에 저장하지는 않는다
        - 보통 파일은 스토리지에 저장. AWS -> S3
        - 데이터베이스에 그 파일이 저장된 경로나 같은 거 정도만 저장
- 저장 후 보여줄 때 (view)
    - Resource, UrlResource 사용
    - 첨부파일 -> 다운로드 받을 수 있는 링크 뿌리기
        - 헤더를 같이 뿌려줘야(attachment) 첨부파일임 인식, 다운로드 할 수 있다. (ResponseEntity 사용)
        - 안하면 그냥 브라우저에서 열리게 되어 버림
        - 파일명 인코딩을 해서 한글이 안깨지도록 하자
    - 이미지 -> 이미지 파일들에 대한 링크
        - UrlResource 로 이미지 파일을 읽어서 @ResponseBody 로 이미지 바이너리를 반환
    - id를 받아오는 것을 통해 item에 접근할 수 있는 사용자만(권한 체크를 추가적으로 수행한 후) 파일에 접근해 주는 것이 좋다 