package db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import beans.Product;
import beans.UserInfo;
import utility.ProjectUtils;

public class DataAccessObject { // database 접근 객체(text파일 read/write)
	File file;
	FileReader reader;
	FileWriter writer;
	BufferedReader bReader;
	BufferedWriter bWriter;
	ProjectUtils pu;
	List<UserInfo> userList = new ArrayList<>();;

	public DataAccessObject() {
		this.pu = new ProjectUtils();
	}
	
	public String viewSalesDaily(String day) {
		String currentLine = null;
		int dailySales = 0;
		try {
			while ((currentLine = this.bReader.readLine()) != null) {
				String[] split = currentLine.split(",");
//			2023-09-04,18:47,5000
				if (split[0].equals(day)) {
					dailySales += Integer.parseInt(split[2]);
				}
			}

		} catch (IOException e) {
			return "fail";
		}
		return String.valueOf(dailySales);
	}

	public int viewSalesByPeriod(Date[] inputDates, SimpleDateFormat sdf) {
		// StoreManagement에서 쓴 impleDateFormat sdf = new
		// SimpleDateFormat("yyyy-MM-dd");를 사용할 수 있음
		String currentLine = null;
		int salesByPeriod = 0;
		try {
			while ((currentLine = this.bReader.readLine()) != null) {
//				2023-09-04,18:47,5000
//				날짜가 그 기간에 있다는 것 2023-09-04 ~ 2023-09-11
//				2023-09-04 <= 날짜 <= 2023-09-11
				String[] split = currentLine.split(",");
				Date currentDate = sdf.parse(split[0]); // 2023-09-04
				if (inputDates[0].compareTo(currentDate) <= 0// 시작일 ~ 메모장 날짜
						&& currentDate.compareTo(inputDates[1]) <= 0) {// 메모장 날짜 ~ 종료일
					salesByPeriod += Integer.parseInt(split[2]); // 해당 기간의 매출액
				}
			}
		} catch (Exception e) {}
		return salesByPeriod;
	}

	public String getMonthlySales(String month) {
		String currentLine = null;
		int monthlySales = 0;
		int dailySales;
//		===========================================
//		primary data type 원시 데이터 타입 : int long
//		BigInteger는 원시 데이터 타입이 아니라서 > < = 가 안되서 compareTo를 사용
//		Wrapper class wrap 포장하다 : Integer Double Long 등 있음
//		int에는 null이 들어가지 못함.
//		int -> Integer로 포장하면 null값을 줄 수 있음.
//		BigInteger monthlySales = BigInteger.ZERO;
//		BigInteger.ONE;
//		BigInteger.ONE.subtract() 빼기
//		BigInteger.ONE.compareTo(val)
//		BigInteger dailySales;
//		===========================================
		try {
			while ((currentLine = this.bReader.readLine()) != null) {
//			2023-09-04,18:47,5000
				dailySales = Integer.parseInt(currentLine.split(",")[2]);
//				dailySales = BigInteger.valueOf(Integer.parseInt(currentLine.split(",")[2]));
				if (currentLine.split("-")[1].equals(month)) {
					monthlySales += dailySales;
//					monthlySales = monthlySales.add(dailySales);
				}
			}

		} catch (IOException e) {
			return "fail";
		}
		return String.valueOf(monthlySales);
	}

	public String getNewEmployeeCode() {
		String currentLine = null;
		String newProductCode = "0000000"; // 2309001
		try {
			while ((currentLine = this.bReader.readLine()) != null) {
//				0001,ルフィの皮,加工食品,20000,100
				newProductCode = currentLine.substring(0, currentLine.indexOf(","));
				// 0001, indexOf의 값=4
				// substring(시작자리, 끝나는자리 - 1) = 0001 추출
				// while문을 돌면서 0001 > 0002로 읽음
			}

		} catch (IOException e) {
			return "fail";
		}
		int intValue = Integer.parseInt(newProductCode) + 1;
		newProductCode = String.valueOf(intValue);
		return intValue < 10 ? "000" + newProductCode
				: intValue < 100 ? "00" + newProductCode
						: intValue < 1000 ? "0" + newProductCode : intValue < 10000 ? newProductCode : "full";
	}

	public String deleteEmployee(List<UserInfo> userList) { // 매장관리 - 직원삭제
		StringBuilder sb = null;
		for (UserInfo ui : userList) {
			sb = new StringBuilder();
			sb.append(ui.getEmployeeCode());
			sb.append(",");
			sb.append(ui.getPassword());
			sb.append(",");
			sb.append(ui.getName());
			sb.append(",");
			sb.append(ui.getPosition());
			try {
				this.bWriter.write(sb.toString()); // 쓰기
				this.bWriter.write("\n"); // 줄바꿈
			} catch (Exception e) {
				return "fail";
			}
		}
		return "success";
	}

