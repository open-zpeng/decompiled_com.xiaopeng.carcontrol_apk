package com.xiaopeng.carcontrol.helper;

import com.xiaopeng.carcontrol.config.CarBaseConfig;
import com.xiaopeng.carcontrolmodule.R;
/* loaded from: classes2.dex */
public class ResHelper {
    public static int getHvacBg() {
        if (CarBaseConfig.getInstance().isSupportSHC()) {
            return R.drawable.bg_pressed;
        }
        return R.drawable.bg_pressed_no_shc;
    }
}
