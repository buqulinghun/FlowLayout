package com.example.flowlayout.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yubin on 2016/5/24.
 */
public class FlowLayout extends ViewGroup {
    private Line mLine;
    private List<Line> mLines = new ArrayList<Line>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        GestureDetector mGesureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                System.out.println("down event");
                return super.onDown(e);
            }
        });

    }

    private int mHorizontalSpace = 10;
    private int mVerticalSpace = 10;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mLines.clear();
        mLine = null;
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
        //       int childHeight = parentHeight - getPaddingTop() - getPaddingBottom();

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            //测量孩子,在这里获取getMeasuredWidth
         /*   measureChild(child, MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));*/

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            //添加到行中
            if (mLine == null) {
                //行为空，直接添加
                mLine = new Line(childWidth, mHorizontalSpace);
                System.out.println("1换行");
                mLine.addView(child);
                mLines.add(mLine);
            } else {
                //
                if (mLine.canAdd(child)) {
                    System.out.println("不换行");
                    mLine.addView(child);
                } else {
                    mLine = new Line(childWidth, mHorizontalSpace);
                    System.out.println("2换行");

                    mLine.addView(child);
                    mLines.add(mLine);
                }
            }

        }

        int parentHeight = getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);

            parentHeight += line.mHeight;

            if (i != 0) {
                parentHeight += mVerticalSpace;
            }
        }
        //设置自己的大小
        setMeasuredDimension(parentWidth, parentHeight);

    }

    //布局
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //布局孩子
        int pLeft = getPaddingLeft();
        int pTop = getPaddingTop();
        //       int bottom = 0;


        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            //让行布局子控件
            line.layout(pLeft, pTop);
            //      line.layout(10, 10);
            pTop += line.mHeight + mVerticalSpace;//累计每一行的高度
        }
        //布局自己
        //layout(pLeft, pLeft + pRight + mLine.mMaxWidth, pTop, bottom);
    }


    private class Line {
        public int mCurrentWidth;//记录当前行宽
        public int mMaxWidth;//记录最大宽度
        public int mHeight;//记录行的高度
        public int mSpace;
        public List<View> views = new ArrayList<View>();//用来存放该行的view

        /**
         * @param width 最大宽度，通过父测量控件传递
         */
        Line(int width, int space) {
            mMaxWidth = width;
            mSpace = space;
        }

        /**
         * @param view 在行中添加视图
         */
        public void addView(View view) {
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            //如果一行中没有view 并且宽度超过一行最大宽度,记录行宽
            if (views.size() == 0) {
                if (measuredWidth > mMaxWidth) {
                    mCurrentWidth = mMaxWidth;
                    views.add(view);
                } else {
                    mCurrentWidth = measuredWidth;
                    views.add(view);
                }
            } else {
                mCurrentWidth += measuredWidth + mSpace;//累加宽度
                views.add(view);
            }

            //记录行高
            mHeight = (mHeight > measuredHeight) ? mHeight : measuredHeight;
        }


        /**
         * @param view 将要被添加的视图
         * @return 在视图添加前判断该视图是否能被添加
         */
        public boolean canAdd(View view) {
            //行中没有视图
            if (views.size() == 0) {
                return true;
            } else if ((mCurrentWidth + view.getMeasuredWidth()) > mMaxWidth) {
                return false;
            } else {
                return true;
            }
        }


        public void layout(int left, int top) {
            int pL = left;//记录子控件左边距

            int avgWidth = 0;
            int size = views.size();
            if(mMaxWidth - mCurrentWidth > 0){
                int extraWidth = mMaxWidth - mCurrentWidth;//最右边多出的部分
                avgWidth = (int) (extraWidth * 1.f / size + .5f);
            }


            for (int i = 0; i < size; i++) {
                View view = views.get(i);

                int measuredWidth = view.getMeasuredWidth();
                int measuredHeight = view.getMeasuredHeight();

                //重新测量
                if(avgWidth > 0){
                    int newWidth = measuredWidth + avgWidth;

                    int widthSpec = MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY);
                    int heightSpec = MeasureSpec.makeMeasureSpec(measuredHeight,MeasureSpec.EXACTLY);
                    view.measure(widthSpec,heightSpec);//重新测量

                    measuredWidth = view.getMeasuredWidth();//更新尺寸
                    measuredHeight = view.getMeasuredHeight();
                }

                int pR = pL + measuredWidth;//累加自身宽度
                int pB = top + measuredHeight;//累加自身高度

                view.layout(pL, top, pR, pB);

                pL += left + measuredWidth + mHorizontalSpace;//累加自己的宽度和水平空隙

            }
        }

    /*    public void layout(int offsetLeft, int offsetTop) {
            // 给孩子布局

            int currentLeft = offsetLeft;

            int size = views.size();

            for (int i = 0; i < size; i++) {
                View view = views.get(i);
                int viewWidth = view.getMeasuredWidth();
                int viewHeight = view.getMeasuredHeight();


                // 布局
                int left = currentLeft;
                int top =offsetTop ;
                // int top = offsetTop;
                int right = left + viewWidth;
                int bottom = top + viewHeight;
                view.layout(left, top, right, bottom);

                currentLeft += viewWidth + mHorizontalSpace;
            }
        }*/


    }
}
