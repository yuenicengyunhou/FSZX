package com.itsv.FSZHZX.ui.activity;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.BaseWebActivity;
import com.itsv.FSZHZX.base.Constant;

import butterknife.BindView;

public class FakeActivity extends BaseWebActivity {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @Override
    protected void beforeWebInit() {
        initToolbar(toolbar,false);
    }

    @Override
    protected void afterWebInit() {
        agentWeb.getJsAccessEntrace().quickCallJs("getToken", Constant.TOKEN);
    }

    @Override
    protected boolean initWeb() {
        return true;
    }

    @Override
    protected String getWebLink(Gson gson) {
        return null;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }



   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_done);
    }*/
}
