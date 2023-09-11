package backend;

import java.util.Arrays;
import java.util.List;

import beans.Product;
import beans.UserInfo;
import db.DataAccessObject;
import utility.ProjectUtils;

public class ProductManagement {
	private ProjectUtils pu;

	public String backController(String jobCode, String parameters) {

		String serverData = null;

		this.pu = new ProjectUtils();
		switch (jobCode) {
//		case "addProduct":
//			serverData = this.addProduct(parameters);
		case "getNewProductCode":
			serverData = this.getNewProductCode();
			break;
		case "addProduct":
			serverData = this.addProduct(parameters);
			break;
		case "getAllProductInfo":
			serverData = this.getAllProductInfo();
			break;
		case "updateProductInfo":
			serverData = this.updateProductInfo(parameters);
			break;
		case "deleteProduct":
			serverData = this.deleteProduct(parameters);
			break;
		default:
			break;
		}
		return serverData;
	}
	
	private String deleteProduct(String parameters) {
		String serverData = null;
		List<Product> productList = null;
		String[][] clientData = this.pu.extractData(parameters);
//		this.pu.println(Arrays.deepToString(clientData)); //[[商品コード, 0003]]

		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "products.txt", false)) { // 읽음
			productList = dao.getAllProductInfo(); // 출력한 2차원 배열을 db와 대조
			//getAllProductInfo : productCode=0001&name=ルフィの皮&category=加工食品&price=20000&stock=100&productCode=0002&name=チ-ズサンド&category=加工食品&price=5000&stock=65&productCode=0003&name=ホットドッグ&category=加工食品&price=7000&stock=100
			dao.fileClose(true);
		}

//		productCode=0001&name=ルフィの皮&category=加工食品&price=20000&stock=100
		int productIndex = 0;
		for (int i = 0; i < productList.size(); i++) {
			if (clientData[0][1].equals(productList.get(i).getProductCode())) { // 해당 상품을 찾으면
				productIndex = i; // 해당 번째의 상품 리스트 저장
				productList.remove(i);
				break;
			}
		}
		if (dao.fileConnection(false, "products.txt", false)) { // 덮어쓰기
			serverData = dao.deleteProduct(productList);
//			System.out.println(serverData); // success
			dao.fileClose(false);
		}
		if (serverData.equals("success")) {
			this.pu.println("削除に成功しました。");
		}
		return serverData;
	}

	private String updateProductInfo(String parameters) {
		String serverData = null;
		List<Product> productList = null;
		String[][] clientData = this.pu.extractData(parameters);
//		this.pu.println(Arrays.deepToString(clientData));

		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "products.txt", false)) { // 읽음
			productList = dao.getAllProductInfo(); // 출력한 2차원 배열을 db와 대조
			//getAllProductInfo : productCode=0001&name=ルフィの皮&category=加工食品&price=20000&stock=100&productCode=0002&name=チ-ズサンド&category=加工食品&price=5000&stock=65&productCode=0003&name=ホットドッグ&category=加工食品&price=7000&stock=100&
			dao.fileClose(true);
		}

//		productCode=0001&name=ルフィの皮&category=加工食品&price=20000&stock=100
		int productIndex = 0;
		for (int i = 0; i < productList.size(); i++) {
			if (clientData[0][1].equals(productList.get(i).getProductCode())) { // 해당 상품을 찾으면
				productIndex = i; // 해당 번째의 상품 리스트 저장
				productList.get(i).setProductName(clientData[1][1]); // name
				productList.get(i).setCategory(clientData[2][1]);
				productList.get(i).setPrice(Integer.parseInt(clientData[3][1]));
				productList.get(i).setStock(Integer.parseInt(clientData[4][1]));
				break;
			}
		}
		if (dao.fileConnection(false, "products.txt", false)) { // 덮어쓰기
			serverData = dao.updateProductInfo(productList);
			dao.fileClose(false);
		}
		if (serverData.equals("success")) {
			serverData = this.pu.makeTransferData(new String[] { "商品コード", "商品名", "カテゴリー", "単価", "在庫" },
					new String[] { productList.get(productIndex).getProductCode(),
							productList.get(productIndex).getProductName(),
							productList.get(productIndex).getCategory(),
							String.valueOf(productList.get(productIndex).getPrice()),
							String.valueOf(productList.get(productIndex).getStock()) });
		}
		return serverData;
	}

	private String getAllProductInfo() {
		StringBuilder sb = new StringBuilder();
		List<Product> productList = new Sales().getAllProductInfo();
		int index = 0;
		for (Product p : productList) {
//			0003,ホットドッグ,加工食品,7000,100
			sb.append("productCode=");
			sb.append(p.getProductCode());
			sb.append("&name=");
			sb.append(p.getProductName());
			sb.append("&category=");
			sb.append(p.getCategory());
			sb.append("&price=");
			sb.append(p.getPrice());
			sb.append("&stock=");
			sb.append(p.getStock());
			sb.append(index < productList.size() - 1 ? "&" : "");
			index++;
		}

		return sb.toString();
	}

	private String addProduct(String parameters) {
//		jobCode = getNewProductCode
//		productCode=0004&
//		productName=testname&
//		category=testcate&
//		price=1111&
//		stock=20
		String serverData = "fail";
		String[][] productInfo = this.pu.extractData(parameters);
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(false, "products.txt", true)) { // 한 줄 추가 쓰기
			serverData = dao.addProduct(new Product(productInfo[0][1], productInfo[1][1], productInfo[2][1],
					Integer.parseInt(productInfo[3][1]), Integer.parseInt(productInfo[4][1]))) ? "success" : "fail";
		}
		dao.fileClose(false);
		return serverData;
	}

	private String getNewProductCode() {
		String newProductCode = "fail";
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "products.txt", false)) {
			newProductCode = dao.getNewProductCode();
		}
		dao.fileClose(true);
		return newProductCode;
	}

//	// 상품 추가
//	private String addProduct(String parameters) {
////		jobCode = getNewProductCode
////		parameters = productCode=0004&productName=testname&category=testcate&price=1111&stock=20
//		String serverData = null;
//		DataAccessObject dao = new DataAccessObject();
//		if (dao.fileConnection(false, "products.txt", true)) { // 한 줄 추가 쓰기
//			// productCode=0001&productName=ルフィの皮&category=加工食品&price=20000&stock=100
//			serverData = dao.addProduct(this.pu.extractData(parameters)); // 출력한 2차원 배열을 db와 대조
//			dao.fileClose(false);
//		}
//		return serverData; // success or fail반환
//	}
}
