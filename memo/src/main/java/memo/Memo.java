package memo;

import java.sql.Date;
import java.sql.Timestamp;

public class Memo {
    private int memoId;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private Timestamp latestAt;  

    // メモID
    public int getMemoId() {
        return memoId;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    // 内容
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // 一覧表示用に内容を50文字制限
    public String getShortContent() {
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }

    // メモ作成日時
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // メモ更新日時
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // 最新の日時 (created_at または updated_at)
    public Timestamp getLatestAt() {
        return latestAt;
    }

    public void setLatestAt(Timestamp latestAt) {
        this.latestAt = latestAt;
    }
}
