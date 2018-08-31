package com.haxi.mh.mvp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haxi.mh.R;
import com.haxi.mh.mvp.base.BaseActivityM;
import com.haxi.mh.mvp.entity.WxInfoDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Han on 2018/8/30
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class WxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private BaseActivityM baseActivityM;
    private List<WxInfoDao> list;

    public WxAdapter(BaseActivityM baseActivityM, List<WxInfoDao> list) {
        this.baseActivityM = baseActivityM;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(baseActivityM).inflate(R.layout.adapter_wx, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder hod = (ViewHolder) holder;
        WxInfoDao wxInfoDao = list.get(position);
        if (wxInfoDao != null) {
            String name = wxInfoDao.getName();
            hod.tvName.setText(name == null ? "" : name);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
