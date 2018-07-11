package refresh.qiang.com.myrefreshlayout.refreshLayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import refresh.qiang.com.myrefreshlayout.R;

public class HeadView extends LinearLayout {

    TextView tx;

    public HeadView(Context context) {
        this(context,null);
    }

    public HeadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){

        LayoutInflater inflater = LayoutInflater.from(getContext());

        inflater.inflate(R.layout.head_view,this,true);

        tx = findViewById(R.id.textView);
    }

    public void setText(String text){

        tx.setText(text);

    }









}
