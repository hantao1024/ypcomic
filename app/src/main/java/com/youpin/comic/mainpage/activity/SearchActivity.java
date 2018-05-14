package com.youpin.comic.mainpage.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
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
import com.youpin.comic.mainpage.bean.SearchKeyWord;
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

//    /** 模糊搜索 */
//    private DMBaseProtocol fuzzySearchProtocol ;
//    /** 全部搜索 */
//    private DMBaseProtocol allSearchProtocol ;
//    /** 热门搜索 */
//    private DMBaseProtocol hotSearchProtocol ;

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
//        fuzzySearchProtocol = new HttpUrlTypeSearchProtocol(getActivity(), URL_ENUM.HttpUrlTypeComicFuzzySearch);
//        allSearchProtocol = new HttpUrlTypeSearchProtocol(getActivity(), URL_ENUM.HttpUrlTypeComicSearch);
//        hotSearchProtocol = new HttpUrlTypeSearchProtocol(getActivity(), URL_ENUM.HttpUrlTypeComicSearchHot);
    }

    /***
     * 本地搜索关键字记录
     */
    private void onLocalKeywords(){

        try {
            int type = intent_extra_type.equals(SearchBean.TYPE_CARTOON) ? SearchKeyWord.TYPE_CARTOON : SearchKeyWord.TYPE_NOVEL ;
//            List<SearchKeyWord> keywords = SearchKeyWordTable.getInstance(getActivity()).getRecentKeyWord(type);
            List<SearchKeyWord> keywords = null;
            if (keywords==null || keywords.size()==0) return ;

            int count = keywords.size()%2 ==0 ?( keywords.size()/2 ) :( keywords.size()/2+1);
            for (int i = 0; i < count; i++) {
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, dip2px(35));
                layout_search_historys.addView(layout, layoutParams);

                TextView left = new TextView(getActivity());
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.FILL_PARENT);
                textParams.weight = 1;
                left.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.txt_size_second));
                left.setTextColor(getResources().getColor(R.color.comm_gray_mid));
                left.setGravity(Gravity.CENTER_VERTICAL);
                left.setSingleLine(true);
                left.setEllipsize(TextUtils.TruncateAt.END);

                TextView right = new TextView(getActivity());
                right.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.txt_size_second));
                right.setTextColor(getResources().getColor(R.color.comm_gray_mid));
                right.setGravity(Gravity.CENTER_VERTICAL);
                right.setSingleLine(true);
                right.setEllipsize(TextUtils.TruncateAt.END);

                left.setPadding(0, 0, dip2px(5), 0);
                layout.addView(left, textParams);
                textParams.setMargins(dip2px(5), 0, 0, 0);
                layout.addView(right, textParams);

                left.setText(keywords.get(i*2).getKeyword());
                final String lefttext = keywords.get(i*2).getKeyword();
                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSearchKeyword(lefttext);
                    }
                });
                if (keywords.size() >(i*2 +1)) {
                    right.setText(keywords.get(i*2 +1).getKeyword());
                    final String rightText = keywords.get(i*2 +1).getKeyword();
                    right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSearchKeyword(rightText);
                        }
                    });
                }else {
                    right.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

//        list_introductions.setOnRefreshListener(new OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                loadData(false) ;
//            }
//            @Override
//            public void onPullUpToRefresh(
//                    PullToRefreshBase<ListView> refreshView) {
//                loadData(true) ;
//            }
//        });

    }

    @Override
    public void free() {
    }

    ////////////////////////////////////////////////////////////////////////////////
    /** 热门词搜索 */
    private void onHotKeywords(){
//        try {
//            hotSearchProtocol.setPathParam(intent_extra_type!=null&&intent_extra_type.equals(BookList.TYPE_CARTOON) ? BookList.TYPE_CARTOON : BookList.TYPE_NOVEL);
//        } catch (Exception e) {
//            hotSearchProtocol.setPathParam( BookList.TYPE_NOVEL);
//        }
//        hotSearchProtocol.runProtocol(null ,null ,CACHEOPR.PAIR , new OnSuccessListener() {
//            @Override
//            public void onSuccess(Object response) {
//                searchHots.clear();
//                JSONArray array = (JSONArray) response ;
//                for (int i = 0; i < array.length(); i++) {
//                    SearchHot hot = ObjectMaker.convert(array.optJSONObject(i).toString(), SearchHot.class);
//                    searchHots.add(hot);
//                }
//                onKeyWordsRecieved(searchHots);
//            }
//        },new OnFailedListener(){
//            @Override
//            public void onFailed(Object response) {
//
//            }
//        });
    }


    //////////////////////////////////////////////////////////////////////////////
    /**
     * 模糊搜索
     */
    private void onSearchBrief(final String k){
//        try {
//            final String wordstub = filterSearchEncodeKey(k);
//            String type = intent_extra_type.equals(BookList.TYPE_CARTOON) ? SearchKeyWord.TYPE_CARTOON+"" : SearchKeyWord.TYPE_NOVEL+"" ;
//            fuzzySearchProtocol.setPathParam(type , wordstub) ;
//            fuzzySearchProtocol.runProtocol(new OnSuccessListener() {
//                @Override
//                public void onSuccess(Object response) {
//                    //TODO 这里需要解析一下
//                    searchBeans.clear();
//                    JSONArray array = (JSONArray) response;
//                    for (int i = 0; i < array.length(); i++) {
//                        SearchBean hot = ObjectMaker.convert(array.optJSONObject(i).toString(), SearchBean.class);
//                        searchBeans.add(hot);
//                    }
//                    onSearchBriefComplete();
//                }
//            }, new OnFailedListener() {
//                @Override
//                public void onFailed(Object response) {
//
//                }
//            });
//
//        } catch (IllegalArgumentException e) {
//            Log.d(TAG, e.getClass().getName()+":"+ e.getMessage());
//            AlertManager.getInstance().showHint(getActivity(), HintType.HT_SUCCESS, getString(R.string.search_contains_special_charactor));
//        }
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

//        search_type = search_type == -1 ? intent_extra_type.equals(SearchBean.TYPE_CARTOON) ? SearchKeyWord.TYPE_CARTOON : SearchKeyWord.TYPE_NOVEL : search_type ;
//        if (search_keyword.trim().length()==0) return ;
//        String keyStr=filterSearchEncodeKey(search_keyword);
//        load_page = more ? load_page+1 : 0 ;
//        try {
//            //
//            allSearchProtocol.setPathParam(search_type+"" , keyStr , load_page+"") ;
//            allSearchProtocol.runProtocol(new OnSuccessListener() {
//                @Override
//                public void onSuccess(Object response) {
//                    //TODO 这里需要解析一下
//                    list_introductions.onRefreshComplete() ;
//                    if (!more) {
//                        searchBeans.clear();
//                    }
//                    JSONArray array = (JSONArray) response;
//                    searchBeans.addAll(ObjectMaker.convert2List(array, SearchBean.class)) ;
//                    onSearchKeywordComplete(more);
//                    if (searchBeans.size() > 0 ) {
//                        txt_search_warning.setVisibility(View.INVISIBLE);
//                    }else {
//                        txt_search_warning.setVisibility(View.VISIBLE);
//                    }
//                }
//            }, new OnFailedListener() {
//                @Override
//                public void onFailed(Object response) {
//
//                }
//            });
//
//            SearchKeyWordTable.getInstance(getActivity()).saveKeyWord(reFilterSearchKey(reFilterSearchKey(search_keyword)) , search_type );
//
//        } catch (IllegalArgumentException e) {
//            Log.d(TAG, e.getMessage());
//            AlertManager.getInstance().showHint(getActivity(), HintType.HT_SUCCESS, getString(R.string.search_contains_special_charactor));
//        }

    }



    /**
     * 关键字搜索完成
     */
    private void onSearchKeywordComplete(boolean more){
        showLayer(LAYER.LAYER_INFO);
//        mInfoAdapter.reLoad(searchBeans);
//        if (more) {
//            mInfoAdapter.notifyDataSetChanged() ;
//        }else {
//            mInfoAdapter.notifyDataSetInvalidated() ;
//        }
    }

    private String filterSearchKey(String k){
//		return k.replaceAll(" ", "%20").replaceAll("|", "%7C").replaceAll("%", "%25").replaceAll("[", "%5B").replaceAll("]", "%5D").replaceAll("{", "%7B").replaceAll("}", "%7D");
//		return k;
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
//		return key.replaceAll("%20", " ").replaceAll("%7C", "|").replaceAll("%25", "%").replaceAll("%5B", "[").replaceAll("%5D", "]").replaceAll("%7B", "{").replaceAll("%7D", "}");
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
//            case SearchBean4InfoAdapter.MSG_WHAT_ONITEM_CHECKED:
//                Bundle data = msg.getData();
//                String id = data.getString(SearchBean4InfoAdapter.MSG_BUNDEL_KEY_ITEM_ID);
//                String title = data.getString(SearchBean4InfoAdapter.MSG_BUNDEL_KEY_ITEM_TITLE);
//                if (intent_extra_type.equals(BookList.TYPE_CARTOON)) {
//                    AppBeanUtils.startCartoonDescriptionActivity(getActivity(), id, title) ;
//                }else {
//                    AppBeanUtils.startNovelDescriptionActivity(getActivity(), id, title) ;
//                }
//                break;
//            default:
//                break;
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
//        SearchKeyWordTable.getInstance(getActivity()).removeAll();
//        layout_search_historys.removeAllViews();
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
