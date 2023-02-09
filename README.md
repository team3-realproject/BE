# Alba Poket Project

![image](https://user-images.githubusercontent.com/117805482/217720232-1199e879-590e-44ce-8074-f77ba68addf0.png)

## 나만의 알바 일지와 알바생 커뮤니티

## 📆 개발 기간  
2022년 12월 30일 ~ 2023년 02월 10일   
<p>

## 👥 팀 소개
#### `Frontend`
 <a href="https://github.com/headwing" target="_blank"><img height="40"  src="https://img.shields.io/static/v1?label=React&message=최유정 &color=61dafb&style=for-the-badge&>"/></a>
 <a href="https://github.com/soomin-world" target="_blank"><img height="40"  src="https://img.shields.io/static/v1?label=React&message=오수민 &color=61dafb&style=for-the-badge&>"/></a>


#### `Backend`
<a href="https://github.com/woooo96" target="_blank"><img height="40"  src="https://img.shields.io/static/v1?label=Spring&message=최진우 &color=08CE5D&style=for-the-badge&>"/></a>
<a href="https://github.com/wogk9854" target="_blank"><img height="40"  src="https://img.shields.io/static/v1?label=Spring&message=최재하 &color=08CE5D&style=for-the-badge&>"/></a>
<a href="https://github.com/kwon-sunghyun" target="_blank"><img height="40"  src="https://img.shields.io/static/v1?label=Spring&message=권성현 &color=08CE5D&style=for-the-badge&>"/></a>


## 서비스 배경
+ 알바생 중 26.9%는 한번에 2개 이상의 알바를 병행한다.
+ 알바 주기별로 보면 1년 내내 알바를 하는 알바생들의 경우 41.5%가 2개 이상의 알바를 동시에 하고 있다.
+ 알바생 10명 중 3명 정도는 아르바이트하면서 부당대우를 당했던 경험이 있는 것으로 나타났으며, 부당대우로는 임금체불이 가장 많았다.

## 목적 및 기대효과
+ 월 또는 주 단위로 자신이 받을 금액을 미리 계산하고 확인할 수 있다.
+ 알하면서 생긴 궁금증을 커뮤니티를 통해 해결할 수 있다.
+ 알바 관련 고민을 해결할 수 있고, 알바 Tip을 알 수 있다.
+ 급하게 알바를 못 하게 되었거나 대타가 필요할 때 대신 알바 해줄 사람을 찾을 수 있다.
+ 자신의 근무 일정을 쉽고 간편하게 관리할 수 있다.
+ 임금체불 신고 시 증거로 사용될 수 있다.

## 컨셉
+ 심플하고 직관적인 UI를 통한 스케줄 관리 및 급여 확인
+ 반응형 웹을 통해 접속한 기기에 맞게 해상도와 레이아웃이 유동적으로 변하게 한다.

## 서비스 타겟
+ 본인의 알바 시간을 정리해 기록 하고싶은 알바생
+ 알바생들끼리의 커뮤니티를 이용하고 싶은 알바생
<br>


## BE 기술스택
<div align=center> 
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/Springjpa-4FC08D?style=for-the-badge&logo=jpa&logoColor=white"> 
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <br>
 
  <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
  <img src="https://img.shields.io/badge/amazon rds-61DAFB?style=for-the-badge&logo=amazonrds&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">
  <img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
  <img src="https://img.shields.io/badge/Apache JMeter-D22128?style=for-the-badge&logo=Apache JMeter&logoColor=white">
 
 
</div>
<br>

## 주요 기능
+ 근무지와 근무 일정 등록 : 아르바이트 스케줄 관리 기능
+ 통계 페이지 : 근무지별 근무 시간 / 최근 5개월 간 급여 출력
+ 게시판 서비스 : 아르바이트생들의 커뮤니티 구성
+ 채팅 서비스 : WebSocket통신을 이용한 채팅 서비스 구성
+ 실시간 알림 : SSE통신을 활용한 실시간 알림 기능 구성
+ 소셜 로그인 : Kakao 소셜 로그인 구성

<br>

## BE서비스 아키텍처
<img width="512" alt="image" src="https://user-images.githubusercontent.com/117805482/217736206-42327332-5d98-4b85-84c9-639ce195499d.png"><br>


## 기술적 의사결정
<details>
<summary> Docker / Jenkins 를 이용한 CICD 환경 구축 </summary>
<div markdown="1">  
<br>
1. Docker 사용이유<br>
    - Docker를 사용하면 어플리케이션을 구동하기 위한 최소한의 환경으로 이미지를 생성하고, <br>  Docker만 설치되어 있다면 해당 이미지를 환경적인 제약 없이 컨테이너로 구동하는것이 가능<br>

<br>
2. Jenkins 사용이유<br>
    - 프로젝트 표준 컴파일 환경에서의 컴파일 오류 검출 가능<br>
    - jenkins를 사용하기 위해서는 별도의 서버가 필요하지만 현업에서 널리 사용되는 jenkins를 경험해보기 위해<br>
  
<br>
3. Jenkins Container Local 환경 구축 이유<br>
  - Jenkins를 운영하기 위해서는 별도의 서버가 필요하지만, 사용하고 있던 ec2 프리티어 스펙으로는 운영이 불가능 -> 로컬환경에서 jenkins container를 실행 후 공유기 포트포워딩을 통해 
</div>
</details>

<details>
<summary> QueryDsl </summary>
<div markdown="1">
<br>   
- 쿼리 성능 개선 : 기존 로직에서 엔티티 fetch type을 lazy로 설정하여 연관된 객체를 spring data jpa를 통해 select 할 때 N+1 문제가 발생했고, DTO에 반환되어야 할 정보를 추가적으로 select하는 로직이어서 쿼리량이 방대하고 성능적으로 문제가 생김.<br>
이를 해결하기 위해서 querydsl를 사용하여 연관된 객체 정보를 같이 select 하여 한번에 처리하도록 변경 -> 쿼리량을 감소시키고 성능적으로도 쿼리 수행속도를 1/5로 개선 <br>
<br>
- 동적 쿼리 : querydsl을 사용하여 쿼리의 조건을 동적으로 처리 <br>
</div>
</details>

<details>
<summary> WebSocket </summary>
<div markdown="1">
<br>   
- 채팅 : 채팅서비스를 구현하기 위해서는 Server-Client 가 연결되어 실시간으로 데이터를 주고 받을 수 있어야 하기 때문에, WebSocket 통신을 이용한 실시간 채팅 기능 구현<br>
<br>
</div>
</details>


<details>
<summary> SSE </summary>
<div markdown="1">
<br>   
- 단방향 통신 : Websocket의 경우 양방향 통신이지만, 연결이 된 이후 서버쪽에서만 클라이언트로 데이터를 보내주면 되기 때문에 단방향 통신인 SSE를 사용하고 http 프로토콜을 사용하기 때문에 websocket 통신보다 가벼움<br>
<br>
</div>
</details>

<details>
<summary> Jasypt </summary>
<div markdown="1">
<br>   
- 보안성 증대 : github에 push하는 데이터 중 DB 정보 / AWS 정보 등 공개되지 말아야 할 데이터를 암호화 하기 위해 Jasypt 통해 암호화 진행<br>
<br>
</div>
</details>
<br>

## 트러블 슈팅
<details>
<summary> SSE 알림 로직에서 Hikari Pool Connection is not available 에러 </summary>
<div markdown="1">
<br>   
- 클라이언트와 Sse 통신하기 위해 SseEmitter 생성하는 로직에 DB에서 데이터를 가져오는 코드가 있었는데 Sse 통신의 경우 세션이 끊어지는 것이 아니어서 SseEmitter를 생성하기 위한 get 요청이 들어올때마다 DB에 연결된 커넥션 수가 늘어남. <br>
  - get 요청이 HikariPool이 MAX 임계치와 동일하게 요청이 왔을 경우 api 요청이 불가능 해지는 현상 발생 <br>
  - SseEmitter를 생성하는 로직에서 DB에서 데이터를 가져오는 코드를 제외 시켜서 해결 완료
<br>
</div>
</details>
<details>
<summary> http통신과 소켓통신의 차이 </summary>
<div markdown="1">
<br>   
- @MessageMapping을 사용할때 Class위에 @RequestMapping을 사용하여 공통된 URL처리를 해주고 있었는데 @RequestMapping이 적용 안되는 문제가 발생 <br>
  - @RequestMapping은 http통신이고 @MessageMapping은 ws://로 시작하는 소켓통신이여서 @RequestMapping으로 공통된URL을 설정해도 적용이 안되는걸 확인하고 @RequestMapping을 지워줌으로써 해결함. <br>
</div>
</details>

  <br>
  
## 성능개선

<details>
<summary> ex) 커뮤니티 전체 게시글 조회 </summary>
<div markdown="1">
<br>   
<br>
기존 수행 쿼리 <br> 
 
  ![image](https://user-images.githubusercontent.com/117805482/217813431-d95576c9-c31c-4b74-aa0d-db56dcd3d5c8.png)

<br>
jmeter 이용한 부하테스트 ( 1000명의 유저가 1초동안 2번씩 요청한 경우 ) <br>

 
 ![image](https://user-images.githubusercontent.com/117805482/217812516-b03c7df2-5d06-4765-a6fb-2579737ddf1c.png)<br>

 ![image](https://user-images.githubusercontent.com/117805482/217812569-fee3d904-be59-4679-91cb-4d645a05db9b.png)<br>

 ![image](https://user-images.githubusercontent.com/117805482/217812618-d1a9e301-c817-455c-8a95-a6b35dce5078.png)<br>
<br>
 <br>   
<br>
변경 수행 쿼리 <br> 
 
  ![image](https://user-images.githubusercontent.com/117805482/217807931-e0dbc0ae-753b-4c56-8f1c-138507d50871.png)<br>

<br>
jmeter 이용한 부하테스트 ( 1000명의 유저가 1초동안 2번씩 요청한 경우 ) <br>

 ![무한스크롤 쿼리 수정 후 (summary report)](https://user-images.githubusercontent.com/117805482/217845059-07e61abb-c204-4c8a-97f2-00328de8cdde.png)<br>

 ![무한스크롤 쿼리 수정 후 (Response Times Over Time)](https://user-images.githubusercontent.com/117805482/217845286-04f23d87-611f-411c-b705-0c7538ed5238.png)<br>
 
 ![무한스크롤 쿼리 수정 후 (Transactions per Second)](https://user-images.githubusercontent.com/117805482/217845202-9b0419a6-d8b2-4ad4-aaa9-7555cb8028a6.png)<br>
<br>
 
 
 
</div>
</details>






