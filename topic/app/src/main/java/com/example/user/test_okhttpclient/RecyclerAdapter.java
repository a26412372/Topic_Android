package com.example.user.test_okhttpclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements View.OnClickListener{

    private Context mContext;
    private ArrayList<Card> mDataList;

    private OnItemClickListener mOnItemClickListener = null;

    public static interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public RecyclerAdapter(Context context, ArrayList<Card> data){
        mContext = context;
        mDataList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_cardview,parent,false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = mDataList.get(position);
        holder.tvTitle.setText(card.title);
        holder.ivPhoto.setImageResource(card.image);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onClick(View v){
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvTitle;
        public ImageView ivPhoto;

        public ViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_Article_Title);
            ivPhoto = itemView.findViewById(R.id.iv_Article_Photo);
        }
    }

}

class Card{
    public String id;
    public String title;
    public int image;
    public Card(String id, String title, int image){
        this.id = id;
        this.title = title;
        this.image = image;
    }
}