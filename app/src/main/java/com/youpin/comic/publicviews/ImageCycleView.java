package com.youpin.comic.publicviews;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youpin.comic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于轮播图的展示,需要用到ad_cycle_view.xml布局,以后会把此xml改写成代码实现,减少偶合
 * @author liuguoyan
 */
public class ImageCycleView extends LinearLayout {
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 图片轮播视图
	 */
	private ViewPager mAdvPager = null;
	/**
	 * 滚动图片视图适配
	 */
	private ImageCycleAdapter mAdvAdapter;
	/**
	 * 图片轮播指示器控件
	 */
	private ViewGroup mGroup;

	/**
	 * 图片轮播指示个图
	 */
	private ImageView mImageView = null;

	/**
	 * 滚动图片指示视图列表
	 */
	private ImageView[] mImageViews = null;

	/**
	 * 图片滚动当前图片下标
	 */
	private int mImageIndex = 0;

	/**
	 * 手机密度
	 */
	private float mScale;
	private boolean isStop;
	private TextView[] mTextViews;
	private TextView mTextView;
	private ViewGroup mGroup2;
	List<String> imageNameList;
	private TextView imageName;
	/**
	 * @param context
	 */
	public ImageCycleView(Context context) {
		super(context);
		init(context) ; 
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public ImageCycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context) ; 
	}
	
	private void init(Context context){
		
		mContext = context;
		mScale = context.getResources().getDisplayMetrics().density;
		LayoutInflater.from(context).inflate(R.layout.main_cycle_view, this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
		pageChangedListener = new OnPageChangedListener() {
			@Override
			public void onPgeSelected(int index) {
				onImageRecyclePageSelected(index) ; 
			}
		};
		
		mAdvPager.setOnPageChangeListener(new GuidePageChangeListener(pageChangedListener));
		mAdvPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					// 开始图片滚动
					startImageTimerTask();
					break;
				default:
					// 停止图片滚动
					stopImageTimerTask();
					break;
				}
				return false;
			}
		});
		// 滚动图片右下指示器视
		mGroup = (ViewGroup) findViewById(R.id.circles);
		imageName = (TextView) findViewById(R.id.viewGroup2);
	}
	
	private void onImageRecyclePageSelected(int index){
		if (outterPageChangedListener != null) {
			outterPageChangedListener.onPgeSelected(index) ; 
		}
	}
	
	public void resetImageResource(List<String> imageUrlList, List<String> imageNameList){
		this.imageNameList=imageNameList;
//		mAdvAdapter.reloadData(imageUrlList, imageNameList) ; 
//		mAdvAdapter.notifyDataSetChanged() ; 
		
		setImageResources((ArrayList<String>)imageUrlList, (ArrayList<String>)imageNameList, imageCycleViewListener) ; 
		
	}
	
	private ImageCycleViewListener imageCycleViewListener ; 
	
	/**
	 * 装填图片数据
	 * 
	 * @param imageUrlList
	 * @param imageCycleViewListener
	 */
	public void setImageResources(ArrayList<String> imageUrlList, ArrayList<String> imageNameList,ImageCycleViewListener imageCycleViewListener) {
		this.imageCycleViewListener = imageCycleViewListener ; 
		
		this.imageNameList=imageNameList;
		// 清除
		mGroup.removeAllViews();
		// 图片广告数量
		final int imageCount = imageUrlList.size();
		mImageViews = new ImageView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(mContext);
			int imageParams = (int) (mScale * 10 + 0.5f);// XP与DP转换，适应应不同分辨率
			int imagePadding = (int) (mScale * 5 + 0.5f);
			LayoutParams params=new LayoutParams(imageParams,imageParams);
			params.leftMargin=10; 
			mImageView.setScaleType(ScaleType.CENTER_CROP);
			mImageView.setLayoutParams(params);
			mImageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
			
			mImageViews[i] = mImageView;
			if (i == 0) {
				mImageViews[i].setBackgroundResource(R.drawable.banner_dian_focus);
			} else {
				mImageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
			}
			mGroup.addView(mImageViews[i]);
		}
		
		imageName.setText(imageNameList!=null?imageNameList.get(0):"");
		mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList, imageNameList,imageCycleViewListener);
		mAdvPager.setAdapter(mAdvAdapter);
		startImageTimerTask();
	}
	
	
	public void notifyCycleView(){
		if (mAdvAdapter!=null) {
			mAdvAdapter.notifyDataSetChanged() ;
		}
	}
	
	
	public void setPriorClickListener(ImagePriorClickListener listener){
		mAdvAdapter.setPriorClickListener(listener) ; 
	}
	
	/**
	 * 图片轮播(手动控制自动轮播与否，便于资源控件）
	 */
	public void startImageCycle() {
		startImageTimerTask();
	}

	/**
	 * 暂停轮播—用于节省资源
	 */
	public void pushImageCycle() {
		stopImageTimerTask();
	}

	/**
	 * 图片滚动任务
	 */
	private void startImageTimerTask() {
		stopImageTimerTask();
		// 图片滚动
		mHandler.postDelayed(mImageTimerTask, 3000);
	}
	
	/**
	 * 停止图片滚动任务
	 */
	private void stopImageTimerTask() {
		isStop=true;
		mHandler.removeCallbacks(mImageTimerTask);
	}
	
	private Handler mHandler = new Handler();
	
	/**
	 * 图片自动轮播Task
	 */
	private Runnable mImageTimerTask = new Runnable() {
		@Override
		public void run() {
			if (mImageViews != null) {
				mAdvPager.setCurrentItem(mAdvPager.getCurrentItem()+1);
				if(!isStop){  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环 
					mHandler.postDelayed(mImageTimerTask, 3000);
				}

			}
		}
	};
	
	private OnPageChangedListener outterPageChangedListener ; 
	
	public void setOnPageChangeListener(OnPageChangedListener outterListener){
		this.outterPageChangedListener = outterListener ; 
	}
	
	private OnPageChangedListener pageChangedListener ; 
	
	public interface OnPageChangedListener{
		public void onPgeSelected(int index) ;
	}
	
	/**
	 * 轮播图片监听
	 * 
	 * @author minking
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener {
		
		OnPageChangedListener listener ; 
		
		public GuidePageChangeListener(OnPageChangedListener listener) {
			this.listener = listener ; 
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE)
				startImageTimerTask(); 
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int index) {
			listener.onPgeSelected(index) ; 
			index=index%mImageViews.length;
			// 设置当前显示的图片
			mImageIndex = index;
			// 设置图片滚动指示器背
			mImageViews[index].setBackgroundResource(R.drawable.banner_dian_focus);
			imageName.setText(imageNameList.get(index));
			for (int i = 0; i < mImageViews.length; i++) {
				if (index != i) {
					mImageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
				}
			}
		}
	}
	
	private class ImageCycleAdapter extends PagerAdapter {
		
		/**
		 * 图片视图缓存列表
		 */
		private ArrayList<MyImageView> mImageViewCacheList;
		
		/**
		 * 图片资源列表
		 */
		private List<String> mAdList = new ArrayList<String>();
		private List<String> nameList = new ArrayList<String>();
		
		public void reloadData(List<String> mAdList , List<String> nameList){
			this.mAdList = mAdList ; 
			this.nameList = nameList ; 
		}
		
		/**
		 * 广告图片点击监听
		 */
		private ImageCycleViewListener mImageCycleViewListener;
		
		private ImagePriorClickListener mPriorClickListener; 
		
		public void setPriorClickListener(ImagePriorClickListener mPriorClickListener){
			this.mPriorClickListener = mPriorClickListener ; 
		}
		
		private Context mContext;
		public ImageCycleAdapter(Context context, ArrayList<String> adList,ArrayList<String> nameList, ImageCycleViewListener imageCycleViewListener) {
			this.mContext = context;
			this.mAdList = adList;
			this.nameList = nameList;
			mImageCycleViewListener = imageCycleViewListener;
			mImageViewCacheList = new ArrayList<MyImageView>();
		}

		@Override
		public int getCount() {
//			return mAdList.size();
			return Integer.MAX_VALUE;
		}
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			String imageUrl = mAdList.get(position%mAdList.size());
			MyImageView imageView = null;
			if (mImageViewCacheList.isEmpty()) {
				imageView = new MyImageView(mContext);
				imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				imageView.setScaleType(ScaleType.CENTER_CROP);
				
			} else {
				imageView = mImageViewCacheList.remove(0);
			}
			// 设置图片点击监听
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					mImageCycleViewListener.onImageClick(position%mAdList.size(), v);
					if (mPriorClickListener!=null && mPriorClickListener.getPriorPosition() == position%mAdList.size()) {
						mPriorClickListener.onImageClick(position%mAdList.size(), v) ;
					}else {
						mImageCycleViewListener.onImageClick(position%mAdList.size(), v);
					}
				}
			});
			imageView.setTag(imageUrl);
			container.addView(imageView);
			try {
				mImageCycleViewListener.displayImage(imageUrl, imageView);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			MyImageView view = (MyImageView) object;
			mAdvPager.removeView(view);
			mImageViewCacheList.add(view);
			
		}

	}

	/**
	 * 轮播控件的监听事件
	 * 
	 * @author minking
	 */
	public static interface ImageCycleViewListener {
		/**
		 * 加载图片资源
		 * 
		 * @param imageURL
		 * @param imageView
		 */
		public void displayImage(String imageURL, ImageView imageView);

		/**
		 * 单击图片事件
		 * 
		 * @param position
		 * @param imageView
		 */
		public void onImageClick(int position, View imageView);
		
	}
	
	public static interface ImagePriorClickListener{
		public void onImageClick(int position, View imageView);
		/**
		 * 获取优先的位置
		 * @return
		 */
		public int getPriorPosition() ;
	}
	

}
