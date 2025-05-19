package com.itheima.topline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {
    public MyHorizontalScrollView(Context context) {
        super(context);
    }
    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = (View) getChildAt(getChildCount() - 1);
        if (view.getLeft() - getScrollX() == 0) {   //如果为0，证明滑动到最左边
            onScrollListener.onLeft();
            Log.d("TAG", "最左边");
            //如果为0证明滑动到最右边
        } else if ((view.getRight() - (getWidth() + getScrollX())) == 0) {
            onScrollListener.onRight();
            Log.d("TAG", "最右边");
        } else {   //说明在中间
            onScrollListener.onScroll();
            Log.d("TAG", "中间");
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }
    private OnScrollListener onScrollListener;
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }
    public interface OnScrollListener {
        void onRight();
        void onLeft();
        void onScroll();
    }
}
