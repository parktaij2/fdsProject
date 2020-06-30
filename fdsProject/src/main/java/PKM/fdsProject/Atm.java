// 입금 : 고객번호, 입금계좌번호, 이금금액, 거래시각
// 출금 : 고객번호, 출금계좌번호, 출금금액, 거래시각
// 이체 : 고객번호, 출금계좌번호, 수취은행, 수취계좌번호, 수취계좌주, 이체금액, 거래시각
package PKM.fdsProject;

import java.sql.*;
import java.util.Scanner;
import org.json.simple.JSONObject;
import PKM.fdsProject.Account;

public class Atm {
	Sql sql = new Sql();
	Scanner scan = new Scanner(System.in);
	Validate validate = new Validate();
	Account account = new Account();
	JSONObject json = new JSONObject();
	
	String temp = "";
	String sqlQeury = "";
	
	PreparedStatement pstmt = null;
	Connection conn = null;
	ResultSet rs = null;
	
	public Atm() {
		sql.load();
		conn = sql.getConn();
	}
	
	// 입금
	@SuppressWarnings("unchecked")
	public JSONObject deposit(JSONObject json) {
		while(true) {
			System.out.print("입금할 금액을 입력하세요 : ");
			this.temp = scan.next();
			
			// 숫자 여부 확인
			if(validate.isNumber(temp)) {
				json.put("amount", Integer.parseInt(temp));
				break;
			} else {
				System.out.println("잘못입력하셨습니다. 숫자만 입력 가능합니다.");
				continue;
			}
		}
		
		// 입금 금액이 0보다 클때 입금 진행
		if((Integer)json.get("amount") >= 0) {
			sqlQeury = "INSERT INTO KAKAOBANK.DEPOSIT (MEMBERNO, ACCOUNTNO, AMOUNT, DEALTIME) "
					 + "VALUES (?, ?, ?, SYSDATE())";
			
			try {
				pstmt = conn.prepareStatement(sqlQeury);
				pstmt.setInt(1, (Integer)json.get("memberNo"));
				pstmt.setInt(2, (Integer)json.get("accountNo"));
				pstmt.setInt(3, (Integer)json.get("amount"));
				int n = pstmt.executeUpdate();
				
				if(n > 0) {
					account.addZangoUpdate(json);
					System.out.println("입금이 성공적으로 진행되었습니다.");
				} else {
					System.out.println("입금에 실패하였습니다.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				pstmt = null;
				rs = null;
			}
		} else {
			System.out.println("잘못입력하셨습니다. 금액은 0보다 커야 합니다.");
		}
		
		return account.getZango(json);
	}
	
	// 출금
	@SuppressWarnings("unchecked")
	public JSONObject withdraw(JSONObject json) {
		while(true) {
			System.out.print("출금할 금액을 입력하세요 : ");
			this.temp = scan.next();
			
			// 숫자 여부 확인
			if(validate.isNumber(temp)) {
				json.put("amount", Integer.parseInt(temp));
				break;
			} else {
				System.out.println("잘못입력하셨습니다. 숫자만 입력 가능합니다.");
				continue;
			}
		}
		
		json = account.getZango(json);
		
		// 입금 금액이 0보다 클때 입금 진행
		if((Integer)json.get("amount") > 0 && (Integer)json.get("zango") > 0 && (Integer)json.get("zango") >= (Integer)json.get("amount")) {
			sqlQeury = "INSERT INTO KAKAOBANK.WITHDRAW (MEMBERNO, ACCOUNTNO, AMOUNT, DEALTIME) "
					 + "VALUES (?, ?, ?, SYSDATE())";
			
			try {
				pstmt = conn.prepareStatement(sqlQeury);
				pstmt.setInt(1, (Integer)json.get("memberNo"));
				pstmt.setInt(2, (Integer)json.get("accountNo"));
				pstmt.setInt(3, (Integer)json.get("amount"));
				int n = pstmt.executeUpdate();
				
				if(n > 0) {
					account.abstractZangoUpdate(json);
					System.out.println("출금이 성공적으로 진행되었습니다.");
				} else {
					System.out.println("출금에 실패하였습니다.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				pstmt = null;
				rs = null;
			}
		} else {
			System.out.println("잔액이 부족합니다.");
		}
		
		return account.getZango(json);
	}
	
	// 이체
	@SuppressWarnings("unchecked")
	public JSONObject transfer(JSONObject json) {
		while(true) {
			System.out.print("입금할 금액을 입력하세요 : ");
			this.temp = scan.next();
			
			// 숫자 여부 확인
			if(validate.isNumber(temp)) {
				json.put("amount", Integer.parseInt(temp));
			} else {
				System.out.println("잘못입력하셨습니다. 숫자만 입력 가능합니다.");
				continue;
			}
			
			System.out.print("송금할 계좌번호를 입력하세요 : ");
			this.temp = scan.next();
			
			// 숫자 여부 확인
			if(validate.isNumber(temp)) {
				json.put("toaccountNo", Integer.parseInt(temp));
			} else {
				System.out.println("잘못입력하셨습니다. 숫자만 입력 가능합니다.");
				continue;
			}
			
			json = account.searchAccount(json);
			
			if(json.get("tomemberNo") == null) {
				System.out.println("계좌번호가 존재하지 않습니다.");
				continue;
			}
			
			System.out.print("송금할 은행을 입력하세요 : ");
			json.put("tobank", scan.next());
			
			System.out.print("송금할 계좌주를 입력하세요 : ");
			json.put("tomemberName", scan.next());
			
			break;
		}
		
		json = account.getZango(json);
		
		// 입금 금액이 0보다 클때 입금 진행
		if((Integer)json.get("amount") > 0 && (Integer)json.get("zango") > 0 && (Integer)json.get("zango") >= (Integer)json.get("amount")) {
			sqlQeury = "INSERT INTO KAKAOBANK.TRANSFER (MEMBERNO, ACCOUNTNO, TOBANK, TOACCOUNTNO, TOMEMBERNAME, AMOUNT, DEALTIME) "
					 + "VALUES (?, ?, ?, ?, ?, ?, SYSDATE())";
			
			try {
				pstmt = conn.prepareStatement(sqlQeury);
				pstmt.setInt(1, (Integer)json.get("memberNo"));
				pstmt.setInt(2, (Integer)json.get("accountNo"));
				pstmt.setString(3, (String)json.get("tobank"));
				pstmt.setInt(4, (Integer)json.get("toaccountNo"));
				pstmt.setString(5, (String)json.get("tomemberName"));
				pstmt.setInt(6, (Integer)json.get("amount"));
				int n = pstmt.executeUpdate();
				
				if(n > 0) {
					account.abstractZangoUpdate(json);
					account.addToZangoUpdate(json);
					System.out.println("이체가 성공적으로 진행되었습니다.");
				} else {
					System.out.println("이체에 실패하였습니다.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				pstmt = null;
				rs = null;
			}
		} else {
			System.out.println("잔액이 부족합니다.");
		}
		
		return account.getZango(json);
	}
 }
