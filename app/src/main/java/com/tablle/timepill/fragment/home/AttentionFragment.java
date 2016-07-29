package com.tablle.timepill.fragment.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tablle.timepill.R;
import com.tablle.timepill.constant.Url;
import com.tablle.timepill.model.DiaryListBean;
import com.tablle.timepill.utils.Utils;
import com.tablle.timepill.widget.DividerItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

import static butterknife.ButterKnife.findById;

public class AttentionFragment extends Fragment {
    private AttentionAdapter attentionAdapter;
    @Bind(R.id.homepage_swipe_refresh_layout)
    SwipeRefreshLayout homeRefreshLayout;
    private DiaryListBean bean;
    private String result;
    private String token;
    private RecyclerView recyclerView;

    public AttentionFragment() {
        // Required empty public constructor
    }

    public static AttentionFragment newInstance() {
        return new AttentionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attention, container, false);
        ButterKnife.bind(this, view);

        recyclerView = findById(view, R.id.attention_recycler_view);

        //initData();
        recyclerView.addItemDecoration(
                new DividerItemDecoration(Utils.getDrawableResource(getActivity(), R.drawable.line)));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String email = "tbl0311@126.com";
        String password = "603616203";
        token = getBase64Token(email, password);

        OkHttpUtils
                .get()
                .url(Url.URL_Diary_ATTENTION+"?page=1"+"&page_size=20")
                .addHeader("Authorization", token)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        System.out.println("faile======="+e.toString());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        System.out.println("success===" + response);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    System.out.println("111111==================");
                                    result = response;
                                    execute();
                                    System.out.println("22222==================");
                                    attentionAdapter = new AttentionAdapter();
                                    System.out.println("33333==================");
                                    recyclerView.setAdapter(attentionAdapter);

                                    System.out.println("444444==================");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


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

    private void execute() {
        bean = new Gson().fromJson(result, DiaryListBean.class);
    }

    private String getBase64Token(String email, String password) {
        return "Basic " + Base64.encodeToString((email + ":" + password).getBytes(), Base64.NO_WRAP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class AttentionAdapter extends RecyclerView.Adapter<AttentionAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.home_page_rv_item, parent,
                    false));

            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position)
        {
            holder.home_diarylist_author.setText(bean.diaries.get(position).user.name);
            holder.home_diarylist_comment.setText(bean.diaries.get(position).comment_count);
            holder.home_diarylist_content.setText(bean.diaries.get(position).content);
            holder.home_diarylist_notebook.setText(bean.diaries.get(position).notebook_subject);
            holder.home_diarylist_time.setText(bean.diaries.get(position).getCreatedTimeString());
            if(bean.diaries.get(position).type.equals("2")&&bean.diaries.get(position).photoThumbUrl!=null){
                holder.home_diarylist_photo.setVisibility(View.VISIBLE);
                OkHttpUtils
                        .get()//
                        .url(bean.diaries.get(position).photoThumbUrl)//
                        .build()//
                        .execute(new BitmapCallback() {

                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(Bitmap response, int id) {
                                holder.home_diarylist_photo.setImageBitmap(response);
                            }
                        });
            }else{
                holder.home_diarylist_photo.setVisibility(View.GONE);
            }
            OkHttpUtils
                    .get()//
                    .url(bean.diaries.get(position).user.iconUrl)//
                    .build()//
                    .execute(new BitmapCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(Bitmap response, int id) {
                            holder.home_diarylist_icon.setImageBitmap(response);
                        }
                    });

        }

        @Override
        public int getItemCount()
        {
            return bean.diaries.size();
        }


        class MyViewHolder extends ViewHolder
        {

            TextView home_diarylist_author;
            TextView home_diarylist_comment;
            TextView home_diarylist_content;
            ImageView home_diarylist_icon;
            TextView home_diarylist_notebook;
            ImageView home_diarylist_photo;
            TextView home_diarylist_time;

            public MyViewHolder(View view)
            {
                super(view);
                home_diarylist_author = (TextView)view.findViewById(R.id.home_diarylist_author);
                home_diarylist_comment = (TextView)view.findViewById(R.id.home_diarylist_comment);
                home_diarylist_content = (TextView)view.findViewById(R.id.home_diarylist_content);
                home_diarylist_icon = (ImageView)view.findViewById(R.id.home_diarylist_icon);
                home_diarylist_notebook = (TextView)view.findViewById(R.id.home_diarylist_notebook);
                home_diarylist_photo = (ImageView)view.findViewById(R.id.home_diarylist_photo);
                home_diarylist_time = (TextView)view.findViewById(R.id.home_diarylist_time);

            }
        }

    }

}
