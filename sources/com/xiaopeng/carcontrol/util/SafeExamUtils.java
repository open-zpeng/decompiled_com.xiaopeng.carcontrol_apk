package com.xiaopeng.carcontrol.util;

import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import com.xiaopeng.carcontrol.config.CarBaseConfig;
import com.xiaopeng.carcontrol.config.feature.BaseFeatureOption;
import com.xiaopeng.carcontrol.viewmodel.account.bean.TaskInfoRequest;
import com.xiaopeng.carcontrol.viewmodel.account.bean.TaskListRequest;
import com.xiaopeng.carcontrol.viewmodel.account.bean.TaskResultRequest;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IRequest;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.lib.framework.netchannelmodule.http.xmart.ServerBean;
import com.xiaopeng.lib.framework.netchannelmodule.http.xmart.bizapi.BizConstants;
import com.xiaopeng.lib.utils.config.CommonConfig;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class SafeExamUtils {
    private static final String CAR_TYPE_KEY = "carType";
    private static final String CAR_TYPE_PARAM_D22 = "G3i";
    private static final String CAR_TYPE_PARAM_D55 = "P5";
    private static final String CAR_TYPE_PARAM_E28 = "P7";
    private static final String CAR_TYPE_PARAM_E28A = "E28A";
    private static final String CAR_TYPE_PARAM_E38 = "G9";
    private static final String CAR_TYPE_PARAM_E38A = "E38A";
    private static final String CAR_TYPE_PARAM_F30 = "G7";
    private static final String CAR_TYPE_PARAM_H93 = "H93";
    private static final int CONNECT_TIME_OUT = 5000;
    private static final String EXAM_APA_QR_STUDY_URL = "/usertask/help5.html";
    private static final String EXAM_CNGP_QR_STUDY_URL = "/usertask/help6.html";
    private static final String EXAM_LCC_L_QR_STUDY_URL = "/usertask/help8.html";
    private static final String EXAM_LCC_QR_STUDY_URL = "/usertask/help2.html";
    private static final String EXAM_MEM_PARK_QR_STUDY_URL = "/usertask/help3.html";
    private static final String EXAM_NGP_QR_STUDY_URL = "/usertask/help.html";
    private static final String EXAM_QR_INFO_URL = "/task/in/getTaskInfoById";
    private static final String EXAM_QR_STATE_URL = "/task/in/getTaskStateById";
    private static final String EXAM_QR_STUDY_DEPLOY_HOST = "https://ur.deploy-test.xiaopeng.com";
    private static final String EXAM_QR_STUDY_HOST = "https://ur.xiaopeng.com";
    private static final String EXAM_QR_STUDY_TEST_HOST = "http://10.0.13.28:8902";
    private static final String EXAM_SUPER_VPA_QR_STUDY_URL = "/usertask/help7.html";
    private static final String EXAM_TASK_LIST_STATE_URL = "/task/in/getTaskStateList";
    private static final String EXAM_TASK_STATE_URL = "/task/in/postTestResult";
    private static final String EXAM_XNGP_QR_STUDY_URL = "/usertask/help9.html";
    private static final String TAG = "X_EXAM_SafeExamUtils";
    public static final String X_EXAM_LOG_PREFIX = "X_EXAM_";

    private static String getStudyHost() {
        return CommonConfig.HTTP_HOST.startsWith("https://xmart.xiaopeng.com") ? EXAM_QR_STUDY_HOST : CommonConfig.HTTP_HOST.startsWith("https://xmart.deploy-test.xiaopeng.com") ? EXAM_QR_STUDY_DEPLOY_HOST : EXAM_QR_STUDY_TEST_HOST;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0081, code lost:
        if (r0.equals(com.xiaopeng.speech.vui.utils.VuiUtils.CAR_PLATFORM_Q1) == false) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String getCarTypeForUrl() {
        /*
            java.lang.String r0 = android.car.Car.getXpCduType()
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            r2 = 0
            r3 = 0
            if (r1 == 0) goto L14
            java.lang.String r0 = "X_EXAM_SafeExamUtils"
            java.lang.String r1 = "get hardware car type is null"
            com.xiaopeng.carcontrol.util.LogUtils.w(r0, r1, r3)
            return r2
        L14:
            r0.hashCode()
            r1 = -1
            int r4 = r0.hashCode()
            switch(r4) {
                case 2560: goto L7b;
                case 2562: goto L70;
                case 2565: goto L65;
                case 2566: goto L5a;
                case 2567: goto L4f;
                case 2568: goto L44;
                case 2577: goto L39;
                case 79487: goto L2e;
                case 79611: goto L22;
                default: goto L1f;
            }
        L1f:
            r3 = r1
            goto L84
        L22:
            java.lang.String r3 = "Q7A"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L2b
            goto L1f
        L2b:
            r3 = 8
            goto L84
        L2e:
            java.lang.String r3 = "Q3A"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L37
            goto L1f
        L37:
            r3 = 7
            goto L84
        L39:
            java.lang.String r3 = "QB"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L42
            goto L1f
        L42:
            r3 = 6
            goto L84
        L44:
            java.lang.String r3 = "Q9"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L4d
            goto L1f
        L4d:
            r3 = 5
            goto L84
        L4f:
            java.lang.String r3 = "Q8"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L58
            goto L1f
        L58:
            r3 = 4
            goto L84
        L5a:
            java.lang.String r3 = "Q7"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L63
            goto L1f
        L63:
            r3 = 3
            goto L84
        L65:
            java.lang.String r3 = "Q6"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L6e
            goto L1f
        L6e:
            r3 = 2
            goto L84
        L70:
            java.lang.String r3 = "Q3"
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L79
            goto L1f
        L79:
            r3 = 1
            goto L84
        L7b:
            java.lang.String r4 = "Q1"
            boolean r0 = r0.equals(r4)
            if (r0 != 0) goto L84
            goto L1f
        L84:
            switch(r3) {
                case 0: goto L9d;
                case 1: goto L9a;
                case 2: goto L97;
                case 3: goto L94;
                case 4: goto L91;
                case 5: goto L8e;
                case 6: goto L8b;
                case 7: goto L9a;
                case 8: goto L88;
                default: goto L87;
            }
        L87:
            return r2
        L88:
            java.lang.String r0 = "E38A"
            return r0
        L8b:
            java.lang.String r0 = "H93"
            return r0
        L8e:
            java.lang.String r0 = "G7"
            return r0
        L91:
            java.lang.String r0 = "E28A"
            return r0
        L94:
            java.lang.String r0 = "G9"
            return r0
        L97:
            java.lang.String r0 = "G3i"
            return r0
        L9a:
            java.lang.String r0 = "P5"
            return r0
        L9d:
            java.lang.String r0 = "P7"
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.carcontrol.util.SafeExamUtils.getCarTypeForUrl():java.lang.String");
    }

    public static String getExamTaskStateUrl() {
        return CommonConfig.HTTP_HOST + EXAM_QR_STATE_URL;
    }

    public static String getTaskStateUrl() {
        return CommonConfig.HTTP_HOST + EXAM_TASK_STATE_URL;
    }

    public static String getTaskListStateUrl() {
        return CommonConfig.HTTP_HOST + EXAM_TASK_LIST_STATE_URL;
    }

    public static String getNgpQrStudyUrl() {
        return wrapCarTypeParam(getStudyHost() + EXAM_NGP_QR_STUDY_URL);
    }

    public static String getLccQrStudyUrl() {
        return wrapCarTypeParam(getStudyHost() + ((CarBaseConfig.getInstance().isSupportLidar() && BaseFeatureOption.getInstance().isSupportLidarSafeExam()) ? EXAM_LCC_L_QR_STUDY_URL : EXAM_LCC_QR_STUDY_URL));
    }

    public static String getTaskInfoHost() {
        return CommonConfig.HTTP_HOST + EXAM_QR_INFO_URL;
    }

    public static String getMemParkQrStudyUrl() {
        return wrapCarTypeParam(getStudyHost() + ((CarBaseConfig.getInstance().isSupportLidar() && BaseFeatureOption.getInstance().isSupportLidarSafeExam()) ? EXAM_SUPER_VPA_QR_STUDY_URL : EXAM_MEM_PARK_QR_STUDY_URL));
    }

    public static String getAutoParkQrStudyUrl() {
        return wrapCarTypeParam(getStudyHost() + EXAM_APA_QR_STUDY_URL);
    }

    public static String getCNGPQrStudyUrl() {
        return wrapCarTypeParam(getStudyHost() + EXAM_CNGP_QR_STUDY_URL);
    }

    public static String getXNGPQrStudyUrl() {
        return wrapCarTypeParam(getStudyHost() + EXAM_XNGP_QR_STUDY_URL);
    }

    public static IRequest buildPostResultRequest(String uid, String otp, long taskId, int result) {
        TaskResultRequest taskResultRequest = new TaskResultRequest();
        taskResultRequest.setTaskId(taskId);
        taskResultRequest.setState(result);
        String json = new Gson().toJson(taskResultRequest);
        HashMap hashMap = new HashMap();
        hashMap.put("otp", otp);
        IHttp http = ModuleUtils.getHttp();
        if (http == null) {
            LogUtils.e(TAG, "Http module is null", false);
            return null;
        }
        String taskStateUrl = getTaskStateUrl();
        LogUtils.i(TAG, "Build post result request:\nRequest url: " + taskStateUrl + "postContent : " + json, false);
        http.config().connectTimeout(5000);
        String appSecret = EnvironmentUtils.getAppSecret();
        IRequest headers = http.bizHelper().post(taskStateUrl, json).appId("xp_car_setting_car").uid(String.valueOf(uid)).needAuthorizationInfo(hashMap).buildWithSecretKey(appSecret).headers(BizConstants.HEADER_ENCRYPTION_TYPE, String.valueOf(3));
        headers.headers(BizConstants.HEADER_AUTHORIZATION, NoneSecurityUtils.getAuthorization(headers, "POST", json, appSecret, otp));
        headers.headers(BizConstants.HEADER_SIGNATURE, NoneSecurityUtils.getSignature(headers, "POST", json, appSecret));
        return headers;
    }

    public static IRequest buildFetchResultRequest(String uid, String otp, long taskId) {
        TaskInfoRequest taskInfoRequest = new TaskInfoRequest();
        taskInfoRequest.setTaskId(taskId);
        String json = new Gson().toJson(taskInfoRequest);
        HashMap hashMap = new HashMap();
        hashMap.put("otp", otp);
        IHttp http = ModuleUtils.getHttp();
        if (http == null) {
            LogUtils.e(TAG, "Http module is null", false);
            return null;
        }
        String examTaskStateUrl = getExamTaskStateUrl();
        LogUtils.i(TAG, "Build fetch result request: Request url: " + examTaskStateUrl + "postContent : " + json, false);
        http.config().connectTimeout(5000);
        String appSecret = EnvironmentUtils.getAppSecret();
        IRequest headers = http.bizHelper().post(examTaskStateUrl, json).appId("xp_car_setting_car").uid(String.valueOf(uid)).needAuthorizationInfo(hashMap).buildWithSecretKey(appSecret).headers(BizConstants.HEADER_ENCRYPTION_TYPE, String.valueOf(3));
        headers.headers(BizConstants.HEADER_AUTHORIZATION, NoneSecurityUtils.getAuthorization(headers, "POST", json, appSecret, otp));
        headers.headers(BizConstants.HEADER_SIGNATURE, NoneSecurityUtils.getSignature(headers, "POST", json, appSecret));
        return headers;
    }

    public static IRequest buildFetchGroupResultRequest(String uid, String otp, List<Long> taskIds) {
        TaskListRequest taskListRequest = new TaskListRequest();
        taskListRequest.setTaskIds(taskIds);
        String json = new Gson().toJson(taskListRequest);
        HashMap hashMap = new HashMap();
        hashMap.put("otp", otp);
        IHttp http = ModuleUtils.getHttp();
        if (http == null) {
            LogUtils.e(TAG, "Http module is null", false);
            return null;
        }
        String taskListStateUrl = getTaskListStateUrl();
        LogUtils.i(TAG, "Build fetch group result request:\nRequest url: " + taskListStateUrl + "postContent : " + json, false);
        http.config().connectTimeout(5000);
        String appSecret = EnvironmentUtils.getAppSecret();
        IRequest headers = http.bizHelper().post(taskListStateUrl, json).appId("xp_car_setting_car").uid(String.valueOf(uid)).needAuthorizationInfo(hashMap).buildWithSecretKey(appSecret).headers(BizConstants.HEADER_ENCRYPTION_TYPE, String.valueOf(3));
        headers.headers(BizConstants.HEADER_AUTHORIZATION, NoneSecurityUtils.getAuthorization(headers, "POST", json, appSecret, otp));
        headers.headers(BizConstants.HEADER_SIGNATURE, NoneSecurityUtils.getSignature(headers, "POST", json, appSecret));
        return headers;
    }

    public static IRequest buildFetchExamRequest(String uid, String otp, long taskId) {
        TaskInfoRequest taskInfoRequest = new TaskInfoRequest();
        taskInfoRequest.setTaskId(taskId);
        String json = new Gson().toJson(taskInfoRequest);
        HashMap hashMap = new HashMap();
        hashMap.put("otp", otp);
        IHttp http = ModuleUtils.getHttp();
        if (http == null) {
            LogUtils.e(TAG, "Http module is null", false);
            return null;
        }
        String taskInfoHost = getTaskInfoHost();
        LogUtils.i(TAG, "Build fetch exam request:\nRequest url: " + taskInfoHost + "postContent : " + json, false);
        http.config().connectTimeout(5000);
        String appSecret = EnvironmentUtils.getAppSecret();
        IRequest headers = http.bizHelper().post(taskInfoHost, json).appId("xp_car_setting_car").uid(String.valueOf(uid)).needAuthorizationInfo(hashMap).buildWithSecretKey(appSecret).headers(BizConstants.HEADER_ENCRYPTION_TYPE, String.valueOf(3));
        headers.headers(BizConstants.HEADER_AUTHORIZATION, NoneSecurityUtils.getAuthorization(headers, "POST", json, appSecret, otp));
        headers.headers(BizConstants.HEADER_SIGNATURE, NoneSecurityUtils.getSignature(headers, "POST", json, appSecret));
        return headers;
    }

    public static String wrapCarTypeParam(String url) {
        if (url == null) {
            LogUtils.w(TAG, "wrap car type, url is null");
            return null;
        }
        String carTypeForUrl = getCarTypeForUrl();
        if (TextUtils.isEmpty(carTypeForUrl)) {
            LogUtils.w(TAG, "car type is null, just return origin url");
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.contains("?")) {
            sb.append("&").append(CAR_TYPE_KEY).append("=").append(carTypeForUrl);
        } else {
            sb.append("?").append(CAR_TYPE_KEY).append("=").append(carTypeForUrl);
        }
        String sb2 = sb.toString();
        LogUtils.i(TAG, "Wrap url:" + sb2);
        return sb2;
    }

    public static ServerBean parseResponse(IResponse response) {
        ServerBean serverBean = new ServerBean();
        if (response == null) {
            return serverBean;
        }
        try {
            JSONObject jSONObject = new JSONObject(response.body());
            serverBean.code(jSONObject.getInt("code"));
            serverBean.data(jSONObject.getString("data"));
            serverBean.message(jSONObject.getString(NotificationCompat.CATEGORY_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverBean;
    }
}
