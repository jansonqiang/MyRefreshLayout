package refresh.qiang.com.myrefreshlayout.refreshLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/***
 * 自个儿写Android的下拉刷新/上拉加载控件
 * https://blog.csdn.net/ghost_programmer/article/details/52682252
 */
public class MyRefreshLayout extends ViewGroup {

    public static final String TAG = MyRefreshLayout.class.getName();

    private int mLayoutContentHeight = 0;

    private int mLastMoveY; //最后一次触碰的y坐标

    int  effectiveScrollY ; //有效距离
    LayoutInflater mInflater;

    // 用于平滑滑动的Scroller对象
    private Scroller mLayoutScroller;


    onRefreshListener onRefreshListener;


    HeadView mHeadView;
    FootView mFootView;


    public MyRefreshLayout(Context context) {
        this(context, null);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mInflater = LayoutInflater.from(context);
        mLayoutScroller=new Scroller(context);

    }

    /**
     * 加载布局文件后才调用的 ， 加入放在构造器，那么footView就排第二了
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addHeadView();
        addFootView();


    }

    private void addHeadView() {

        HeadView headView = new HeadView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        headView.setLayoutParams(layoutParams);
        addView(headView);

        this.mHeadView = headView;


    }

    private void addFootView() {
        FootView footView = new FootView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        footView.setLayoutParams(layoutParams);
        addView(footView);
        this.mFootView = footView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mLayoutContentHeight = 0;
        effectiveScrollY = mHeadView.getMeasuredHeight()*2;

        // 置位
        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);

            //getMeasuredHeight 在onMeasure后得知值
            //getWidth 要在onLayout才知道
            if (child == mHeadView) {
                child.layout(0,0 -child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
            } else if (child == mFootView) {
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(),
                        child.getMeasuredHeight() + mLayoutContentHeight);
            } else {

                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(),
                        mLayoutContentHeight + child.getMeasuredHeight());

                mLayoutContentHeight += child.getMeasuredHeight();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
      int y = (int) event.getY();
      Log.e(TAG, "getScrollY() = " +getScrollY());


      switch (event.getAction()){
          case MotionEvent.ACTION_DOWN:
              mLastMoveY = y;
              mHeadView.setText("下拉刷新");
              break;
          case MotionEvent.ACTION_MOVE:
              int dy = mLastMoveY - y;//手指移动的距离
             // Log.d(TAG,"dy = "+dy+" mLastMoveY = "+mLastMoveY+" y"+y);
              if(dy<0){

                if(Math.abs(getScrollY()) <= this.getMeasuredHeight() / 2){
                    scrollBy(0, dy);
                    if(Math.abs(getScrollY()) >= effectiveScrollY)
                    mHeadView.setText("松开刷新");
                }
              }

              break;

          case MotionEvent.ACTION_UP:

              boolean isEffective =false;
              //拉到有效范围了
              if(Math.abs(getScrollY())>=effectiveScrollY){
                  mLayoutScroller.startScroll(0,getScrollY(),0 ,-(getScrollY() + mHeadView.getMeasuredHeight()) );
                  mHeadView.setText("正在刷新");
                  isEffective = true;
              }else {
                  mLayoutScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                  isEffective = false;
              }
              //注意，一定要进行invalidate刷新界面，触发computeScroll()方法，
              // 因为单纯的startScroll()是属于Scroller的，只是一个辅助类，并不会触发界面的绘制
              invalidate();

              if(onRefreshListener!=null){
                  onRefreshListener.onRefresh();
              }



              break;
      }
      mLastMoveY = y;

      return true;

    }

    //https://blog.csdn.net/shakespeare001/article/details/51588657
    //https://blog.csdn.net/lfdfhl/article/details/53143114
    //计算 scroll
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mLayoutScroller.computeScrollOffset()){//第二步
            scrollTo(mLayoutScroller.getCurrX(),mLayoutScroller.getCurrY());//第三步
            invalidate();
        }
    }


    public interface  onRefreshListener{
        void onRefresh();
    }

    public void setOnRefreshListener(MyRefreshLayout.onRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    private onRefreshListener mRefreshListener;

    public void setRefreshListener(onRefreshListener listener){
        mRefreshListener = listener;
    }

}
