package frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import backend.Controller;
import beans.Product;
import beans.UserInfo;
import utility.ProjectUtils;
// String[][] clientData 이중배열 출력 방법 : this.pu.println(Arrays.deepToString(clientData));
// List<Product> productList 리스트 셋 방법 : productList.get(i).setProductName(clientData[1][1]);

// 포스기

// 상품코드(바코드) 를 입력하면 상품이 선택되고 장바구니에 담기고 구매할 수 있다. -> 상품조회, 구매기능
// 매장 관리 
// 상품관리 - 상품 추가, 수정, 삭제
// 직원 관리 - 직원 등록(직급, 이름, 비밀번호, 아이디)
// 매출 조회 - 월별, 일별, 순이익(매입가-판매가)
//----------------------------------------------

//등급 : 노예 < 매니저 < 사장님
// A. 시작 - 로고 홈화면 -> 계속하려면 Enter
// B. 로그인(사번, 비번, 이름, 등급)
// C. 메뉴
//		1.판매 - 노예 v
//			a.상품조회 v
//			b.장바구니 추가 v
//			c.결제 v
//			d.영수증 출력 v
//		2.상품관리 v
//			a.상품추가(상품코드) v
//				- 등록 : 노예
//			b.상품수정 v
//				- 등록 : 노예
//			c.상품삭제 - 매니저 vv
//		3.매장관리 - 사장님
//			a.직원등록
//				- 초기비번 : 등록일 + 노예순서
//			b.직원수정 vv
//			c.직원삭제 vv
//			d.매출조회
//				- 일별조회 v
//				- 월별조회 v
//				- 기간별조회 v
//		4.마이페이지
//			a. 내 정보 확인 / 수정 v
// D. 종료

public class FrontEnd {
	private Scanner sc;
	private ProjectUtils pu;
	private UserInfo ui;
	private List<Product> order;

	public FrontEnd() {
//		System.out.println(this.logo()); // this : 현재 실행중인 객체의 로고 함수
		this.sc = new Scanner(System.in);
		this.pu = new ProjectUtils();
		this.run();
		this.sc.close();
	}

	private void run() {
		this.pu.println(this.getTitle()); // 타이틀 불러옴
		this.sc.nextLine(); // enter입력
		while (true) {
			if (this.login()) {
				this.main();
				break;
			} else {
				this.pu.println("ログインに失敗しました。");
			}
		}
	}

	private boolean login() {
		String employeeCode;
		String password;
		String[] names = { "employeeCode", "password" };
		String serverData = null;

		this.pu.println(this.getTitle("ログイン"));
		this.pu.println("社員番号を入力してください:	");
		employeeCode = this.input(); // nextLine
		this.pu.println("暗証番号を入力してください:	");
		password = this.input();

		// login?employeeCode=loopy&password=1234
		serverData = new Controller()
				.entrance(this.pu.makeTransferData("login", names, new String[] { employeeCode, password }));// jobCode=login,
		// Controller.java로 데이터 전송
		System.out.println(serverData);
		if (!serverData.equals("fail")) {
			// employeeCode, name, position
			String[][] extractedData = this.pu.extractData(serverData);
			this.ui = new UserInfo(extractedData[0][1], extractedData[1][1], extractedData[2][1]); // loopy, 노예
			return true;
		}
//		this.pu.println(serverData); // 로그인 데이터
		return false;
	}

	private void main() {
		String select = null;
		String serverData = null;
		this.pu.println(this.getTitle(this.ui.getName() + "様 (" + this.ui.getPosition() + ") ようこそ!"));
		boolean run = true;
		while (run) {
			this.pu.println(this.getMenu(new String[] { "商品販売", "商品管理", "店舗管理", "マイページ" }));
			select = this.input(); // nextLine
			switch (select) {
			case "1": // 商品販売
				this.sales();
				break;

			case "2": // 商品管理
				this.productManagement();
				break;

			case "3": // 店舗管理
				this.storeManagement();
				break;

			case "4": // マイペ-ジ
				this.myPage();
				break;

			case "0": // 終了
				run = false;
				this.pu.println("システムを終了します。");
				break;

			default:
				this.pu.println("もう一度選択してください。");
				break;
			}
		}
	}

