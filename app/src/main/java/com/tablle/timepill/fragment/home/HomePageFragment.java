package com.tablle.timepill.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.tablle.timepill.R;
import com.tablle.timepill.activity.DiaryDetailActivity;
import com.tablle.timepill.adapter.HomepageAdapter;
import com.tablle.timepill.constant.Url;
import com.tablle.timepill.model.DiaryBean;
import com.tablle.timepill.model.DiaryListBean;
import com.tablle.timepill.utils.Utils;
import com.tablle.timepill.widget.DividerItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

import static butterknife.ButterKnife.findById;

public class HomePageFragment extends Fragment {
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView
    public static final int PULLUP_LOAD_MORE = 0; //上拉加载更多
    public static final int LOADING_MORE = 1; //正在加载中
    static int SORT_ASC = 1;
    static int SORT_DESC = 2;
    private int load_more_status = 0;
    private int lastVisibleItem;
    private int page = 1;
    private int sort = SORT_DESC;
    private ArrayList item;
    private LinearLayoutManager layoutManager;
    //private List<String> mDatas;
    private HomepageAdapter homePageAdapter;
    @Bind(R.id.homepage_swipe_refresh_layout)
    SwipeRefreshLayout homeRefreshLayout;
    private DiaryListBean bean;
    private DiaryBean diaryBean;
    private String result;
    private String token;
    private RecyclerView recyclerView;
    private List<DiaryBean> diaries;
    private List<DiaryBean> moreDiaries;

    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ButterKnife.bind(this, view);

