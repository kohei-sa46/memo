package memo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

	public void insertUser(String userId, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO users (user_id, password,theme_mode) VALUES (?, ?,'light')";

        try {
            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    
    public boolean validateUser(String userId, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
    	
    	String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
    	
        try {
        	conn = new OracleConnector().getCn(); 
        	ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, password);
            rs = ps.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
        	e.printStackTrace();  
        }
        return rs.next();
    }
    //モード状態をトグルボタン押した時に更新
    public void updateTheme(String userId, String theme) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        String sql = "UPDATE users SET theme_mode = ? WHERE user_id = ?";

        try {
            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, theme);
            ps.setString(2, userId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("テーマ更新失敗: ユーザーが見つかりませんでした。");
            } else {
                System.out.println("テーマが正常に更新されました。"); // 成功メッセージを追加
            }
        } catch (SQLException e) {
            System.out.println("SQLエラー: " + e.getMessage()); // エラーメッセージを出力
            throw e;
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    //現在のモードを取得(ログイン時)
    public String getCurrentTheme(String userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT theme_mode FROM users WHERE user_id = ?";

        try {
            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("theme_mode");
            } else {
                return "light"; // ユーザーが見つからない場合はデフォルトで 'light'
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

}