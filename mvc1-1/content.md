### 웹 어플리케이션
- 웹 시스템 구성 WEB, WAS, DB
  - 정적 리소스는 웹 서버가 처리하고, 동적인 처리가 필요한 경우 웹 서버가 WAS에 요청을 위임.
  - WAS는 중요한 애플리케이션 로직 처리에 전념. 
  - 효율적인 리소스 관리  정적 리소스 사용이 많으면 웹 서버를, 애플리케이션 리소스 사용이 많으면 WAS를 증설 
  - 장애 극복 : 정적 리소스만 제공하는 웹 서버는 비교적 안정적, WAS 또는 DB 장애 시 웹 서버가 오류 화면을 제공할 수 있다.
- 서블릿
  - 서블릿은 개발자가 HTTP 요청과 응답을 편리하게 다룰 수 있도록 돕는 자바 기술
  - 서블릿을 사용하면 복잡한 TCP/IP 소켓 연결, HTTP 메시지 파싱 등의 작업을 WAS가 대신 처리해주므로, 개발자는 핵심 비즈니스 로직에만 집중할 수 있다
  - 서블릿 컨테이너 : 톰캣과 같이 서블릿을 지원하는 WAS
  - 서블릿 컨테이너는 서블릿 객체의 생성, 호출, 소멸 등 생명주기를 관리, 요청마다 객체를 생성하는 비효율을 막기 위해 서블릿 객체를 싱글톤으로 관리
- HTML, HTTP API, CSR, SSR 
  - 정적 리소스: 웹 서버가 고정된 HTML, CSS, JS, 이미지, 영상 파일 등을 제공
  - HTML 페이지: WAS가 동적으로 HTML 파일을 생성하여 웹 브라우저에 전달 (예: JSP, 타임리프)
  - HTTP API: HTML이 아닌 데이터를 전달하며 주로 JSON 형식을 사용 
    - 다양한 시스템(웹 클라이언트, 앱 클라이언트, 서버 간 통신)에서 호출 
    - 데이터만 주고받으며, UI 화면은 클라이언트가 별도로 처리 
  - SSR (Server-Side Rendering)
    - HTML 최종 결과를 서버에서 만들어서 웹 브라우저에 전달
    - 주로 정적인 화면에 사용되며, JSP, 타임리프 등이 관련 기술 
  - CSR (Client-Side Rendering)
    - 자바스크립트를 사용하여 웹 브라우저에서 동적으로 HTML 결과를 생성하고 적용
    - 주로 동적인 화면에 사용되며, 웹 환경을 앱처럼 부분적으로 변경할 수 있다 (예: 구글 지도, Gmail).
      <details>
        <summary>작동 순서</summary>
          <ol>
            <li>웹 브라우저가 서버에 /orders.html과 같은 초기 HTML 파일을 요청 <br>
                이때 서버는 내용이 비어있거나 최소한의 구조만 가진 HTML을 응답. 이 HTML 파일에는 중요한 자바스크립트 링크가 포함되어 있다.
            </li>
            <li>자바스크립트 요청 <br>
                웹 브라우저가 서버로부터 받은 HTML에 포함된 자바스크립트 링크를 통해 필요한 자바스크립트 파일을 다시 서버에 요청. 이 자바스크립트 파일 안에는 클라이언트 로직과 HTML을 화면에 그려주는(렌더링) 코드가 들어있다.
            </li>
            <li>HTTP API - 데이터 요청<br>
                웹 브라우저가 다운로드한 자바스크립트 코드(클라이언트 로직)를 실행하여, 필요한 데이터를 서버에 HTTP API 형태로 요청<br>
                서버는 데이터베이스(DB)에서 주문 정보를 조회한 후, 요청된 데이터를 JSON 형식으로 클라이언트에 응답 (예:{"상품명":"A", "가격":10000}).
            </li>
            <li>자바스크립트로 HTML 결과 렌더링<br>
                웹 브라우저는 서버로부터 받은 JSON 데이터를 이용하여, 2단계에서 가져온 자바스크립트 렌더링 코드를 실행<br>
                이 과정을 통해 최종적인 HTML이 동적으로 생성되고, 웹 브라우저 화면에 표시
            </li>
          </ol>
      </details>

