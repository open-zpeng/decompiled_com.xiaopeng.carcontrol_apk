package com.xiaopeng.lib.framework.moduleinterface.appresourcemodule;

import android.content.res.AssetFileDescriptor;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.speech.protocol.bean.recommend.RecommendBean;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class AppResourceResponse {
    private long mCode;
    private String mData;
    private Object mExtra;
    private String mMsg;
    private AppResourceRequest mRequest;
    private String mResult;

    public AppResourceResponse request(AppResourceRequest request) {
        this.mRequest = request;
        return this;
    }

    public AppResourceResponse result(String result) {
        this.mResult = result;
        return this;
    }

    public AppResourceResponse data(String data) {
        this.mData = data;
        return this;
    }

    public AppResourceResponse code(long code) {
        this.mCode = code;
        return this;
    }

    public AppResourceResponse msg(String msg) {
        this.mMsg = msg;
        return this;
    }

    public String msg() {
        return this.mMsg;
    }

    public Object extra() {
        return this.mExtra;
    }

    public AppResourceResponse extra(Object mExtra) {
        this.mExtra = mExtra;
        return this;
    }

    public long code() {
        return this.mCode;
    }

    public AppResourceRequest request() {
        return this.mRequest;
    }

    public String result() {
        return this.mResult;
    }

    public String data() {
        return this.mData;
    }

    public AssetFileDescriptor getFileDescriptor() {
        Object obj = this.mExtra;
        if (obj instanceof AssetFileDescriptor) {
            return (AssetFileDescriptor) obj;
        }
        return null;
    }

    public String toString() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(Progress.REQUEST, this.mRequest);
            jSONObject.put(RecommendBean.SHOW_TIME_RESULT, this.mResult);
            jSONObject.put("data", this.mData);
            jSONObject.put("code", this.mCode);
            jSONObject.put(NotificationCompat.CATEGORY_MESSAGE, this.mMsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static AppResourceResponse from(String src) {
        try {
            if (TextUtils.isEmpty(src)) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(src);
            AppResourceResponse appResourceResponse = new AppResourceResponse();
            appResourceResponse.mRequest = AppResourceRequest.from(jSONObject.optString(Progress.REQUEST));
            appResourceResponse.mResult = jSONObject.optString(RecommendBean.SHOW_TIME_RESULT);
            appResourceResponse.mData = jSONObject.optString("data");
            appResourceResponse.mCode = jSONObject.optLong("code");
            appResourceResponse.mMsg = jSONObject.optString(NotificationCompat.CATEGORY_MESSAGE);
            return appResourceResponse;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
