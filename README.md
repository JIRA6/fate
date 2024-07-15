# 프로젝트 소개

<img src="https://github.com/JIRA6/fate/blob/dev/readme_image/지라로고.png" alt="JIRA 로고" width="50%">

- 프로젝트 명 : JIRA
- 소개 : 프로젝트 협업 도구인 JIRA의 주요기능인 칸반 보드 기능 구현

## 주요 기능
- **보드 관리** : 프로젝트 보드를 생성, 수정, 삭제할 수 있습니다.
- **카드 관리** : 카드(이슈)를 생성, 조회, 수정, 삭제할 수 있습니다.
- **컬럼 관리** : 컬럼을 생성, 수정, 삭제할 수 있으며, 컬럼 간 카드 이동이 가능합니다.
- **사용자 관리** : 사용자 등록 및 인증(JWT, Security)을 구현합니다.
- **댓글 기능** : 카드 내 댓글을 생성, 조회할 수 있습니다.
- **초대 기능** : 프로젝트 보드에 사용자를 초대할 수 있습니다.

## 사용기술

| 분야         | 기술           |
| ------------ | -------------- |
| 프론트엔드   | React, React Router, React DnD, Axios |
| 백엔드       | Spring Boot, JPA, MySQL |
| 인증         | JWT, Spring Security |

## 개발환경
- **IDE** : IntelliJ IDEA
- **JDK** : 17
- **Spring** : 3.1.0
- **DB** : MySQL

<br>

***

<br>

# 📑 Technical Documentation

## ⚙ 와이어프레임

![와이어프레임_전체](https://github.com/JIRA6/fate/blob/dev/readme_image/와이어프레임_전체.png)

<details>
<summary>와이어프레임_로그인 & 회원가입</summary>

![와이어프레임_로그인 & 회원가입](https://github.com/JIRA6/fate/blob/dev/readme_image/와이어프레임1_로그인%20&%20회원가입.png)

<details>
<summary>와이어프레임_메인페이지</summary>

![와이어프레임_메인페이지](https://github.com/JIRA6/fate/blob/dev/readme_image/와이어프레임2_메인페이지.png)

</details>

</details>

<details>
<summary>와이어프레임_보드상세페이지</summary>

![와이어프레임_보드상세페이지](https://github.com/JIRA6/fate/blob/dev/readme_image/와이어프레임3_보드상세페이지.png)

</details>

<details>
<summary>와이어프레임_카드상세페이지</summary>

![와이어프레임_카드상세페이지](https://github.com/JIRA6/fate/blob/dev/readme_image/와이어프레임4_카드상세페이지.png)

</details>

### 🧬 ERD DIAGRAM

![ERD](https://github.com/JIRA6/fate/blob/dev/readme_image/ERD.png)

### 🔧 API 명세서

![기능담당1](https://github.com/JIRA6/fate/blob/dev/readme_image/담당1_유균한.png)

![기능담당2](https://github.com/JIRA6/fate/blob/dev/readme_image/담당2_박태순.png)

![기능담당3](https://github.com/JIRA6/fate/blob/dev/readme_image/담당3_장경진.png)

![기능담당4](https://github.com/JIRA6/fate/blob/dev/readme_image/담당4_최영주.png)


<br>

***

<br>

# 👨‍👩‍👧‍👧 팀 소개 및 규칙

## 팀원 소개

| 담당자 | 역할 |
| ------ | ---- |
| <a href="https://github.com/ysy56"><img src="https://avatars.githubusercontent.com/u/78634780?v=4" alt="최영주" width="100"/><br><div align="center">최영주</div> </a> | - 카드 관리 기능 구현 (생성, 조회, 수정, 삭제) <br> - 카드 이동 기능 구현 <br> - 카드 내부 댓글 기능 구현 (생성, 조회) |
| <a href="https://github.com/ryurbsgks5114"><img src="https://avatars.githubusercontent.com/u/165640275?v=4" alt="유균한" width="100"/><br><div align="center">유균한</div> </a> | - 사용자 기능 구현 <br> - JWT 기능 구현 <br> - Security 기능 구현 |
| <a href="https://github.com/marlboro09"><img src="https://avatars.githubusercontent.com/u/165752968?v=4" alt="박태순" width="100"/><br><div align="center">박태순</div> </a> | - 보드 관리 기능 구현 <br> - 보드 초대 기능 구현 |
| <a href="https://github.com/jinny7"><img src="https://avatars.githubusercontent.com/u/152242318?v=4" alt="장경진" width="100"/><br><div align="center">장경진</div> </a> | - 컬럼 CRUD 기능 구현 <br> - 컬럼 이동 기능 구현 |

## Code Convention
- 인텔리제이 구글 코드 컨벤션 적용
- package 정리 : 도메인 vs 계층형 → 도메인
- ResponseEntity (CommenResponse 사용) +Json
- 생성자 vs 빌더? → 빌더
- yml vs properties? → yml
- 생성자로 결정될 경우 : 어노테이션(빌더) vs 직접 작성 → 빌더이므로 어노테이션 추가
- naming (controller, service, DB table)
<br>
> **Controller & Service Method**
> - getAll - 전체
> - get - 단건
> - create - 생성
> - update - 수정
> - delete - 삭제
<br>
> **DB Table**
> - 소문자, 띄어쓰기는 _로 대체 (@Table(name = “(원래의 이름)_table”)

## GitHub Rules
### ✔ GitHub Commit Rules

| 작업 타입 | 작업 내용 |
| --------- | ---------- |
| 🎉 feat   | 없던 파일을 생성함, 초기 세팅 해당 파일에 새로운 기능이 생김 |
| 🐛 bugfix | 버그 수정 |
| 🔨 refactor | 코드 리팩토링 |
| 🩹 fix    | 코드 수정 |
| 🚚 move   | 파일 옮김/정리 |
| 🗑️ del    | 기능/파일을 삭제 |
| 🧪 test   | 테스트 코드를 작성 |
| 🎨 style  | CSS |
| 🛠️ gitfix | gitignore 수정 |
| 📦 script | package.json 변경(npm 설치 등) |

### ✔ Branch Rule
- 이슈 사용
- 기능 개발 완료시 dev 브랜치로 merge

### ✔ Github branch naming

- 작업타입/#이슈 기능

> ex) feat/#1 user

### ✔ PR title naming

- [날짜] 브랜치명 >> 작업내용

> ex) [2024.06.20] feat/#10 admin-user >> 백오피스 유저 기능 추가

### ✔ PR & merge rules

1. pr 후 슬랙에 남기기
> ex) pr 링크와 간단한 내용
2. pr 본 사람은 github 답글 남기기
3. 1번째 본 사람이 답글 후 merge
4. merge 후 슬랙에 남기기
> ex) 누구의 어떤 내용의 pr을 merge 했는지

## 트러블 슈팅
 ☞ [구경하기⚽](https://teamsparta.notion.site/1a5e7ab60f9d4e1cabc6329901c2e3bd)