### 서블릿
- HttpServletRequest - HTTP 요청 데이터 처리
  - HttpServletRequest는 서블릿이 파싱한 HTTP 요청 메시지 정보를 담고 있는 객체.
    - start-line 정보조회, 헤더 정보조회
    - 부가기능 : 요청이 유지되는 동안 데이터를 저장하는 임시 저장소(request.setAttribute()) 및 세션 관리(request.getSession()) 기능
  - GET - 쿼리 파라미터 : **URL**에 ?username=hello&age=20와 같이 데이터를 포함하여 전송. request.getParameter("username")으로 값을 조회할 수 있다. 
  - POST - HTML Form : **메시지 바디**에 username=hello&age=20 형식으로 데이터를 담아 전송. GET 방식과 동일하게 request.getParameter()로 조회할 수 있다. 
  - API - **메시지 바디** (단순 텍스트, JSON)
    - 단순 텍스트 : request.getInputStream()으로 바디 데이터를 읽고, StreamUtils.copyToString()을 사용해 문자열로 변환. 
    - JSON : 텍스트와 동일하게 읽은 후, Jackson 라이브러리의 ObjectMapper를 사용해 JSON 문자열을 자바 객체로 변환.
- HttpServletResponse - HTTP 응답 데이터 생성 
  - HttpServletResponse는 HTTP 응답 메시지를 생성하는 역할
    - 상태 코드, 헤더, 바디, 편의 기능(Content-Type, 쿠키, Redirect)
  - 단순 텍스트 및 HTML : Content-Type을 각각 text/plain 또는 text/html로 설정하고, PrintWriter를 통해 응답 내용을 작성 
  - API - JSON : Content-Type을 application/json으로 설정하고, ObjectMapper를 사용하여 자바 객체를 JSON 문자열로 변환한 뒤 응답으로 전송. application/json은 기본적으로 UTF-8을 사용, 별도의 charset 설정은 불필요.

### Java 웹 개발의 기술적 진화 과정, 스프링 MVC 프레임워크

#### 원시적 개발 방식과 그 한계 (서블릿, JSP)

- 서블릿(Servlet)을 이용한 개발
  - Java 코드만으로 웹 페이지의 요청 처리와 HTML 응답 생성을 모두 담당
- JSP
  - HTML을 중심으로 하고, 동적인 처리가 필요한 부분에만 Java 코드(스크립틀릿 <% %>, 표현식 <%= %>)를 삽입
  - JSP 파일 하나에 HTML(View), Java 비즈니스 로직, 데이터베이스 접근 로직(Repository 호출)이 모두 섞여 있게 된다

#### 역할 분리를 통한 구조 개선 (MVC 패턴)

- MVC 패턴 적용 
  - 사용자 요청이 컨트롤러 서블릿으로 들어옵니다 (/servlet-mvc/members/save). 
  - 컨트롤러는 요청 파라미터를 받고, 비즈니스 로직(회원 저장)을 처리 
  - 처리 결과를 request.setAttribute("member", member)와 같이 Model(request)에 담는다
  - RequestDispatcher.forward()를 사용해 화면을 그릴 JSP(View)로 제어권을 넘긴다. 이때 request, response 객체가 함께 전달 
  - JSP는 ${member.id}와 같은 EL(Expression Language)을 사용해 Model에 담긴 데이터를 꺼내 동적인 HTML을 생성
- MVC 패턴의 한계
  - MVC 패턴 적용으로 역할 분리는 이루어졌지만, 여전히 다음과 같은 문제들이 남음 
  - 포워드(forward) 로직 중복 : 모든 컨트롤러마다 View로 이동하기 위한 RequestDispatcher 코드가 반복적으로 나타난다
  - View Path 중복 : View의 경로("/WEB-INF/views/...")를 매번 문자열로 작성해야 한다
  - 불필요한 객체 사용 : service() 메서드는 항상 HttpServletRequest, HttpServletResponse를 받지만, 실제로는 사용하지 않는 경우도 있다
  - 공통 처리의 어려움 : 인증, 인코딩 등 여러 컨트롤러에서 공통으로 처리해야 할 로직을 넣을 "수문장" 역할이 없다

#### 프레임워크 직접 만들기 (Front Controller 패턴)

- v1 : 프론트 컨트롤러 도입
- v2: View 분리
  - View 포워딩 로직을 MyView라는 객체로 분리. MyView는 render() 메서드를 통해 실제 포워딩을 담당
  - 컨트롤러는 이제 MyView 객체를 생성해서 반환하는 역할만 담당
