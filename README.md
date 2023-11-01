# WithPool-ScoreBoard

![README_banner](https://github.com/KwangWookHa/WithPool-ScoreBoard/assets/44091641/1964913d-589c-44cb-a196-f140ad6fbd7c)


## 💡 App Purpose
- **활동중인 포켓볼 동호회를 위한 오프라인 태블릿 스코어보드 App**  
<br>

## 🏆 무슨 성과를 거뒀나
- 회원간 경기 기록을 DB에 집계하고, 해당 통계를 이용한 이벤트를 진행하여 **월간 정기 모임 회원 참여도 약 60% 증가 (월 평균 18명 > 29명)**
   - 분기/반기별 이벤트 및 리그전을 총 5회 성공적으로 완료
- 도입 6개월 후, 앱 사용 만족도 평가 결과 **4.5점**/5점 기록
<br>

## ❓ 어떤 문제 상황이었나
- 제가 현재 활동중인 포켓볼 동호회는 활성 회원이 60명이고, 회원 전용 당구장을 운영하며, 월 평균 약 400경기가 진행되는 꽤 큰 규모의 동호회입니다.
- 그럼에도 불구하고 게임마다 사용해야하는 스코어보드가 한 장씩 넘기는 종이 형식이라서, 모든 회원들이 매 경기마다 불편함을 느끼고 있었습니다.
- 수많은 경기에도 불구하고, 데이터베이스가 없어 회원간 전적, 승률, 참여도 등의 통계가 없었습니다.
<br>

## 🛠️ 어떻게 해결했는가
- **원터치 방식의 디지털 스코어 보드 앱 개발**
  - 한 장씩 종이를 넘기던 아날로그 스코어보드의 불편함을 원터치 방식으로 개선
- **경기 종료 후 경기 기록 저장**
  - Firebase Firestore DB에 회원 간 경기 기록을 저장하여, 월 평균 400 경기 기록을 계속하여 집계하고 있음
<br>

## 🤚🏻 역할 (1인 프로젝트)
- 전체 프로젝트 기획 및 제안
- 안드로이드 앱 설계 및 개발
- 프로젝트 예산 확보 및 충전기 및 테블릿 구매
- 각 당구대에 태블릿과 앱을 설치하고, 사용 가이드를 제작하여 프로젝트를 성공적으로 완료
<br>

## ⚙️ App Architecture
[권장 앱 아키텍쳐](https://developer.android.com/topic/architecture?hl=ko#recommended-app-arch)에 따른 Model-View-ViewModel 아키텍쳐 패턴 적용

<img src="https://github.com/KwangWookHa/WithPool-ScoreBoard/assets/44091641/57c0c432-3dc8-41d6-9c6f-d9b8edefe0b0" width=480></img>


<br>

## 💻 Tech
- **`Kotlin`** Language
- 효율적인 비동기 프로그래밍을 위한 **`Kotlin Coroutine`**
- 의존성 주입을 위한 **`Hilt`**
- 화면 전환을 위한 **`Navigiation`**
- **`ViewModel`**, **`LiveData`**, **`DataBinding`** 을 통한 UI Layer 구성
- 이미지 및 애니메이션 표현을 위한 **`Glide`**, **`Lottie`**
- Database 구축을 위한 **`Firebase Firestore`**
- **`Trello`** 를 활용한 프로젝트 관리

<br>

## 📷 Screenshot

![home](https://github.com/KwangWookHa/WithPool-ScoreBoard/assets/44091641/41f940f0-cf7c-4adf-b0b3-a6796c9890c0)
  
![select_player](https://github.com/KwangWookHa/WithPool-ScoreBoard/assets/44091641/401a1c66-0e7a-4130-b0ff-cd5539364ae1)
  
![score_board](https://github.com/KwangWookHa/WithPool-ScoreBoard/assets/44091641/47cdedc7-acfb-4227-b033-5f32ace7e1c1)  
<br>

## 📺 Key Feature Demo Video

https://github.com/KwangWookHa/WithPool-ScoreBoard/assets/44091641/a481b667-5f52-428d-8007-6c8549981295
