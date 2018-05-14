package com.youpin.comic.mainpage.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hantao on 2018/5/14.
 */
@Entity
public class SearchKeyWord {
    /** 漫画 */
    public static final int TYPE_CARTOON = 0 ;

    /** 小说 */
    public static final int TYPE_NOVEL = 1 ;


    @Id(autoincrement = true) // id自增长
    private Long userId; // id


    private int type = TYPE_CARTOON ;

    private String keyword;

    private String searchTime;

    @Generated(hash = 36428561)
    public SearchKeyWord(Long userId, int type, String keyword, String searchTime) {
        this.userId = userId;
        this.type = type;
        this.keyword = keyword;
        this.searchTime = searchTime;
    }

    @Generated(hash = 1710054922)
    public SearchKeyWord() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSearchTime() {
        return this.searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }
}
