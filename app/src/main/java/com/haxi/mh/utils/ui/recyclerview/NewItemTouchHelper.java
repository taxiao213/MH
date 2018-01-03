package com.haxi.mh.utils.ui.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.haxi.mh.base.BaseActivity;

import java.util.Collections;
import java.util.List;

/**
 * recyclerview 实现快速上下左右拖到
 * Created by Han on 2018/01/03
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 * 用法：
 * NewItemTouchHelper helper = new NewItemTouchHelper(context, adapter);
 * ItemTouchHelper itemTouchHelper = new ItemTouchHelper(helper);
 * itemTouchHelper.attachToRecyclerView(recyclerview);
 */

public class NewItemTouchHelper<T> extends android.support.v7.widget.helper.ItemTouchHelper.Callback {
    private RecyclerView.Adapter adapter;
    private BaseActivity mActivity;
    private final Vibrator mVibrator;
    private List<T> results;

    public NewItemTouchHelper(BaseActivity activity, RecyclerView.Adapter adapter) {
        super();
        this.adapter = adapter;
        this.mActivity = activity;
        mVibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);//震动

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags;//GridLayoutManager可拖动的方向分为上 下 左 右 LinearLayoutManager可拖动的方向分为上 下
        int swipFlags;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipFlags = 0;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipFlags = 0;
        }
        return makeMovementFlags(dragFlags, swipFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(results, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(results, i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }


    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            mVibrator.vibrate(60);
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundColor(0);

        super.clearView(recyclerView, viewHolder);
    }


}
