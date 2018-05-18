package com.youpin.comic.mainpage.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youpin.comic.R;
import com.youpin.comic.base.StepActivity;
import com.youpin.comic.mainpage.adapter.SearchBean4BriefAdapter;
import com.youpin.comic.mainpage.adapter.SearchBean4InfoAdapter;
import com.youpin.comic.mainpage.bean.SearchBean;
import com.youpin.comic.mainpage.bean.SearchHot;
import com.youpin.comic.publicviews.FlowLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hantao on 2018/5/14.
 */

public class SearchActivity extends StepActivity implements View.OnClickListener {

    public static final String TAG = "SearchActivity" ;

    /** 搜索结果列表 */
    public static final String INTENT_EXTRA_SEARCHBEANS_KEY= "intent_extra_searchbeans_key";
    public static final String INTENT_EXTRA_SEARCHBEANS= "intent_extra_searchbeans";

    /** 从外部传入我们想要搜索的关键字 */
    public static final String INTENT_EXTRA_KEYWORD= "intent_extra_keyword";

    /** 搜索类型,是漫画还是小说 */
    public static final String INTENT_EXTRA_TYPE= "intent_extra_type";
    private String intent_extra_type ;

    private FlowLayout mLayout_autowrap;

    private EditText edit_search_inputer;

    private TextView txtbtn_clearhistory;

    /** 热门搜索词 */
    private List<SearchHot> searchHots = new ArrayList<SearchHot>();

    private LinearLayout layout_brife;
    private SmartRefreshLayout layout_introductions;
    private ListView list_brief;
    private ListView list_introductions;

    /** 搜索不到的提示框 */
    private LinearLayout txt_search_warning;

    /** 本地搜索记录 */
    private LinearLayout layout_search_historys;

    private SearchBean4BriefAdapter mBriefAdapter;
    private SearchBean4InfoAdapter mInfoAdapter;

    /*** 搜索到的漫画 */
    private List<SearchBean> searchBeans = new ArrayList<SearchBean>();

    private boolean onlyKeyWord = false;


    /***
     * 搜索文字的颜色
     */
    private int[] keyscolor = {R.color.search_random_a,R.color.search_random_b,R.color.search_random_c};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void createContent() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void findViews() {
        setEnabledefault_keyevent(false) ;
        mLayout_autowrap = (FlowLayout) findViewById(R.id.layout_autowrap);
        edit_search_inputer = (EditText) findViewById(R.id.edit_search_inputer);
        txtbtn_clearhistory = (TextView) findViewById(R.id.txtbtn_clearhistory);

        layout_brife = (LinearLayout) findViewById(R.id.layout_brife);
        layout_introductions = (SmartRefreshLayout) findViewById(R.id.layout_introductions);
        list_brief = (ListView) findViewById(R.id.list_brief);
        list_introductions = (ListView) findViewById(R.id.list_introductions);
        list_introductions.setDivider(getResources().getDrawable(R.drawable.img_common_list_divider));

        layout_search_historys = (LinearLayout) findViewById(R.id.layout_search_historys);

        txt_search_warning = (LinearLayout) findViewById(R.id.txt_search_warning);
    }

    @Override
    public void onAction(View v) {
        closeOpration();
    }

