package com.example.fling.flower;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.BaseViewHolder>{
    private ArrayList<String> list = new ArrayList<>();
    private Resources res;

    public OnItemClickListener onItemClickListener;
    public void replaceAll(ArrayList<String> l){
        list.clear();
        if(l !=null && l.size()>0){
            list.addAll(l);
        }
        notifyDataSetChanged();
    }

    @Override
    public DemoAdapter.BaseViewHolder onCreateViewHolder(ViewGroup viewGroup,int i) {
        //绑定layout,创建viewHolder
        return new OneViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.one, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DemoAdapter.BaseViewHolder baseViewHolder, final int i) {     //进行数据和视图绑定
//
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,i);
            }
        });

        baseViewHolder.setData(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }   //确定item的个数


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
        void setData(Object data){}
    }

    class OneViewHolder extends BaseViewHolder{
        private ImageView ivImage;
        public OneViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView)itemView.findViewById(R.id.ivImage);
            int width = ((Activity) ivImage.getContext()).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = ivImage.getLayoutParams();

            //设置图片的相对于屏幕的宽高比
            params.width = width/2;
            params.height =  (int) (200 + Math.random() * 400) ;
            ivImage.setLayoutParams(params);
            res = itemView.getContext().getResources();
        }

        void setData(Object data) {
            if (data != null) {
                String text = (String) data;
                Glide.with(itemView.getContext())
                        .load(text)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ic_launcher)
                        .crossFade()
                        .into(ivImage);
//                Glide.with(itemView.getContext())
//                        .load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg")
//                        .into(ivImage);
//                Bitmap bitmap = BitmapFactory.decodeResource(res,R.mipmap.ic_launcher);

//                });
            }
        }
    }
}
