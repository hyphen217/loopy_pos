package backend;

import java.util.ArrayList;
import java.util.List;

import beans.UserInfo;
import db.DataAccessObject;
import utility.ProjectUtils;

public class Authentication {

	private ProjectUtils pu;

	public String backController(String jobCode, String parameters) {

		String serverData = null;

		this.pu = new ProjectUtils();

		switch (jobCode) {
		case "login":
			serverData = this.login(parameters); // 현재 실행중인 객체의 로그인 함수
			break;

		case "logout":

			break;
		case "updateUserInfo":
			serverData = this.updateUserInfo(parameters);

		default:
			break;
		}
		return serverData;
	}

	private String updateUserInfo(String parameters) {
		String serverData = null;
		List<UserInfo> userList = null;
		String[][] clientData = this.pu.extractData(parameters);
		
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "employees.txt", false)) { // 읽음
			userList = dao.getUserList(); // 출력한 2차원 배열을 db와 대조
			dao.fileClose(true);
		}
//		employeeCode=9797
//		name=루피&
//		password=9797
		int userIndex = 0;
		for (int i = 0; i < userList.size(); i++) {
			if (clientData[0][1].equals(userList.get(i).getEmployeeCode())) { // 해당 사원을 찾으면
				userIndex = i; // 해당 번째의 유저 리스트 저장
				userList.get(i).setName(clientData[1][1]);
				userList.get(i).setPassword(clientData[2][1]);
				break;
			}
		}
		if (dao.fileConnection(false, "employees.txt", false)) { // 쓰기
			serverData = dao.updateUserInfo(userList);
			dao.fileClose(false);
}
		if(serverData.equals("success")) {
			serverData = this.pu.makeTransferData(
					new String[] {"employeeCode","name","position"},
					new String[] {
							userList.get(userIndex).getEmployeeCode(),
							userList.get(userIndex).getName(),
							userList.get(userIndex).getPosition()
							});
		}
		return serverData;
	}

	private String login(String parameters) { // dao의 login함수와 세트

		// login?employeeCode=loopy&password=1234

//		데이터 추출 검증
//		for (String[] ar : arr) // 2차원 배열 > 1차원 배열
//			for (String s : ar) // 1차원 배열 > 문자
//				this.pu.print(s);
		// 고전방식
//		this.pu.print("______");
//		for(int i=0; i < arr.length; i++) {
//			for(int j=0; j < arr[i].length; j++) {
//				this.pu.print(arr[i][j]);
//			}
//			this.pu.print("______");
//		}
//		return employeeCode.equals(arr[0][1]) && password.equals(arr[1][1]) ? employeeCode : "fail";
		String serverData = "fail";
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "employees.txt", false)) {
			serverData = dao.login(this.pu.extractData(parameters)); // 출력한 2차원 배열을 db와 대조
			dao.fileClose(true);
		}
		;
		return serverData;
	}
}
