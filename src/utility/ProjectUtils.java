package utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class ProjectUtils {

	public String makeTransferData(String jobCode, String name, String value) {
		return jobCode + "?" + name + "=" + value;
	}

	public String makeTransferData(String jobCode, String[] names, String[] values) {
//		System.out.println(Arrays.deepToString(data)); // 배열에 담긴 값 출력
//		System.out.println(jobCode + "?employeeCode=" + data[0] + "&password=" + data[1]);
		StringBuilder sb = new StringBuilder(); // 메모리 절약을 위해 StringBuilder 사용
		sb.append(jobCode);
		sb.append("?");
		for (int i = 0; i < values.length; i++) {
			sb.append(names[i]);
			sb.append("=");
			sb.append(values[i]);
			sb.append(i < values.length - 1 ? "&" : "");
		}
		return sb.toString();
	}

	public String makeTransferData(String[] names, String[] values) {
		StringBuilder sb = new StringBuilder(); // 메모리 절약을 위해 StringBuilder 사용
		sb.append(names);
		sb.append("?");
		for (int i = 0; i < values.length; i++) {
			sb.append(names[i]);
			sb.append("=");
			sb.append(values[i]);
			sb.append(i < values.length - 1 ? "&" : "");
		}
		return sb.toString();
	}

	// print util
	public void print(String s) {
		System.out.print(s);
	}

	public void println(String s) {
		System.out.println(s);
	}

	// extract util
	// employeeCode=loopy&password=1234
	public String[][] extractData(String data) { // 0사번, 1비번, 2이름, 3등급

		StringTokenizer st = new StringTokenizer(data, "=&"); // 4개로 쪼개짐
		String[][] extractedData = new String[st.countTokens() / 2][2]; // name, value

		if (st.countTokens() < 2)
			return null;

		for (int i = 0; st.hasMoreTokens(); i++) {
			extractedData[i][0] = st.nextToken(); // name 0-0 : employeeCode
			extractedData[i][1] = st.nextToken(); // value 0-1 : loopy
													// name 1-0 : password
													// value 1-1 1234
		}
//		for (String[] arr : extractedData) {
//			for (String s : arr) {
//				System.out.println(s);
//			}
//		}
		return extractedData;
	}

	public String getDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public String getTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
	}
}
