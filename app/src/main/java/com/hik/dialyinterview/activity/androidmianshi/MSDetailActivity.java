package com.hik.dialyinterview.activity.androidmianshi;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.hik.dialyinterview.BaseActivity;
import com.hik.dialyinterview.R;

import org.w3c.dom.Text;


public class MSDetailActivity extends BaseActivity {
    TextView textItem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ms_detail);

        textItem=(TextView) findViewById(R.id.testview);
        textItem.setText(getIntent().getStringExtra("content"));
        textItem.setMovementMethod(new ScrollingMovementMethod());
    }
}
