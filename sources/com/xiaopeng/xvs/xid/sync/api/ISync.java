package com.xiaopeng.xvs.xid.sync.api;

import android.net.Uri;
import java.util.List;
/* loaded from: classes2.dex */
public interface ISync {
    public static final String ACTION_SERVICE_REQ_SYNC_GROUP_CREATE = "com.xiaopeng.xvs.sync.ACTION_SERVICE_REQ_SYNC_GROUP_CREATE";
    public static final String ACTION_SERVICE_REQ_SYNC_GROUP_DELETE = "com.xiaopeng.xvs.sync.ACTION_SERVICE_REQ_SYNC_GROUP_DELETE";
    public static final String ACTION_SERVICE_REQ_SYNC_GROUP_RENAME = "com.xiaopeng.xvs.sync.ACTION_SERVICE_REQ_SYNC_GROUP_RENAME";
    public static final String ACTION_SERVICE_REQ_SYNC_GROUP_SELECT = "com.xiaopeng.xvs.sync.ACTION_SERVICE_REQ_SYNC_GROUP_SELECT";
    public static final String ACTION_SERVICE_REQ_TRAFFIC_BUY = "com.xiaopeng.xvs.sync.ACTION_SERVICE_REQ_TRAFFIC_BUY";
    public static final String EXTRA_NAME_APP_ID = "extra_name_app_id";
    public static final String EXTRA_NAME_GROUP_INDEX = "extra_name_group_index";
    public static final String EXTRA_NAME_HABIT = "extra_rename_habit";
    public static final String EXTRA_NAME_KEY = "extra_name_key";
    public static final String EXTRA_NAME_VALUE = "extra_name_value";
    public static final String EXTRA_RENAME_STRING = "extra_rename_string";
    public static final String EXTRA_SYNC_GROUP_KEY_SEPARATOR = "@";
    public static final String EXTRA_SYNC_GROUP_KEY_SUFFIX = "group";
    public static final String KEY_VEHICLE_USER_INFO = "key_vehicle_user_info";
    public static final String PROVIDER_ACCOUNT_SYNC_AUTHORITY = "com.xiaopeng.caraccount.sync.AUTHORITY_TYPE_XP_PROVIDER";
    public static final String SYNC_CALL_CASHIER_KEY_GET = "get_cashier";
    public static final String SYNC_CALL_METHOD_GET = "get";
    public static final String SYNC_CALL_METHOD_GET_CASHIER_HABIT = "sync_group";
    public static final String SYNC_CALL_METHOD_GET_CASHIER_HABIT_SUM = "sync_habit_sum";
    public static final String SYNC_CALL_METHOD_GET_INIT_COMPLETE = "get_init_complete";
    public static final String SYNC_CALL_METHOD_PUT = "put";
    public static final String SYNC_CALL_METHOD_PUT_INDEX = "put_index";
    public static final Uri SYNC_CONTENT_URI = Uri.parse("content://com.xiaopeng.caraccount.sync.AUTHORITY_TYPE_XP_PROVIDER/");

    IAiAssistantSync getAiAssistantSync();

    ICarCameraSync getCarCameraSync();

    ICarControlSync getCarControlSync();

    ICarDemoSync getCarDemo();

    ICarDvrSync getCarDvrSync();

    ICarSettingsSync getCarSettingsSync();

    int getCurrentHabitSum();

    List<SyncGroup> getHabitList();

    String getKey(String str, String str2);

    String getVehicleUserBindTime();

    String getVehicleUserInfo();

    void init();

    void putKey(String str, String str2);

    void removeSyncChangedListener();

    void requestCreateNewSyncGroup();

    void requestDeleteSyncGroup(SyncGroup syncGroup);

    void requestRenameSyncGroup(SyncGroup syncGroup, String str);

    void requestSelectSyncGroupIndex(int i);

    void requestTrafficBuy();

    void setSyncChangedListener(OnSyncChangedListener onSyncChangedListener, boolean z);
}
