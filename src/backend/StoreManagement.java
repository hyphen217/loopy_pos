package backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import beans.UserInfo;
import db.DataAccessObject;
import utility.ProjectUtils;

public class StoreManagement {
	private ProjectUtils pu;

	public String backController(String jobCode, String parameters) {

		String serverData = null;

		this.pu = new ProjectUtils();
		switch (jobCode) {
		case "getMonthlySales":
			serverData = this.getMonthlySales(parameters);
			break;
		case "updateEmployeeInfo":
			serverData = this.updateEmployeeInfo(parameters);
			break;
		case "getAllEmployeesInfo":
			serverData = this.getAllEmployeesInfo();
			break;
		case "deleteEmployee":
			serverData = this.deleteEmployee(parameters);
			break;
		case "getNewEmployeeCode":
			serverData = this.getNewEmployeeCode();
			break;
		case "viewSalesByPeriod":
			serverData = this.viewSalesByPeriod(parameters);
			break;
		case "viewSalesDaily":
			serverData = this.viewSalesDaily(parameters);
		default:
			break;
		}
		return serverData;
	}
	
	private String viewSalesDaily(String parameters) {
//		getDailySales?day=2023-09-01
		String day = null;
		String dailySales = "0";
		day = parameters.substring(parameters.indexOf("=") + 1); // =의 인덱스 반환

		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "sales.txt", false)) { // 읽음
			dailySales = dao.viewSalesDaily(day);
		}
		dao.fileClose(true);
		return dailySales;
	}

//	viewSalesByPeriod?startDate=2023-09-04&endData=2023-09-11
	private String viewSalesByPeriod(String parameters) {
		String[][] clientData = null;
		int salesByPeriod = 0;
//		backend와 dao 경계선 >> bacend는 데이터 가공 / dao는 데이터베이스 접근
//		String 2023-09-04 -> Data형식으로 변경
		clientData = this.pu.extractData(parameters);
//		starDate 2023-08-30
//		endDate 2023-09-01
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		sdf -> 변환하는 기준
		Date[] dates = new Date[2];
		try {
			dates[0] = sdf.parse(clientData[0][1]);
			dates[1] = sdf.parse(clientData[1][1]);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "sales.txt", false)) { // 읽음
			salesByPeriod = dao.viewSalesByPeriod(dates, sdf);
		}
		dao.fileClose(true);
//		Date[]
//		reader
//		기간별 매출 총액 string
		return String.valueOf(salesByPeriod);
	}

	private String getMonthlySales(String parameters) {
//		getMonthlySales?month=9
		String month = null;
		String monthlySales = "0";
		month = parameters.substring(parameters.indexOf("=") + 1); // =의 인덱스 반환

		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "sales.txt", false)) { // 읽음
			monthlySales = dao.getMonthlySales(month);
		}
		dao.fileClose(true);
		return monthlySales;
	}

	private String getNewEmployeeCode() {
		String newEmployeeCode = "fail";
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "employees.txt", false)) {
			newEmployeeCode = dao.getNewEmployeeCode();
		}
		dao.fileClose(true);
		return newEmployeeCode;
	}

	private String deleteEmployee(String parameters) {
		String serverData = null;
		List<UserInfo> userList = null;
		String[][] clientData = this.pu.extractData(parameters);
//		this.pu.println(Arrays.deepToString(clientData)); //[[商品コード, 0003]]

		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "employees.txt", false)) { // 읽음
			userList = dao.getUserList(); // 출력한 2차원 배열을 db와 대조
			dao.fileClose(true);
		}

		int productIndex = 0;
		for (int i = 0; i < userList.size(); i++) {
			if (clientData[0][1].equals(userList.get(i).getEmployeeCode())) { // 해당 사원코드를 찾으면
				productIndex = i; // 해당 번째의 상품 리스트 저장
				userList.remove(i);
				break;
			}
		}
		if (dao.fileConnection(false, "employees.txt", false)) { // 덮어쓰기
			serverData = dao.deleteEmployee(userList);
//			System.out.println(serverData); // success
			dao.fileClose(false);
		}
		if (serverData.equals("success")) {
			this.pu.println("削除に成功しました。");
		}
		return serverData;
	}

	private String updateEmployeeInfo(String parameters) {
		String serverData = null;
		List<UserInfo> userList = null;
		String[][] clientData = this.pu.extractData(parameters);
//		System.out.println(Arrays.deepToString(clientData));
		// [[employeeCode, 9797], [position, 노예]]

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
				userList.get(i).setPosition(clientData[1][1]);
				break;
			}
		}
		if (dao.fileConnection(false, "employees.txt", false)) { // 쓰기
			serverData = dao.updateEmployeeInfo(userList);
			dao.fileClose(false);
		}
		if (serverData.equals("success")) {
			serverData = this.pu.makeTransferData(new String[] { "employeeCode", "name", "position" },
					new String[] { userList.get(userIndex).getEmployeeCode(), userList.get(userIndex).getName(),
							userList.get(userIndex).getPosition() });
		}
		return serverData;
	}

	private List<UserInfo> getAllEmployeesInfoList() {
		List<UserInfo> allEmployees = null;
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "employees.txt", false)) {
			// date=2023-08-30&time=20:43&total=2000
			allEmployees = dao.getUserList();
			dao.fileClose(true);
		}
		return allEmployees;
	}

	private String getAllEmployeesInfo() {
		StringBuilder sb = new StringBuilder();
		List<UserInfo> employeeList = this.getAllEmployeesInfoList();
		int index = 0;
		for (UserInfo ui : employeeList) {
//			9797,9797,loopy,奴隷
			sb.append("employeeCode=");
			sb.append(ui.getEmployeeCode());
			sb.append("&password=");
			sb.append(ui.getPassword());
			sb.append("&name=");
			sb.append(ui.getName());
			sb.append("&position=");
			sb.append(ui.getPosition());
			sb.append(index < employeeList.size() - 1 ? "&" : "");
			index++;
		}

		return sb.toString();
	}

}
