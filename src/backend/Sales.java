package backend;

import java.util.List;

import beans.Product;
import db.DataAccessObject;
import utility.ProjectUtils;

public class Sales {

	private ProjectUtils pu;

	public String backController(String jobCode, String parameters) {

		String serverData = null;

		this.pu = new ProjectUtils();

		switch (jobCode) {
		case "getProductInfo":
			serverData = this.getProductInfo(parameters); // 현재 실행중인 객체의 로그인 함수
			break;

		case "insertSalesInfo":
			serverData = this.insertSalesInfo(parameters);

			break;

		case "updateStock":
			serverData = this.updateStock(parameters);

		default:
			break;
		}
		return serverData;
	}

	private String updateStock(String parameters) {// database 재고수정
		String serverData = null;
//		1. db에서 상품 정보 가져와서 List로 만들기 > 읽기
		List<Product> allProducts = this.getAllProductInfo();
//		for(Product a : allProducts) {
//			System.out.println(a.getProductCode());
//		}
		
//		2. List를 Front에서 가져온 데이터로 업데이트
//		0번지 : productCode=processed0001
//		1번지 : stock=5
//		2번지 : productCode=processed0002
//		3번지 : stock=10
//		4번지 : productCode=processed0003
//		5번지 : stock=5
		String[][] clientData = this.pu.extractData(parameters); // 2차원 배열
		for(int i = 0; i < clientData.length; i += 2) { // 0, 2, 4 : 상품코드
			for(int j = 0; j < allProducts.size(); j++) {
				if(clientData[i][1].equals(allProducts.get(j).getProductCode())) { // 전체 상품 코드와 주문한 상품코드 비교
					allProducts.get(j).setStock(Integer.parseInt(clientData[i+1][1])); // 1, 3, 5 : 재고
					break;
				}
			}
		}
//		for(Product p : allProducts) {
//			System.out.println(p.getProductCode() + ":" + p.getStock());
//		}
		
//		3. List를 활용해서 db 업데이트 > 쓰기
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(false, "products.txt", false)) {
			// date=2023-08-30&time=20:43&total=2000
			serverData = dao.updateStock(allProducts) ? "success" : "fail"; // true or false
			dao.fileClose(false);
		}
		return serverData;
	}

	List<Product> getAllProductInfo() {
		List<Product> allProducts = null;
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "products.txt", false)) {
			allProducts = dao.getAllProductInfo();
			dao.fileClose(true);
		}
		return allProducts;
	}

	// 매출 입력
	private String insertSalesInfo(String parameters) {
		// insertSalesInfo
		String serverData = null;
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(false, "sales.txt", true)) { // 한 줄 추가 쓰기
			// date=2023-08-30&time=20:43&total=2000
			serverData = dao.insertSalesInfo(this.pu.extractData(parameters)); // 출력한 2차원 배열을 db와 대조
			dao.fileClose(false);
		}
		return serverData; // success or fail반환
	}

	private String getProductInfo(String parameters) {
		String serverData = null;
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "products.txt", false)) {
			serverData = dao.getProductInfo(this.pu.extractData(parameters)); // 출력한 2차원 배열을 db와 대조
			dao.fileClose(true);
		}
		return serverData;
	}
}
