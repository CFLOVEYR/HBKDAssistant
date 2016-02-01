package com.tofirst.study.hbkdassistant.acitvity.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tofirst.study.hbkdassistant.R;
import com.tofirst.study.hbkdassistant.utils.common.SharePreUtils;
import com.tofirst.study.hbkdassistant.view.SlideSwitch;

public class UpdateSelectActivity extends AppCompatActivity {
    private boolean isLight;
    private Toolbar toolbar;
    private SlideSwitch tb_auto;
    private SlideSwitch tb_slide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_select);
        initView();

    }

    private void initView() {
        initToolBar();
        tb_auto = (SlideSwitch) findViewById(R.id.tb_autoupdate);
        tb_slide = (SlideSwitch) findViewById(R.id.tb_slideupdate);
        boolean auto = SharePreUtils.getsPreBoolean(UpdateSelectActivity.this, "AutoUpdateFlag", true);
        boolean slide = SharePreUtils.getsPreBoolean(UpdateSelectActivity.this, "SilentUpdateFlag", false);
        tb_auto.setState(auto);
        tb_slide.setState(slide);

        tb_auto.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                SharePreUtils.putPreBoolean(UpdateSelectActivity.this, "AutoUpdateFlag", true);
                SharePreUtils.putPreBoolean(UpdateSelectActivity.this, "SilentUpdateFlag", false);
                if (tb_slide.isChecked()) {
                    tb_slide.setState(false);
                }
            }

            @Override
            public void close() {
                SharePreUtils.putPreBoolean(UpdateSelectActivity.this, "AutoUpdateFlag", false);
            }
        });
        tb_slide.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                if (tb_auto.isChecked()) {
                    tb_auto.setState(false);
                }
                SharePreUtils.putPreBoolean(UpdateSelectActivity.this, "SilentUpdateFlag", true);
                SharePreUtils.putPreBoolean(UpdateSelectActivity.this, "AutoUpdateFlag", false);
            }

            @Override
            public void close() {
                SharePreUtils.putPreBoolean(UpdateSelectActivity.this, "SilentUpdateFlag", false);
            }
        });
    }

    private void initToolBar() {
        isLight = SharePreUtils.getsPreBoolean(this, "isLight", true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }
}
