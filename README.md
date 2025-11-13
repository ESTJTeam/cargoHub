# 1. Project Overview (프로젝트 개요)
- 프로젝트 이름: CargoHub
- 프로젝트 설명: 물류 관리 및 배송 시스템을 위한 MSA 기반 플랫폼

<br/>
<br/>

# 2. Team Members (팀원 및 팀 소개)
| 구대윤 | 김하정 | 김승중 | 전유림 | 김선우 |
|:------:|:------:|:------:|:------:|:------:|
| <img src="https://avatars.githubusercontent.com/u/92318119?v=4" alt="구대윤" width="150"> | <img src="https://avatars.githubusercontent.com/u/198740308?v=4" alt="김하정" width="150"> | <img src="https://avatars.githubusercontent.com/u/53886275?v=4" alt="김승중" width="150"> | <img src="https://avatars.githubusercontent.com/u/113421377?v=4" alt="전유림" width="150"> |<img src="https://avatars.githubusercontent.com/u/80505704?v=4" alt="김선우" width="150"> |
| 팀장 | 서기 | 코드관리 | 문서관리 | 발표자 |
| [GitHub](https://github.com/kookong2) | [GitHub](https://github.com/mueiso) | [GitHub](https://github.com/kimsj0970) | [GitHub](https://github.com/Jyurim) | [GitHub](https://github.com/mathos6147) |

<br/>
<br/>

# 3. Key Features (주요 기능)
- **회원 Context**:
    - ApiGateway에서 인증과 기본 인가를 담당하고, 세부 인가는 각 서비스가 처리하도록 함.
    - 배송 담당자 관리를 통한 배송 서비스 관리

- **허브 Context**:
    - 허브 데이터 관리 기능 개발
    - Hub to Hub Relay 경로 최적화 알고리즘 기반 허브간 이동정보 조회 기능 구현

- **업체 Context**:
    - 업체 데이터 관리 기능 개발

- **배송 Context**:
    - 주문 기준 오케스트레이션 기반의 업체/허브 배송 프로세스 관리
    - 업체/허브 배송 데이터 관리 기능 개발

- **상품 Context**:
    - 주문 기준 오케스트레이션 기반의 상품 재고 관리
    - 상품 데이터 관리 기능 개발

- **주문 Context**:
    - 주문 데이터 관리 기능 개발

- **외부 Context**:
    - 슬랙 메세지 관리 및 슬랙 메세지 로그 데이터 관리
    - 주문 기준 AI 배송 데이터 전송 관리 및 장애 데이터 복구