	private void productManagement() {
		boolean run = true;
		String select = null;

		this.pu.print(getTitle("商品管理"));
		while (run) {
			this.pu.println(this.getMenu(new String[] { "商品追加", "商品修正", "商品削除" }));
			select = this.input(); // nextLine
			switch (select) {
			case "1":
				this.addProduct();
				this.getAllProductInfo();
				break;
			case "2":
				this.updateProductInfo();
				this.getAllProductInfo();
				break;
			case "3":
				this.deleteProduct();
				this.getAllProductInfo();
				break;
			case "0":
				run = false;
				break;

			default:
				System.out.println("???");
				break;
			}
		}
	}

//	private void deleteProduct() {
//	String productCode = null;
//	String serverData = null;
//	List<Product> productList = new ArrayList<>();
//	StringBuilder sb = new StringBuilder();
	// UserInfo에서 등급확인(매니저 이상부터 가능)
//		if (this.ui.getPosition().equals("奴隷")) {
//			this.pu.println("권한이 없습니다.");
//			return; // productManagement()의 while문 다시 실행
//		}
	// 전체 상품 리스트 가져오기
//	this.getAllProductInfo();
	// 숙제1 : 상품삭제 잡 완성 > 상품전체 가져오기, 상품코드 입력받아 해당 상품 인덱스 찾기, 해당 라인 읽고 삭제

	// 지울 상품 상품코드 서버로 보내기
	// 메모장에서 해당상품 제거하기
	// 성공여부 리턴하기
//	}

	private void deleteProduct() {
		Product p;
		String serverData = null;
		String[][] extractedData = null;
		String employeeCode = null;
		String productCode, productName, category;
		int price, stock;

		// UserInfo에서 등급확인(매니저 이상부터 가능)
		if (this.ui.getPosition().equals("奴隷")) {
			this.pu.println("권한이 없습니다.");
			return; // productManagement()의 while문 다시 실행
		}
		// 전체 상품 리스트 가져오기
		this.getAllProductInfo();

//		0001,ルフィの皮,加工食品,20000,100
		this.pu.print("削除する商品コードを入力して下さい: ");
		productCode = this.input();

//		productCode=0001&name=ルフィの皮&category=加工食品&price=20000&stock=100
//		productCode=0002&name=チ-ズサンド&category=加工食品&price=5000&stock=65
//		productCode=0003&name=ホットドッグ&category=加工食品&price=7000&stock=100
		p = new Product();
		serverData = new Controller().entrance(this.pu.makeTransferData("deleteProduct", "商品コード", productCode));
	}

	private void getAllProductInfo() {
		String serverData = null;
		List<Product> productList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		serverData = new Controller().entrance("getAllProductInfo");
//		System.out.println("allProductInfo: " + serverData);
//		productCode=0001&name=ルフィの皮&category=加工食品&price=20000&stock=100
//		productCode=0002&name=チ-ズサンド&category=加工食品&price=5000&stock=65
//		productCode=0003&name=ホットドッグ&category=加工食品&price=7000&stock=100
		StringTokenizer st = new StringTokenizer(serverData, "=&");
		Product product;
		while (st.hasMoreTokens()) {
			product = new Product();
			st.nextToken(); // productCode
			product.setProductCode(st.nextToken()); // 0001
			st.nextToken(); // name
			product.setProductName(st.nextToken()); // ルフィの皮
			st.nextToken(); // category
			product.setCategory(st.nextToken()); // 加工食品
			st.nextToken(); // price
			product.setPrice(Integer.parseInt(st.nextToken())); // 20000
			st.nextToken(); // stock
			product.setStock(Integer.parseInt(st.nextToken())); // 100
			productList.add(product);
		}
//		Set List Queue Map
//		extends Object
//		Collection > List > ArrayList
//		for(Product p : productList) {}

//		==같은 코딩==
//		productList.forEach(System.out::println);
//		productList.forEach(p -> System.out.println(p));
//		==========

//		productList.forEach(p -> {
//			System.out.println(p.getProductCode() + ":" +
//								p.getProductName() + ":" +
//								p.getPrice());
//		});

		sb.append("================================================================================\n");
		sb.append("No. 商品コード\t商品名\t\tカテゴリー\t単価\t在庫\n");
		sb.append("--------------------------------------------------------------------------------\n");
		for (int i = 0; i < productList.size(); i++) {
			sb.append(String.valueOf(i + 3));
			sb.append(". ");
			sb.append(productList.get(i).getProductCode());
			sb.append("\t");
			sb.append("\t");
			String name = productList.get(i).getProductName();
			sb.append(name + (name.length() < 4 ? "	".repeat(4 - name.length()) : ""));
			sb.append("\t");
			sb.append(productList.get(i).getCategory());
			sb.append("\t");
			sb.append("\t");
			sb.append(productList.get(i).getPrice());
			sb.append("\t");
			sb.append(productList.get(i).getStock());
			sb.append("\n");
		}
		sb.append("================================================================================");
		this.pu.println(sb.toString());
	}

