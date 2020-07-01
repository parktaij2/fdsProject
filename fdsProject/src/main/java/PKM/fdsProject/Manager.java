package PKM.fdsProject;

import PKM.fdsProject.Account;
import PKM.fdsProject.Atm;
import PKM.fdsProject.Member;
import PKM.fdsProject.Rule;
import PKM.fdsProject.Validate;
import PKM.fdsProject.Sql;

import java.util.Scanner;
import org.json.simple.JSONObject;

public class Manager {
	static long oldTime;
	static long finalTime;
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Account account = new Account();
		Atm atm = new Atm();
		Member member = new Member();
		Rule rule = new Rule();
		Validate validate = new Validate();
		Scanner scan = new Scanner(System.in);
		Sql sql = new Sql();
		
		// json 객체 생성
		JSONObject json = new JSONObject();
		
		// json 고객번호 초기화
		json.put("memberNo", 0);
		
		// 기능 선택 변수
		String function = "";
		
		while(true) {
			// 기능선택 출력
			System.out.println("==================================================");
			System.out.println("1.가입 / 2.계좌계설 / 3.입금 / 4.출금 / 5.이체 / 6.종료");
			System.out.println("==================================================");
			System.out.print("사용할 기능을 선택하세요 : ");
			function = scan.next();
			
			if(!validate.isNumber(function)) {
				System.out.println("잘못 입력하셨습니다. 숫자만 입력 가능합니다.");
				continue;
			}
			
			switch(function) {
			case("1") :
				// 가입 선택 시 신규가입 진행
				json = member.regist();
				
				// 룰 실행
				stopwatch(1);
				json = rule.detections(json);
				stopwatch(0);
				System.out.println("룰 트랜잭션 경과 시간 : " + finalTime + "ms");
				
				// json 계좌번호 초기화
				json.put("accountNo", 0);
				break;
			case("2") :
				if((Integer)json.get("memberNo") == 0) {
					System.out.println("가입부터 진행해주세요.");
					break;
				} else {
					// 계좌개설 선택 시 신규계좌개설 진행
					json = account.regist(json);
					
					// 룰 실행
					stopwatch(1);
					json = rule.detections(json);
					stopwatch(0);
					System.out.println("룰 트랜잭션 경과 시간 : " + finalTime + "ms");
					
					break;
				}
			case("3") :
				if((Integer)json.get("memberNo") == 0) {
					System.out.println("가입부터 진행해주세요.");
					break;
				} else if((Integer)json.get("accountNo") == 0) {
					System.out.println("계좌개설부터 진행해주세요.");
					break;
				} else {
					// 입금 선택 시 내 계좌에 입금 진행
					json = atm.deposit(json);
					
					// 룰 실행
					stopwatch(1);
					json = rule.detections(json);
					stopwatch(0);
					System.out.println("룰 트랜잭션 경과 시간 : " + finalTime + "ms");
					
					break;
				}
			case("4") :
				if((Integer)json.get("memberNo") == 0) {
					System.out.println("가입부터 진행해주세요.");
					break;
				} else if((Integer)json.get("accountNo") == 0) {
					System.out.println("계좌개설부터 진행해주세요.");
					break;
				} else {
					// 출금 선택 시 내 계좌에 출금 진행
					json = atm.withdraw(json);
					
					// 룰 실행
					stopwatch(1);
					json = rule.detections(json);
					stopwatch(0);
					System.out.println("룰 트랜잭션 경과 시간 : " + finalTime + "ms");
					
					break;
				}
			case("5") :
				if((Integer)json.get("memberNo") == 0) {
					System.out.println("가입부터 진행해주세요.");
					break;
				} else if((Integer)json.get("accountNo") == 0) {
					System.out.println("계좌개설부터 진행해주세요.");
					break;
				} else {
					// 이체 선택 시 계좌송금 진행
					json = atm.transfer(json);
					
					// 룰 실행
					stopwatch(1);
					json = rule.detections(json);
					stopwatch(0);
					System.out.println("룰 트랜잭션 경과 시간 : " + finalTime + "ms");
					
					break;
				}
			case("6") :
				// 종료 선택 시 중지
				System.out.println("이용해주셔서 감사합니다.");
				sql.disconnect();
				break;
			default:
				break;
			}
			
			if("6".equals(function)) {
				break;
			}
		}
	}
	
	// 스톱워치 함수
	public static void stopwatch(int onOff) {
		if(onOff == 1)
			oldTime = System.currentTimeMillis();
		
		if(onOff == 0)
			finalTime = System.currentTimeMillis() - oldTime;
	}
}
