// 고객번호, 계좌번호, 거래시각
package PKM.fdsProject;

import java.sql.*;
import org.json.simple.JSONObject;
import java.util.Scanner;

public class Account {
	Sql sql = new Sql();
	Scanner scan = new Scanner(System.in);
	JSONObject json = new JSONObject();
	
	String sqlQeury = "";
	
	PreparedStatement pstmt = null;
	Connection conn = null;
	ResultSet rs = null;
	
	public Account() {
		sql.load();
		conn = sql.getConn();
	}
	
	// 계좌가 있는 고객인지 체크
	@SuppressWarnings("unchecked")
	public JSONObject checkAccountNo(JSONObject json) {
		sqlQeury = "SELECT ACCOUNTNO, COUNT(1) AS NUM FROM KAKAOBANK.ACCOUNT WHERE MEMBERNO = ?";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			pstmt.setInt(1, (Integer)json.get("memberNo"));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getInt("NUM") == 1) {
					System.out.println("이미 존재하는 계좌입니다. 당신의 계좌번호는 " + rs.getInt("ACCOUNTNO") + "입니다.");
					json.put("accountNo", rs.getInt("ACCOUNTNO"));
					return json;
				} else {
					json.put("accountNo", 0);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
		
		return json;
	}
	
	// 계좌번호 채번
	@SuppressWarnings("unchecked")
	public JSONObject getAccountNo(JSONObject json) {
		String sqlQeury = "SELECT IFNULL(MAX(ACCOUNTNO), 0) + 1 AS NUM FROM KAKAOBANK.ACCOUNT";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				json.put("accountNo", rs.getInt("NUM"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
		
		return json;
	}
	
	// 계좌 개설
	public JSONObject regist(JSONObject json) {
		json = checkAccountNo(json);
		
		if((Integer)json.get("accountNo") != 0) {
			return json;
		}
		
		json = getAccountNo(json);
		
		sqlQeury = "INSERT INTO KAKAOBANK.ACCOUNT (MEMBERNO, ACCOUNTNO, ZANGO, DEALTIME) "
				 + "VALUES (?, ?, 0, SYSDATE())";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			pstmt.setInt(1, (Integer)json.get("memberNo"));
			pstmt.setInt(2, (Integer)json.get("accountNo"));
			int n = pstmt.executeUpdate();
			
			if(n > 0) {
				System.out.println("계좌개설이 성공적으로 진행되었습니다.");
			} else {
				System.out.println("계좌개설에 실패하였습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
		
		return json;
	}
	
	// 잔고 가져오기
	@SuppressWarnings("unchecked")
	public JSONObject getZango(JSONObject json) {
		String sqlQeury = "SELECT ZANGO FROM KAKAOBANK.ACCOUNT "
				        + "WHERE MEMBERNO = ? AND ACCOUNTNO = ?";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			pstmt.setInt(1, (Integer)json.get("memberNo"));
			pstmt.setInt(2, (Integer)json.get("accountNo"));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				json.put("zango", rs.getInt("ZANGO"));
				System.out.println("계좌의 잔고는 : " + (Integer)json.get("zango") + "원 남았습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
		
		return json;
	}
	
	// 입금잔고수정
	public void addZangoUpdate(JSONObject json) {
		sqlQeury = "UPDATE KAKAOBANK.ACCOUNT SET ZANGO = ZANGO + ? "
				 + "WHERE MEMBERNO = ? AND ACCOUNTNO = ?";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			pstmt.setInt(1, (Integer)json.get("amount"));
			pstmt.setInt(2, (Integer)json.get("memberNo"));
			pstmt.setInt(3, (Integer)json.get("accountNo"));
			int n = pstmt.executeUpdate();
			
			if(n > 0) {
				System.out.println("계좌 잔고가 성공적으로 변경되었습니다.");
			} else {
				System.out.println("계좌 잔고 변경에 실패하였습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
	}
	
	// 출금잔고수정
	public void abstractZangoUpdate(JSONObject json) {
		sqlQeury = "UPDATE KAKAOBANK.ACCOUNT SET ZANGO = ZANGO - ? "
				 + "WHERE MEMBERNO = ? AND ACCOUNTNO = ?";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			pstmt.setInt(1, (Integer)json.get("amount"));
			pstmt.setInt(2, (Integer)json.get("memberNo"));
			pstmt.setInt(3, (Integer)json.get("accountNo"));
			int n = pstmt.executeUpdate();
			
			if(n > 0) {
				System.out.println("계좌 잔고가 성공적으로 변경되었습니다.");
			} else {
				System.out.println("계좌 잔고 변경에 실패하였습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
	}
	
	// 계좌찾기
	@SuppressWarnings("unchecked")
	public JSONObject searchAccount(JSONObject json) {
		sqlQeury = "SELECT MEMBERNO, ACCOUNTNO FROM KAKAOBANK.ACCOUNT "
				 + "WHERE ACCOUNTNO = ?";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			pstmt.setInt(1, (Integer)json.get("toaccountNo"));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				json.put("tomemberNo", rs.getInt("MEMBERNO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
		
		return json;
	}
	
	
	// 수신인입금잔고수정
	public void addToZangoUpdate(JSONObject json) {
		sqlQeury = "UPDATE KAKAOBANK.ACCOUNT SET ZANGO = ZANGO + ? "
				 + "WHERE ACCOUNTNO = ?";
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			pstmt.setInt(1, (Integer)json.get("amount"));
			pstmt.setInt(2, (Integer)json.get("toaccountNo"));
			int n = pstmt.executeUpdate();
			
			if(n > 0) {
				System.out.println("수신인 계좌 잔고가 성공적으로 변경되었습니다.");
			} else {
				System.out.println("수신인 계좌 잔고 변경에 실패하였습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
	}
}