	private boolean updateProductInfo() {
		Product p;
		String serverData = null;
		String[][] extractedData = null;
		String employeeCode = null;
		String productCode, productName, category;
		int price, stock;

//		모든 상품 정보 가져오기
		this.getAllProductInfo();

//		0001,ルフィの皮,加工食品,20000,100
//		수정가능: 이름,카테고리,가격,재고
		this.pu.print("変更する商品コードを入力して下さい: ");
		productCode = this.input();
		this.pu.print("新しい商品名を入力して下さい: ");
		productName = this.input();
		this.pu.print("新しいカテゴリーを入力して下さい: ");
		category = this.input();
		this.pu.print("新しい単価を入力して下さい: ");
		price = this.sc.nextInt();
		this.pu.print("新しい在庫を入力して下さい: ");
		stock = this.sc.nextInt();
		this.input();

//		productCode=0001&name=ルフィの皮&category=加工食品&price=20000&stock=100
//		productCode=0002&name=チ-ズサンド&category=加工食品&price=5000&stock=65
//		productCode=0003&name=ホットドッグ&category=加工食品&price=7000&stock=100
//		전체 List<Product> 가져오고, 해당 상품 정보 수정, 수정된 List를 메모장에 덮어쓰기
//		출력 : 이름,상품코드,카테고리,가격,재고
		p = new Product();
		serverData = new Controller().entrance(this.pu.makeTransferData("updateProductInfo",
				new String[] { "商品コード", "商品名", "カテゴリー", "単価", "在庫" },
				new String[] { productCode, productName, category, String.valueOf(price), String.valueOf(stock) }));

		if (!serverData.equals("fail") && serverData != null) {
			p.setProductCode(this.pu.extractData(serverData)[0][1]);
			this.pu.println("\n修正を完了しました。");
			return true;
		} else {
			this.pu.println("修正に失敗しました。");
		}
		return false;
	}

//		if (!serverData.equals("fail") && serverData != null) {
//			p.setProductName(values[1]);
//			p.setCategory(values[2]);
//			p.setPrice(Integer.parseInt(values[3]));
//			p.setStock(Integer.parseInt(values[4]));

	private void addProduct() { // 상품추가 - productCode 자동생성
		String[] labels = { "商品コード", "商品名", "カテゴリー", "単価", "在庫" };
		String[] values = new String[5];
		String serverData = null;
//		상품코드 + 1 가져오기
//		jobCode = getNewProductCode
		values[0] = new Controller().entrance("getNewProductCode");

//		추가할 상품정보 요청 makeTransferData로 작성
//		0001,ルフィの皮,加工食品,20000,100
		for (int i = 1; i < values.length; i++) { // 1~4까지
			this.pu.print(labels[i] + ": ");
			values[i] = this.input();
		}
		this.pu.println("商品を登録します(Enter)");
		this.input();
		// addProduct?productCode=0004&name=testname&category=testcate&price=1111&stock=20
		serverData = new Controller().entrance(this.pu.makeTransferData("addProduct",
				new String[] { "productCode", "name", "category", "price", "stock" }, values));
		System.out.println(serverData);

//		성공실패 여부 받아오고 출력하기
		String[] names = {};
	}

