package com.xiaopeng.carcontrol.direct.action;

import android.content.Context;
import android.net.Uri;
import com.xiaopeng.carcontrol.GlobalConstant;
import com.xiaopeng.carcontrol.view.dialog.panel.ControlPanelManager;
/* loaded from: classes2.dex */
public class NgpSettingPageAction implements PageAction {
    @Override // com.xiaopeng.carcontrol.direct.action.PageAction
    public void doAction(Context context, Uri uri) {
        ControlPanelManager.getInstance().show(GlobalConstant.ACTION.ACTION_SHOW_NGP_SETTING_PANEL, 2008, uri, false);
    }
}
