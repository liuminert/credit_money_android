package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.bean.AreaBean;
import com.tesu.creditgold.response.GetArticleListResponse;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class ArticleListAdapter extends BaseAdapter{

    private String TAG="AreaAdapter";
    private List<GetArticleListResponse.ArticleBean> articleBeanList;
    private Context mContext;
    public ArticleListAdapter(Context context, List<GetArticleListResponse.ArticleBean> articleBeanList){
        mContext=context;
        this.articleBeanList=articleBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return articleBeanList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        final ViewHolder vh;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.article_item, null);
            vh=new ViewHolder();
            vh.tv_title=(TextView)view.findViewById(R.id.tv_title);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        vh.tv_title.setText(""+articleBeanList.get(pos).article_title);
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return articleBeanList.size();
    }

    class ViewHolder{
        public TextView tv_title;
    }
}
