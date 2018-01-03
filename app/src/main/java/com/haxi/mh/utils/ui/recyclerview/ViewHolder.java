package com.haxi.mh.utils.ui.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haxi.mh.utils.model.SharedPreHelper;


/**
 * recyclerview.ViewHolder 基类
 * Created by Han on 2018/01/03
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;

    /**
     * 私有构造方法
     *
     * @param itemView
     */
    private ViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public static ViewHolder create(Context context, int layoutId, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(itemView);
    }

    public static ViewHolder create(View itemView) {
        return new ViewHolder(itemView);
    }

    /**
     * 通过id获得控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public View getSwipeView() {
        ViewGroup itemLayout = ((ViewGroup) mConvertView);
        if (itemLayout.getChildCount() == 2) {
            return itemLayout.getChildAt(1);
        }
        return null;
    }

    public void setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public void setText(int viewId, int textId) {
        TextView textView = getView(viewId);
        textView.setText(textId);
    }
    public void setTextSize(int viewId, float size) {
        TextView textView = getView(viewId);
        if(!"".equals(SharedPreHelper.getInstance().getStringData("setTextSizeNew"))&&
                !"".equals(SharedPreHelper.getInstance().getStringData("currentProgress"))){

            if(!"".equals(SharedPreHelper.getInstance().getStringData("TextSizeNew"))){

            }else{
                //如果已经设置过字体大小，但是没有保存，则用现有的字体大小除以已设置的字体放缩倍数，计算出原始字体的大小,并保存
                SharedPreHelper.getInstance().setData("TextSizeNew",textView.getTextSize()/ Float.parseFloat(SharedPreHelper.getInstance().getStringData("setTextSizeNew"))+"");
            }

        }else{
            //未调节过字体大小
            SharedPreHelper.getInstance().setData("TextSizeNew", textView.getTextSize() + "");
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(SharedPreHelper.getInstance().getStringData("TextSizeNew"))*size);
    }

    public void setImage(int viewId, boolean isVisible) {
        ImageView imageView = getView(viewId);
        if(isVisible==true){
            imageView.setVisibility(View.VISIBLE);
        }else{
            imageView.setVisibility(View.INVISIBLE);
        }

    }
    public void setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener clickListener) {
        View view = getView(viewId);
        view.setOnClickListener(clickListener);
    }

    public void setBgRes(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
    }

    public void setBgColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
    }

    public void setClickListener(int viewId, View.OnClickListener listener){
        View view = getView(viewId);
        view.setOnClickListener(listener);
    }

    public void setVisibility(int id, int isGone) {
        View view = getView(id);
        view.setVisibility(isGone);
    }
}
