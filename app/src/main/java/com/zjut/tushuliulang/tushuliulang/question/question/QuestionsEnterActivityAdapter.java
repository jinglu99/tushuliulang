package com.zjut.tushuliulang.tushuliulang.question.question;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zjut.tushuliulang.tushuliulang.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/1 0001.
 */
public class QuestionsEnterActivityAdapter extends BaseAdapter {

    private ArrayList<RespondAndStudentID> list;
    private Context context;

    private DisplayImageOptions options	= null;

    public QuestionsEnterActivityAdapter(Context context, ArrayList<RespondAndStudentID> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        ;
        if (convertView == null) {
            //这里修改布局
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_listview,
                    null);
            //这里修改组件
            holder.tv_activity_name = (TextView) convertView
                    .findViewById(R.id.tv_activity_name);
            holder.tv_activity_content = (TextView) convertView
                    .findViewById(R.id.tv_activity_content);
            holder.tv_activity_time = (TextView) convertView
                    .findViewById(R.id.tv_activity_time);
            holder.iv_circle = (CircleImageView) convertView
                    .findViewById(R.id.iv_circle);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //初始化Options设置
        initOptions();

        //改变数据 参数类型：ArrayList<RespondAndStudentID>
        ImageLoader.getInstance().displayImage(list.get(position).getUrl(), holder.iv_circle, options);
        holder.tv_activity_name.setText(list.get(position).getStudentID());
        holder.tv_activity_content.setText(list.get(position).getRespond());
        holder.tv_activity_time.setText(list.get(position).getDate());
        return convertView;
    }

    /**
     * 初始化Options设置
     * */
    private void initOptions() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

    class ViewHolder {
        TextView tv_activity_name;
        TextView tv_activity_content;
        TextView tv_activity_time;
        CircleImageView iv_circle;
    }

    //弹框提示
   /* public void Pop_up(final int position){
        new AlertDialog.Builder(context).setTitle("删除提示")//设置对话框标题

                .setMessage("确定删除吗？")//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        //这里是删除键设置，需要与服务器交互，提交删除数据
                        list.remove(position);
                        notifyDataSetChanged();
                    }

                }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮


            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件

                Log.i("MY", " 不删除哦");
            }

        }).show();//在按键响应事件中显示此对话框
    }*/


}
