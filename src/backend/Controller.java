package backend;

import java.util.Arrays;

public class Controller {

//  login?employeeCode=loopy&password=1234
//	insertSalesInfo?total=2000&date=2023-08-30&time=20:43
	public String entrance(String clientData) {
		String[] clientDataSplit = clientData.split("\\?");
		String jobCode = clientDataSplit[0]; // login
		String parameters = clientDataSplit.length > 1 ? clientDataSplit[1] : null; // employeeCode=loopy&password=1234
		// jobCode만 있을 때 null값 반환, parameter까지 있으면 값 반환
		//		System.out.println(Arrays.toString(clientDataSplit));
		String serverData = null;

		switch (jobCode) {
		case "login":
		case "moveMyPage":
		case "updateUserInfo":
			serverData = new Authentication().backController(jobCode, parameters);
			break;

		case "logout":
			break;

		case "getProductInfo": // get을 해도 insert를 해도 update를 해도 한 번만 실행된다.
		case "insertSalesInfo":
		case "updateStock":
			serverData = new Sales().backController(jobCode, parameters);
			break;
		case "getNewProductCode":
		case "addProduct":
		case "getAllProductInfo":
		case "updateProductInfo":
		case "deleteProduct":
			serverData = new ProductManagement().backController(jobCode, parameters);
			break;
		case "getNewEmployeeCode":
		case "getAllEmployeesInfo":
		case "updateEmployeeInfo":
		case "deleteEmployee":
		case "viewSalesDaily":
		case "getMonthlySales":
		case "viewSalesByPeriod":
			serverData = new StoreManagement().backController(jobCode, parameters);
			break;
		default:
			break;
		}
		return serverData;
	}
}
