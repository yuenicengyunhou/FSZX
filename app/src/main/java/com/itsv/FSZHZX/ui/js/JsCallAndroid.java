package com.itsv.FSZHZX.ui.js;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itsv.FSZHZX.R;
import com.itsv.FSZHZX.base.BaseWebActivity;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.model.MyAttachModel;
import com.itsv.FSZHZX.ui.activity.AddressBookActivity;
import com.itsv.FSZHZX.ui.activity.MtNotifyActivity;
import com.itsv.FSZHZX.ui.activity.PdfActivity;
import com.itsv.FSZHZX.ui.activity.QuizActivity;
import com.itsv.FSZHZX.ui.activity.WebActivity;
import com.itsv.FSZHZX.utils.DesignUtils;
import com.itsv.FSZHZX.utils.ToastUtils;
import com.just.agentweb.AgentWeb;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class JsCallAndroid {
    private final Context context;
    private SharedPreferences preferences;
    //    private MainActivity activity;
    private BaseWebActivity webActivity;

    @SuppressLint("CommitPrefEdits")
    public JsCallAndroid(Context context, AgentWeb agentWeb) {
        this.context = context;
        if (this.preferences == null) {
            this.preferences = context.getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        }
    }

//    public void setActivity(MainActivity homeActivity) {
//        this.appCompatActivity = homeActivity;
//        activity = homeActivity;
//    }

    public void setActivity(BaseWebActivity activity) {
        this.webActivity = activity;
    }

    @JavascriptInterface
    public void toAddress(Object object) {
        Intent intent = new Intent(webActivity, AddressBookActivity.class);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void toMeeting(Object object) {
        if (!TextUtils.isEmpty(Constant.USER_NAME)) {
            Intent intent = new Intent(webActivity, MtNotifyActivity.class);
            intent.putExtra("title", "视频会议");
            intent.putExtra("userName", Constant.USER_NAME);
            context.startActivity(intent);
        } else {
            ToastUtils.showSingleToast("未获取用户名");
        }

    }

    @JavascriptInterface
    public void toMtNoti(Object object) {
        Intent intent = new Intent(webActivity, MtNotifyActivity.class);
        intent.putExtra("title", "会议通知");
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void newPage(String params) {
        Log.e("WQ", "params====" + params);
        if (null == params) return;
        try {
            JSONObject object = new JSONObject(params);
            String title = object.getString("title");
            String url = object.getString("url");
            if (!title.equals("学习材料")) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                context.startActivity(intent);
            } else {
                ToastUtils.showSingleToast("功能建设中，后续开放");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public String getToken(Object object) {
        return Constant.TOKEN;
    }

    @JavascriptInterface
    public void toQuiz(Object object) {
        ToastUtils.showSingleToast("功能建设中，后续开放");
//        Intent intent = new Intent(webActivity, QuizActivity.class);
//        context.startActivity(intent);
    }

    @JavascriptInterface
    public void toPDF(String params) {
        if (null == params) return;
        Gson gson = new Gson();
        List<MyAttachModel> attachModels = gson.fromJson(params, new TypeToken<List<MyAttachModel>>() {
        }.getType());
        String[] strings = new String[attachModels.size()];
        for (int i = 0; i < attachModels.size(); i++) {
            MyAttachModel model = attachModels.get(i);
            if (model != null) {
                String fileName = model.fileName;
                strings[i] = fileName;
            } else {
                strings[i] = "此附件参数错误";
            }
        }
        showAttachDialog(attachModels, strings);
    }

    private int pdfPickPosition;

    public void showAttachDialog(List<MyAttachModel> fileListBeans, String[] strings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialogStyle);
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_attach, null);
        builder.setView(contentView);
        AlertDialog alertDialog = builder.create();
        ListView listView = contentView.findViewById(R.id.dialog_list);
        TextView title = contentView.findViewById(R.id.dialog_title);
        TextView confirm = contentView.findViewById(R.id.dialog_confirm);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.item_single_choice, strings);
        listView.setAdapter(arrayAdapter);
        listView.setItemChecked(0, true);
        listView.setOnItemClickListener((parent, view, position, id) -> pdfPickPosition = position);
        title.setOnClickListener(v -> alertDialog.dismiss());
        confirm.setOnClickListener(v -> {
            if (!strings[pdfPickPosition].equals("此附件参数错误")) {
                MyAttachModel model = fileListBeans.get(pdfPickPosition);
                toPDFActivity(model.fileUrl, model.fileId);
                alertDialog.dismiss();
            } else {
                Toast.makeText(context, "该附件错误，不能打开", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        Objects.requireNonNull(alertDialog.getWindow()).setLayout(4 * DesignUtils.getScreenWidth(context) / 5, 2 * DesignUtils.getScreenHeight(context) / 5);
    }

    private void toPDFActivity(String url, String id) {
        Intent intent = new Intent(context, PdfActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("fileId", id);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void showToast(String params) {
        if (null == params) return;
        ToastUtils.showSingleToast(params);

    }

}