	private void storeManagement() {
		boolean run = true;
		String select = null;
		this.pu.print(getTitle("店舗管理"));
		while (run) {
			this.pu.println(this.getMenu(new String[] { "職員登録", "職員修正", "職員削除", "売上確認" }));

			select = this.input(); // nextLine

			switch (select) {
			case "1":
				this.addEmployee();
				this.getAllEmployeesInfo();
				break;
			case "2":
				this.updateEmployeeInfo();
				this.getAllEmployeesInfo();
				break;
			case "3":
				this.deleteEmployee();
				this.getAllEmployeesInfo();
				break;
			case "4":
				this.viewSales();
				break;
			case "0":
				run = false;
				break;

			default:
				break;
			}
		}
	}

	private void deleteEmployee() {
		UserInfo ui;
		String serverData = null;
		String[][] extractedData = null;
		String employeeCode = null;
		String password, name, position;

		// UserInfo에서 등급확인(사장만 가능)
		if (!this.ui.getPosition().equals("社長")) {
			this.pu.println("권한이 없습니다.");
			return; // storeManagement()의 while문 다시 실행
		}
		// 전체 직원 리스트 가져오기
		this.getAllEmployeesInfo();

		this.pu.print("削除する社員番号を入力して下さい: ");
		employeeCode = this.input();

		ui = new UserInfo();
		serverData = new Controller().entrance(this.pu.makeTransferData("deleteEmployee", "社員番号", employeeCode));
	}

	private boolean updateEmployeeInfo() {
		String serverData = null;
		String[][] extractedData = null;
		String employeeCode;
		String position;

		// UserInfo에서 등급확인(사장님만 가능)
		if (!this.ui.getPosition().equals("社長")) {
			this.pu.println("권한이 없습니다.");
			return false; // productManagement()의 while문 다시 실행
		}

//		9797,9797,loopy,奴隷
		this.getAllEmployeesInfo();
		this.pu.print("変更する社員番号を入力して下さい: ");
		employeeCode = this.input();
		this.pu.print("新しい役職を入力して下さい: ");
		position = this.input();
//		입력 : updateEmployeeInfo?employeeCode=9797&position=奴隷
//		전체 List<UserInfo> 가져오고, 해당 사원 정보 수정, 수정된 List를 메모장에 덮어쓰기
//		출력 : 사번, 이름, 등급
		serverData = new Controller().entrance(this.pu.makeTransferData("updateEmployeeInfo",
				new String[] { "employeeCode", "position" }, new String[] { employeeCode, position }));

		if (!serverData.equals("fail") && serverData != null) {
			this.ui.setPosition(this.pu.extractData(serverData)[1][1]);
			// [[employeeCode, 9797], [position, 노예]]
			this.pu.println("修正に成功しました。");
			return true;
		} else {
			this.pu.println("修正に失敗しました。");
		}
		return false;
	}

	
	private void addEmployee() { // 매장관리 - 직원추가 - employeeCode 년+월+번째 자동생성
		String[] labels = { "社員番号", "パスワード", "職員名", "役職" };
		String[] values = new String[4];
		String serverData = null;
//		사원번호 + 1 가져오기
//		jobCode = getNewEmployeeCode
		values[0] = new Controller().entrance("getNewEmployeeCode");

//		추가할 상품정보 요청 makeTransferData로 작성
//		0001,ルフィの皮,加工食品,20000,100
		for (int i = 1; i < values.length; i++) { // 1~4까지
			this.pu.print(labels[i] + ": ");
			values[i] = this.input();
		}
		this.pu.println("商品を登録します(Enter)");
		this.input();
		// addProduct?productCode=0004&name=testname&category=testcate&price=1111&stock=20
		serverData = new Controller().entrance(this.pu.makeTransferData("addProduct",
				new String[] { "productCode", "name", "category", "price", "stock" }, values));
		System.out.println(serverData);

//		성공실패 여부 받아오고 출력하기
		String[] names = {};
	}
	
