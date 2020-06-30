package PKM.fdsProject;

import java.sql.*;
import org.json.simple.JSONObject;

public class Rule {
	JSONObject json = new JSONObject();
	Sql sql = new Sql();
	
	String sqlQeury = "";
	
	PreparedStatement pstmt = null;
	Connection conn = null;
	ResultSet rs = null;
	
	String ruleResult = "";
	
	public Rule() {
		sql.load();
		conn = sql.getConn();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject ruleExcute(JSONObject json) {
		json.put("ruleNo", "0");
		json.put("ruleMemberNo", 0);
		
		// 고객의 만 나이가 60세 이상
		// 48시간 이내 신규 개설 된 계좌에 100만원 이상 입금 후
		// 2시간 이내 이체 또는 출금되어 잔액이 1만원 이하인 경우
		sqlQeury = "SELECT IFNULL(KAKAOBANK.ACCOUNT.MEMBERNO, 0) AS NUM FROM KAKAOBANK.ACCOUNT " + 
				"WHERE " + 
				"1 = (SELECT COUNT(1) " + 
				"     FROM KAKAOBANK.REGIST " + 
				"     WHERE IF(LEFT(NOW(),10) > MEMBERBIRTHDAY, LEFT(NOW(),4) - LEFT(MEMBERBIRTHDAY,4), LEFT(NOW(),4)-LEFT(MEMBERBIRTHDAY,4)-1) >= 60 " + 
				"     AND KAKAOBANK.REGIST.MEMBERNO = ?" + 
				"	) " + 
				"AND 1= (SELECT COUNT(1) " + 
				"	     FROM KAKAOBANK.ACCOUNT, KAKAOBANK.DEPOSIT " + 
				"        WHERE KAKAOBANK.ACCOUNT.MEMBERNO = KAKAOBANK.DEPOSIT.MEMBERNO " + 
				"		 AND KAKAOBANK.ACCOUNT.ACCOUNTNO = KAKAOBANK.DEPOSIT.ACCOUNTNO " + 
				"		 AND KAKAOBANK.ACCOUNT.DEALTIME > DATE_ADD(NOW(), INTERVAL -48 HOUR) " + 
				"		 AND KAKAOBANK.DEPOSIT.AMOUNT >= 1000000 " + 
				"		 AND KAKAOBANK.ACCOUNT.MEMBERNO = ?" + 
				"	   ) " + 
				"AND (1 = (SELECT COUNT(1) " + 
				"          FROM KAKAOBANK.ACCOUNT, KAKAOBANK.WITHDRAW " + 
				"          WHERE KAKAOBANK.ACCOUNT.MEMBERNO = KAKAOBANK.WITHDRAW.MEMBERNO " + 
				"          AND KAKAOBANK.ACCOUNT.ACCOUNTNO = KAKAOBANK.WITHDRAW.ACCOUNTNO " + 
				"          AND KAKAOBANK.WITHDRAW.DEALTIME > DATE_ADD(NOW(), INTERVAL -2 HOUR) " + 
				"          AND KAKAOBANK.ACCOUNT.ZANGO <= 10000 " + 
				"          AND KAKAOBANK.ACCOUNT.MEMBERNO = ?" + 
				"         ) " + 
				"      OR " + 
				"      1 = (SELECT COUNT(1) " + 
				"           FROM KAKAOBANK.ACCOUNT, KAKAOBANK.TRANSFER " + 
				"		   WHERE KAKAOBANK.ACCOUNT.MEMBERNO = KAKAOBANK.TRANSFER.MEMBERNO " + 
				"           AND KAKAOBANK.ACCOUNT.ACCOUNTNO = KAKAOBANK.TRANSFER.ACCOUNTNO " + 
				"           AND KAKAOBANK.TRANSFER.DEALTIME > DATE_ADD(NOW(), INTERVAL -2 HOUR) " + 
				"           AND KAKAOBANK.ACCOUNT.ZANGO <= 10000 " + 
				"           AND KAKAOBANK.ACCOUNT.MEMBERNO = ?" + 
				"		  )" + 
				"     ) " + 
				"AND KAKAOBANK.ACCOUNT.MEMBERNO = ?";
		
		
		try {
			pstmt = conn.prepareStatement(sqlQeury);
			pstmt.setInt(1, (Integer)json.get("memberNo"));
			pstmt.setInt(2, (Integer)json.get("memberNo"));
			pstmt.setInt(3, (Integer)json.get("memberNo"));
			pstmt.setInt(4, (Integer)json.get("memberNo"));
			pstmt.setInt(5, (Integer)json.get("memberNo"));
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getInt("NUM") != 0) {
					System.out.println("******룰에 탐지되었습니다. 탐지된 고객번호는 " + rs.getInt("NUM") + "입니다.*******");
					json.put("ruleNo", "1");
					json.put("ruleMemberNo", rs.getInt("NUM"));
				} else {
					System.out.println("******룰에 탐지되지 않았습니다.*******");
					
				}
			}
			
			if("0".equals((String)json.get("ruleNo"))) {
				System.out.println("******룰에 탐지되지 않았습니다.*******");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
			rs = null;
		}
		
		return json;
	}
}
