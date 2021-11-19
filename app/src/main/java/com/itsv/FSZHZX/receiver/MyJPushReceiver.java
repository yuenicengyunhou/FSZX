package com.itsv.FSZHZX.receiver;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.itsv.FSZHZX.base.Constant;
import com.itsv.FSZHZX.base.TagAliasOperatorHelper;
import com.itsv.FSZHZX.model.ProfileDetailsM;
import com.itsv.FSZHZX.ui.activity.MtNotifyActivity;
import com.itsv.FSZHZX.ui.activity.WebActivity;

import java.text.MessageFormat;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyJPushReceiver extends JPushMessageReceiver {
    private static final String TAG = "PushMessageReceiver";

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] " + customMessage);
        processCustomMessage(context, customMessage);
    }

    /**
     * 通过notificationExtras判断通知类型：
     * 通知公告 alertType:tzgg
     * 政协文件 alertType:zxwj
     */
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageOpened] " + message);
        try {
            Intent i = null;
            //打开自定义的Activity
            if (message.notificationExtras.equals("{}")) {
                i = new Intent(context, MtNotifyActivity.class);
                i.putExtra("title", "视频会议");
                i.putExtra("userName", Constant.USER_NAME);
            } else if (message.notificationExtras.contains("tzgg")) {
                String mEncodeRealName = readUserInfoCache(context, 0);
                String userId = readUserInfoCache(context, 1);
                if (null != mEncodeRealName) {
                    i = new Intent(context, WebActivity.class);
                    i.putExtra("url", MessageFormat.format("{0}{1}{2}&userName={3}&userId={4}", Constant.BASE_H5_URL, Constant.TAG_NOTICE, Constant.listNotice, mEncodeRealName, userId));
                }
            } else if (message.notificationExtras.contains("zxwj")) {
                i = new Intent(context, WebActivity.class);
                String mEncodeRealName = readUserInfoCache(context, 0);
                String userId = readUserInfoCache(context, 1);
                if (null != mEncodeRealName) {
                    i = new Intent(context, WebActivity.class);
                    i.putExtra("url", MessageFormat.format("{0}{1}{2}&userName={3}&userId={4}", Constant.BASE_H5_URL, Constant.TAG_CPPCC, Constant.listCPPCC, mEncodeRealName, userId));
                }
            }
            Bundle bundle = new Bundle();
            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle);
            bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent);
            if (null != i) {
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        } catch (Throwable throwable) {
            throwable.getStackTrace();
        }
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageArrived] " + message);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG, "[onRegister] " + registrationId);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, CustomMessage customMessage) {
//        if (MainActivity.isForeground) {
//            String message = customMessage.message;
//            String extras = customMessage.extra;
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//        }
    }

    @Override
    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
        super.onNotificationSettingsCheck(context, isOn, source);
        Log.e(TAG, "[onNotificationSettingsCheck] isOn:" + isOn + ",source:" + source);
    }

    /**
     * 读取存储的userInfo数据
     * type =0 返回二次编码过的用户名
     * type=1 返回userId
     */
    private String readUserInfoCache(Context context, int type) {
        SharedPreferences preferences = context.getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        String params;
        if (type == 0) {
            params = preferences.getString("encodeRealName", "");
        } else {
            params = String.valueOf(preferences.getLong("userId", 0));
        }
        if (TextUtils.isEmpty(params)) {
            return null;
        }
        return params;
    }
}
