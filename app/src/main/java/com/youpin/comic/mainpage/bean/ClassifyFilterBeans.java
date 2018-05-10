package com.youpin.comic.mainpage.bean;


import com.youpin.comic.base.BaseBean;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 分类筛选条件列表实体bean
 * @author hantao
 * 
 */
public class ClassifyFilterBeans extends BaseBean implements Serializable {
	
	private String title ;
	
	private ArrayList<ClassifyFilterItem> items ;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public ArrayList<ClassifyFilterItem> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<ClassifyFilterItem> items) {
		this.items = items;
	}
	
	/**
	 * 每一项实体bean
	 */
	public static class ClassifyFilterItem extends BaseBean{
		
		public ClassifyFilterItem() {
			
		}
		
		public ClassifyFilterItem(int tag_id , String tag_name) {
			this.tag_id = tag_id ; 
			this.tag_name = tag_name ; 
		}
		
		public static enum FILTER_STATUS{
			/** 无状态 */
			NONE,
			/** 被选定状态 */
			SELECTED,
		}
		
//	    "tag_id": 0,
		private int tag_id = 0 ; 
//	    "tag_name": "全部"
		private String tag_name ;
		
		/** 选项状态,被选中状态和正常状态 */
		private FILTER_STATUS status = FILTER_STATUS.NONE; 
		
		public int getTag_id() {
			return tag_id;
		}
		public void setTag_id(int tag_id) {
			this.tag_id = tag_id;
		}
		public String getTag_name() {
			return tag_name;
		}
		public void setTag_name(String tag_name) {
			this.tag_name = tag_name;
		}
		public FILTER_STATUS getStatus() {
			return status;
		}
		public void setStatus(FILTER_STATUS status) {
			this.status = status;
		}
		
		
		
	}
	
	
}
