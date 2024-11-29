package memo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class dummy {

	private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl"; // JDBC URLを適宜変更
	private static final String DB_USER = "info"; 
	private static final String DB_PASSWORD = "pro";

	public static void main(String[] args) {
		String[] insertQueries = {
				"INSERT INTO memos (content, user_id) VALUES ('コーラ　お茶'|| CHR(10) ||'グミ', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('日曜18-20'|| CHR(10) ||'1000円', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('240,000', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('美馬', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('ティッシュ　歯磨き粉　'|| CHR(10) ||'シャンプー', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('鈴木　08035234344', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('土曜日'|| CHR(10) ||'13:00', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('焼肉', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('再配達たのむ', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('記帳する', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('充電器探す', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('あ, い, う, え, お.'|| CHR(10) ||' a, i, u, e, o. か, き, く, け, こ. ka, ki, ku, ke, ko. さ, し, す, せ, そ. sa, shi, su, se, so. た, ち, つ, て, と. ta, chi, tsu, te, to.あ, い, う, え, お. a, i, u, e, o. か, き, く, け, こ. ka, ki, ku, ke, ko. さ, し, す, せ, そ. sa, shi, su, se, so. た, ち, つ, て, と. ta, chi, tsu, te, to.あ, い, う, え, お. a, i, u, e, o. か, き, く, け, こ. ka, ki, ku, ke, ko. さ, し, す, せ, そ. sa, shi, su, se, so. た, ち, つ, て, と. ta, chi, tsu, te, to.あ, い, う, え, お. a, i, u, e, o. か, き, く, け, こ. ka, ki, ku, ke, ko. さ, し, す, せ, そ. sa, shi, su, se, so. た, ち, つ, て, と. ta, chi, tsu, te, to.', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('828 3500 - 460', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('ゴミ袋', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('21-27', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('ライター捨てる', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('ウット', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('ノルウェージャン', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('単文とは主語と述語の1組の文です．なお，日本語文では主語は省かれたり，主語のない場合もあります．文例1の主語は「原子は」と「世界の平均気温は」で，述語は「なる」と「上昇している」です．', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('アメリカ', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('鏡', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('解約する'|| CHR(10) ||'10月中', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('フィルター買う', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('リードル', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('5400 -460 -8000', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('鍵', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('京都府京都市東山区三条通南二筋目白川筋西入ル二丁目北木之元町', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('35000 55000' || CHR(10) ||'-22000 -10000 -5000', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('9/27 10/3' || CHR(10) ||'10/5 10/15 10/21 10/23', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('ネット代払う3000' || CHR(10) || '月末', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('コーヒー' || CHR(10) || '　　　　カフェオレ', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('11/11' || CHR(10) || '17:00', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('ジム解約', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('28日　新宿', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('火', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('川崎', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('カバ　GE', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('大阪府大阪市中央区千日前2丁目11-5', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('benjazzy', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('9000 5.8 tr9', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('電気代払う 7000', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('12/04'|| CHR(10) ||　'美容院　16:00', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('808' || CHR(10) ||'809 813 815 822', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('水　お茶　' || CHR(10) || 'お菓子', 'Admin')",
				"INSERT INTO memos (content, user_id) VALUES ('08031756274', 'Admin')"
		};


		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

			conn.setAutoCommit(false);

			// 5秒おきに実行
			for (String query : insertQueries) {
				try (PreparedStatement pstmt = conn.prepareStatement(query)) {
					pstmt.executeUpdate();
					System.out.println(query);


					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// コミット
			conn.commit();
			System.out.println("すべてのINSERTが成功しました。");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
