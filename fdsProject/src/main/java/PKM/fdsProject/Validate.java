package PKM.fdsProject;

public class Validate {
	// 숫자 여부 확인 함수
	public boolean isNumber(String str) {
		boolean check = true;
		
		for(int i = 0; i< str.length(); i++) {
			if(!Character.isDigit(str.charAt(i))) {
				check = false;
				break;
			}
		}
		
		return check;
	}
}
