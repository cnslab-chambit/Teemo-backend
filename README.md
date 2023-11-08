# Teemo-backend

`#📍위치기반 근거리 만남주선 서비스` `#🎓광운대학교 2023 소프트웨어학과 졸업작품`

## 🔥 프로젝트 소개

<div align=center>
  
![APP_Logo](https://github.com/cnslab-chambit/Teemo_backend/assets/50646145/dbd2675e-af63-4b4c-af43-3415e758a459)

</div>

### 프로젝트 명 :  
  - TagTalk

### 프로젝트 기간 : 
  - 2023.04 ~ 2023.10

### 담당자
  - BackEnd - [이윤신](https://github.com/TransparentDeveloper)
  - FrontEnd - [권한결](https://github.com/gosari0123)

### 프로젝트 배경 :
  1. 불특정 다수에게 사용자의 최근 위치가 노출되는 (위치기반) 서비스 들의 문제점 인식
  2. K-Anonymity, Privarcy Protection Model 을 서비스에 적용


### 프로젝트 목표 :
  1. 불특정 다수에게 노출되는 위치와 Host 의 실제 위치를 분리하여, 위치공유에 대한 안전한 솔루션 제공
  2. 오프라인 커뮤니티 권장을 위해 근거리에서 빠른 만남이 가능한 플렛폼 제공

<br/>
<br/>

## 🍀 개발환경

### 1) 주요 환경
- Java 17
- SprintBoot 3.0.6
- Gradle 7.6.1
- IntelliJ IDEA

## 2) Depandency
- Spring Web
- Spring Data JPA
- WebSocket 
- MySQL Driver
- Lombok

## 3) etc.
- Git
- GitHub

<br/>
<br/>

## 📡 API 상세:
- [ 📄 wiki를 참고해주세요. ](https://github.com/cnslab-chambit/Teemo_backend/wiki/%5BUI%5D)

<br/>
<br/>

## 😃 사용자 요구사항:
_※ [ 📚 용어사전을 참고해주세요. ](https://github.com/cnslab-chambit/Teemo_backend/edit/APIServer(version-1.2.1)/root/README.md#%EC%9A%A9%EC%96%B4-%EC%82%AC%EC%A0%84)_

  - 로그인을 마친 모든 사용자는 Viewer 이다.
  - 모든 사용자의 계정 정보는 6개월 뒤에 파기되어야 한다.
  - 로그아웃 시, 모든 사용자는 해당 사용자와 관련된 Tag 와의 연관성을 잃는다.
  - 모든 Viewer 는, Tag 의 모임조건(나이대, 성별) 에 일치하는 Tag 만 확인할 수 있다.
  - 특정 Tag 를 구독 시, 사용자는 해당 Tag 의 Guest 가 된다.

  <br/>
  
  - Tag 는 Host에 의해 생성된다.
  - Tag 의 유효 시간은 1시간이다.
  - 모든 사용자는 단 하나의 Tag 만 구독할 수 있다.
  - Tag 의 위치는 실제 Host 의 위치로부터 반경 500m 이내로 설정되어야 한다.
  - Host 가 자신이 게시한 Tag의 위치로부터 반경 500m 를 벗어날 경우, 해당 Tag 는 삭제되어야 한다.
  - Tag 업로드 시, Host가 설정할 수 있는 모임의 나이대와 성별 조건에는 반드시 Host 의 나이와 성별을 포함할 수 있어야 한다.
    + Host 나이: 20 ＆ 모임조건(나이대): 10~19 => ❌
    + Host 성별: 남 ＆ 모임조건(성별): 여 => ❌

  <br/>

  - Host - Guest 간, 실제 위치 정보에 대한 정보교환이 이뤄지기 위해서 두 사용자 간에 1:1 Chatroom 이 개설되어야 한다.
  - Chatroom 의 유효시간은 Tag 업로드 기준, 2시간이다.
  - Host 는 여러 Guest 를 상대로 1:1 Chatroom 을 다수 (최대 10) 가질 수 있다.
  - Guest 는 Host 와의 1:1 Chatroom 단 하나를 가질 수 있다.
  - Chatroom 에서 전달되는 data 의 형태는 text 뿐이다.
  - Host 에 의해 Host 의 위치를 특정 Guest 에게 전달할 수 있고, 해당 Guest 와의 채팅을 차단할 수 있다.
  - Host 가 Tag 를 삭제하거나 Guest 가 해당 Tag의 구독을 취소 할 경우, Chatroom 이 제거된다.

<br/>
<br/>

    
## ETC

### 용어 사전:
1. Tag
    - 모집, 만남 에 대한 목적과 조건 정보를 담은 게시물
2. Host
    - Tag 를 게시한 사용자
3. Guest
    - 특정 Tag 를 확인하고, 향하는 사용자  
4. Viewer
    - Host, Guest 가 아닌 사용자

<br/>

### UI 설계 
- [ 🎨 wiki를 참고해주세요. ](https://github.com/cnslab-chambit/Teemo_backend/wiki/%5BUI%5D)

