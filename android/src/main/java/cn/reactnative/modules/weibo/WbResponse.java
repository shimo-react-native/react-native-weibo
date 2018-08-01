/*
 * Copyright (c) 2018. chenqiang Inc. All rights reserved.
 */

package cn.reactnative.modules.weibo;

/**
 * 分享到新浪微博的结果类
 *
 * @author chenqiang
 * @date 2018/7/31
 */
public enum WbResponse {

    SUCCESS(0, "send ok!!!"),
    CANCEL(1, "send cancel!!!"),
    FAIL(2, "send fail!!!");

    public int mErrorCode;
    public String mErrorMsg;

    WbResponse(int errorCode, String errorMsg) {
        this.mErrorCode = errorCode;
        this.mErrorMsg = errorMsg;
    }
}
