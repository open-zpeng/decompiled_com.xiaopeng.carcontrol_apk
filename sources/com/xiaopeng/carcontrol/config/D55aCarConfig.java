package com.xiaopeng.carcontrol.config;
/* loaded from: classes2.dex */
public class D55aCarConfig extends D55CarConfig {
    private static final int CFC_CODE_HIGH = 4;
    private static final int CFC_CODE_INVALID = 0;
    private static final int CFC_CODE_LOW = 2;
    private static final int CFC_CODE_MIDDLE = 3;
    private static final int CFC_CODE_TRIP = 1;
    private static final int CFC_LEV_1 = 1;
    private static final int CFC_LEV_2 = 2;
    private static final int CFC_LEV_3 = 3;
    private static final int CFC_LEV_4 = 4;
    private static final int CFC_LEV_7 = 7;
    public static final int PROPERTY_BODY_COLOR_BLACK = 3;
    public static final int PROPERTY_BODY_COLOR_BLUE = 7;
    public static final int PROPERTY_BODY_COLOR_CHAMPAGNE = 11;
    public static final int PROPERTY_BODY_COLOR_COFFEE = 6;
    public static final int PROPERTY_BODY_COLOR_GRAY = 4;
    public static final int PROPERTY_BODY_COLOR_GREEN = 10;
    public static final int PROPERTY_BODY_COLOR_INVALID = 0;
    public static final int PROPERTY_BODY_COLOR_PURPLE = 8;
    public static final int PROPERTY_BODY_COLOR_RED_BLACK = 1;
    public static final int PROPERTY_BODY_COLOR_SILVER = 5;
    public static final int PROPERTY_BODY_COLOR_WHITE = 2;
    public static final int PROPERTY_BODY_COLOR_WHITE_BLACK = 12;
    private static final String PROPERTY_CFC = "persist.sys.xiaopeng.cfcVehicleLevel";
    public static final int onPROPERTY_BODY_COLOR_YELLOW = 9;

    @Override // com.xiaopeng.carcontrol.config.D55CarConfig, com.xiaopeng.carcontrol.config.CarBaseConfig
    public boolean isSupportVipSeat() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public D55aCarConfig() {
        sIsD2xSeries = false;
    }

    @Override // com.xiaopeng.carcontrol.config.D55CarConfig, com.xiaopeng.carcontrol.config.CarBaseConfig
    public boolean isWiperSensitiveNegative() {
        return !hasFeature(CarBaseConfig.PROPERTY_RLS);
    }

    @Override // com.xiaopeng.carcontrol.config.D55CarConfig, com.xiaopeng.carcontrol.config.CarBaseConfig
    public boolean isSupportDrvSeatVent() {
        return hasFeature(CarBaseConfig.PROPERTY_DRV_SEAT_VENT);
    }

    @Override // com.xiaopeng.carcontrol.config.D55CarConfig, com.xiaopeng.carcontrol.config.CarBaseConfig
    public boolean isSupportDrvSeatHeat() {
        return hasFeature(CarBaseConfig.PROPERTY_DRV_SEAT_HEAT);
    }

    @Override // com.xiaopeng.carcontrol.config.D55CarConfig, com.xiaopeng.carcontrol.config.CarBaseConfig
    public boolean isSupportPsnSeatVent() {
        return hasFeature(CarBaseConfig.PROPERTY_PSN_SEAT_VENT);
    }

    @Override // com.xiaopeng.carcontrol.config.D55CarConfig, com.xiaopeng.carcontrol.config.CarBaseConfig
    public boolean isSupportPsnSeatHeat() {
        return hasFeature(CarBaseConfig.PROPERTY_PSN_SEAT_HEAT);
    }

    @Override // com.xiaopeng.carcontrol.config.CarBaseConfig
    public boolean isSupportSaberLightMode() {
        return getCfcCode() >= 3 && getCfcCode() != 7;
    }
}