- v3: Model 추가 및 서블릿 종속성 제거
  - 컨트롤러가 서블릿 기술을 전혀 사용하지 않도록 만든다
    - 요청 파라미터는 프론트 컨트롤러가 Map<String, String> 형태로 가공해서 전달
    - 결과 데이터는 ModelView 라는 별도의 객체에 담아 반환. 이 객체는 논리적 뷰 이름과 모델 데이터(Map)를 가진다
  - 프론트 컨트롤러는 다음 역할을 추가로 수행
    - viewResolver(): 컨트롤러가 반환한 논리적 뷰 이름(new-form)을 실제 물리 경로 (/WEB-INF/views/new-form.jsp)로 변환
    - modelToRequestAttribute(): ModelView의 모델 데이터를 request.setAttribute()를 통해 request 객체에 옮겨 담아 JSP가 사용할 수 있게 한다
- v4: 실용적인 컨트롤러
  - v3 방식은 컨트롤러 개발자가 매번 ModelView 객체를 생성해야 하는 번거로움이 있었다
    - 컨트롤러는 뷰의 논리 이름(String)을 바로 반환
    - 모델 데이터는 파라미터로 전달된 model에 담는다
  - 프론트 컨트롤러가 모델 객체를 생성해서 컨트롤러에 전달해주므로, 개발자는 더 편리하게 핵심 로직에만 집중
- v5: 유연한 컨트롤러
  - 목표: v3, v4 등 다양한 방식의 컨트롤러를 모두 지원하는 유연한 프레임워크를 만든다
  - **어댑터 패턴(Adapter Pattern)** 도입
    - MyHandlerAdapter 인터페이스를 정의. 어댑터는 특정 컨트롤러(핸들러)를 처리할 수 있는지(supports()) 확인하고, 실제 실행(handle())하는 역할을 한다
    - handle() 메서드는 어떤 방식의 컨트롤러든 처리한 후, 결과를 표준화된 ModelView 객체로 변환하여 반환
  - 결과: 이제 새로운 타입의 컨트롤러를 추가하고 싶으면, 그에 맞는 어댑터만 추가하면 된다.

#### 스프링 MVC 구조 이해
- 스프링 MVC의 동작 순서 (DispatcherServlet의 doDispatch 메서드)핸들러
  - 조회 : HandlerMapping을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회 
  - 핸들러 어댑터 조회 : 조회된 핸들러를 실행할 수 있는 HandlerAdapter를 찾는다 
  - 핸들러 어댑터 실행 : HandlerAdapter가 실제 핸들러를 실행 
  - ModelAndView 반환 : 핸들러의 실행 결과(예: String 뷰 이름, Model 객체)를 HandlerAdapter가 ModelAndView 객체로 변환하여 DispatcherServlet에 반환 
  - 뷰 리졸버 호출 : ModelAndView에 담긴 뷰 이름을 ViewResolver에 전달하여 처리할 View 객체를 찾음
  - 뷰 렌더링 : View 객체의 render() 메서드를 호출하여 최종 응답 화면을 생성

### 스프링 MVC 기본 기능

#### 요청 파라미터 조회 (@RequestParam)
- GET 방식의 쿼리 파라미터와 POST 방식의 HTML Form 데이터를 조회할 때 사용
- 생략해서 사용 가능하나 비권장
- required: 파라미터 필수 여부를 지정(기본값 true). false로 설정하고 값이 넘어오지 않으면 null이 입력되므로, 기본형(int) 대신 래퍼 타입(Integer)을 사용해야 한다.
  - 래퍼타입 아닐 시 500 에러 발생
- defaultValue: 파라미터 값이 없거나 빈 문자열일 경우 사용할 기본값을 지정. defaultValue가 설정되면 required 속성은 의미가 없어진다 

#### 요청 파라미터 객체 바인딩 (@ModelAttribute)
- 모델(Model)에 @ModelAttribute 로 지정한 객체를 자동으로 넣어 준다
  - 객체 생성, setter를 호출해서 파라미터의 값을 입력 까지
  - 사용하는 객체에 기본 생성자, setter 필요(프로퍼티 규약에 맞는 이름). getter는 보통 사용할 일 많으므로 같이 선언해주자
- 생략해서 사용 가능하나 비권장

#### HTTP 메시지 바디 조회 (단순 텍스트)
- HTTP 메시지 바디에 담긴 순수 텍스트 데이터는 @RequestParam이나 @ModelAttribute로 조회할 수 없습니다.
- InputStream / Reader: 서블릿에서처럼 요청의 스트림을 직접 읽을 수 있다. 
- HttpEntity<String>: HTTP 헤더와 바디 정보를 함께 조회할 수 있는 객체입니다. 
- @RequestBody: 가장 편리한 방법으로, 이 애노테이션을 사용하면 메시지 바디의 내용을 문자열로 직접 변환하여 받을 수 있다.

