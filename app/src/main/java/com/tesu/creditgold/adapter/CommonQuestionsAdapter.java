package com.tesu.creditgold.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tesu.creditgold.R;
import com.tesu.creditgold.bean.AreaBean;
import com.tesu.creditgold.bean.CommonQuestionBean;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class CommonQuestionsAdapter extends BaseAdapter{

    private String TAG="CommonQuestionsAdapter";
    private List<CommonQuestionBean> commonQuestionBeanList;
    private Context mContext;
    private int  selectItem=-1;
    public CommonQuestionsAdapter(Context context, List<CommonQuestionBean> commonQuestionBeanList){
        mContext=context;
        this.commonQuestionBeanList=commonQuestionBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return commonQuestionBeanList.get(arg0);
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
            view= LayoutInflater.from(mContext).inflate(R.layout.common_questions_item, null);
            vh=new ViewHolder();
            vh.tv_question=(TextView)view.findViewById(R.id.tv_question);
            view.setTag(vh);
        }else{
            vh=(ViewHolder)view.getTag();
        }
        vh.tv_question.setText(""+commonQuestionBeanList.get(pos).getQuestion());


        return view;
    }

    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return commonQuestionBeanList.size();
    }

    class ViewHolder{
        public TextView tv_question;
    }
}
