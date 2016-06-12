package com.jingyan.newsimooc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class NewsAdapter extends BaseAdapter {

    private List<NewsBean> mList;
    private LayoutInflater mInflater;

    public NewsAdapter(Context context,List<NewsBean> data){
        this.mList = data;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_layout,null);
            viewHolder.mIcon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.mTilte = (TextView) view.findViewById(R.id.title);
            viewHolder.mContent = (TextView) view.findViewById(R.id.content);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
//        viewHolder.mIcon.setImageResource(Integer.parseInt(mList.get(i).getNewsIcon()));
        viewHolder.mIcon.setImageResource(R.mipmap.ic_launcher);
        String url = mList.get(i).getNewsIcon();
        viewHolder.mIcon.setTag(url);
        new ImageLoader().showImageByThread(viewHolder.mIcon,url);
        viewHolder.mTilte.setText(mList.get(i).getNewsTilte());
        viewHolder.mContent.setText(mList.get(i).getNewsContent());
        return view;
    }

    class ViewHolder{
        public ImageView mIcon;
        public TextView mTilte,mContent;
    }
}
