# DevTask

프로젝트와 태스크를 관리할 수 있는 개인용 작업 관리 API입니다.

## 기술 스택

- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database
- JWT
- BCrypt
- Git / GitHub

## 주요 기능

- 회원가입 / 로그인
- JWT 기반 인증
- 내 정보 조회
- 프로젝트 CRUD
- 태스크 CRUD
- 태스크 검색 / 필터링
  - 키워드 검색
  - 상태 필터
  - 우선순위 필터
- 태스크 상태 관리
  - TODO
  - IN_PROGRESS
  - DONE
- 태스크 우선순위 관리
  - LOW
  - MEDIUM
  - HIGH
- 태스크 완료 / 다시 열기
- 프로젝트 완료율 계산
- 태스크 마감일 관리
- 마감일 빠른 순 정렬
- 오늘까지 해야 하는 미완료 태스크 조회

## API 목록

### Auth / User

| 기능 | Method | URL |
|---|---|---|
| 회원가입 | POST | `/api/users/signup` |
| 로그인 | POST | `/api/auth/login` |
| 내 정보 조회 | GET | `/api/users/me` |

### Project

| 기능 | Method | URL |
|---|---|---|
| 프로젝트 생성 | POST | `/api/projects` |
| 프로젝트 목록 조회 | GET | `/api/projects` |
| 프로젝트 단건 조회 | GET | `/api/projects/{projectId}` |
| 프로젝트 수정 | PUT | `/api/projects/{projectId}` |
| 프로젝트 삭제 | DELETE | `/api/projects/{projectId}` |

### Task

| 기능 | Method | URL |
|---|---|---|
| 태스크 생성 | POST | `/api/projects/{projectId}/tasks` |
| 태스크 목록 조회 | GET | `/api/projects/{projectId}/tasks` |
| 태스크 단건 조회 | GET | `/api/projects/{projectId}/tasks/{taskId}` |
| 태스크 수정 | PUT | `/api/projects/{projectId}/tasks/{taskId}` |
| 태스크 삭제 | DELETE | `/api/projects/{projectId}/tasks/{taskId}` |
| 태스크 완료 처리 | PATCH | `/api/projects/{projectId}/tasks/{taskId}/done` |
| 태스크 다시 열기 | PATCH | `/api/projects/{projectId}/tasks/{taskId}/reopen` |
| 오늘까지 해야 하는 미완료 태스크 조회 | GET | `/api/projects/{projectId}/tasks/today` |

### Task 검색 / 필터 예시

```http
GET /api/projects/{projectId}/tasks?keyword=로그인

GET /api/projects/{projectId}/tasks?status=TODO

GET /api/projects/{projectId}/tasks?priority=HIGH

GET /api/projects/{projectId}/tasks?keyword=로그인&status=TODO&priority=HIGH
```

## 인증 방식

로그인 성공 시 JWT 토큰을 발급받고, 인증이 필요한 요청에서는 아래 헤더를 사용합니다.

```http
Authorization: Bearer {token}
```

## 실행 환경

현재 개발 환경에서는 H2 인메모리 DB를 사용합니다.

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create
```

서버를 재시작하면 저장된 데이터는 초기화됩니다.