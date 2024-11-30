package memo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemoDAO {
    //メモの総数取得
    public int getTotalMemoCount(String userId) {
        int totalMemos = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM memos WHERE user_id = ?";
        try {
            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                totalMemos = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return totalMemos;
    }
    //メモ一覧取得
    public List<Memo> getMemoList(String userId, int offset, int limit) {
        List<Memo> memoList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // SQLクエリを修正して、最初の改行までの部分を取得
        String sql = "SELECT memo_id, " +
                     "CASE " +
                     "  WHEN INSTR(content, CHR(10)) > 0 THEN SUBSTR(content, 1, INSTR(content, CHR(10)) - 1) " +
                     "  ELSE content " +
                     "END AS truncated_content, " +
                     "created_at, updated_at, GREATEST(created_at, NVL(updated_at, created_at)) AS latest_at " +
                     "FROM memos WHERE user_id = ? ORDER BY latest_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Memo memo = new Memo();
                memo.setMemoId(rs.getInt("memo_id"));
                String content = Utils.escapeHtml(rs.getString("truncated_content"));
                memo.setContent(content);
                memo.setLatestAt(rs.getTimestamp("latest_at"));
                memoList.add(memo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return memoList;
    }


    // 新規メモ作成用
    public void addMemo(String content, String userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "INSERT INTO memos (content, user_id) VALUES (?, ?)";

        try {

            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, content); 
            ps.setString(2, userId);         
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, null);  
        }
    }

    // メモの個別ページ取得
    public Memo getMemoId(int memoId) {
        Memo memo = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT memo_id, content, created_at, updated_at FROM memos WHERE memo_id = ?";

        try {
            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, memoId);
            rs = ps.executeQuery();
            if (rs.next()) {
                memo = new Memo();
                memo.setMemoId(rs.getInt("memo_id"));
                String content = Utils.escapeHtmlDetail(rs.getString("content"));
                memo.setContent(content);  
                memo.setCreatedAt(rs.getDate("created_at"));
                memo.setUpdatedAt(rs.getDate("updated_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);  
        }
        return memo;
    }

    // メモ削除
    public void deleteMemo(int memoId) {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM memos WHERE memo_id = ?";
        
        try {
            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, memoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, null);  
        }
    }

    // メモ更新
    public void updateMemo(int memoId, String content) {
        // 保存前にエスケープ解除
        String decodedContent = Utils.unescapeHtml(content);

        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "UPDATE memos SET content = ?, updated_at = CURRENT_TIMESTAMP WHERE memo_id = ?";

        try {
            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, decodedContent);
            ps.setInt(2, memoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, null);
        }
    }


    //モード状態取得
    public String getUserTheme(String userId) {
        String theme = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT theme_mode FROM users WHERE user_id = ?";  // テーマを取得
        try {
            conn = new OracleConnector().getCn();           
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                theme = rs.getString("theme_mode");  // テーマ設定を取得
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);  
        }

        return theme;
    }

 // メモ検索用（ページネーション対応）
    public List<Memo> searchMemos(String userId, String keyword, int offset, int limit) {
        List<Memo> memoList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT memo_id, content, created_at, updated_at, GREATEST(created_at, NVL(updated_at, created_at)) AS latest_at " +
                     "FROM memos WHERE user_id = ? AND LOWER(content) LIKE LOWER(?) ORDER BY latest_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, "%" + keyword + "%");
            ps.setInt(3, offset);
            ps.setInt(4, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Memo memo = new Memo();
                memo.setMemoId(rs.getInt("memo_id"));
                String content = rs.getString("content");

                // キーワードを検索し、前後10文字を取得
                int index = content.toLowerCase().indexOf(keyword.toLowerCase());
                if (index != -1) {
                    int start = Math.max(0, index - 10);
                    int end = Math.min(content.length(), index + keyword.length() + 10);
                    String snippet = content.substring(start, end);

                    // 前後10文字のスニペットに対してHTMLエスケープを適用
                    snippet = Utils.escapeHtml(snippet);
                    memo.setContent(snippet);  // エスケープ後のコンテンツを設定
                } else {
                    // エスケープ処理を全体のコンテンツに適用
                    content = Utils.escapeHtml(content);
                    memo.setContent(content);
                }

                memo.setLatestAt(rs.getTimestamp("latest_at"));
                memoList.add(memo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return memoList;
    }


 // 検索結果件数を取得
    public int searchMemoCount(String userId, String keyword) {
        int totalSearchMemos = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM memos WHERE user_id = ? AND LOWER(content) LIKE LOWER(?)";
        try {
            conn = new OracleConnector().getCn();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, "%" + keyword + "%");
            rs = ps.executeQuery();
            if (rs.next()) {
                totalSearchMemos = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, ps, rs);
        }
        return totalSearchMemos;
    }

    // クローズ処理
    private void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();  
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
}