package com.xiaopeng.carcontrol.view.fragment.spacecapsule;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.xiaopeng.carcontrol.R;
import com.xiaopeng.carcontrol.helper.SpeechHelper;
/* loaded from: classes2.dex */
public class SeatLayFlatFragment extends SpaceBaseFragment {
    private static final int MSG_WHAT_TTS = 10;
    private static final int PLAY_TTS_INTERVAL_MS = 1000;
    private static final int[] TTS_RES_ID = {R.string.capsule_sleep_guide_headrest_seat_tts, R.string.capsule_sleep_guide_mattress_setup_tts_4};
    private final Handler mHandler = new Handler() { // from class: com.xiaopeng.carcontrol.view.fragment.spacecapsule.SeatLayFlatFragment.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != 10 || SeatLayFlatFragment.this.mTtsResIdIndex >= SeatLayFlatFragment.TTS_RES_ID.length) {
                return;
            }
            SpeechHelper.getInstance().speak(SeatLayFlatFragment.this.getString(SeatLayFlatFragment.TTS_RES_ID[SeatLayFlatFragment.this.mTtsResIdIndex]));
        }
    };
    private ICapsuleSleepGuideInterface mSleepGuideListener;
    private int mTtsResIdIndex;

    @Override // com.xiaopeng.carcontrol.view.fragment.VuiFragment
    protected int getRootLayoutId() {
        return R.layout.fragment_seat_layflat;
    }

    @Override // com.xiaopeng.carcontrol.view.fragment.spacecapsule.SpaceBaseFragment
    protected void initViewModel() {
    }

    @Override // com.xiaopeng.carcontrol.view.fragment.spacecapsule.SpaceBaseFragment
    protected void initViewModelObserver() {
    }

    @Override // com.xiaopeng.carcontrol.view.fragment.spacecapsule.SpaceBaseFragment
    public void setClickSelected(int position) {
    }

    @Override // com.xiaopeng.carcontrol.view.fragment.spacecapsule.SpaceBaseFragment
    protected boolean supportVui() {
        return false;
    }

    @Override // com.xiaopeng.carcontrol.view.fragment.spacecapsule.SpaceBaseFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ICapsuleSleepGuideInterface) {
            this.mSleepGuideListener = (ICapsuleSleepGuideInterface) context;
        }
    }

    @Override // com.xiaopeng.carcontrol.view.fragment.spacecapsule.SpaceBaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        view.findViewById(R.id.capsule_sleep_seat_btn).setOnClickListener(new View.OnClickListener() { // from class: com.xiaopeng.carcontrol.view.fragment.spacecapsule.-$$Lambda$SeatLayFlatFragment$NxLSU_WN6OK-EFub2A9WtTAND3I
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                SeatLayFlatFragment.this.lambda$init$0$SeatLayFlatFragment(view2);
            }
        });
        SpeechHelper.getInstance().setTtsPlayListener(new SpeechHelper.TtsPlayListener() { // from class: com.xiaopeng.carcontrol.view.fragment.spacecapsule.-$$Lambda$SeatLayFlatFragment$JkD4-se17l2NSE4XxCaKZdb7ECY
            @Override // com.xiaopeng.carcontrol.helper.SpeechHelper.TtsPlayListener
            public final void onTtsPlayEnd(String str) {
                SeatLayFlatFragment.this.lambda$init$1$SeatLayFlatFragment(str);
            }
        });
        this.mHandler.sendEmptyMessageDelayed(10, 1000L);
    }

    public /* synthetic */ void lambda$init$0$SeatLayFlatFragment(View v) {
        ICapsuleSleepGuideInterface iCapsuleSleepGuideInterface = this.mSleepGuideListener;
        if (iCapsuleSleepGuideInterface != null) {
            iCapsuleSleepGuideInterface.onMattressSetup();
        }
    }

    public /* synthetic */ void lambda$init$1$SeatLayFlatFragment(String tts) {
        int i = this.mTtsResIdIndex + 1;
        this.mTtsResIdIndex = i;
        if (i < TTS_RES_ID.length) {
            this.mHandler.sendEmptyMessageDelayed(10, 1000L);
        } else {
            releaseTts();
        }
    }

    @Override // com.xiaopeng.carcontrol.view.fragment.spacecapsule.SpaceBaseFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            releaseTts();
        }
    }

    @Override // com.xiaopeng.carcontrol.view.fragment.spacecapsule.SpaceBaseFragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        releaseTts();
    }

    private void releaseTts() {
        SpeechHelper.getInstance().stop();
        SpeechHelper.getInstance().setTtsPlayListener(null);
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public static SeatLayFlatFragment newInstance() {
        return new SeatLayFlatFragment();
    }
}