	public String updateEmployeeInfo(List<UserInfo> userList) { // 매장관리 - 직원수정
		StringBuilder sb = null;
		for (UserInfo ui : userList) {
			sb = new StringBuilder();
			sb.append(ui.getEmployeeCode());
			sb.append(",");
			sb.append(ui.getPassword());
			sb.append(",");
			sb.append(ui.getName());
			sb.append(",");
			sb.append(ui.getPosition());
			try {
				this.bWriter.write(sb.toString()); // 쓰기
				this.bWriter.write("\n"); // 줄바꿈
			} catch (Exception e) {
				return "fail";
			}
		}
		return "success";
	}

	public String deleteProduct(List<Product> productList) { // 상품관리 - 상품삭제
		StringBuilder sb = null;
		for (Product p : productList) {
			sb = new StringBuilder();
			sb.append(p.getProductCode());
			sb.append(",");
			sb.append(p.getProductName());
			sb.append(",");
			sb.append(p.getCategory());
			sb.append(",");
			sb.append(p.getPrice());
			sb.append(",");
			sb.append(p.getStock());
			try {
				this.bWriter.write(sb.toString()); // 쓰기
				this.bWriter.write("\n"); // 줄바꿈
			} catch (Exception e) {
				return "fail";
			}
		}
		return "success";
	}

	public String updateProductInfo(List<Product> productList) { // 상품관리 - 상품수정
		StringBuilder sb = null;
		for (Product p : productList) {
			sb = new StringBuilder();
			sb.append(p.getProductCode());
			sb.append(",");
			sb.append(p.getProductName());
			sb.append(",");
			sb.append(p.getCategory());
			sb.append(",");
			sb.append(p.getPrice());
			sb.append(",");
			sb.append(p.getStock());
			try {
				this.bWriter.write(sb.toString()); // 쓰기
				this.bWriter.write("\n"); // 줄바꿈
			} catch (Exception e) {
				return "fail";
			}
		}
		return "success";
	}

