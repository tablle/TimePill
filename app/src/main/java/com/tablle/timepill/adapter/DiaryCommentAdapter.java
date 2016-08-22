package com.tablle.timepill.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tablle.timepill.R;
import com.tablle.timepill.view.FixTextView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by tong on 2016/8/20.
 */
public class DiaryCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView
    private Context context;
    private JSONArray comments;

    public DiaryCommentAdapter(Context context,JSONArray comments){
        this.context = context;
        this.comments = comments;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder==================" );
        if (viewType == TYPE_ITEM) {
            MyViewHolder itemViewHolder = new MyViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.item_recycleview_comment, parent,
                    false));

            return itemViewHolder;
        } /*else if (viewType == TYPE_FOOTER) {
            FootViewHolder footViewHolder = new FootViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.item_recycleview_foot, parent,
                    false));

            return footViewHolder;
        }*/
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        System.out.println("onBindViewHolder==================" );
        if (holder instanceof MyViewHolder) {
            System.out.println("onBindViewHolder==================1111111111" );
            final MyViewHolder itemViewHolder = (MyViewHolder) holder;
            try {
                System.out.println("onBindViewHolder==================2222222222");
                System.out.println("size==================1111111111"+comments.length() );
                JSONObject temp = (JSONObject) comments.get(position);
                System.out.println("temp==================" + temp);
                String content = temp.getString("content");
                System.out.println("content==================" + content);
                itemViewHolder.comment_author.setText(temp.getJSONObject("user").getString("name"));
                //System.out.println("comment_author==================" + temp.getString("name"));
                itemViewHolder.comment_content.setText(temp.getString("content"));
                itemViewHolder.comment_time.setText(temp.getString("created"));

                OkHttpUtils
                        .get()//
                        .url(temp.getJSONObject("user").getString("iconUrl"))//
                        .build()//
                        .execute(new BitmapCallback() {

                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(Bitmap response, int id) {
                                itemViewHolder.comment_icon.setImageBitmap(response);
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } /*else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
            }
        }*/




    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView comment_author;
        FixTextView comment_content;
        ImageView comment_icon;
        TextView comment_time;

        public MyViewHolder(View view) {
            super(view);
            comment_author = (TextView) view.findViewById(R.id.comment_author);
            comment_content = (FixTextView) view.findViewById(R.id.comment_content);
            comment_icon = (ImageView) view.findViewById(R.id.comment_icon);
            comment_time = (TextView) view.findViewById(R.id.comment_time);


        }
    }

    /*class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView) view.findViewById(R.id.item_rv_foot);
        }
    }*/

    /*@Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }*/

    @Override
    public int getItemCount() {
        return comments.length();
    }
}
