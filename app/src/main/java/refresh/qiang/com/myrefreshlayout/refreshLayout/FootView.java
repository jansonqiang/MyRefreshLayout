package refresh.qiang.com.myrefreshlayout.refreshLayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import refresh.qiang.com.myrefreshlayout.R;

public class FootView extends LinearLayout {
    public FootView(Context context) {
        this(context,null);
    }

    public FootView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FootView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){

        LayoutInflater inflater = LayoutInflater.from(getContext());

        inflater.inflate(R.layout.foot_view,this,true);

    }









}