	private void getAllEmployeesInfo() {
		String serverData = null;
		List<UserInfo> userList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		serverData = new Controller().entrance("getAllEmployeesInfo");
//		System.out.println("allEmployeeInfo: " + serverData);
//		9797,9797,loopy,奴隷
//		5252,5252,pororo,マネージャー
//		4444,4444,beaver,社長
		StringTokenizer st = new StringTokenizer(serverData, "=&");
		UserInfo ui;
		while (st.hasMoreTokens()) {
			ui = new UserInfo();
			st.nextToken(); // employeeCode
			ui.setEmployeeCode(st.nextToken()); // 9797
			st.nextToken(); // password
			ui.setPassword(st.nextToken()); // 9797
			st.nextToken(); // name
			ui.setName(st.nextToken()); // loopy
			st.nextToken(); // position
			ui.setPosition(st.nextToken()); // 奴隷
			userList.add(ui);
		}

		sb.append("================================================================================\n");
		sb.append("社員番号\t社員名\t役職\n");
		sb.append("--------------------------------------------------------------------------------\n");
		for (int i = 0; i < userList.size(); i++) {
			sb.append(userList.get(i).getEmployeeCode());
			sb.append("\t");
			String name = userList.get(i).getName();
			sb.append(name + (name.length() < 4 ? "	".repeat(4 - name.length()) : ""));
			sb.append("\t");
			sb.append(userList.get(i).getPosition());
			sb.append("\t");
			sb.append("\n");
		}
		sb.append("================================================================================");
		this.pu.println(sb.toString());
	}

	private void viewSales() {
		boolean run = true;
		String select = null;
		this.pu.println(getTitle("売上確認"));
		while (run) {
			this.pu.print(this.getMenu(new String[] { "일별조회", "월별조회", "기간별조회" }));
			this.pu.println("");
			select = this.input(); // nextLine
			switch (select) {
			case "1":
				this.viewSalesDaily();
				// sus&월&일 정보 받고
				break;
			case "2":
				this.viewSalesMonthly();
				break;
			case "3":
				this.viewSalesByPeriod();
				break;
			case "0":
				run = false;// 돌아가기
				break;
			default:
				break;
			}
		}
	}
	
	private void viewSalesDaily() {
		String serverData = null;
		String day = null;

		if (!this.ui.getPosition().equals("社長")) {
			this.pu.println("권한이 없습니다.");
			return;
		}

		this.pu.println("조회할 일자를 선택해주세요(yyyy-MM-dd): ");
		day = this.input();
//		getDailySales?day=2023-09-01
		serverData = new Controller().entrance(this.pu.makeTransferData("viewSalesDaily", "day", day));
		if (!serverData.equals("fail")) {
			this.pu.println(day + "일 매출은 ￥" + serverData + "입니다.");
		} else {
			this.pu.println("매출정보를 가져오지 못했습니다.");
		}
	}

	private void viewSalesByPeriod() {
		String serverData = null;
		String[] dates = new String[2];
		
		if (!this.ui.getPosition().equals("社長")) {
			this.pu.println("권한이 없습니다.");
			return;
		}
		this.pu.print("시작일을 입력해주세요(yyyy-MM-dd): ");
		dates[0] = this.input();
		this.pu.print("종료일을 입력해주세요(yyyy-MM-dd): ");
		dates[1] = this.input();
		
//		viewSalesByPeriod?startDate=2023-09-04&endData=2023-09-11
		serverData = new Controller().entrance(this.pu.makeTransferData(
				"viewSalesByPeriod", 
				new String[] { "startDate", "endDate" }, 
				dates));
System.out.println(dates[0] + " ~ " + dates[1] + "의 매출: " + serverData);
	}
//	2023-09-04,18:47,5000

	private void viewSalesMonthly() {
//		0. 권한 확인
//		1. 월 선택 입력받기
//		2. 해당 월 정보 가져오기 (메모장에서 컬렉션 객체로 변환)
//		3. 매출 계산 (해당월 매출 전부 합치기)
//		4. 출력
		String serverData = null;
		String month = null;

		if (!this.ui.getPosition().equals("社長")) {
			this.pu.println("권한이 없습니다.");
			return;
		}

		this.pu.println("조회할 월을 선택해주세요: ");
		month = this.input();
		month = month.length() < 2 ? "0" + month : month;
//		getMonthlySales?month=9
		serverData = new Controller().entrance(this.pu.makeTransferData("getMonthlySales", "month", month));
		if (!serverData.equals("fail")) {
			this.pu.println(month + "월 매출은 ￥" + serverData + "입니다.");
		} else {
			this.pu.println("매출정보를 가져오지 못했습니다.");
		}
	}