#### HTTP 메시지 바디 조회 (JSON)
- API 통신에서 가장 보편적인 JSON 데이터를 조회하는 방법 
- @RequestBody + 객체: @RequestBody HelloData data와 같이 사용하면, 스프링의 HTTP 메시지 컨버터가 JSON 데이터를 분석하여 지정된 HelloData 객체로 자동 변환(역직렬화)
  - 기본생성자, getter/setter 중 하나 필요.
- @RequestBody는 생략할 수 없다. 만약 생략하면 @ModelAttribute가 적용되어 HTTP 메시지 바디가 아닌 요청 파라미터를 조회.

#### HTTP 응답 데이터 API
- @ResponseBody : 메소드의 반환값을 뷰 템플릿이 아닌 HTTP 응답 바디에 직접 쓰도록 지시합니다. HTTP 메시지 컨버터가 동작하여 자바 객체를 JSON 같은 문자열로 직렬화 
- ResponseEntity<T> : @ResponseBody의 기능에 더해, HTTP 상태 코드(e.g., 201 CREATED)나 응답 헤더를 동적으로 설정할 수 있어 더 세밀한 제어가 가능

#### HTTP 메시지 컨버터 (HttpMessageConverter)
- 스프링 MVC는 다음의 경우에 HTTP 메시지 컨버터를 적용한다.
  - HTTP 요청: @RequestBody , HttpEntity(RequestEntity)
  - HTTP 응답: @ResponseBody , HttpEntity(ResponseEntity)
  - viewResolver 대신 동작
- 주요 구현체: 스프링 부트는 다양한 메시지 컨버터를 내장하고 있으며, 조건에 맞는 것을 우선순위에 따라 사용
  - ByteArrayHttpMessageConverter: byte[] 데이터 처리 
  - StringHttpMessageConverter: String 데이터 처리 
  - MappingJackson2HttpMessageConverter: JSON 데이터 처리 (내부적으로 Jackson 라이브러리 사용)
- 동작 원리
  - canRead() (요청 시) : 컨트롤러 파라미터 타입과 요청의 Content-Type 헤더를 보고 처리 가능 여부를 판단
  - canWrite() (응답 시) : 메소드 반환 객체 타입과 요청의 Accept 헤더를 보고 처리 가능 여부를 판단

#### 스프링 MVC 내부 동작 : RequestMappingHandlerAdapter
- @RequestMapping 을 처리하는 핸들러 어댑터인 RequestMappingHandlerAdapter (요청 매핑 헨들러 어뎁터)
- ArgumentResolver (인자 해석기) : 컨트롤러 메소드가 필요로 하는 다양한 파라미터(e.g., HttpServletRequest, @RequestParam, @RequestBody, Model)를 생성해주는 역할을 한다. @RequestBody를 처리하는 ArgumentResolver가 바로 HttpMessageConverter를 사용하여 요청 바디를 객체로 만든다.
- ReturnValueHandler (반환값 처리기) : 컨트롤러 메소드의 반환값을 처리. String을 반환하면 뷰 이름으로 해석하고, @ResponseBody가 붙은 객체를 반환하면 HttpMessageConverter를 사용해 응답 바디를 생성
- 기능 확장은 WebMvcConfigurer 를 상속 받아서 스프링 빈으로 등록 (별로 할일 없음)

### POST REDIRECT GET 

- PRG 패턴이 필요한 이유 - 중복 등록 문제
  - 사용자가 웹 페이지에서 상품 등록 폼에 데이터를 입력하고 '저장' 버튼을 누르면, 브라우저는 서버로 POST 요청을 보낸다
  - 서버는 이 데이터를 받아 데이터베이스에 저장하고, 사용자에게는 상품 상세 정보와 같은 결과 페이지를 보여준다 
  - 문제는 이 상태에서 사용자가 웹 브라우저의 새로고침 버튼을 누를 때 발생
  - 새로고침은 마지막에 서버로 보냈던 요청을 그대로 다시 보내는 동작
  - 이 경우, 마지막 요청은 상품 데이터를 포함한 POST 요청이었기 때문에, 동일한 데이터가 서버로 한 번 더 전송.
  - 그 결과, 내용은 같지만 ID만 다른 상품이 데이터베이스에 중복으로 쌓이게 됩니다. 
- PRG 패턴의 해결 방식
  - POST 로직 처리가 끝나면, 서버는 렌더링할 HTML 뷰(View)를 직접 반환하는 대신, 클라이언트(웹 브라우저)에게 다른 URL로 이동하라는 리다이렉트(Redirect) 응답을 보낸다.
