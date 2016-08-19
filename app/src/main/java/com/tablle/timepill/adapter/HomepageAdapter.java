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
import com.tablle.timepill.model.DiaryBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by tong on 2016/8/16.
 */
public class HomepageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView
    public static final int PULLUP_LOAD_MORE = 0; //上拉加载更多
    public static final int LOADING_MORE = 1; //正在加载中
    private int load_more_status = 0;
    private Context context;
    private List<DiaryBean> diaries;

    public HomepageAdapter(Context context, List<DiaryBean> diaries) {
        this.context = context;
        this.diaries = diaries;

    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            MyViewHolder itemViewHolder = new MyViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.home_page_rv_item, parent,
                    false));

            return itemViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            FootViewHolder footViewHolder = new FootViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.item_recycleview_foot, parent,
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


    class MyViewHolder extends RecyclerView.ViewHolder {

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

    class FootViewHolder extends RecyclerView.ViewHolder {
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
