package com.itheima.topline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.itheima.topline.R;
import com.itheima.topline.bean.NewsBean;
import com.itheima.topline.utils.DBUtils;
import com.itheima.topline.utils.UtilsHelper;
import com.itheima.topline.view.SlidingButtonView;
import java.util.ArrayList;
import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.
        MyViewHolder> implements SlidingButtonView.IonSlidingButtonListener {
    private Context mContext;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private List<NewsBean> newsList = new ArrayList<>();
    private SlidingButtonView mMenu = null;
    public CollectionAdapter(Context context) {
        mContext = context;
        mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;
    }
    public void setData(List<NewsBean> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return newsList.size();
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        NewsBean bean = newsList.get(position);
        holder.tv_name.setText(bean.getNewsName());
        holder.tv_newsTypeName.setText(bean.getNewsTypeName());
        Glide
                .with(mContext)
                .load(bean.getImg1())
                .error(R.mipmap.ic_launcher)
                .into((holder).iv_img);
        //设置内容布局的宽为屏幕宽度
        holder.layout_content.getLayoutParams().width = UtilsHelper.getScreenWidth(
                mContext);
        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(v, n);
                }
            }
        });
        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);
            }
        });
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.collection_item, arg0,
                false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_img;
        public TextView btn_Delete, tv_name, tv_newsTypeName;
        public ViewGroup layout_content;
        public MyViewHolder(View itemView) {
            super(itemView);
            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_newsTypeName = (TextView) itemView.findViewById(R.id.tv_newsType_name);
            ((SlidingButtonView) itemView).setSlidingButtonListener(CollectionAdapter.
                    this);
        }
    }
    public void removeData(int position, TextView tv_none, String userName) {
        NewsBean bean = newsList.get(position);
        //从收藏新闻的数据库中也要删除此数据
        DBUtils.getInstance(mContext).delCollectionNewsInfo(bean.getId(), bean.
                getType(), userName);
        newsList.remove(position);
        notifyItemRemoved(position);
        if (newsList.size() == 0)
            tv_none.setVisibility(View.VISIBLE);
    }
    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }
    /**
     * 滑动或者点击了Item监听
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
    }
    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;
    }
    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }
    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);
        void onDeleteBtnCilck(View view, int position);
    }
}
