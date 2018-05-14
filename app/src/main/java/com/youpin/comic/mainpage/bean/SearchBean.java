package com.youpin.comic.mainpage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.youpin.comic.base.BaseBean;


/**
 * 
 * 
 * 搜索结果实体类
 * @author hantao
 * @version 1.0
 */
public class SearchBean  extends BaseBean implements Parcelable  {

	/** 书单类型 (0:漫画,1:小说) */
	public static final int TAG_TYPE = 0x101 ;
	/** 漫画 */
	public static final String TYPE_CARTOON = "0" ;
	/** 小说 */
	public static final String TYPE_NOVEL = "1" ;

//	id	:	5658
	
	private String id;
	
//	title	:	AKB49
	private String title;
			
//	last_update_chapter_name	:	第199话
	private String last_name;
			
//	cover	:	http://images.dmzj.com/webpic/18/akb49-21101224.jpg
	private String cover;
			
//	authors	:	宫岛礼吏
	private String authors;
			
//	types	:	欢乐向/伪娘/后宫
	private String types;
			
//	sum_chapters	:	202
	private String sum_chapters;
			
//	sum_source	:	4466
	private String sum_source;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getAuthors() {
		return authors;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getSum_chapters() {
		return sum_chapters;
	}

	public void setSum_chapters(String sum_chapters) {
		this.sum_chapters = sum_chapters;
	}

	public String getSum_source() {
		return sum_source;
	}

	public void setSum_source(String sum_source) {
		this.sum_source = sum_source;
	}
	
	
	// /////////////////////////////////////////////////////////////////////////

	public final static Creator<SearchBean> CREATOR = new Creator<SearchBean>() {

		@Override
		public SearchBean createFromParcel(Parcel in) {
			SearchBean info = new SearchBean();
			info.id = in.readString();
			info.title = in.readString();
			info.setLast_name(in.readString());
			info.cover = in.readString();
			info.authors = in.readString();
			info.types = in.readString();
			info.sum_chapters = in.readString();
			info.sum_source = in.readString();
			return info;
		}

		@Override
		public SearchBean[] newArray(int size) {
			return new SearchBean[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(getLast_name());
		dest.writeString(cover);
		dest.writeString(authors);
		dest.writeString(types);
		dest.writeString(sum_chapters);
		dest.writeString(sum_source);
	}

	public static Creator<SearchBean> getCreator() {
		return CREATOR;
	}
	
	
}
