import com.jinter.core.Jinter;
import com.jinter.exception.JinterException;

public class JinterTest {
	public static void main(String[] args) {
		String jsonStr = "{\"tableName\":\"Jinter\"," + "\"jsonDataType\":" + "["
				+ "{\"isNullable\":false,\"columnName\":\"id\",\"columnType\":\"int\",\"columnLength\":11,\"isPrimaryKey\":1,,\"fkTableAndColumn\":\"test1;id;id\"},"
				+ "{\"isNullable\":true,\"columnName\":\"name\",\"columnType\":\"varchar\",\"columnLength\":256,\"isPrimaryKey\":0},"
				+ "{\"isNullable\":true,\"columnName\":\"time\",\"columnType\":\"date\",\"columnLength\":0,\"isPrimaryKey\":0},"
				+ "{\"isNullable\":true,\"columnName\":\"remark\",\"columnType\":\"varchar\",\"columnLength\":256,\"isPrimaryKey\":0}"
				+ "]," + "\"jsonDataVal\":" + "["
				+ "[{\"id\":3} ,{ \"name\":\"xiaoming\"},{\"time\": \"2016-07-09 00:00:00\"},{\"remark\":\"hello test\"}],"
				+ "[{\"id\":4} ,{ \"name\":\"xiaoming\"},{\"time\": \"2016-07-10 00:00:00\"},{\"remark\":\"ello test\"}]"
				+ "]" + "}";
		Jinter jinter = new Jinter();
		try {
			jinter.goFetch(jsonStr);
		} catch (JinterException e) {
			System.out.println(e.getMessage());
		}
	}
}