        recyclerView = findById(view, R.id.home_page_recycler_view);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(Utils.getDrawableResource(getActivity(), R.drawable.line)));
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /*homePageAdapter = new HomepageAdapter();
        recyclerView.setAdapter(homePageAdapter);*/

        homeRefreshLayout.setColorSchemeResources(R.color.mt_main_blue);
        homeRefreshLayout.measure(0, 0);
        homeRefreshLayout.setRefreshing(true);
        homeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        refreshContent();

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == homePageAdapter.getItemCount()) {
                    System.out.println("lastVisibleItem+1==================" + lastVisibleItem + 1);
                    System.out.println("homePageAdapter.getItemCount()==================" + homePageAdapter.getItemCount());
                    load_more_status = LOADING_MORE;
                    requestHttpForMoreData();
                    /*if (null != moreDiaries && moreDiaries.size() > 0) {
                        diaries.addAll(moreDiaries);

                        homePageAdapter.notifyDataSetChanged();
                        load_more_status = PULLUP_LOAD_MORE;
                    } else {
                        Toast.makeText(getActivity(), "已无更多数据", Toast.LENGTH_SHORT).show();
                    }*/

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                System.out.println("lastVisibleItem==================" + lastVisibleItem);
            }
        });
        /*OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Authorization", token)
                .url(Url.URL_Diary_all+"?page=1"+"&page_size=20")
                .build();

       Call call = okHttpClient.newCall(request);
        System.out.println("call");
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                System.out.println("faile");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("success===" + response.code());
                    result = response.body().string();
                    System.out.println("result======" + result);

                    System.out.println("before==================");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("111111==================");
                                execute();
                                System.out.println("22222==================");
                                homePageAdapter = new HomepageAdapter();
                                System.out.println("33333==================");
                                recyclerView.setAdapter(homePageAdapter);

                                System.out.println("444444==================");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } else {
                    System.out.println("no success");
                }

            }
        });*/
        return view;
    }

    private void requestHttpForMoreData() {
        String email = "tbl0311@126.com";
        String password = "603616203";
        page++;
        System.out.println("page++ =======" + page);
        token = getBase64Token(email, password);

        OkHttpUtils
                .get()
                .url(Url.URL_Diary_ALL + "?page=" + page + "&page_size=20")
                .addHeader("Authorization", token)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println("faile=======" + e.toString());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        System.out.println("success===" + response);
                        load_more_status = LOADING_MORE;
                        System.out.println("111111==================");
                        String morResult = response;
                        DiaryListBean moreBean = new Gson().fromJson(morResult, DiaryListBean.class);
                        moreDiaries = moreBean.diaries;
                        System.out.println("22222==================");

                        //Diary last = (Diary) listAdapter.getItem(listAdapter.getCount() - 1);
                        int lastItemId = diaries.get(diaries.size() - 1).id;
                        int start = 0;
                        for (DiaryBean diary : moreDiaries) {
                            if ((sort == SORT_DESC && diary.id >= lastItemId) || (sort == SORT_ASC && diary.id <= lastItemId)) {
                                start++;
                            } else {
                                break;
                            }
                        }
                        if (moreDiaries.size() - start > 0) {

                            diaries.addAll(moreDiaries.subList(start, moreDiaries.size() - 1));

                            homePageAdapter.notifyDataSetChanged();
                        }
                        //如果返回少于10条，就再加载一次
                        if (moreDiaries.size() - start < 10 && moreDiaries.size() > 0) {

                            requestHttpForMoreData();
                        }

                        /*if (moreDiaries.get(0).id >= diaries.get(diaries.size() - 1).id) {
                            System.out.println("moreDiaries.get(0).id=================="+moreDiaries.get(0).id);
                            System.out.println("diaries.get(diaries.size() - 1).id==================" + diaries.get(diaries.size() - 1).id);
                            for (int i = 0; i < moreDiaries.size(); i++) {
                                if (moreDiaries.get(i).id >= diaries.get(diaries.size() - 1).id) {
                                    System.out.println("大于的==================" + moreDiaries.get(i));
                                    moreDiaries.remove(i);
                                }
                            }
                            System.out.println("剩下的==================" + moreDiaries.size() + moreDiaries);
                            if (moreDiaries.size() < 10) {
                                requestHttpForMoreData();
                            }
                        }*/
                        System.out.println("444444==================");
                    }
                });
    }

    private void refreshContent() {
        requestHttpForData();
        homeRefreshLayout.setRefreshing(false);
    }

    private void requestHttpForData() {
        String email = "tbl0311@126.com";
        String password = "603616203";
        page = 1;
        System.out.println("page =======" + page);
        token = getBase64Token(email, password);

        OkHttpUtils
                .get()
                .url(Url.URL_Diary_ALL + "?page=1" + "&page_size=20")
                .addHeader("Authorization", token)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println("faile=======" + e.toString());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        System.out.println("success===" + response);

                        result = response;
                        System.out.println("111111==================" + result);
                        bean = new Gson().fromJson(result, DiaryListBean.class);
                        diaries = bean.diaries;
                        System.out.println("22222==================");
                        homePageAdapter = new HomepageAdapter(getActivity(),diaries);
                        System.out.println("33333==================");
                        recyclerView.setAdapter(homePageAdapter);
                        // homePageAdapter.notifyDataSetChanged();
                        homePageAdapter.notifyDataSetChanged();
                        System.out.println("444444==================");
                        homePageAdapter.setOnItemClickLitener(new HomepageAdapter.OnItemClickLitener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(),DiaryDetailActivity.class);
                                DiaryBean diary = diaries.get(position);
                                intent.putExtra("diary", diary);
                                startActivity(intent);
                            }
                        });
                        /*new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    System.out.println("111111==================");
                                    result = response;
                                    execute();
                                    System.out.println("22222==================");
                                    homePageAdapter = new HomepageAdapter();
                                    System.out.println("33333==================");
                                    recyclerView.setAdapter(homePageAdapter);

                                    System.out.println("444444==================");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();*/


                    }
                });
    }

    private String getBase64Token(String email, String password) {
        return "Basic " + Base64.encodeToString((email + ":" + password).getBytes(), Base64.NO_WRAP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /*public class HomepageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public interface OnItemClickLitener
        {
            void onItemClick(View view, int position);
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
        {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                MyViewHolder itemViewHolder = new MyViewHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.home_page_rv_item, parent,
                        false));

                return itemViewHolder;
            } else if (viewType == TYPE_FOOTER) {
                FootViewHolder footViewHolder = new FootViewHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.item_recycleview_foot, parent,
                        false));

                return footViewHolder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof MyViewHolder) {
                final MyViewHolder itemViewHolder = (MyViewHolder) holder;
                itemViewHolder.home_diarylist_author.setText(diaries.get(position).user.name);
                itemViewHolder.home_diarylist_comment.setText(diaries.get(position).comment_count);
                itemViewHolder.home_diarylist_content.setText(diaries.get(position).content);
                itemViewHolder.home_diarylist_notebook.setText(diaries.get(position).notebook_subject);
                itemViewHolder.home_diarylist_time.setText(diaries.get(position).getCreatedTimeString());
                if (diaries.get(position).type.equals("2") && diaries.get(position).photoThumbUrl != null) {
                    itemViewHolder.home_diarylist_photo.setVisibility(View.VISIBLE);
                    OkHttpUtils
                            .get()//
                            .url(diaries.get(position).photoThumbUrl)//
                            .build()//
                            .execute(new BitmapCallback() {

                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(Bitmap response, int id) {
                                    itemViewHolder.home_diarylist_photo.setImageBitmap(response);
                                }
                            });
                } else {
                    itemViewHolder.home_diarylist_photo.setVisibility(View.GONE);
                }
                OkHttpUtils
                        .get()//
                        .url(diaries.get(position).user.iconUrl)//
                        .build()//
                        .execute(new BitmapCallback() {

                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(Bitmap response, int id) {
                                itemViewHolder.home_diarylist_icon.setImageBitmap(response);
                            }
                        });
            } else if (holder instanceof FootViewHolder) {
                FootViewHolder footViewHolder = (FootViewHolder) holder;
                switch (load_more_status) {
                    case PULLUP_LOAD_MORE:
                        footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                        break;
                    case LOADING_MORE:
                        footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                        break;
                }
            }

            // 如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    }
                });
            }

            }

        @Override
        public int getItemCount() {
            return diaries.size() + 1;
        }


        class MyViewHolder extends ViewHolder {

            TextView home_diarylist_author;
            TextView home_diarylist_comment;
            TextView home_diarylist_content;
            ImageView home_diarylist_icon;
            TextView home_diarylist_notebook;
            ImageView home_diarylist_photo;
            TextView home_diarylist_time;

            public MyViewHolder(View view) {
                super(view);
                home_diarylist_author = (TextView) view.findViewById(R.id.home_diarylist_author);
                home_diarylist_comment = (TextView) view.findViewById(R.id.home_diarylist_comment);
                home_diarylist_content = (TextView) view.findViewById(R.id.home_diarylist_content);
                home_diarylist_icon = (ImageView) view.findViewById(R.id.home_diarylist_icon);
                home_diarylist_notebook = (TextView) view.findViewById(R.id.home_diarylist_notebook);
                home_diarylist_photo = (ImageView) view.findViewById(R.id.home_diarylist_photo);
                home_diarylist_time = (TextView) view.findViewById(R.id.home_diarylist_time);

            }
        }

        class FootViewHolder extends ViewHolder {
            private TextView foot_view_item_tv;

            public FootViewHolder(View view) {
                super(view);
                foot_view_item_tv = (TextView) view.findViewById(R.id.item_rv_foot);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }*/
}
