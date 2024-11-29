package memo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
public class UpdateMemoServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");

	    // JSONデータをリクエストボディから取得する
	    StringBuilder sb = new StringBuilder();
	    String line;
	    try (BufferedReader reader = request.getReader()) {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	    }
	    String requestBody = sb.toString();

	    // JSONデータを解析
	    JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
	    int memoId = jsonObject.get("memoId").getAsInt();
	    String content = jsonObject.get("content").getAsString();

	    String unescapedContent = Utils.unescapeHtml(content);

	    MemoDAO memoDAO = new MemoDAO();
	    memoDAO.updateMemo(memoId, unescapedContent);

	    // データベースから更新後のメモを取得
	    Memo updatedMemo = memoDAO.getMemoId(memoId);
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd(E) HH:mm:ss");
	    String updatedAt = updatedMemo != null && updatedMemo.getUpdatedAt() != null
	        ? dateFormat.format(updatedMemo.getUpdatedAt())
	        : "";

	    // JSON形式でレスポンスを返す
	    response.setContentType("application/json");
	    PrintWriter out = response.getWriter();

	    String contentWithBr = updatedMemo.getContent().replace("\n", "<br>");

	    String jsonResponse = String.format("{\"status\": \"Success\", \"updatedAt\": \"%s\", \"content\": \"%s\"}",
	    	    updatedAt.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r"),
	    	    contentWithBr.replace("\"", "\\\"").replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r"));

	    out.print(jsonResponse);
	    out.flush();
	}


}