    @Override
    protected void initData() {
        intent_extra_type = getIntent().getStringExtra(INTENT_EXTRA_TYPE) ;
        initProtocol() ;
        mBriefAdapter = new SearchBean4BriefAdapter(getActivity(), getDefaultHandler());
        mInfoAdapter = new SearchBean4InfoAdapter(getActivity(), getDefaultHandler());
        list_brief.setAdapter(mBriefAdapter);
        list_introductions.setAdapter(mInfoAdapter);
        onHotKeywords();
        onLocalKeywords();


        if (getIntent().hasExtra(INTENT_EXTRA_SEARCHBEANS) && getIntent().hasExtra(INTENT_EXTRA_SEARCHBEANS_KEY)) {
            searchBeans = getIntent().getParcelableArrayListExtra(INTENT_EXTRA_SEARCHBEANS);
            //这里是一进来就会包含搜索的内容,我们这样来处理
            onlyKeyWord = true;
            edit_search_inputer.setText(getIntent().getStringExtra(INTENT_EXTRA_SEARCHBEANS_KEY));
            onSearchKeywordComplete(false);
        }else if (getIntent().hasExtra(INTENT_EXTRA_KEYWORD)) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            /**
             * 判断是否有搜索关键字,如果有的话,就执行关键字搜索功能
             */
            String keyInExtra = getIntent().getStringExtra(INTENT_EXTRA_KEYWORD);
            if (keyInExtra!=null && keyInExtra.length()>0) {
                onSearchKeyword(keyInExtra);
            }
        }
    }

    /**
     * 初始化协议
     */
    private void initProtocol(){

    }

    /***
     * 本地搜索关键字记录
     */
    private void onLocalKeywords(){

    }

    /** 模糊搜索的Runnable,因为我们要屏幕输入之后马上搜索  */
    private Runnable mBriefRunnable;

    @Override
    protected void setListener() {
        txtbtn_clearhistory.setOnClickListener(this);

        edit_search_inputer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //这里要执行关键字搜索了
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    getDefaultHandler().removeCallbacks(mBriefRunnable);
                    onSearchKeyword(edit_search_inputer.getText().toString());
                }
                return false;
            }
        });
        edit_search_inputer.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (onlyKeyWord) {
                    return;
                }
                if (s.length()==0) {
                    searchBeans.clear();
                    onSearchBriefComplete();
                    showLayer(LAYER.LAYER_HOT);
                    return;
                }
                mBriefRunnable = new Runnable() {
                    @Override
                    public void run() {
                        onSearchBrief(s.toString());
                    }
                };
                getDefaultHandler().postDelayed(mBriefRunnable,100);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        list_brief.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    if (searchBeans!=null&&!searchBeans.isEmpty()&&searchBeans.size()>0) {
                        onSearchKeyword(searchBeans.size()>position?searchBeans.get(position).getTitle():searchBeans.get(searchBeans.size()-1).getTitle());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void free() {
    }

    ////////////////////////////////////////////////////////////////////////////////
    /** 热门词搜索 */
    private void onHotKeywords(){
    }


    //////////////////////////////////////////////////////////////////////////////
    /**
     * 模糊搜索
     */
    private void onSearchBrief(final String k){
    }

    ////////////////////////////////////////////////////////////////////////////////

    private int load_page = 0 ;

    private int search_type = -1 ;

    private String search_keyword = "" ;

    /***
     * 搜索关键字(全部搜索)
     */
    private void onSearchKeyword(final String k){
        final String keyworkd = filterSearchKey(k);
        onlyKeyWord = true;
        edit_search_inputer.setText(k);
        edit_search_inputer.setSelection(k.length());
        search_keyword = keyworkd ;

        loadData(false) ;
    }

    private void loadData(final boolean more){


    }



    /**
     * 关键字搜索完成
     */
    private void onSearchKeywordComplete(boolean more){
        showLayer(LAYER.LAYER_INFO);
    }

    private String filterSearchKey(String k){
        return k.replaceAll(" ", "%20");
    }

    private String filterSearchEncodeKey(String k){
        String s="";
        try {
            s= URLEncoder.encode(k, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (Exception e){

        }
        return s;
    }

    private String reFilterSearchKey(String key){
        return key.replaceAll("%20", " ");
    }

    /**
     * 模糊搜索完成
     */
    private void onSearchBriefComplete(){
        showLayer(LAYER.LAYER_BRIEF);
        mBriefAdapter.reLoad(searchBeans);
        mBriefAdapter.notifyDataSetInvalidated();
    }

    /**
     * 初始化热词界面
     * */
    private void onKeyWordsRecieved(List<SearchHot> names){
        mLayout_autowrap.removeAllViews();
        mLayout_autowrap.setVerticalSpacing(dip2px(1));
        mLayout_autowrap.setHorizontalSpacing(dip2px(1)) ;
        Random random = new Random();
        for (int i = 0; i < names.size(); i++) {
            final TextView textView = new TextView(getActivity());
            textView.setBackgroundColor(getResources().getColor(keyscolor[random.nextInt(keyscolor.length)]));
            textView.setPadding(dip2px(10), 0, dip2px(10), 0);
            textView.setGravity(Gravity.CENTER);
            textView.setText(names.get(i).getName());
            textView.setClickable(true);
            textView.setSingleLine(true);
            textView.setTextColor(getResources().getColor(android.R.color.white));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.txt_size_second));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSearchKeyword(textView.getText().toString());
                }
            });
            android.view.ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, dip2px(35));
            mLayout_autowrap.addView(textView,params);
        }
    }

    @Override
    protected void onHandleMessage(Message msg) {
        switch (msg.what) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtbtn_clearhistory:
                clearHistory();
                break;
        }
    }

    /**
     * 清除历史
     */
    private void clearHistory(){
        layout_search_historys.removeAllViews();
    }

    /**
     *显示哪一层
     */
    public static enum LAYER{
        LAYER_HOT,
        LAYER_BRIEF,
        LAYER_INFO
    }

    /**
     * 显示层
     **/
    private void showLayer(LAYER layer){
        switch (layer) {
            case LAYER_HOT:
                txt_search_warning.setVisibility(View.INVISIBLE);
                layout_brife.setVisibility(View.GONE);
                layout_introductions.setVisibility(View.GONE);
                break;
            case LAYER_BRIEF:
                txt_search_warning.setVisibility(View.INVISIBLE);
                layout_brife.setVisibility(View.VISIBLE);
                layout_introductions.setVisibility(View.GONE);
                break;
            case LAYER_INFO:
                layout_brife.setVisibility(View.GONE);
                layout_introductions.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && (layout_brife.getVisibility()==View.VISIBLE || layout_introductions.getVisibility()==View.VISIBLE)) {
            showLayer(LAYER.LAYER_HOT);
            edit_search_inputer.setText("");
            onlyKeyWord = false;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