	private void myPage() {
		boolean run = true;
		String select = null;
		this.pu.println(getTitle("マイページ"));
		this.printUserInfo();

		while (run) {
			this.pu.print(this.getMenu(new String[] { "情報修正" }));
			select = this.input(); // nextLine
			switch (select) {
			case "1":
				if (this.updateUserInfo()) {
					this.printUserInfo();
				}
				break;
			case "0":
				run = false;// 돌아가기
				break;
			default:
				break;
			}
		}
	}

	private void printUserInfo() {
//		4444,4444,beaver,社長
//		사번,비번,이름,등급 (노예,매니저,사장님)
//		이름: beaver
//		등급: 사장
//		사번: 4444
		StringBuilder sb = new StringBuilder();
		sb.append("名前: ");
		sb.append(this.ui.getName());
		sb.append("\n");
		sb.append("職級: ");
		sb.append(this.ui.getPosition());
		sb.append("\n");
		sb.append("社員番号: ");
		sb.append(this.ui.getEmployeeCode());
		sb.append("\n");
		this.pu.print(sb.toString());
	}

	private boolean updateUserInfo() {
		String serverData = null;
		String[][] extractedData = null;
		String employeeCode = null;
		String name;
		String password;

		this.pu.print("変更する名前を入力して下さい: ");
		name = this.input();
		this.pu.print("新しいパスワードを入力して下さい: ");
		password = this.input();
//		입력 : updateUserInfo?employeeCode=9797&name=looppy&password=9797
//		전체 List<UserInfo> 가져오고, 해당 사원 정보 수정, 수정된 List를 메모장에 덮어쓰기
//		출력 : 사번, 이름, 등급
		serverData = new Controller().entrance(
				this.pu.makeTransferData("updateUserInfo", new String[] { "employeeCode", "name", "password" },
						new String[] { this.ui.getEmployeeCode(), name, password }));

		if (!serverData.equals("fail") && serverData != null) {
			this.ui.setName(this.pu.extractData(serverData)[1][1]);
			this.pu.println("수정이 완료됐습니다.");
			return true;
		} else {
			this.pu.println("네트워크 오류.");
		}
		return false;
	}

	private void sales() {
		order = new ArrayList<>();
		String select = null;
		String serverData = null;
		int Ttotal = 0;

		this.pu.println(getTitle("商品販売"));
		boolean run = true;
		while (run) {
			this.pu.println(this.getMenu(new String[] { "レジへ進む", "数量変更" }));
			this.pu.println("コードを入力してください");
			select = this.input(); // nextLine

			switch (select) {
			case "1": // レジへ進む
				if (this.checkOut(Ttotal)) {
					this.printOrder();
				}
				break;
			case "2": // 수량변경
				this.modifyOrder();
				Ttotal = this.printOrder();
				break;

			case "0":
				run = false;
				// 돌아가기
				break;

			default: // 상품코드
				if (this.addProductToCart(select)) {
					Ttotal = this.printOrder();
				}
				break;
			}
		}
		// 상품코드 입력 시
		// 상품정보 상품코드/상품이름/카테고리
		// 상품추가 될 때마다 전체 주문 재출력
		// 바코드→ 상품추가 / 1.주문완료 2.주문수정 / 0. 돌아가기
		// 상품 갱신될 때마다 전체 주문 재출력
		// 2.주문수정 : 상품 앞에 번호로 상품을 선택해서 수량 변경 가능.
	}

