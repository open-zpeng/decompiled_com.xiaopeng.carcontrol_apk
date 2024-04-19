package com.xiaopeng.xvs.xid.sync;

import android.content.ComponentName;
import android.content.Intent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaopeng.xvs.xid.XId;
import com.xiaopeng.xvs.xid.base.IClient;
import com.xiaopeng.xvs.xid.sync.api.IAiAssistantSync;
import com.xiaopeng.xvs.xid.sync.api.ICarCameraSync;
import com.xiaopeng.xvs.xid.sync.api.ICarControlSync;
import com.xiaopeng.xvs.xid.sync.api.ICarDemoSync;
import com.xiaopeng.xvs.xid.sync.api.ICarDvrSync;
import com.xiaopeng.xvs.xid.sync.api.ICarSettingsSync;
import com.xiaopeng.xvs.xid.sync.api.ISync;
import com.xiaopeng.xvs.xid.sync.api.OnSyncChangedListener;
import com.xiaopeng.xvs.xid.sync.api.SyncException;
import com.xiaopeng.xvs.xid.sync.api.SyncGroup;
import com.xiaopeng.xvs.xid.utils.L;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class SyncProviderImpl extends IClient implements ISync {
    private static final String TAG = "SyncProviderImpl";
    private static volatile SyncProviderImpl mInstance;
    private final IAiAssistantSync mAiAssistantSync;
    private final ICarCameraSync mCarCameraSync;
    private final ICarControlSync mCarControlSync;
    private final ICarDemoSync mCarDemoSync;
    private final ICarDvrSync mCarDvrSync;
    private final ICarSettingsSync mCarSettingsSync;
    private final SyncProviderWrapper mContextWrapper;
    private final String pkg = "com.xiaopeng.caraccount";
    private final String cls = "com.xiaopeng.caraccount.AccountService";

    private SyncProviderImpl() {
        L.d(TAG, "SyncProviderImpl onCreate true");
        SyncProviderWrapper syncProviderWrapper = new SyncProviderWrapper(XId.getApplication());
        this.mContextWrapper = syncProviderWrapper;
        this.mCarControlSync = new CarControlSyncImpl(syncProviderWrapper);
        this.mAiAssistantSync = new AiAssistantSyncImpl(syncProviderWrapper);
        this.mCarCameraSync = new CarCameraSyncImpl(syncProviderWrapper);
        this.mCarDvrSync = new CarDvrSyncImpl(syncProviderWrapper);
        this.mCarSettingsSync = new CarSettingsSyncImpl(syncProviderWrapper);
        this.mCarDemoSync = new CarDemoSyncImpl(syncProviderWrapper);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SyncProviderImpl getInstance() {
        if (mInstance == null) {
            synchronized (SyncProviderImpl.class) {
                if (mInstance == null) {
                    mInstance = new SyncProviderImpl();
                }
            }
        }
        return mInstance;
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public void init() {
        this.mContextWrapper.init();
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public void setSyncChangedListener(OnSyncChangedListener onSyncChangedListener, boolean z) {
        this.mContextWrapper.setSyncChangedListener(onSyncChangedListener, z);
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public void removeSyncChangedListener() {
        this.mContextWrapper.removeSyncChangedListener();
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public ICarControlSync getCarControlSync() {
        if (!"xp_car_setting_car".equals(XId.getAppId())) {
            throw new IllegalArgumentException(new SyncException(SyncException.ERROR_CODE_SYNC_APP_ID_ERROR).getMessage());
        }
        return this.mCarControlSync;
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public ICarSettingsSync getCarSettingsSync() {
        if (!XId.APP_ID_CAR_SETTINGS.equals(XId.getAppId()) && !XId.APP_ID_CAR_SETTINGS_D21.equals(XId.getAppId())) {
            throw new IllegalArgumentException(new SyncException(SyncException.ERROR_CODE_SYNC_APP_ID_ERROR).getMessage());
        }
        return this.mCarSettingsSync;
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public IAiAssistantSync getAiAssistantSync() {
        if (!XId.APP_ID_AI_ASSISTANT.equals(XId.getAppId())) {
            throw new IllegalArgumentException(new SyncException(SyncException.ERROR_CODE_SYNC_APP_ID_ERROR).getMessage());
        }
        return this.mAiAssistantSync;
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public ICarCameraSync getCarCameraSync() {
        if (!XId.APP_ID_CAR_CAMERA.equals(XId.getAppId())) {
            throw new IllegalArgumentException(new SyncException(SyncException.ERROR_CODE_SYNC_APP_ID_ERROR).getMessage());
        }
        return this.mCarCameraSync;
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public ICarDvrSync getCarDvrSync() {
        if (!XId.APP_ID_CAR_DVR.equals(XId.getAppId())) {
            throw new IllegalArgumentException(new SyncException(SyncException.ERROR_CODE_SYNC_APP_ID_ERROR).getMessage());
        }
        return this.mCarDvrSync;
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public ICarDemoSync getCarDemo() {
        if (!XId.APP_ID_CAR_DEMO.equals(XId.getAppId())) {
            throw new IllegalArgumentException(new SyncException(SyncException.ERROR_CODE_SYNC_APP_ID_ERROR).getMessage());
        }
        return this.mCarDemoSync;
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public void requestCreateNewSyncGroup() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.xiaopeng.caraccount", "com.xiaopeng.caraccount.AccountService"));
        intent.setAction(ISync.ACTION_SERVICE_REQ_SYNC_GROUP_CREATE);
        XId.getApplication().startService(intent);
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public void requestSelectSyncGroupIndex(int i) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.xiaopeng.caraccount", "com.xiaopeng.caraccount.AccountService"));
        intent.setAction(ISync.ACTION_SERVICE_REQ_SYNC_GROUP_SELECT);
        intent.putExtra(ISync.EXTRA_NAME_GROUP_INDEX, i);
        XId.getApplication().startService(intent);
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public int getCurrentHabitSum() {
        return Integer.parseInt(this.mContextWrapper.getCashier(ISync.SYNC_CALL_METHOD_GET_CASHIER_HABIT_SUM, ISync.SYNC_CALL_CASHIER_KEY_GET, "0"));
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public List<SyncGroup> getHabitList() {
        return (List) new Gson().fromJson(this.mContextWrapper.getCashier(ISync.SYNC_CALL_METHOD_GET_CASHIER_HABIT, ISync.SYNC_CALL_CASHIER_KEY_GET, ""), new TypeToken<List<SyncGroup>>() { // from class: com.xiaopeng.xvs.xid.sync.SyncProviderImpl.1
        }.getType());
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public String getKey(String str, String str2) {
        return this.mContextWrapper.get(str, str2);
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public void putKey(String str, String str2) {
        this.mContextWrapper.put(str, str2);
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public void requestRenameSyncGroup(SyncGroup syncGroup, String str) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.xiaopeng.caraccount", "com.xiaopeng.caraccount.AccountService"));
        intent.setAction(ISync.ACTION_SERVICE_REQ_SYNC_GROUP_RENAME);
        intent.putExtra(ISync.EXTRA_NAME_HABIT, new Gson().toJson(syncGroup));
        intent.putExtra(ISync.EXTRA_RENAME_STRING, str);
        XId.getApplication().startService(intent);
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public void requestDeleteSyncGroup(SyncGroup syncGroup) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.xiaopeng.caraccount", "com.xiaopeng.caraccount.AccountService"));
        intent.setAction(ISync.ACTION_SERVICE_REQ_SYNC_GROUP_DELETE);
        intent.putExtra(ISync.EXTRA_NAME_HABIT, new Gson().toJson(syncGroup));
        XId.getApplication().startService(intent);
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public String getVehicleUserInfo() {
        return this.mContextWrapper.getCashier(ISync.KEY_VEHICLE_USER_INFO, ISync.SYNC_CALL_CASHIER_KEY_GET, "");
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public String getVehicleUserBindTime() {
        String cashier = this.mContextWrapper.getCashier(ISync.KEY_VEHICLE_USER_INFO, ISync.SYNC_CALL_CASHIER_KEY_GET, "");
        if (cashier == null || "".equals(cashier)) {
            return null;
        }
        try {
            return new JSONObject(cashier).optString("createTime", null);
        } catch (JSONException e) {
            L.d(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.xiaopeng.xvs.xid.sync.api.ISync
    public void requestTrafficBuy() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.xiaopeng.caraccount", "com.xiaopeng.caraccount.AccountService"));
        intent.setAction(ISync.ACTION_SERVICE_REQ_TRAFFIC_BUY);
        XId.getApplication().startService(intent);
    }
}
