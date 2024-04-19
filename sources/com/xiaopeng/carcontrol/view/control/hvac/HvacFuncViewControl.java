package com.xiaopeng.carcontrol.view.control.hvac;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import com.xiaopeng.carcontrol.config.CarBaseConfig;
import com.xiaopeng.carcontrol.config.feature.BaseFeatureOption;
import com.xiaopeng.carcontrol.statistic.BtnEnum;
import com.xiaopeng.carcontrol.statistic.PageEnum;
import com.xiaopeng.carcontrol.statistic.StatisticUtils;
import com.xiaopeng.carcontrol.util.LogUtils;
import com.xiaopeng.carcontrol.util.ResUtils;
import com.xiaopeng.carcontrol.util.UIUtils;
import com.xiaopeng.carcontrol.view.control.BaseViewControl;
import com.xiaopeng.carcontrol.view.dialog.panel.SeatHeatVentHelper;
import com.xiaopeng.carcontrol.view.speech.SeatAirVuiEventActor;
import com.xiaopeng.carcontrol.view.speech.SeatHotVuiEventActor;
import com.xiaopeng.carcontrol.view.speech.StatefulButtonConstructorFactory;
import com.xiaopeng.carcontrol.view.speech.ValuePickerFictitiousElementsConstructor;
import com.xiaopeng.carcontrol.view.widget.TempImmerseView;
import com.xiaopeng.carcontrol.view.widget.ValuePickerView;
import com.xiaopeng.carcontrol.viewmodel.ViewModelManager;
import com.xiaopeng.carcontrol.viewmodel.hvac.HvacViewModel;
import com.xiaopeng.carcontrol.viewmodel.hvac.IHvacViewModel;
import com.xiaopeng.carcontrol.viewmodel.hvac.SeatHeatLevel;
import com.xiaopeng.carcontrol.viewmodel.hvac.SeatVentLevel;
import com.xiaopeng.carcontrol.viewmodel.sfs.ISfsViewModel;
import com.xiaopeng.carcontrol.viewmodel.sfs.SfsViewModel;
import com.xiaopeng.carcontrolmodule.R;
import com.xiaopeng.speech.vui.VuiEngine;
import com.xiaopeng.speech.vui.actor.VuiEventHandler;
import com.xiaopeng.vui.commons.IVuiElement;
import com.xiaopeng.vui.commons.IVuiElementBuilder;
import com.xiaopeng.vui.commons.IVuiElementListener;
import com.xiaopeng.vui.commons.VuiElementType;
import com.xiaopeng.vui.commons.model.VuiElement;
import com.xiaopeng.vui.commons.model.VuiEvent;
import com.xiaopeng.xui.widget.XImageView;
import com.xiaopeng.xui.widget.XTextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/* loaded from: classes2.dex */
public class HvacFuncViewControl extends BaseViewControl {
    public static final int IMMERSE_TYPE_DRV_TEMP = 2;
    public static final int IMMERSE_TYPE_FAN_SPEED = 1;
    public static final int IMMERSE_TYPE_NORMAL = 0;
    public static final int IMMERSE_TYPE_PSN_TEMP = 3;
    private static final int MAX_TEMP_VALUE = 64;
    private static final int MIN_TEMP_VALUE = 36;
    private static final int PANEL_ANIMATION_DURATION = 200;
    private static final long PANEL_SHOW_TIME = 10000;
    private static final String TAG = "HvacFuncViewControl";
    private Runnable freeBreathPanelRun = new Runnable() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$KgDS96fH8wmEZT0T6bU11hblOg0
        @Override // java.lang.Runnable
        public final void run() {
            HvacFuncViewControl.this.closeFreeBreathPanel();
        }
    };
    private Handler mCloseFreeBreathPanelHandler;
    private TempImmerseView mDrvTempImmerseView;
    private ValuePickerView mDrvTempPickerView;
    private ValueAnimator mFreeBreathCloseAnim;
    private ValueAnimator mFreeBreathOpenAnim;
    private int mFreeBreathPanelHeight;
    private ConstraintLayout mFreeBreathPanelView;
    private ImageView mImgDrvTempBg;
    private XImageView mImgFlSeat;
    private XImageView mImgFlSeatSmall;
    private XImageView mImgFrSeat;
    private XImageView mImgFrSeatSmall;
    private ImageView mImgFreeBreath;
    protected ImageView mImgPsnSeatHeatSmall;
    private ImageView mImgPsnTempBg;
    private XImageView mImgRlSeatSmall;
    private XImageView mImgRrSeatSmall;
    protected ImageView mImgSeatHeatSmall;
    protected ImageView mImgSeatVentSmall;
    private ImageView mImgTempSyncBtn;
    private View mLayoutSeatHeatVent;
    private TempImmerseView mPsnTempImmerseView;
    private ValuePickerView mPsnTempPickerView;
    protected SfsViewModel mSfsViewModel;
    protected TextView mTvDeodorant;
    protected TextView mTvFragrance;
    protected XTextView mTvPsnSeatHeat;
    protected TextView mTvPurify;
    protected TextView mTvRapidCooling;
    protected XTextView mTvSeatHeat;
    private TextView mTvSeatHeatVent;
    protected XTextView mTvSeatVent;
    protected HvacViewModel mViewModel;

    /* JADX INFO: Access modifiers changed from: private */
    public static int inversionTempValue(int value) {
        return 100 - value;
    }

    @Override // com.xiaopeng.carcontrol.view.control.BaseViewControl
    public void onCreate(AppCompatActivity activity, View view) {
        super.onCreate(activity, view);
        if (BaseFeatureOption.getInstance().isSeatHeatVentGather()) {
            CarBaseConfig carBaseConfig = CarBaseConfig.getInstance();
            boolean isSupportDrvSeatVent = carBaseConfig.isSupportDrvSeatVent();
            boolean isSupportDrvSeatHeat = carBaseConfig.isSupportDrvSeatHeat();
            boolean isSupportPsnSeatHeat = carBaseConfig.isSupportPsnSeatHeat();
            boolean isSupportPsnSeatVent = carBaseConfig.isSupportPsnSeatVent();
            boolean isSupportRearSeatHeat = carBaseConfig.isSupportRearSeatHeat();
            if (isSupportDrvSeatVent || isSupportDrvSeatHeat || isSupportPsnSeatHeat || isSupportPsnSeatVent || isSupportRearSeatHeat) {
                View findViewById = this.mRootView.findViewById(R.id.layout_seat_heat_vent);
                this.mLayoutSeatHeatVent = findViewById;
                findViewById.setVisibility(0);
                TextView textView = (TextView) this.mRootView.findViewById(R.id.tv_seat_heat_vent);
                this.mTvSeatHeatVent = textView;
                textView.setVisibility(0);
                this.mImgFlSeat = (XImageView) this.mRootView.findViewById(R.id.img_fl_seat);
                this.mImgFlSeatSmall = (XImageView) this.mRootView.findViewById(R.id.img_fl_seat_small);
                XTextView xTextView = (XTextView) this.mRootView.findViewById(R.id.tv_front_seat);
                if (!isSupportDrvSeatVent || !isSupportDrvSeatHeat) {
                    this.mTvSeatHeatVent.setText(isSupportDrvSeatVent ? R.string.hvac_seat_vent_adjust : R.string.hvac_seat_heat_adjust);
                }
                this.mImgFrSeat = (XImageView) this.mRootView.findViewById(R.id.img_fr_seat);
                this.mImgFrSeatSmall = (XImageView) this.mRootView.findViewById(R.id.img_fr_seat_small);
                if (isSupportPsnSeatHeat || isSupportPsnSeatVent) {
                    this.mImgFrSeat.setVisibility(0);
                    this.mImgFrSeatSmall.setVisibility(0);
                } else {
                    this.mImgFrSeat.setVisibility(4);
                    this.mImgFrSeatSmall.setVisibility(4);
                }
                XImageView xImageView = (XImageView) this.mRootView.findViewById(R.id.img_seat_split);
                View findViewById2 = this.mRootView.findViewById(R.id.layout_back_seat);
                this.mImgRlSeatSmall = (XImageView) this.mRootView.findViewById(R.id.img_rl_seat_small);
                this.mImgRrSeatSmall = (XImageView) this.mRootView.findViewById(R.id.img_rr_seat_small);
                if (isSupportRearSeatHeat) {
                    xImageView.setVisibility(0);
                    findViewById2.setVisibility(0);
                    xTextView.setVisibility(0);
                    return;
                }
                xTextView.setVisibility(4);
                xImageView.setVisibility(8);
                findViewById2.setVisibility(8);
                return;
            }
            return;
        }
        this.mTvSeatVent = (XTextView) this.mRootView.findViewById(R.id.tv_seat_vent);
        this.mImgSeatVentSmall = (ImageView) this.mRootView.findViewById(R.id.img_seat_vent_small);
    }

    @Override // com.xiaopeng.carcontrol.view.control.BaseViewControl
    public void initView() {
        super.initView();
        initViewModel();
        this.mFreeBreathPanelHeight = UIUtils.dp2px(this.mActivity, 262);
        initTopMenu();
        initTempPickerView();
    }

    protected void initViewModel() {
        this.mViewModel = (HvacViewModel) ViewModelManager.getInstance().getViewModelImpl(IHvacViewModel.class);
        this.mSfsViewModel = (SfsViewModel) ViewModelManager.getInstance().getViewModelImpl(ISfsViewModel.class);
    }

    @Override // com.xiaopeng.carcontrol.view.control.BaseViewControl
    public void onResume() {
        super.onResume();
    }

    protected void initTopMenu() {
        initSeatHeatAndVent();
        initPurify();
        this.mImgFreeBreath = (ImageView) this.mRootView.findViewById(R.id.img_x_free_breath);
        if (!this.mViewModel.isSupportXFreeBreath()) {
            this.mImgFreeBreath.setVisibility(8);
        } else {
            this.mImgFreeBreath.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$cHaA0JB653aIxoAR_O55C42tJ8I
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HvacFuncViewControl.this.lambda$initTopMenu$0$HvacFuncViewControl(view);
                }
            });
        }
        initRapidCooling();
        initDeodorant();
        onSmartModeChange();
        initFragrance();
    }

    public /* synthetic */ void lambda$initTopMenu$0$HvacFuncViewControl(View v) {
        ConstraintLayout constraintLayout = this.mFreeBreathPanelView;
        if (constraintLayout == null || constraintLayout.getHeight() <= 1) {
            showFreeBreathPanel();
        }
    }

    protected void initSeatHeatAndVent() {
        if (BaseFeatureOption.getInstance().isSeatHeatVentGather()) {
            if (this.mViewModel.isShowSeatHeatVentEntry()) {
                this.mLayoutSeatHeatVent.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$rv_tobDOrHwy8i8niVA_m9VIru4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        SeatHeatVentHelper.getInstance().showSeatVentHeatDialog();
                    }
                });
                this.mTvSeatHeatVent.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$SjF40FJCtB4_kwQbGIuWnsSFEjE
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        SeatHeatVentHelper.getInstance().showSeatVentHeatDialog();
                    }
                });
                if (this.mViewModel.isSupportDrvSeatVent()) {
                    updateSettingVentState(this.mViewModel.getHvacSeatVentLevel());
                    this.mViewModel.getHvacSeatVentData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$3e0gCUV-Cri_hAHpfjN3hgYnoNc
                        @Override // androidx.lifecycle.Observer
                        public final void onChanged(Object obj) {
                            HvacFuncViewControl.this.lambda$initSeatHeatAndVent$3$HvacFuncViewControl((SeatVentLevel) obj);
                        }
                    });
                } else {
                    updateSettingHeat(0, this.mViewModel.getHvacSeatHeatLevel());
                }
                if (!this.mViewModel.isSupportDrvSeatVent() || !this.mViewModel.isSupportDrvSeatHeat()) {
                    this.mTvSeatHeatVent.setText(this.mViewModel.isSupportDrvSeatVent() ? R.string.hvac_seat_vent_adjust : R.string.hvac_seat_heat_adjust);
                }
                this.mViewModel.getHvacSeatHeatData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$zoHVp0w4tCV6wGPk0NQkzI0CshE
                    @Override // androidx.lifecycle.Observer
                    public final void onChanged(Object obj) {
                        HvacFuncViewControl.this.lambda$initSeatHeatAndVent$4$HvacFuncViewControl((SeatHeatLevel) obj);
                    }
                });
                if (this.mViewModel.isSupportPsnSeatVent()) {
                    this.mViewModel.getHvacPsnSeatVentData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$_aPB2e8Uzg3Xa77787Nb0DA7SBg
                        @Override // androidx.lifecycle.Observer
                        public final void onChanged(Object obj) {
                            HvacFuncViewControl.this.lambda$initSeatHeatAndVent$5$HvacFuncViewControl((SeatVentLevel) obj);
                        }
                    });
                }
                if (this.mViewModel.isSupportPsnSeatHeat()) {
                    this.mViewModel.getHvacPsnSeatHeatData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$K9fsQypmWfy53WCTZ0C9FI3V84c
                        @Override // androidx.lifecycle.Observer
                        public final void onChanged(Object obj) {
                            HvacFuncViewControl.this.lambda$initSeatHeatAndVent$6$HvacFuncViewControl((SeatHeatLevel) obj);
                        }
                    });
                }
                if (this.mViewModel.isSupportRearSeatHeat()) {
                    this.mViewModel.getHvacRLSeatHeatData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$yfyDpsi1L1ouEQSrRAQOSrgph_g
                        @Override // androidx.lifecycle.Observer
                        public final void onChanged(Object obj) {
                            HvacFuncViewControl.this.lambda$initSeatHeatAndVent$7$HvacFuncViewControl((SeatHeatLevel) obj);
                        }
                    });
                    this.mViewModel.getHvacRRSeatHeatData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$pEwDnxmD00eak1-_5RUsErdNz5w
                        @Override // androidx.lifecycle.Observer
                        public final void onChanged(Object obj) {
                            HvacFuncViewControl.this.lambda$initSeatHeatAndVent$8$HvacFuncViewControl((SeatHeatLevel) obj);
                        }
                    });
                    return;
                }
                return;
            }
            return;
        }
        this.mTvSeatVent = (XTextView) this.mRootView.findViewById(R.id.tv_seat_vent);
        this.mImgSeatVentSmall = (ImageView) this.mRootView.findViewById(R.id.img_seat_vent_small);
        if (this.mViewModel.isSupportDrvSeatVent()) {
            this.mTvSeatVent.setVisibility(0);
            this.mImgSeatVentSmall.setVisibility(0);
            this.mTvSeatVent.setVuiElementType(VuiElementType.STATEFULBUTTON);
            this.mTvSeatVent.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$B57MrrjX6g_cRFl9qQdkUcHfmmc
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HvacFuncViewControl.this.lambda$initSeatHeatAndVent$9$HvacFuncViewControl(view);
                }
            });
            setSeatVentLevel(this.mViewModel.getHvacSeatVentLevel());
            this.mViewModel.getHvacSeatVentData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$rsIqYHXZiRz2y8ayK2tXqsYgn64
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    HvacFuncViewControl.this.lambda$initSeatHeatAndVent$10$HvacFuncViewControl((SeatVentLevel) obj);
                }
            });
            VuiEngine.getInstance(this.mActivity.getApplicationContext()).setVuiElementUnSupportTag(this.mTvSeatVent, false);
        } else {
            this.mTvSeatVent.setVisibility(8);
            this.mImgSeatVentSmall.setVisibility(8);
        }
        this.mTvSeatHeat = (XTextView) this.mRootView.findViewById(R.id.tv_seat_heat);
        this.mImgSeatHeatSmall = (ImageView) this.mRootView.findViewById(R.id.img_seat_heat_small);
        if (this.mViewModel.isSupportDrvSeatHeat()) {
            this.mTvSeatHeat.setVisibility(0);
            this.mImgSeatHeatSmall.setVisibility(0);
            this.mTvSeatHeat.setVuiElementType(VuiElementType.STATEFULBUTTON);
            this.mTvSeatHeat.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$rsrc-hzqvy0_-5_zEKYZTSiaAtU
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HvacFuncViewControl.this.lambda$initSeatHeatAndVent$11$HvacFuncViewControl(view);
                }
            });
            setSeatHeatLevel(this.mTvSeatHeat, this.mImgSeatHeatSmall, this.mViewModel.getHvacSeatHeatLevel());
            this.mViewModel.getHvacSeatHeatData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$-0wmHqoLuoBJPSf3gYh2Ge8GXo4
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    HvacFuncViewControl.this.lambda$initSeatHeatAndVent$12$HvacFuncViewControl((SeatHeatLevel) obj);
                }
            });
            VuiEngine.getInstance(this.mActivity.getApplicationContext()).setVuiElementUnSupportTag(this.mTvSeatHeat, false);
        } else {
            this.mTvSeatHeat.setVisibility(8);
            this.mImgSeatHeatSmall.setVisibility(8);
        }
        if (!this.mViewModel.isSupportDrvSeatVent() && this.mViewModel.isSupportDrvSeatHeat()) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mTvSeatHeat.getLayoutParams();
            layoutParams.horizontalChainStyle = 2;
            layoutParams.startToStart = 0;
            this.mTvSeatHeat.setLayoutParams(layoutParams);
        }
        initPsnSeatHeat();
    }

    public /* synthetic */ void lambda$initSeatHeatAndVent$3$HvacFuncViewControl(SeatVentLevel seatVentLevel) {
        updateSettingVentState(this.mViewModel.getHvacSeatVentLevel());
    }

    public /* synthetic */ void lambda$initSeatHeatAndVent$4$HvacFuncViewControl(SeatHeatLevel seatHeatLevel) {
        updateSettingHeat(0, this.mViewModel.getHvacSeatHeatLevel());
    }

    public /* synthetic */ void lambda$initSeatHeatAndVent$5$HvacFuncViewControl(SeatVentLevel seatHeatLevel) {
        updateHeatVent();
    }

    public /* synthetic */ void lambda$initSeatHeatAndVent$6$HvacFuncViewControl(SeatHeatLevel seatHeatLevel) {
        updateHeatVent();
    }

    public /* synthetic */ void lambda$initSeatHeatAndVent$7$HvacFuncViewControl(SeatHeatLevel seatHeatLevel) {
        updateSettingHeat(2, this.mViewModel.getHvacRLSeatHeatLevel());
    }

    public /* synthetic */ void lambda$initSeatHeatAndVent$8$HvacFuncViewControl(SeatHeatLevel seatHeatLevel) {
        updateSettingHeat(3, this.mViewModel.getHvacRRSeatHeatLevel());
    }

    public /* synthetic */ void lambda$initSeatHeatAndVent$9$HvacFuncViewControl(View v) {
        SeatVentLevel seatVentLevel = this.mViewModel.getHvacSeatVentLevel().toggle();
        this.mViewModel.setHvacSeatVentLevel(seatVentLevel);
        PageEnum pageEnum = PageEnum.HVAC_PAGE;
        BtnEnum btnEnum = BtnEnum.SEAT_VENT_BTN;
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(StatisticUtils.getSwitchOnOff(seatVentLevel != SeatVentLevel.Off));
        StatisticUtils.sendHvacStatistic(pageEnum, btnEnum, objArr);
    }

    public /* synthetic */ void lambda$initSeatHeatAndVent$10$HvacFuncViewControl(SeatVentLevel seatVentLevel) {
        setSeatVentLevel(this.mViewModel.getHvacSeatVentLevel());
    }

    public /* synthetic */ void lambda$initSeatHeatAndVent$11$HvacFuncViewControl(View v) {
        SeatHeatLevel seatHeatLevel = this.mViewModel.getHvacSeatHeatLevel().toggle();
        this.mViewModel.setHvacSeatHeatLevel(seatHeatLevel);
        PageEnum pageEnum = PageEnum.HVAC_PAGE;
        BtnEnum btnEnum = BtnEnum.SEAT_HEAT_BTN;
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(StatisticUtils.getSwitchOnOff(seatHeatLevel != SeatHeatLevel.Off));
        StatisticUtils.sendHvacStatistic(pageEnum, btnEnum, objArr);
    }

    public /* synthetic */ void lambda$initSeatHeatAndVent$12$HvacFuncViewControl(SeatHeatLevel seatHeatLevel) {
        setSeatHeatLevel(this.mTvSeatHeat, this.mImgSeatHeatSmall, this.mViewModel.getHvacSeatHeatLevel());
    }

    protected void initPsnSeatHeat() {
        this.mTvPsnSeatHeat = (XTextView) this.mRootView.findViewById(R.id.tv_psn_seat_heat);
        this.mImgPsnSeatHeatSmall = (ImageView) this.mRootView.findViewById(R.id.img_psn_seat_heat_small);
        if (this.mViewModel.isSupportPsnSeatHeat()) {
            this.mTvPsnSeatHeat.setVisibility(0);
            this.mImgPsnSeatHeatSmall.setVisibility(0);
            this.mTvPsnSeatHeat.setVuiElementType(VuiElementType.STATEFULBUTTON);
            this.mTvPsnSeatHeat.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$ALGbFmT7Wtwpdfo0q5ebH-8lMlY
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HvacFuncViewControl.this.lambda$initPsnSeatHeat$13$HvacFuncViewControl(view);
                }
            });
            setSeatHeatLevel(this.mTvPsnSeatHeat, this.mImgPsnSeatHeatSmall, this.mViewModel.getHvacPsnSeatHeatLevel());
            this.mViewModel.getHvacPsnSeatHeatData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$Y-1PIzOen0H8cquJSNwNblZFPns
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    HvacFuncViewControl.this.lambda$initPsnSeatHeat$14$HvacFuncViewControl((SeatHeatLevel) obj);
                }
            });
            VuiEngine.getInstance(this.mActivity.getApplicationContext()).setVuiElementUnSupportTag(this.mTvPsnSeatHeat, false);
            return;
        }
        this.mTvPsnSeatHeat.setVisibility(8);
        this.mImgPsnSeatHeatSmall.setVisibility(8);
    }

    public /* synthetic */ void lambda$initPsnSeatHeat$13$HvacFuncViewControl(View v) {
        HvacViewModel hvacViewModel = this.mViewModel;
        hvacViewModel.setHvacPsnSeatHeatLevel(hvacViewModel.getHvacPsnSeatHeatLevel().toggle());
    }

    public /* synthetic */ void lambda$initPsnSeatHeat$14$HvacFuncViewControl(SeatHeatLevel seatHeatLevel) {
        setSeatHeatLevel(this.mTvPsnSeatHeat, this.mImgPsnSeatHeatSmall, this.mViewModel.getHvacPsnSeatHeatLevel());
    }

    protected void setSeatVentLevel(SeatVentLevel seatVentLevel) {
        int i = AnonymousClass4.$SwitchMap$com$xiaopeng$carcontrol$viewmodel$hvac$SeatVentLevel[seatVentLevel.ordinal()];
        if (i == 1) {
            this.mTvSeatVent.setSelected(true);
            this.mImgSeatVentSmall.setImageResource(R.drawable.ic_small_aeration01);
        } else if (i == 2) {
            this.mTvSeatVent.setSelected(true);
            this.mImgSeatVentSmall.setImageResource(R.drawable.ic_small_aeration02);
        } else if (i == 3) {
            this.mTvSeatVent.setSelected(true);
            this.mImgSeatVentSmall.setImageResource(R.drawable.ic_small_aeration03);
        } else {
            this.mTvSeatVent.setSelected(false);
            this.mImgSeatVentSmall.setImageResource(R.drawable.ic_small_aeratioff);
        }
        updateDriveSeatHeat(StatefulButtonConstructorFactory.ConstructorType.SEAT_AIR, this.mTvSeatVent, this.mViewModel.getSeatVentLevel());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xiaopeng.carcontrol.view.control.hvac.HvacFuncViewControl$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$xiaopeng$carcontrol$viewmodel$hvac$SeatHeatLevel;
        static final /* synthetic */ int[] $SwitchMap$com$xiaopeng$carcontrol$viewmodel$hvac$SeatVentLevel;

        static {
            int[] iArr = new int[SeatHeatLevel.values().length];
            $SwitchMap$com$xiaopeng$carcontrol$viewmodel$hvac$SeatHeatLevel = iArr;
            try {
                iArr[SeatHeatLevel.Level1.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$xiaopeng$carcontrol$viewmodel$hvac$SeatHeatLevel[SeatHeatLevel.Level2.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$xiaopeng$carcontrol$viewmodel$hvac$SeatHeatLevel[SeatHeatLevel.Level3.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[SeatVentLevel.values().length];
            $SwitchMap$com$xiaopeng$carcontrol$viewmodel$hvac$SeatVentLevel = iArr2;
            try {
                iArr2[SeatVentLevel.Level1.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$xiaopeng$carcontrol$viewmodel$hvac$SeatVentLevel[SeatVentLevel.Level2.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$xiaopeng$carcontrol$viewmodel$hvac$SeatVentLevel[SeatVentLevel.Level3.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private void setSeatHeatLevel(TextView tvSeatHeat, ImageView imgSeatHeatSmall, SeatHeatLevel seatHeatLevel) {
        int i = AnonymousClass4.$SwitchMap$com$xiaopeng$carcontrol$viewmodel$hvac$SeatHeatLevel[seatHeatLevel.ordinal()];
        if (i == 1) {
            tvSeatHeat.setSelected(true);
            imgSeatHeatSmall.setImageResource(R.drawable.ic_small_seatheatingon01);
        } else if (i == 2) {
            tvSeatHeat.setSelected(true);
            imgSeatHeatSmall.setImageResource(R.drawable.ic_small_seatheatingon02);
        } else if (i == 3) {
            tvSeatHeat.setSelected(true);
            imgSeatHeatSmall.setImageResource(R.drawable.ic_small_seatheatingon03);
        } else {
            tvSeatHeat.setSelected(false);
            imgSeatHeatSmall.setImageResource(R.drawable.ic_small_seatheatingoff);
        }
        updateDriveSeatHeat(StatefulButtonConstructorFactory.ConstructorType.SEAT_HOT, tvSeatHeat, seatHeatLevel.ordinal());
    }

    public void updateSettingHeat(int type, SeatHeatLevel seatHeatLevel) {
        updateHeatVent();
    }

    public void updateSettingVentState(SeatVentLevel seatVentLevel) {
        updateHeatVent();
    }

    public void updateHeatVent() {
        HvacViewModel hvacViewModel = this.mViewModel;
        if (hvacViewModel != null) {
            if ((hvacViewModel.isSupportDrvSeatHeat() || this.mViewModel.isSupportDrvSeatVent()) && BaseFeatureOption.getInstance().isSeatHeatVentGather()) {
                int seatHeatLevel = this.mViewModel.getSeatHeatLevel();
                int seatVentLevel = this.mViewModel.isSupportDrvSeatVent() ? this.mViewModel.getSeatVentLevel() : 0;
                this.mImgFlSeat.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_mid_lefton));
                this.mImgFlSeatSmall.setVisibility(0);
                if (seatHeatLevel > 0) {
                    setSeatHeatDrawable(seatHeatLevel, this.mImgFlSeatSmall);
                } else if (seatVentLevel > 0) {
                    setSeatVentDrawable(seatVentLevel, this.mImgFlSeatSmall);
                } else if (this.mViewModel.isSupportDrvSeatVent()) {
                    if (!this.mViewModel.isSupportDrvSeatHeat()) {
                        this.mImgFlSeatSmall.setVisibility(0);
                        setSeatVentDrawable(seatVentLevel, this.mImgFlSeatSmall);
                    } else {
                        this.mImgFlSeatSmall.setVisibility(8);
                        this.mImgFlSeat.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_mid_seat_close));
                    }
                } else {
                    this.mImgFlSeatSmall.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_small_seatheatingoff));
                }
                this.mImgFrSeat.setImageResource(R.drawable.ic_mid_righton);
                if (!this.mViewModel.isSupportPsnSeatVent()) {
                    if (this.mViewModel.isSupportPsnSeatHeat()) {
                        setSeatHeatDrawable(this.mViewModel.getPsnSeatHeatLevel(), this.mImgFrSeatSmall);
                    }
                } else if (!this.mViewModel.isSupportPsnSeatHeat()) {
                    setSeatVentDrawable(this.mViewModel.getPsnSeatVentLevel(), this.mImgFrSeatSmall);
                } else {
                    int psnSeatVentLevel = this.mViewModel.getPsnSeatVentLevel();
                    int psnSeatHeatLevel = this.mViewModel.getPsnSeatHeatLevel();
                    if (psnSeatHeatLevel > 0) {
                        setSeatHeatDrawable(psnSeatHeatLevel, this.mImgFrSeatSmall);
                    } else if (psnSeatVentLevel > 0) {
                        setSeatVentDrawable(psnSeatVentLevel, this.mImgFrSeatSmall);
                    } else {
                        this.mImgFrSeatSmall.setVisibility(8);
                        this.mImgFrSeat.setImageResource(R.drawable.ic_mid_seat_close_psn);
                    }
                }
                if (this.mViewModel.isSupportRearSeatHeat()) {
                    setSeatHeatDrawable(this.mViewModel.getRLSeatHeatLevel(), this.mImgRlSeatSmall);
                    setSeatHeatDrawable(this.mViewModel.getRRSeatHeatLevel(), this.mImgRrSeatSmall);
                }
            }
        }
    }

    private void setSeatVentDrawable(int level, XImageView view) {
        view.setVisibility(0);
        if (level == 1) {
            view.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_small_aeration01));
        } else if (level == 2) {
            view.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_small_aeration02));
        } else if (level == 3) {
            view.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_small_aeration03));
        } else {
            view.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_small_aeratioff));
        }
    }

    private void setSeatHeatDrawable(int level, XImageView view) {
        view.setVisibility(0);
        if (level == 1) {
            view.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_small_seatheatingon01));
        } else if (level == 2) {
            view.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_small_seatheatingon02));
        } else if (level == 3) {
            view.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_small_seatheatingon03));
        } else {
            view.setImageDrawable(this.mActivity.getDrawable(R.drawable.ic_small_seatheatingoff));
        }
    }

    protected void initPurify() {
        this.mTvPurify = (TextView) this.mRootView.findViewById(R.id.tv_air_purify);
        if (!CarBaseConfig.getInstance().isSupportInnerPm25()) {
            this.mTvPurify.setVisibility(8);
            return;
        }
        this.mTvPurify.setVisibility(0);
        this.mTvPurify.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$lU5J8dZ3028Lb1rId7aaHiYFZ2Q
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HvacFuncViewControl.this.lambda$initPurify$15$HvacFuncViewControl(view);
            }
        });
        this.mTvPurify.setSelected(this.mViewModel.isHvacQualityPurgeEnable());
    }

    public /* synthetic */ void lambda$initPurify$15$HvacFuncViewControl(View v) {
        if (this.mViewModel.isSmartModeInProtected()) {
            LogUtils.d(TAG, "smartMode in protect mode purge click", false);
            return;
        }
        StatisticUtils.sendHvacStatistic(PageEnum.HVAC_PAGE, BtnEnum.HVAC_PM25_BTN, new Object[0]);
        HvacViewModel hvacViewModel = this.mViewModel;
        hvacViewModel.setHvacQualityPurgeMode(!hvacViewModel.isHvacQualityPurgeEnable());
    }

    protected void initRapidCooling() {
        TextView textView = (TextView) this.mRootView.findViewById(R.id.tv_quick_cooling);
        this.mTvRapidCooling = textView;
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$JxpeietsSzIuPSlyyggrX0FqpiM
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HvacFuncViewControl.this.lambda$initRapidCooling$16$HvacFuncViewControl(view);
            }
        });
        this.mViewModel.getHvacRapidCoolingData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$ZSsEvBM-qZtI2QBtwXcvUiOx6-c
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                HvacFuncViewControl.this.lambda$initRapidCooling$17$HvacFuncViewControl((Boolean) obj);
            }
        });
        this.mViewModel.getHvacRapidCoolingTimeData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$eloQ5sbo_WA0g1_PGOd3N0vv0Sg
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                HvacFuncViewControl.this.lambda$initRapidCooling$18$HvacFuncViewControl((Integer) obj);
            }
        });
    }

    public /* synthetic */ void lambda$initRapidCooling$16$HvacFuncViewControl(View v) {
        hvacRapidCoolingClick();
    }

    public /* synthetic */ void lambda$initRapidCooling$17$HvacFuncViewControl(Boolean aBoolean) {
        onSmartModeChange();
    }

    public /* synthetic */ void lambda$initRapidCooling$18$HvacFuncViewControl(Integer integer) {
        if (this.mViewModel.isHvacRapidCoolingEnable()) {
            if (!this.mTvRapidCooling.isSelected()) {
                this.mTvRapidCooling.setSelected(true);
                VuiEngine.getInstance(this.mActivity.getApplicationContext()).updateScene("hvac", this.mTvRapidCooling);
            }
            this.mTvRapidCooling.setText(integer + "s");
        }
    }

    protected void initDeodorant() {
        TextView textView = (TextView) this.mRootView.findViewById(R.id.tv_deodorant);
        this.mTvDeodorant = textView;
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$gyuCnsjzbSz_wadULvNtLegCM5g
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                HvacFuncViewControl.this.lambda$initDeodorant$19$HvacFuncViewControl(view);
            }
        });
        this.mViewModel.getHvacDeodorantData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$vz6FqWIlHCtmObm8_juLRj7Q1J4
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                HvacFuncViewControl.this.lambda$initDeodorant$20$HvacFuncViewControl((Boolean) obj);
            }
        });
        this.mViewModel.getHvacDeodorantTimeData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$cEGjIrzf3IiVFPvSx0keKmTMb74
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                HvacFuncViewControl.this.lambda$initDeodorant$21$HvacFuncViewControl((Integer) obj);
            }
        });
    }

    public /* synthetic */ void lambda$initDeodorant$19$HvacFuncViewControl(View v) {
        hvacDeodorantClick();
    }

    public /* synthetic */ void lambda$initDeodorant$20$HvacFuncViewControl(Boolean aBoolean) {
        onSmartModeChange();
    }

    public /* synthetic */ void lambda$initDeodorant$21$HvacFuncViewControl(Integer integer) {
        if (this.mViewModel.isHvacDeodorantEnable()) {
            if (!this.mTvDeodorant.isSelected()) {
                this.mTvDeodorant.setSelected(true);
                VuiEngine.getInstance(this.mActivity.getApplicationContext()).updateScene("hvac", this.mTvDeodorant);
            }
            this.mTvDeodorant.setText(integer + "s");
        }
    }

    protected void initFragrance() {
        this.mTvFragrance = (TextView) this.mRootView.findViewById(R.id.tv_fragrance);
        if (CarBaseConfig.getInstance().isSupportSfs()) {
            this.mTvFragrance.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$J3UfrLQGsQ6khbiVx2c9aCAPV1w
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HvacFuncViewControl.this.lambda$initFragrance$22$HvacFuncViewControl(view);
                }
            });
            this.mTvFragrance.setSelected(this.mSfsViewModel.isSwEnabled());
            this.mSfsViewModel.getSfsSwData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$znhGETV9dcRMbcVxEgCVUi6iCAo
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    HvacFuncViewControl.this.lambda$initFragrance$23$HvacFuncViewControl((Boolean) obj);
                }
            });
            return;
        }
        this.mTvFragrance.setVisibility(8);
    }

    public /* synthetic */ void lambda$initFragrance$22$HvacFuncViewControl(View v) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(268468224);
            if ((this.mActivity.getIntent().getFlags() & 1024) == 1024) {
                intent.addFlags(1024);
            }
            intent.setData(Uri.parse("xiaopeng://aiot/device/detail?type=fragrance&from=air"));
            this.mActivity.startActivity(intent);
        } catch (Exception unused) {
            LogUtils.e(TAG, "cant find fragrance app");
        }
    }

    public /* synthetic */ void lambda$initFragrance$23$HvacFuncViewControl(Boolean aBoolean) {
        this.mTvFragrance.setSelected(this.mSfsViewModel.isSwEnabled());
    }

    public void onAirPurgeChanged() {
        TextView textView = this.mTvPurify;
        if (textView != null) {
            textView.setSelected(this.mViewModel.isHvacQualityPurgeEnable());
            VuiEngine.getInstance(this.mActivity.getApplicationContext()).updateScene("hvac", this.mTvPurify);
        }
    }

    protected void hvacRapidCoolingClick() {
        if (this.mViewModel.isSmartModeInProtected()) {
            LogUtils.d(TAG, "hvacRapidCoolingClick smart mode is in protect time");
            return;
        }
        HvacViewModel hvacViewModel = this.mViewModel;
        hvacViewModel.setHvacRapidCoolingEnable(!hvacViewModel.isHvacRapidCoolingEnable());
    }

    protected void hvacDeodorantClick() {
        if (this.mViewModel.isSmartModeInProtected()) {
            LogUtils.d(TAG, "hvacDeodorantClick smart mode is in protect time");
            return;
        }
        HvacViewModel hvacViewModel = this.mViewModel;
        hvacViewModel.setHvacDeodorantEnable(!hvacViewModel.isHvacDeodorantEnable());
    }

    protected void onSmartModeChange() {
        TextView textView;
        TextView textView2;
        if ((!this.mViewModel.isHvacPowerModeOn() || !this.mViewModel.isHvacRapidCoolingEnable()) && (textView = this.mTvRapidCooling) != null) {
            textView.setSelected(false);
            this.mTvRapidCooling.setText(R.string.hvac_quick_cooling);
        }
        if ((!this.mViewModel.isHvacPowerModeOn() || !this.mViewModel.isHvacDeodorantEnable()) && (textView2 = this.mTvDeodorant) != null) {
            textView2.setSelected(false);
            this.mTvDeodorant.setText(R.string.hvac_deodorant);
        }
        VuiEngine.getInstance(this.mActivity.getApplicationContext()).updateScene("hvac", this.mTvRapidCooling);
        VuiEngine.getInstance(this.mActivity.getApplicationContext()).updateScene("hvac", this.mTvDeodorant);
    }

    private void updateDriveSeatHeat(StatefulButtonConstructorFactory.ConstructorType type, View View, int level) {
        StatefulButtonConstructorFactory.getConstructor(type, this.mActivity, level).construct((IVuiElement) View);
        VuiEngine.getInstance(this.mActivity.getApplicationContext()).updateScene("hvac", View);
    }

    private void showFreeBreathPanel() {
        ViewStub viewStub = (ViewStub) this.mRootView.findViewById(R.id.view_stub_free_breath_panel);
        if (viewStub != null && viewStub.getParent() != null) {
            this.mFreeBreathPanelView = (ConstraintLayout) viewStub.inflate();
            if (!CarBaseConfig.getInstance().isSupportInnerPm25Show()) {
                ((TextView) this.mFreeBreathPanelView.findViewById(R.id.tvFreeBreathInfo)).setText(ResUtils.getString(R.string.hvac_free_breath_info_no_pm_display));
            }
            this.mFreeBreathPanelView.setOnTouchListener(new View.OnTouchListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$q7S5wvaZ4tm0M_u0YaX6C0bWsoM
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    return HvacFuncViewControl.this.lambda$showFreeBreathPanel$24$HvacFuncViewControl(view, motionEvent);
                }
            });
        }
        ValueAnimator valueAnimator = this.mFreeBreathCloseAnim;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mFreeBreathCloseAnim.cancel();
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(1, this.mFreeBreathPanelHeight);
        this.mFreeBreathOpenAnim = ofInt;
        ofInt.setDuration(200L);
        this.mFreeBreathOpenAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$-FKBnuBcW6s-mUO7aT8pb32cFak
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                HvacFuncViewControl.this.lambda$showFreeBreathPanel$25$HvacFuncViewControl(valueAnimator2);
            }
        });
        this.mFreeBreathOpenAnim.start();
        closeFreeBreathPanelHandler();
    }

    public /* synthetic */ boolean lambda$showFreeBreathPanel$24$HvacFuncViewControl(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            Handler handler = this.mCloseFreeBreathPanelHandler;
            if (handler != null) {
                handler.removeCallbacks(this.freeBreathPanelRun);
            }
        } else if (action == 1 || action == 3) {
            closeFreeBreathPanelHandler();
        }
        return true;
    }

    public /* synthetic */ void lambda$showFreeBreathPanel$25$HvacFuncViewControl(ValueAnimator animation) {
        int intValue = ((Integer) animation.getAnimatedValue()).intValue();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mFreeBreathPanelView.getLayoutParams();
        layoutParams.height = intValue;
        this.mFreeBreathPanelView.setLayoutParams(layoutParams);
    }

    private void closeFreeBreathPanelHandler() {
        if (this.mCloseFreeBreathPanelHandler == null) {
            this.mCloseFreeBreathPanelHandler = new Handler();
        }
        this.mCloseFreeBreathPanelHandler.removeCallbacks(this.freeBreathPanelRun);
        this.mCloseFreeBreathPanelHandler.postDelayed(this.freeBreathPanelRun, PANEL_SHOW_TIME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeFreeBreathPanel() {
        if (this.mFreeBreathPanelView == null) {
            return;
        }
        Handler handler = this.mCloseFreeBreathPanelHandler;
        if (handler != null) {
            handler.removeCallbacks(this.freeBreathPanelRun);
        }
        ValueAnimator valueAnimator = this.mFreeBreathOpenAnim;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mFreeBreathOpenAnim.cancel();
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(this.mFreeBreathPanelView.getHeight(), 1);
        this.mFreeBreathCloseAnim = ofInt;
        ofInt.setDuration((this.mFreeBreathPanelView.getHeight() * 200) / this.mFreeBreathPanelHeight);
        this.mFreeBreathCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$4e34htUGvViSkTmD4AYELusG86U
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                HvacFuncViewControl.this.lambda$closeFreeBreathPanel$26$HvacFuncViewControl(valueAnimator2);
            }
        });
        this.mFreeBreathCloseAnim.addListener(new Animator.AnimatorListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.HvacFuncViewControl.1
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                HvacFuncViewControl.this.mImgFreeBreath.setClickable(true);
            }
        });
        this.mFreeBreathCloseAnim.start();
    }

    public /* synthetic */ void lambda$closeFreeBreathPanel$26$HvacFuncViewControl(ValueAnimator animation) {
        int intValue = ((Integer) animation.getAnimatedValue()).intValue();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mFreeBreathPanelView.getLayoutParams();
        layoutParams.height = intValue;
        this.mFreeBreathPanelView.setLayoutParams(layoutParams);
    }

    public void analyzeXFreeBreathPanel(float touchX, float touchY) {
        ConstraintLayout constraintLayout = this.mFreeBreathPanelView;
        if (constraintLayout == null || constraintLayout.getHeight() <= 1) {
            return;
        }
        if (touchX < this.mFreeBreathPanelView.getLeft() || touchX > this.mFreeBreathPanelView.getRight() || touchY < this.mFreeBreathPanelView.getTop() || touchY > this.mFreeBreathPanelView.getBottom()) {
            closeFreeBreathPanel();
        }
    }

    private void initTempPickerView() {
        this.mImgTempSyncBtn = (ImageView) this.mRootView.findViewById(R.id.img_drv_temp_sync);
        if (!this.mViewModel.isSupportDulTemp()) {
            this.mImgTempSyncBtn.setVisibility(8);
        } else {
            this.mImgTempSyncBtn.setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$5VzmECw2LIc24tj9uFdOyI8OQh0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    HvacFuncViewControl.this.lambda$initTempPickerView$27$HvacFuncViewControl(view);
                }
            });
            this.mImgTempSyncBtn.setSelected(this.mViewModel.isHvacDriverSyncMode());
            this.mViewModel.getHvacDriverSyncData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$i4dAkjSSUbvOZuBAsS4jJgsjTKg
                @Override // androidx.lifecycle.Observer
                public final void onChanged(Object obj) {
                    HvacFuncViewControl.this.lambda$initTempPickerView$28$HvacFuncViewControl((Boolean) obj);
                }
            });
        }
        this.mImgDrvTempBg = (ImageView) this.mRootView.findViewById(R.id.img_drv_temp);
        this.mDrvTempPickerView = (ValuePickerView) this.mRootView.findViewById(R.id.temp_picker_drv);
        this.mImgPsnTempBg = (ImageView) this.mRootView.findViewById(R.id.img_psn_temp);
        this.mPsnTempPickerView = (ValuePickerView) this.mRootView.findViewById(R.id.temp_picker_psn);
        String[] strArr = new String[29];
        for (int i = 0; i < 29; i++) {
            strArr[i] = String.format(Locale.ENGLISH, this.mActivity.getString(R.string.hvac_temperature_symbol), Float.valueOf((64 - i) * 0.5f));
        }
        this.mDrvTempImmerseView = (TempImmerseView) this.mRootView.findViewById(R.id.drv_temp_immerse_view);
        this.mDrvTempPickerView.setDisplayedValues(strArr);
        this.mDrvTempPickerView.setMinValue(36);
        this.mDrvTempPickerView.setMaxValue(64);
        this.mDrvTempPickerView.setOnValueChangedListener(new ValuePickerView.OnValueChangeListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.HvacFuncViewControl.2
            @Override // com.xiaopeng.carcontrol.view.widget.ValuePickerView.OnValueChangeListener
            public void onTouchDown() {
            }

            @Override // com.xiaopeng.carcontrol.view.widget.ValuePickerView.OnValueChangeListener
            public void onTouchUp() {
            }

            @Override // com.xiaopeng.carcontrol.view.widget.ValuePickerView.OnValueChangeListener
            public void onValueChange(ValuePickerView picker, int oldVal, int newVal) {
                float inversionTempValue = HvacFuncViewControl.inversionTempValue(newVal) / 2.0f;
                LogUtils.d(HvacFuncViewControl.TAG, "drv newVal:" + inversionTempValue);
                HvacFuncViewControl.this.mViewModel.setHvacTempDriver(inversionTempValue);
            }
        });
        this.mDrvTempPickerView.setOnValueChangeListenerInScrolling(new ValuePickerView.OnValueChangeListenerInScrolling() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$bxa5M5uUYxckl7LXfoJ06VeFcc4
            @Override // com.xiaopeng.carcontrol.view.widget.ValuePickerView.OnValueChangeListenerInScrolling
            public final void onValueChangeInScrolling(ValuePickerView valuePickerView, int i2, int i3) {
                HvacFuncViewControl.this.lambda$initTempPickerView$29$HvacFuncViewControl(valuePickerView, i2, i3);
            }
        });
        try {
            int inversionTempValue = inversionTempValue((int) (this.mViewModel.getHvacDriverTemp() * 2.0f));
            this.mDrvTempPickerView.setValue(inversionTempValue);
            this.mDrvTempImmerseView.setTempValue(inversionTempValue - 36);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mViewModel.getHvacTempDriverData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$vi2BcLWy9ppqMDUiI1g_lUe9vRY
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                HvacFuncViewControl.this.lambda$initTempPickerView$30$HvacFuncViewControl((Float) obj);
            }
        });
        this.mPsnTempImmerseView = (TempImmerseView) this.mRootView.findViewById(R.id.psn_temp_immerse_view);
        this.mPsnTempPickerView.setDisplayedValues(strArr);
        this.mPsnTempPickerView.setMinValue(36);
        this.mPsnTempPickerView.setMaxValue(64);
        this.mPsnTempPickerView.setOnValueChangedListener(new ValuePickerView.OnValueChangeListener() { // from class: com.xiaopeng.carcontrol.view.control.hvac.HvacFuncViewControl.3
            @Override // com.xiaopeng.carcontrol.view.widget.ValuePickerView.OnValueChangeListener
            public void onTouchDown() {
            }

            @Override // com.xiaopeng.carcontrol.view.widget.ValuePickerView.OnValueChangeListener
            public void onTouchUp() {
            }

            @Override // com.xiaopeng.carcontrol.view.widget.ValuePickerView.OnValueChangeListener
            public void onValueChange(ValuePickerView picker, int oldVal, int newVal) {
                float inversionTempValue2 = HvacFuncViewControl.inversionTempValue(newVal) / 2.0f;
                LogUtils.d(HvacFuncViewControl.TAG, "psn newVal:" + inversionTempValue2);
                HvacFuncViewControl.this.mViewModel.setHvacTempPsn(inversionTempValue2);
            }
        });
        this.mPsnTempPickerView.setOnValueChangeListenerInScrolling(new ValuePickerView.OnValueChangeListenerInScrolling() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$Q2IVMAHdcQH99vBv63bUsakssbQ
            @Override // com.xiaopeng.carcontrol.view.widget.ValuePickerView.OnValueChangeListenerInScrolling
            public final void onValueChangeInScrolling(ValuePickerView valuePickerView, int i2, int i3) {
                HvacFuncViewControl.this.lambda$initTempPickerView$31$HvacFuncViewControl(valuePickerView, i2, i3);
            }
        });
        try {
            int inversionTempValue2 = inversionTempValue((int) (this.mViewModel.getHvacPsnTemp() * 2.0f));
            this.mPsnTempPickerView.setValue(inversionTempValue2);
            this.mPsnTempImmerseView.setTempValue(inversionTempValue2 - 36);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.mViewModel.getHvacTempPsnData().observe(this.mActivity, new Observer() { // from class: com.xiaopeng.carcontrol.view.control.hvac.-$$Lambda$HvacFuncViewControl$wAnZJciZkbsHHlFO-DkZHSQ1EmA
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                HvacFuncViewControl.this.lambda$initTempPickerView$32$HvacFuncViewControl((Float) obj);
            }
        });
    }

    public /* synthetic */ void lambda$initTempPickerView$27$HvacFuncViewControl(View v) {
        boolean isHvacDriverSyncMode = this.mViewModel.isHvacDriverSyncMode();
        this.mViewModel.setHvacDriverSyncMode(!isHvacDriverSyncMode);
        StatisticUtils.sendHvacStatistic(PageEnum.HVAC_PAGE, BtnEnum.TEMPERATURE_SYNC_BTN, Integer.valueOf(StatisticUtils.getSwitchOnOff(!isHvacDriverSyncMode)));
    }

    public /* synthetic */ void lambda$initTempPickerView$28$HvacFuncViewControl(Boolean aBoolean) {
        this.mImgTempSyncBtn.setSelected(this.mViewModel.isHvacDriverSyncMode());
        VuiEngine.getInstance(this.mActivity.getApplicationContext()).updateScene("hvac", this.mImgTempSyncBtn);
    }

    public /* synthetic */ void lambda$initTempPickerView$29$HvacFuncViewControl(ValuePickerView picker, int oldVal, int newVal) {
        LogUtils.d(TAG, "setOnValueChangeListenerInScrolling drv oldVal:" + oldVal + " ,newVal:" + newVal);
        this.mDrvTempImmerseView.setTempValue(newVal);
    }

    public /* synthetic */ void lambda$initTempPickerView$30$HvacFuncViewControl(Float aFloat) {
        try {
            int inversionTempValue = inversionTempValue((int) (this.mViewModel.getHvacDriverTemp() * 2.0f));
            this.mDrvTempPickerView.smoothScrollToValue(inversionTempValue, false);
            this.mDrvTempImmerseView.setTempValue(inversionTempValue - 36);
            ArrayList arrayList = new ArrayList();
            arrayList.add(Integer.valueOf(this.mDrvTempPickerView.getId()));
            VuiEngine.getInstance(this.mActivity.getApplicationContext()).updateScene("hvac", this.mDrvTempPickerView, arrayList, (IVuiElementListener) this.mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public /* synthetic */ void lambda$initTempPickerView$31$HvacFuncViewControl(ValuePickerView picker, int oldVal, int newVal) {
        this.mPsnTempImmerseView.setTempValue(newVal);
    }

    public /* synthetic */ void lambda$initTempPickerView$32$HvacFuncViewControl(Float aFloat) {
        try {
            int inversionTempValue = inversionTempValue((int) (this.mViewModel.getHvacPsnTemp() * 2.0f));
            this.mPsnTempPickerView.smoothScrollToValue(inversionTempValue, false);
            this.mPsnTempImmerseView.setTempValue(inversionTempValue - 36);
            ArrayList arrayList = new ArrayList();
            arrayList.add(Integer.valueOf(this.mPsnTempPickerView.getId()));
            VuiEngine.getInstance(this.mActivity.getApplicationContext()).updateScene("hvac", this.mPsnTempPickerView, arrayList, (IVuiElementListener) this.mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hiddenDrvTempSyncBtn() {
        ImageView imageView = this.mImgTempSyncBtn;
        if (imageView != null) {
            imageView.setClickable(false);
            this.mImgTempSyncBtn.animate().alpha(0.0f).start();
        }
    }

    private void showTempSyncBtn() {
        ImageView imageView = this.mImgTempSyncBtn;
        if (imageView != null) {
            imageView.setClickable(true);
            this.mImgTempSyncBtn.animate().alpha(1.0f).start();
        }
    }

    public void drvTempSmoothNew(int s) {
        ValuePickerView valuePickerView = this.mDrvTempPickerView;
        if (valuePickerView != null) {
            valuePickerView.smoothScrollToValueFromUser(valuePickerView.getValue() + s);
        }
    }

    public void psnTempSmoothNew(int s) {
        ValuePickerView valuePickerView = this.mPsnTempPickerView;
        if (valuePickerView != null) {
            valuePickerView.smoothScrollToValueFromUser(valuePickerView.getValue() + s);
        }
    }

    public void intoImmerseMode(int mImmerseType) {
        if (CarBaseConfig.getInstance().isSupportInnerPm25()) {
            alphaHidden(this.mTvPurify);
            alphaHidden(this.mImgFreeBreath);
        }
        if (BaseFeatureOption.getInstance().isSeatHeatVentGather()) {
            if (this.mViewModel.isShowSeatHeatVentEntry()) {
                alphaHidden(this.mLayoutSeatHeatVent);
                alphaHidden(this.mTvSeatHeatVent);
            }
        } else {
            if (this.mViewModel.isSupportDrvSeatHeat()) {
                alphaHidden(this.mTvSeatHeat);
                alphaHidden(this.mImgSeatHeatSmall);
            }
            if (this.mViewModel.isSupportPsnSeatHeat()) {
                alphaHidden(this.mTvPsnSeatHeat);
                alphaHidden(this.mImgPsnSeatHeatSmall);
            }
            if (this.mViewModel.isSupportDrvSeatVent()) {
                alphaHidden(this.mTvSeatVent);
                alphaHidden(this.mImgSeatVentSmall);
            }
        }
        if (CarBaseConfig.getInstance().isSupportSfs()) {
            alphaHidden(this.mTvFragrance);
        }
        alphaHidden(this.mTvRapidCooling);
        alphaHidden(this.mTvDeodorant);
        hiddenDrvTempSyncBtn();
        if (mImmerseType != 2) {
            alphaHidden(this.mDrvTempPickerView);
            alphaHidden(this.mImgDrvTempBg);
            if (mImmerseType != 1) {
                alphaShow(this.mPsnTempImmerseView);
            }
        }
        if (mImmerseType != 3) {
            alphaHidden(this.mPsnTempPickerView);
            alphaHidden(this.mImgPsnTempBg);
            if (mImmerseType != 1) {
                alphaShow(this.mDrvTempImmerseView);
            }
        }
    }

    protected void alphaHidden(View view) {
        if (view == null) {
            return;
        }
        view.animate().alpha(0.0f).start();
    }

    public void exitImmerseMode(int mImmerseType) {
        if (mImmerseType == 2) {
            this.mViewModel.setHvacTempDriver(inversionTempValue(this.mDrvTempPickerView.getValue()) / 2.0f);
        } else if (mImmerseType == 3) {
            this.mViewModel.setHvacTempPsn(inversionTempValue(this.mPsnTempPickerView.getValue()) / 2.0f);
        }
        if (CarBaseConfig.getInstance().isSupportInnerPm25()) {
            alphaShow(this.mTvPurify);
        }
        if (this.mViewModel.isSupportXFreeBreath()) {
            alphaShow(this.mImgFreeBreath);
        }
        if (BaseFeatureOption.getInstance().isSeatHeatVentGather()) {
            if (this.mViewModel.isShowSeatHeatVentEntry()) {
                alphaShow(this.mLayoutSeatHeatVent);
                alphaShow(this.mTvSeatHeatVent);
            }
        } else {
            if (this.mViewModel.isSupportDrvSeatHeat()) {
                alphaShow(this.mTvSeatHeat);
                alphaShow(this.mImgSeatHeatSmall);
            }
            if (this.mViewModel.isSupportPsnSeatHeat()) {
                alphaShow(this.mTvPsnSeatHeat);
                alphaShow(this.mImgPsnSeatHeatSmall);
            }
            if (this.mViewModel.isSupportDrvSeatVent()) {
                alphaShow(this.mTvSeatVent);
                alphaShow(this.mImgSeatVentSmall);
            }
        }
        if (CarBaseConfig.getInstance().isSupportSfs()) {
            alphaShow(this.mTvFragrance);
        }
        alphaShow(this.mTvRapidCooling);
        alphaShow(this.mTvDeodorant);
        showTempSyncBtn();
        alphaShow(this.mDrvTempPickerView);
        alphaShow(this.mImgDrvTempBg);
        alphaHidden(this.mPsnTempImmerseView);
        alphaShow(this.mPsnTempPickerView);
        alphaShow(this.mImgPsnTempBg);
        alphaHidden(this.mDrvTempImmerseView);
    }

    protected void alphaShow(View view) {
        if (view == null) {
            return;
        }
        view.animate().alpha(1.0f).start();
    }

    @Override // com.xiaopeng.carcontrol.view.control.BaseViewControl
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateHeatVent();
    }

    @Override // com.xiaopeng.carcontrol.view.control.BaseViewControl
    public void onDestroy() {
        super.onDestroy();
        Handler handler = this.mCloseFreeBreathPanelHandler;
        if (handler != null) {
            handler.removeCallbacks(this.freeBreathPanelRun);
        }
        SeatHeatVentHelper.getInstance().onDestroy();
    }

    @Override // com.xiaopeng.carcontrol.view.control.BaseViewControl
    public void onBuildScene(List<Integer> list) {
        list.add(Integer.valueOf(this.mDrvTempPickerView.getId()));
        list.add(Integer.valueOf(this.mPsnTempPickerView.getId()));
    }

    public VuiElement onBuildVuiElement(String id, IVuiElementBuilder iVuiElementBuilder) {
        if (id.contains(String.valueOf(this.mDrvTempPickerView.getId()))) {
            return new ValuePickerFictitiousElementsConstructor(this.mActivity.getApplicationContext(), true).getValuePickerElement(id, this.mViewModel.getHvacDriverTemp(), this.mDrvTempPickerView.getDisplayedValues());
        }
        if (id.contains(String.valueOf(this.mPsnTempPickerView.getId()))) {
            return new ValuePickerFictitiousElementsConstructor(this.mActivity.getApplicationContext(), false).getValuePickerElement(id, this.mViewModel.getHvacPsnTemp(), this.mPsnTempPickerView.getDisplayedValues());
        }
        return null;
    }

    public void onVuiEvent(View view, VuiEvent event) {
        VuiElement hitVuiElement = event.getHitVuiElement();
        if (hitVuiElement.getId().contains(String.valueOf(R.id.tv_seat_heat))) {
            VuiEventHandler.getInstance().runMain(new SeatHotVuiEventActor(true, this.mViewModel, event));
            showVuiFloatingLayer(view);
        }
        if (hitVuiElement.getId().contains(String.valueOf(R.id.tv_seat_vent))) {
            VuiEventHandler.getInstance().runMain(new SeatAirVuiEventActor(this.mViewModel, event, true));
            showVuiFloatingLayer(view);
        }
        if (hitVuiElement.getId().contains(String.valueOf(R.id.tv_psn_seat_heat))) {
            VuiEventHandler.getInstance().runMain(new SeatHotVuiEventActor(false, this.mViewModel, event));
            showVuiFloatingLayer(view);
        }
    }

    @Override // com.xiaopeng.carcontrol.view.control.BaseViewControl
    public void showSeatHeatVentDialog() {
        SeatHeatVentHelper.getInstance().showSeatVentHeatDialog();
    }
}