	private boolean checkOut(int Ttotal) {

		// 결제 변수
		int JCB = 0; // 사용자가 지불한 돈
		String serverData;

		// 재고 수정 변수
		String[] names = new String[this.order.size() * 2]; // 5개의 상품을 판 경우 10.(상품코드,재고)
		String[] values = new String[names.length];
		int j = 0;

//			pro.getQuantity(); 사용자가 살 수량
//			pro.getStock(); 재고
		for (Product pro : this.order) { // this.order : 주문 수량
			if (pro.getQuantity() > pro.getStock()) { // 재고 유효성검사
				this.pu.println("在庫が足りません.");
				return false; // checkOut을 바로 빠져나감
			}
		}
		this.pu.println("お支払額を入力して下さい: ");
		JCB = Integer.parseInt(this.input()); // 사용자가 지불한 돈
		if (Ttotal > JCB) {
			this.pu.println("金額が足りません.");
			return false;
		}
		System.out.println(Ttotal);
		this.pu.println("お釣り ￥" + (JCB - Ttotal) + "です."); // 돈 계산

//		 매출입력
//		insertSalesInfo?date=2023-08-30&time=20:43&total=2000
		serverData = new Controller()
				.entrance(this.pu.makeTransferData("insertSalesInfo", new String[] { "date", "time", "total" },
						new String[] { this.pu.getDate(), this.pu.getTime(), String.valueOf(Ttotal) }));

		if (serverData.equals("fail"))
			return false;

//		 재고수정
//		updateStock?productCode=0001&stock=5&productCode0002&stock=10&productCode=0003=stock=5
		for (int i = 0; i < names.length; i++) {
			names[i] = "productCode";
			i++;
			names[i] = "stock";
		}

		for (int i = 0; i < this.order.size(); i++) {
			values[j] = String.valueOf(this.order.get(i).getProductCode());
			j++;
			values[j] = String.valueOf(this.order.get(i).getStock() - this.order.get(i).getQuantity());
			j++;

		}
		serverData = new Controller()// 재고업데이트
				.entrance(this.pu.makeTransferData("updateStock", names, values));

		if (serverData.equals("fail"))
			return false;
		this.pu.println("お会計完了しました.");
		this.printReceipt(JCB);
		this.order = new ArrayList<>(); // 수량 초기화
		return true;

	}

	private void printReceipt(int JCB) {
		StringBuilder sb = new StringBuilder();
		sb.append("No. 商品コード\t商品名\t\tカテゴリー\t単価\t数量\t金額\n");
		int total = 0;
		for (int i = 0; i < this.order.size(); i++) { // getOrder로 전달
			sb.append(String.valueOf(i + 3));
			sb.append(". ");
			sb.append(this.order.get(i).getProductCode());
			sb.append("\t");
			String name = this.order.get(i).getProductName();
			sb.append(name + (name.length() < 4 ? "	".repeat(4 - name.length()) : ""));
			sb.append("\t");
			sb.append(this.order.get(i).getCategory());
			sb.append("\t");
			sb.append("\t");
			sb.append(this.order.get(i).getPrice());
			sb.append("\t");
			sb.append(this.order.get(i).getQuantity());
			sb.append("\t");
			int amount = this.order.get(i).getQuantity() * this.order.get(i).getPrice();
			sb.append(amount);
			total += amount;
			sb.append("\n");
		}
		sb.append("--------------------------------------------------------------------------------\n");
		sb.append("総額:\t￥");
		sb.append(total);
		sb.append("\n--------------------------------------------------------------------------------\n");
		sb.append("お預り:\t￥");
		sb.append(JCB);
		sb.append("\n================================================================================\n");
		sb.append("お釣り:\t￥");
		sb.append(JCB - total);
		this.pu.println(sb.toString());
	}

	private void modifyOrder() {
		this.pu.print("変更する商品 No.を選んで下さい: ");
		String select = this.input();
		// Prooduct 객체의 order의 List타입
		Product selectedProduct = this.order.get(Integer.parseInt(select) - 3); // 사용자가 클릭한 번째
		this.pu.print("変更する数量: ");
		select = this.input();
		selectedProduct.setQuantity(Integer.parseInt(select));
	}

