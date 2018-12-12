/*
 * Copyright (c) 2018. chenqiang Inc. All rights reserved.
 */

package cn.reactnative.modules.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.comori.tools.databus.DataBus;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

/**
 * 微博分享activity
 *
 * @author chenqiang
 * @date 2018/7/31
 */
public class WbShareActivity extends Activity implements WbShareCallback {

    private static final String SHARE_MSG = "share_to_wb_msg";
    WbShareHandler mShareHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShareHandler = new WbShareHandler(this);
        mShareHandler.registerApp();

        share(getIntent());
    }

    private void share(Intent intent) {

        Bundle msgBundle = intent.getBundleExtra(SHARE_MSG);
        if (msgBundle != null) {
            WeiboMultiMessage wbMsg = new WeiboMultiMessage();
            mShareHandler.shareMessage(wbMsg.toObject(msgBundle), false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mShareHandler.doResultIntent(intent, this);
    }

    @Override
    public void onWbShareSuccess() {
        handleShareResult(WbResponse.SUCCESS);
    }

    @Override
    public void onWbShareCancel() {
        handleShareResult(WbResponse.CANCEL);
    }

    @Override
    public void onWbShareFail() {
        handleShareResult(WbResponse.FAIL);
    }

    private void handleShareResult(WbResponse wbResponse) {
        finish();
        DataBus.get().with(DataBusEvents.WB_SHARE)
                .post(wbResponse);
    }

    public static void share(Context ctx, Bundle msgBundle) {
        Intent intent = new Intent(ctx, WbShareActivity.class);
        if (!(ctx instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(SHARE_MSG, msgBundle);
        ctx.startActivity(intent);
    }
}
