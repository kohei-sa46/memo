package memo;

public class Utils {

	public static String escapeHtml(String text) {//一覧表示用
		String escapedString = text
				.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&#39;")
				.replace("\n", "")
				.replaceAll(" +", "")
				.replaceAll("　+", "")
				.replace("<br\\s*/?>", " ");

		// タグ排除後2000文字を超えている場合、2000文字までにトリミング
		if (escapedString.length() > 2000) {
			escapedString = escapedString.substring(0, 2000);
		}

		return escapedString;
	}


	public static String escapeHtmlDetail(String text) {//個別表示用
		String escapedString = text
				.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&#39;")
				.replace("\n", "<br>");


		return escapedString;
	}
	//メモ更新時、ポップアップでJavaScript用にエスケープした文字を
	//更新反映時に元に戻すエスケープ
	public static String unescapeHtml(String escapedContent) {
		if (escapedContent == null) {
			return null;
		}

		return escapedContent
				.replace("&lt;", "<")
				.replace("&gt;", ">")
				.replace("&amp;", "&")
				.replace("&quot;", "\"")
				.replace("&#39;", "'")
				.replace("\\n", "\n");  
	}




}