	private boolean addProductToCart(String productCode) { // 장바구니추가
		String serverData;
//			getProductInfo?prooductCode=snack0001
		serverData = new Controller().entrance(this.pu.makeTransferData("getProductInfo", "productCode", productCode));
//		serverData = "productCode=processed0001&productName=루피가죽&category=가공식품";
		if (!serverData.equals("fail")) {
			String[][] extractedData = this.pu.extractData(serverData);
			// List<Product> order
			boolean included = false;
			for (int i = 0; i < this.order.size(); i++) {
				if (this.order.get(i).getProductCode().equals(extractedData[0][1])) {
					// DAO 상품 주문 리스트와 productCode의 recordSplit을 확인하여 필드에 이미 있다면 수량 추가
					this.order.get(i).setQuantity(this.order.get(i).getQuantity() + 1);
					included = true;
					break;
				}
			}
			if (!included) {
				this.order.add(new Product(extractedData[0][1], extractedData[1][1], extractedData[2][1],
						Integer.parseInt(extractedData[3][1]), Integer.parseInt(extractedData[4][1])));
			} // code / name / category / price / stock
			return true;
		} else {
			return false;
		}
	}

	private int printOrder() {
		StringBuilder sb = new StringBuilder();
		sb.append("No. 商品コード\t商品名\t\tカテゴリー\t単価\t在庫\t数量\t金額\n");
		int total = 0;
		for (int i = 0; i < this.order.size(); i++) { // getOrder로 전달
			sb.append(String.valueOf(i + 3));
			sb.append(". ");
			sb.append(this.order.get(i).getProductCode());
			sb.append("\t");
			String name = this.order.get(i).getProductName();
			sb.append(name + (name.length() < 4 ? "	".repeat(4 - name.length()) : ""));
			sb.append("\t");
			sb.append(this.order.get(i).getCategory());
			sb.append("\t");
			sb.append("\t");
			sb.append(this.order.get(i).getPrice());
			sb.append("\t");
			sb.append(this.order.get(i).getStock());
			sb.append("\t");
			sb.append(this.order.get(i).getQuantity());
			sb.append("\t");
			int amount = this.order.get(i).getQuantity() * this.order.get(i).getPrice();
			sb.append(amount);
			total += amount;
			sb.append("\n");
		}
		sb.append("--------------------------------------------------------------------------------\n");
		sb.append("総額:\t￥");
		sb.append(total);
		sb.append("\n");
		sb.append("================================================================================");
		this.pu.println(sb.toString());
//		System.out.println(order.size());
		return total;
	}

	private String getMenu(String[] options) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < options.length; i++) {
//			1. 상품판매		2. 상품관리
//			3. 매장관리		4. 마이페이지
//			0. 종료
			sb.append(i + 1 + ". " + options[i] + (i % 2 == 0 ? "\t" : "\n"));
		}
		sb.append("0. 終了");
		return sb.toString();
	}

	private String getTitle() {
//		String@Builder > 좀 더 빠름
//		StringBuffer
		StringBuilder sb = new StringBuilder();
		sb.append("=============================================================\n");
		sb.append("  _                                  ______             \r\n"
				+ " | |                                 | ___ \\            \r\n"
				+ " | |      ___    ___   _ __   _   _  | |_/ /  ___   ___ \r\n"
				+ " | |     / _ \\  / _ \\ | '_ \\ | | | | |  __/  / _ \\ / __|\r\n"
				+ " | |____| (_) || (_) || |_) || |_| | | |    | (_) |\\__ \\\r\n"
				+ " \\_____/ \\___/  \\___/ | .__/  \\__, | \\_|     \\___/ |___/\r\n"
				+ "                      | |      __/ |                    \r\n"
				+ "                      |_|     |___/                     \r\n" + "");
		sb.append("\r\n" + "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠛⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠀⠞⠛⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⢀⣤⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠛⠉⠉⠛⠿⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠉⠙⠛⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠠⠒⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡌⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⣱⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠁⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⣧⠀⠀⠀⠀⠀⠀⠀⣀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠁⠀⠀⠀⠀⠀⠀⢸⣿⡷⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣶⣶⣶⠶⢤⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠸⣿⣿⣿⣦⣴⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠓⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣤⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣀⣤⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n"
				+ "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\r\n\n\n");
		sb.append("	  	Enterを押してください...\n\n\n");
		sb.append("=============================================================\n");
		return sb.toString();
	}

	private String getTitle(String title) {
		StringBuilder sb = new StringBuilder();
		sb.append("=============================================================\n\n\n");
		sb.append("	");
		sb.append(title);
		sb.append("\n\n\n");
		sb.append("=============================================================\n");
		return sb.toString();
	}

	private String input() {
		return this.sc.nextLine();
	}
}
