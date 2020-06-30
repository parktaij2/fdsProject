# fdsProject
[[ 프로젝트 사용 기술 ]]
1. MAVEN 프로젝트
2. JAVA 언어
3. MYSQL 저장소
4. JSON-SIMPLE 포멧

[[ 테이블 설계 ]]
1. 거래로그와 같이 5개의 DB 테이블을 사용
2. 계좌 테이블에 추가로 잔고 컬럼을 생성
3. 테이블 설계서 : 데이터베이스 설계서 엑셀 파일 참고
4. DB 테이블 생성 : CRATE TABLE.sql 파일 참고

[[ 기능 설계 ]]
1. 가입
  - 고객명 입력
  - 입력한 고객명이 존재하는지 체크(존재한다면 고객번호를 출력하고 다시 기능선택으로 돌아감)
  - 고객번호 채번(고객번호 MAX값 + 1)
  - 생년월일 입력
  - 가입 테이블에 데이터 INSERT
  - 해당 고객번호로 룰 실행
  - 룰 처리 시간 출력

2. 계좌개설
  - 가입을 진행하지 않았다면 메시지 출력 후 다시 기능선택으로 돌아감
  - 고객번호로 계좌가 존재하는지 체크(존재한다면 계좌번호를 출력하고 다시 기능선택으로 돌아감)
  - 계좌번호 채번(계좌번호 MAX값 + 1)
  - 계좌 테이블에 데이터 INSERT
  - 해당 고객번호로 룰 실행
  - 룰 처리 시간 출력

3. 입금
  - 가입, 계좌개설을 진행하지 않았다면 메시지 출력 후 다시 기능선택으로 돌아감
  - 입금액 입력
  - 입금액 숫자여부 체크
  - 입금액이 0보타 클 때 입금 테이블에 데이터 INSERT
  - 해당 고객번호 계좌 잔고에서 잔고 UPDATE
  - 해당 고객번호로 룰 실행
  - 룰 처리 시간 출력

4. 출금
  - 가입, 계좌개설을 진행하지 않았다면 메시지 출력 후 다시 기능선택으로 돌아감
  - 출금액 입력
  - 출금액 숫자여부 체크
  - 출금액이 0보타 크고 계좌잔고가 0보다 크고 계좌잔고가 출금액보다 크거나 같을 때 출금 테이블에 데이터 INSERT
  - 해당 고객번호 계좌 잔고에서 잔고 UPDATE
  - 해당 고객번호로 룰 실행
  - 룰 처리 시간 출력

5. 이체
  - 가입, 계좌개설을 진행하지 않았다면 메시지 출력 후 다시 기능선택으로 돌아감
  - 이체금액 입력
  - 이체금액 숫자여부 체크
  - 송금계좌번호 입력
  - 송금계좌번호 숫자여부 체크
  - 송금계좌번호 찾기
  - 송금은행 입력
  - 송금계좌주 입력
  - 이체금액이 0보타 크고 계좌잔고가 0보다 크고 계좌잔고가 이체금액보다 크거나 같을 때 이체 테이블에 데이터 INSERT
  - 해당 고객번호 계좌 잔고에서 잔고 UPDATE
  - 해당 고객번호로 룰 실행
  - 룰 처리 시간 출력

6. 종료
  - 시스템 종료