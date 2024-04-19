package com.xiaopeng.speech.protocol.node.asrToText;

import android.text.TextUtils;
import com.xiaopeng.speech.SpeechClient;
import com.xiaopeng.speech.asr.RecognizeListener;
import com.xiaopeng.speech.asr.Recognizer;
import com.xiaopeng.speech.common.SpeechConstant;
import com.xiaopeng.speech.common.util.LogUtils;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class AsrToTextNode {
    private AsrToTextListener mListener;
    private Recognizer mRecognizer;
    private String TAG = "AsrToTextNode";
    private String mInitParam = null;
    private boolean isRecording = false;
    private final RecognizeListener recognizeListener = new RecognizeListener() { // from class: com.xiaopeng.speech.protocol.node.asrToText.AsrToTextNode.1
        volatile int mState;
        boolean isEnd = false;
        String resultStr = "";

        @Override // com.xiaopeng.speech.asr.RecognizeListener
        public void onExtra(int i, int i2, int i3, String str, byte[] bArr) {
        }

        @Override // com.xiaopeng.speech.asr.RecognizeListener
        public void onResult(String str, boolean z) {
            LogUtils.i(AsrToTextNode.this.TAG, "on result: " + str + ", last: " + z + ",state:" + this.mState);
            try {
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                JSONObject jSONObject = new JSONObject(str);
                JSONObject jSONObject2 = new JSONObject();
                String optString = jSONObject.optString("text");
                this.resultStr = replaceBlank(optString);
                if (this.mState == 3 && !z && TextUtils.isEmpty(optString)) {
                    jSONObject2.put("text", optString);
                    jSONObject2.put("last", true);
                    jSONObject2.put("messageCode", "401");
                    AsrToTextNode.this.isRecording = false;
                } else if (this.mState == 4 && TextUtils.isEmpty(optString)) {
                    jSONObject2.put("text", optString);
                    jSONObject2.put("last", true);
                    jSONObject2.put("messageCode", "401");
                    AsrToTextNode.this.isRecording = false;
                } else {
                    jSONObject2.put("text", replaceBlank(optString));
                    jSONObject2.put("last", z);
                    jSONObject2.put("messageCode", "200");
                }
                if (z) {
                    this.isEnd = true;
                    this.resultStr = "";
                    AsrToTextNode.this.isRecording = false;
                }
                if (AsrToTextNode.this.mListener != null) {
                    AsrToTextNode.this.mListener.onResult(jSONObject2.toString());
                }
            } catch (JSONException e) {
                e.fillInStackTrace();
            }
        }

        private String replaceBlank(String str) {
            return !TextUtils.isEmpty(str) ? Pattern.compile("\\s*|\t|\r|\n").matcher(str).replaceAll("") : "";
        }

        @Override // com.xiaopeng.speech.asr.RecognizeListener
        public void onError(int i, String str) {
            LogUtils.e(AsrToTextNode.this.TAG, "on error: " + i + ", info: " + str);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("messageCode", "" + i);
                if (AsrToTextNode.this.mListener != null) {
                    AsrToTextNode.this.mListener.onResult(jSONObject.toString());
                }
                AsrToTextNode.this.isRecording = false;
            } catch (JSONException e) {
                e.fillInStackTrace();
            }
        }

        @Override // com.xiaopeng.speech.asr.RecognizeListener
        public void onState(int i, int i2) {
            LogUtils.d(AsrToTextNode.this.TAG, "on onState " + i + ", info: " + i2);
            this.mState = i;
            if (i == 6) {
                try {
                    if (!this.isEnd) {
                        try {
                            JSONObject jSONObject = new JSONObject();
                            jSONObject.put("text", this.resultStr);
                            jSONObject.put("last", true);
                            if (TextUtils.isEmpty(this.resultStr)) {
                                jSONObject.put("messageCode", "401");
                            } else {
                                jSONObject.put("messageCode", "200");
                            }
                            if (AsrToTextNode.this.mListener != null) {
                                AsrToTextNode.this.mListener.onResult(jSONObject.toString());
                            }
                            AsrToTextNode.this.isRecording = false;
                        } catch (JSONException e) {
                            e.fillInStackTrace();
                        }
                        return;
                    }
                    this.resultStr = "";
                    this.isEnd = false;
                } finally {
                    this.resultStr = "";
                }
            }
        }
    };

    public void onServiceConnect() {
        LogUtils.i(this.TAG, "onConnect:" + this.mListener);
        if (this.mListener != null) {
            getRecognizer();
        }
    }

    public void onServiceDisconnect() {
        LogUtils.i(this.TAG, "onDisconnect:" + this.isRecording + ",mListener:" + this.mListener);
        if (this.isRecording) {
            this.mRecognizer = null;
            this.isRecording = false;
            if (this.mListener != null) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("messageCode", "501");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                this.mListener.onResult(jSONObject.toString());
            }
        }
    }

    /* loaded from: classes2.dex */
    private static class SingleHolder {
        static final AsrToTextNode INSTANCE = new AsrToTextNode();

        private SingleHolder() {
        }
    }

    public static AsrToTextNode getInstance() {
        return SingleHolder.INSTANCE;
    }

    public void initRecord(String str, AsrToTextListener asrToTextListener) {
        LogUtils.i(this.TAG, "initRecognizer:" + str);
        this.mListener = asrToTextListener;
        this.mInitParam = str;
        getRecognizer();
    }

    private Recognizer getRecognizer() {
        if (this.mRecognizer == null) {
            this.mRecognizer = SpeechClient.instance().getRecognizer();
            if (TextUtils.isEmpty(this.mInitParam) || this.mInitParam.equals("null")) {
                this.mRecognizer.setString(Recognizer.AUDIO_SAVE_PATH, null);
                this.mRecognizer.setBool(Recognizer.KEEP_AUDIO_RECORD, true);
                this.mRecognizer.setBool(Recognizer.ENABLE_ASR_PUNCT, true);
                this.mRecognizer.setBool(Recognizer.ASR_BUFFER, false);
                this.mRecognizer.setInt(Recognizer.AUDIO_FORMAT, 1);
            } else {
                try {
                    JSONObject jSONObject = new JSONObject(this.mInitParam);
                    Iterator<String> keys = jSONObject.keys();
                    while (keys.hasNext()) {
                        String next = keys.next();
                        Object opt = jSONObject.opt(next);
                        if (opt instanceof Integer) {
                            this.mRecognizer.setInt(next, ((Integer) opt).intValue());
                        } else if (opt instanceof Boolean) {
                            this.mRecognizer.setBool(next, ((Boolean) opt).booleanValue());
                        } else if (opt instanceof String) {
                            this.mRecognizer.setString(next, (String) opt);
                        }
                    }
                } catch (JSONException e) {
                    e.fillInStackTrace();
                }
            }
        }
        return this.mRecognizer;
    }

    public void initRecord(AsrToTextListener asrToTextListener) {
        initRecord(null, asrToTextListener);
    }

    public void startRecord(String str) {
        startRecord(str, null);
    }

    public void startRecord(String str, String str2) {
        try {
            LogUtils.i(this.TAG, "startRecord:" + str2 + ",packageName:" + str);
            String canPerformASRToText = SpeechClient.instance().getSpeechState().canPerformASRToText(str);
            if (TextUtils.isEmpty(canPerformASRToText)) {
                if (this.mListener != null) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("messageCode", "501");
                    this.mListener.onResult(jSONObject.toString());
                }
            } else if (TextUtils.isEmpty(new JSONObject(canPerformASRToText).optString("messageCode"))) {
                startListen(str2);
            } else {
                AsrToTextListener asrToTextListener = this.mListener;
                if (asrToTextListener != null) {
                    asrToTextListener.onResult(canPerformASRToText);
                }
            }
        } catch (JSONException e) {
            e.fillInStackTrace();
        }
    }

    private void startListen(String str) {
        try {
            Recognizer recognizer = getRecognizer();
            if (recognizer == null || recognizer.isListening()) {
                return;
            }
            LogUtils.i(this.TAG, "startListen param:" + str);
            if (!TextUtils.isEmpty(str) && !str.equals("null")) {
                JSONObject jSONObject = new JSONObject(str);
                Iterator<String> keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    Object opt = jSONObject.opt(next);
                    if (opt instanceof Integer) {
                        recognizer.setInt(next, ((Integer) opt).intValue());
                    } else if (opt instanceof Boolean) {
                        recognizer.setBool(next, ((Boolean) opt).booleanValue());
                    } else if (opt instanceof String) {
                        recognizer.setString(next, (String) opt);
                    }
                }
                recognizer.startListening(this.recognizeListener);
                this.isRecording = true;
            }
            recognizer.setString(Recognizer.AUDIO_SAVE_PATH, null);
            recognizer.setInt(Recognizer.EOS, 1000);
            recognizer.setInt(Recognizer.BOS, SpeechConstant.VAD_TIMEOUT);
            recognizer.setInt(Recognizer.MAX_ACTIVE_TIME, 60000);
            recognizer.setBool(Recognizer.DISABLE_ASR, false);
            recognizer.startListening(this.recognizeListener);
            this.isRecording = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord() {
        LogUtils.i(this.TAG, "stopRecord");
        Recognizer recognizer = getRecognizer();
        if (recognizer != null && recognizer.isListening()) {
            recognizer.stopListening();
        }
        this.isRecording = false;
    }

    public void destroyRecord() {
        LogUtils.i(this.TAG, "destroyRecord");
        Recognizer recognizer = getRecognizer();
        if (recognizer != null) {
            recognizer.cancel();
        }
        this.mListener = null;
        this.isRecording = false;
    }
}
