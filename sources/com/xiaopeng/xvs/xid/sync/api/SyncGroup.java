package com.xiaopeng.xvs.xid.sync.api;

import com.xiaopeng.lib.utils.DateUtils;
import com.xiaopeng.xvs.xid.base.ISerializable;
import java.util.Objects;
/* loaded from: classes2.dex */
public class SyncGroup implements ISerializable, Comparable<SyncGroup> {
    private String mGroupId;
    private String mGroupName;
    private boolean mIsDefault;
    private boolean mIsSelect;
    private int mStatus;
    private long mUpdateTimestamp;

    public SyncGroup() {
        this.mGroupName = "";
        this.mGroupId = "";
        this.mIsSelect = false;
        this.mIsDefault = false;
        this.mStatus = 0;
        this.mUpdateTimestamp = 0L;
    }

    public SyncGroup(String str, String str2, long j) {
        this.mGroupName = "";
        this.mGroupId = "";
        this.mIsSelect = false;
        this.mIsDefault = false;
        this.mStatus = 0;
        this.mUpdateTimestamp = 0L;
        this.mGroupName = str;
        this.mGroupId = str2;
        this.mUpdateTimestamp = j;
    }

    public String getGroupName() {
        return this.mGroupName;
    }

    public void setGroupName(String str) {
        this.mGroupName = str;
    }

    public String getGroupId() {
        return this.mGroupId;
    }

    public void setGroupId(String str) {
        this.mGroupId = str;
    }

    public boolean isSelect() {
        return this.mIsSelect;
    }

    public void setSelect(boolean z) {
        this.mIsSelect = z;
    }

    public boolean isDefault() {
        return this.mIsDefault;
    }

    public void setDefault(boolean z) {
        this.mIsDefault = z;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setStatus(int i) {
        this.mStatus = i;
    }

    public long getUpdateTimestamp() {
        return this.mUpdateTimestamp;
    }

    public void setUpdateTimestamp(long j) {
        this.mUpdateTimestamp = j;
    }

    @Override // java.lang.Comparable
    public int compareTo(SyncGroup syncGroup) {
        if (syncGroup.isDefault()) {
            return 1;
        }
        return Long.compare(getUpdateTimestamp(), syncGroup.getUpdateTimestamp());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SyncGroup syncGroup = (SyncGroup) obj;
        return this.mIsSelect == syncGroup.mIsSelect && this.mIsDefault == syncGroup.mIsDefault && this.mStatus == syncGroup.mStatus && this.mUpdateTimestamp == syncGroup.mUpdateTimestamp && Objects.equals(this.mGroupName, syncGroup.mGroupName) && Objects.equals(this.mGroupId, syncGroup.mGroupId);
    }

    public int hashCode() {
        return Objects.hash(this.mGroupName, this.mGroupId, Boolean.valueOf(this.mIsSelect), Boolean.valueOf(this.mIsDefault), Integer.valueOf(this.mStatus), Long.valueOf(this.mUpdateTimestamp));
    }

    public String toString() {
        return "Habit{mGroupName='" + this.mGroupName + "', mGroupId='" + this.mGroupId + "', mIsSelect=" + this.mIsSelect + ", mIsDefault=" + this.mIsDefault + ", mStatus=" + this.mStatus + ", mUpdateTimestamp=" + DateUtils.formatDate8(this.mUpdateTimestamp) + '}';
    }
}
