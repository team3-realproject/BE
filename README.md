# Alba Poket Project

![image](https://user-images.githubusercontent.com/117805482/217720232-1199e879-590e-44ce-8074-f77ba68addf0.png)

## 나만의 알바 일지와 알바생 커뮤니티

### 서비스 배경
+ 알바생 중 26.9%는 한번에 2개 이상의 알바를 병행한다.
+ 알바 주기별로 보면 1년 내내 알바를 하는 알바생들의 경우 41.5%가 2개 이상의 알바를 동시에 하고 있다.
+ 알바생 10명 중 3명 정도는 아르바이트하면서 부당대우를 당했던 경험이 있는 것으로 나타났으며, 부당대우로는 임금체불이 가장 많았다.

### 목적 및 기대효과
+ 월 또는 주 단위로 자신이 받을 금액을 미리 계산하고 확인할 수 있다.
+ 알하면서 생긴 궁금증을 커뮤니티를 통해 해결할 수 있다.
+ 알바 관련 고민을 해결할 수 있고, 알바 Tip을 알 수 있다.
+ 급하게 알바를 못 하게 되었거나 대타가 필요할 때 대신 알바 해줄 사람을 찾을 수 있다.
+ 자신의 근무 일정을 쉽고 간편하게 관리할 수 있다.
+ 임금체불 신고 시 증거로 사용될 수 있다.

### 컨셉
+ 심플하고 직관적인 UI를 통한 스케줄 관리 및 급여 확인
+ 반응형 웹을 통해 접속한 기기에 맞게 해상도와 레이아웃이 유동적으로 변하게 한다.

### 서비스 타겟
+ 본인의 알바 시간을 정리해 기록 하고싶은 알바생
+ 알바생들끼리의 커뮤니티를 이용하고 싶은 알바생



### BE 기술스택
<div align=center> 
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/Springjpa-4FC08D?style=for-the-badge&logo=jpa&logoColor=white"> 
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <br>
 
  <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
  <img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">
  <img src="https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
</div>


### 주요 기능
+ 근무지와 근무 일정 등록 : 아르바이트 스케줄 관리 기능
+ 통계 페이지 : 근무지별 근무 시간 / 최근 5개월 간 급여 출력
+ 게시판 서비스 : 아르바이트생들의 커뮤니티 구성
+ 채팅 서비스 : WebSocket통신을 이용한 채팅 서비스 구성
+ 실시간 알림 : SSE통신을 활용한 실시간 알림 기능 구성
+ 소셜 로그인 : Kakao 소셜 로그인 구성


### BE서비스 아키텍처
<img width="524" alt="image" src="https://user-images.githubusercontent.com/117805482/217735997-68278b92-74c4-48fa-920d-49d1220ed865.png">



  
