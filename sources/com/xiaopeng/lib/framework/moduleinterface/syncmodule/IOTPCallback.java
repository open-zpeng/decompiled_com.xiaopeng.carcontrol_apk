package com.xiaopeng.lib.framework.moduleinterface.syncmodule;
/* loaded from: classes2.dex */
public interface IOTPCallback {
    void onError(String seq, Integer code, String errMsg);

    void onGetOTP(String seq, String deviceID, String otp, long uid);
}