	public boolean addProduct(Product productInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append(productInfo.getProductCode());
		sb.append(",");
		sb.append(productInfo.getProductName());
		sb.append(",");
		sb.append(productInfo.getCategory());
		sb.append(",");
		sb.append(productInfo.getPrice());
		sb.append(",");
		sb.append(productInfo.getStock());
		try {
			this.bWriter.write(sb.toString()); // 작성
			this.bWriter.newLine(); // 줄바꿈
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getNewProductCode() {
		String currentLine = null;
		String newProductCode = "0000";
		try {
			while ((currentLine = this.bReader.readLine()) != null) {
//				0001,ルフィの皮,加工食品,20000,100
				newProductCode = currentLine.substring(0, currentLine.indexOf(","));
				// 0001, indexOf의 값=4
				// substring(시작자리, 끝나는자리 - 1) = 0001 추출
				// while문을 돌면서 0001 > 0002로 읽음
			}

		} catch (IOException e) {
			return "fail";
		}
		int intValue = Integer.parseInt(newProductCode) + 1;
		newProductCode = String.valueOf(intValue);
		return intValue < 10 ? "000" + newProductCode
				: intValue < 100 ? "00" + newProductCode
						: intValue < 1000 ? "0" + newProductCode : intValue < 10000 ? newProductCode : "full";
	}

//	public String addProduct(String[][] extractedData) { // 신규상품추가
//		// productCode=0001&productName=ルフィの皮&category=加工食品&price=20000&stock=100
//		// 0001,ルフィの皮,加工食品,20000,100
//		StringBuilder sb = new StringBuilder();
//		sb.append(extractedData[0][1]); // productCode
//		sb.append(",");
//		sb.append(extractedData[1][1]); // productName
//		sb.append(",");
//		sb.append(extractedData[2][1]); // category
//		sb.append(",");
//		sb.append(extractedData[3][1]); // price
//		sb.append(",");
//		sb.append(extractedData[4][1]); // stock
//		try {
//			this.bWriter.write(sb.toString()); // 작성
//			this.bWriter.newLine(); // 줄바꿈
//			return "success";
//		} catch (Exception e) {
//			this.pu.println("상품추가실패");
//		}
//		return "fail";
//	}

	public String updateUserInfo(List<UserInfo> userList) { // 마이페이지 정보 변경 - 수정
		StringBuilder sb = null;
		for (UserInfo ui : userList) {
			sb = new StringBuilder();
			sb.append(ui.getEmployeeCode());
			sb.append(",");
			sb.append(ui.getPassword());
			sb.append(",");
			sb.append(ui.getName());
			sb.append(",");
			sb.append(ui.getPosition());
			try {
				this.bWriter.write(sb.toString()); // 쓰기
				this.bWriter.write("\n"); // 줄바꿈
			} catch (Exception e) {
				return "fail";
			}
		}
		return "success";
	}

	public List<UserInfo> getUserList() { // 마이페이지 정보 변경 - 읽기
		List<UserInfo> userList = new ArrayList<>();
		String record = null;
		String[] recordSplit = null;
		try {
			while ((record = this.bReader.readLine()) != null) {
//				9797,9797,looppy,노예
				recordSplit = record.split(",");
				userList.add(new UserInfo(recordSplit[0], recordSplit[1], recordSplit[2], recordSplit[3]));
			}
		} catch (Exception e) {
		}

		return userList;
	}

	public boolean updateStock(List<Product> allProducts) {
		StringBuilder sb = null;
		for (Product p : allProducts) {
			sb = new StringBuilder();
			// processed0001,루피가죽,가공식품,200000,15
			sb.append(p.getProductCode());
			sb.append(",");
			sb.append(p.getProductName());
			sb.append(",");
			sb.append(p.getCategory());
			sb.append(",");
			sb.append(p.getPrice());
			sb.append(",");
			sb.append(p.getStock());
			try {
				this.bWriter.write(sb.toString());
				this.bWriter.write("\n");
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

//	updateStock?productCode=0001&stock=5&productCode0002&stock=10&productCode=0003=stock=5
	public List<Product> getAllProductInfo() {
		List<Product> allProducts = new ArrayList<>();
		String record = null;
		try {
			while ((record = this.bReader.readLine()) != null) {
//		processed0001,루피가죽,가공식품,2000,15
				String[] splitRecord = record.split(",");
				// String productCode, String productName, String category, int price, int stock
				allProducts.add(new Product(splitRecord[0], splitRecord[1], splitRecord[2],
						Integer.parseInt(splitRecord[3]), Integer.parseInt(splitRecord[4])));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return allProducts;
	}

	// 매출입력
	public String insertSalesInfo(String[][] extractedData) {
		// date=2023-08-30&time=20:43&total=2000
		// 2023-08-29,20:56,200000
		StringBuilder sb = new StringBuilder();
		sb.append(extractedData[0][1]); // date
		sb.append(",");
		sb.append(extractedData[1][1]); // time
		sb.append(",");
		sb.append(extractedData[2][1]); // total
		try {
			this.bWriter.write(sb.toString()); // 작성
			this.bWriter.newLine(); // 줄바꿈
			return "success";
		} catch (Exception e) {
			this.pu.println("쓰기실패");
		}
		return "fail";
	}

	public String getProductInfo(String[][] extractedData) { // mybatis / sql 데이터베이스에 접근하는 함수
		String record = null;
		String[] recordSplit = null;

		try {
			while ((record = this.bReader.readLine()) != null) {
				recordSplit = record.split(",");
				// getProductInfo?productCode=snack0001
				// 루피가죽,processed0001,가공식품
				if (recordSplit[0].equals(extractedData[0][1])) {
//					"productCode=processed0001&productName=루피가죽&category=가공식품&price=200000&stock=15"
					return this.pu.makeTransferData(
							new String[] { "productCode", "productName", "category", "price", "stock" }, recordSplit);
//					return "productCode=" + recordSplit[1] + "&productName=" + recordSplit[0] + "&category="
//							+ recordSplit[2];
				}
			}
			;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "fail";
	}

	public String login(String[][] extractedData) { // mybatis / sql 데이터베이스에 접근하는 함수
		String record = null;
		String[] recordSplit = null;

		try {
			while ((record = this.bReader.readLine()) != null) {
				// 9797,9797,loopy,노예
				recordSplit = record.split("\\,");
				if (recordSplit[0].equals(extractedData[0][1]) && recordSplit[1].equals(extractedData[1][1])) {
					return "employeeCode=" + recordSplit[0] + "&name=" + recordSplit[2] + "&position=" + recordSplit[3];
				}
			}
			;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "fail";
	}

	// 생성
	public boolean fileConnection(boolean readOrWrite, String fileName, boolean append) {
		// reardOrWrite : 파일을 읽을 것인지 수정할 것인지
		// fileName : 파일 이름
		// append : 파일 쓰기 종류 (내용 추가 or 내용 덮어쓰기)

		boolean result;
		String ap = new File("").getAbsolutePath(); // 현재 워크스페이스 경로 가져옴
		this.file = new File(ap + "\\src\\" + "\\db\\" + fileName);

		try {
			if (readOrWrite) {
				this.bReader = new BufferedReader(new FileReader(this.file));
			} else {
				if (append) {
					this.writer = new FileWriter(this.file, true); // 파일 내용 추가
				} else {
					this.writer = new FileWriter(this.file); // 파일 내용 덮어쓰기
				}
				this.bWriter = new BufferedWriter(this.writer);
			}
			result = true; // 객체 생성 파일 접근에 성공
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	// 삭제
	public void fileClose(boolean readOrWrite) {
		if (readOrWrite) {
			try {
				if (this.bReader != null) {
					this.bReader.close();
				}
			} catch (Exception e2) {
			}
			try {
				if (this.reader != null) {
					this.reader.close();
				}
			} catch (Exception e) {
			}
		} else {
			try {
				if (this.bWriter != null) {
					this.bWriter.close();
				}
			} catch (Exception e2) {
			}
			try {
				if (this.writer != null) {
					this.writer.close();
				}
			} catch (Exception e) {
			}
		}
	}
}