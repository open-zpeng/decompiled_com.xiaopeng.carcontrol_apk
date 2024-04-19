package com.xiaopeng.carcontrol.viewmodel.endurance;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.xiaopeng.appstore.storeprovider.AssembleInfo;
import com.xiaopeng.appstore.storeprovider.AssembleRequest;
import com.xiaopeng.carcontrol.CarConfig;
import com.xiaopeng.carcontrol.helper.NotificationHelper;
import com.xiaopeng.carcontrol.util.LogUtils;
import com.xiaopeng.lib.HttpInitEventListener;
import com.xiaopeng.lib.framework.moduleinterface.carcontroller.IInputController;
import com.xiaopeng.libconfig.ipc.IpcConfig;
import org.apache.commons.compress.archivers.zip.UnixStat;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class D55HvacConsumptionTable extends HvacConsumptionTable {
    private static final int HVAC_MAX_TEMP = 32;
    private static final int HVAC_MIN_TEMP = 18;
    private static final int HVAC_TEMP_MAX_IDX = 14;
    private static final int HVAC_TEMP_STEP = 1;
    private static final int OUTER_MAX_TEMP = 45;
    private static final int OUTER_MIN_TEMP = -25;
    private static final int OUTER_TEMP_MAX_IDX = 70;
    private static final int OUTER_TEMP_STEP = 1;
    private static final int[][] INNER_TABLE = {new int[]{1200, 1160, 1120, 1080, NotificationHelper.SCENE_CAPSULE_SLEEP_OVER_TIME, 1000, 990, 980, 970, 960, 950, 860, 770, 680, 590, 500, 490, 480, 470, 460, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 460, IInputController.KEYCODE_KNOB_VOL_UP, 580, 640, TypedValues.Transition.TYPE_DURATION, 720, 740, 760, 780, 800, 830, 860, 890, 920, 950, NotificationHelper.SCENE_CAPSULE_SLEEP_OVER_TIME, 1130, CarConfig.KEYCODE_NEW_LONG_LEFT_UP, 1310, IpcConfig.DeviceCommunicationConfig.SEND_CAR_CONTROL_TOPIC}, new int[]{1275, 1230, 1185, 1140, 1095, 1050, 1035, 1020, 1005, 990, 975, 885, 795, TypedValues.Transition.TYPE_INTERPOLATOR, 615, 525, 515, 505, 495, 485, 475, 460, 445, 430, 415, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 405, 410, 415, 420, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, 455, 485, 515, 545, 575, TypedValues.Motion.TYPE_QUANTIZE_MOTIONSTEPS, 645, 680, 715, 750, 780, 810, 840, 870, TypedValues.Custom.TYPE_INT, 980, 1060, 1140, CarConfig.KEYCODE_NEW_LONG_LEFT_UP, 1300}, new int[]{1350, 1300, 1250, 1200, AssembleInfo.STATE_CANCELLED, AssembleInfo.STATE_COMPLETE, 1080, 1060, NotificationHelper.SCENE_CAPSULE_SLEEP_OVER_TIME, 1020, AssembleInfo.STATE_COMPLETE, 910, 820, 730, 640, 550, 540, 530, IInputController.KEYCODE_KNOB_VOL_UP, 510, 500, 480, 460, 440, 420, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 500, 550, 600, 650, TypedValues.Transition.TYPE_DURATION, 730, 760, 790, 820, 850, 920, 990, 1060, 1130, 1200}, new int[]{1375, 1335, 1295, 1255, 1215, 1175, AssembleInfo.STATE_CANCELLED, 1125, AssembleInfo.STATE_COMPLETE, 1075, 1050, 955, 860, 765, 670, 575, 565, 555, 545, 535, 525, 505, 485, 465, 445, TypedValues.Cycle.TYPE_WAVE_PHASE, 420, 415, 410, 405, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 445, 440, 435, 430, TypedValues.Cycle.TYPE_WAVE_PHASE, 475, 525, 575, 625, 675, TypedValues.Transition.TYPE_DURATION, 725, 750, 775, 800, 860, 920, 980, NotificationHelper.SCENE_CAPSULE_SLEEP_OVER_TIME, 1000}, new int[]{IpcConfig.DeviceCommunicationConfig.SEND_CAR_CONTROL_TOPIC, 1370, 1340, 1310, 1280, 1250, CarConfig.KEYCODE_NEW_LONG_LEFT_UP, 1190, 1160, 1130, AssembleInfo.STATE_COMPLETE, 1000, TypedValues.Custom.TYPE_INT, 800, TypedValues.Transition.TYPE_DURATION, 600, 590, 580, 570, 560, 550, 530, 510, 490, 470, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 450, 500, 550, 600, 650, 670, 690, 710, 730, 750, 800, 850, TypedValues.Custom.TYPE_INT, 950, 1000}, new int[]{1425, 1395, 1365, 1335, IpcConfig.OTAConfig.OTA_STATUS_REQUEST, 1275, 1245, 1215, 1185, 1155, 1125, 1025, 925, 825, 725, 625, 613, 600, 588, 575, 563, 545, 528, 510, UnixStat.DEFAULT_DIR_PERM, 475, 465, 455, 445, 435, TypedValues.Cycle.TYPE_WAVE_PHASE, 420, 415, 410, 405, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 430, 460, 490, IInputController.KEYCODE_KNOB_VOL_UP, 550, 570, 590, TypedValues.Motion.TYPE_QUANTIZE_MOTIONSTEPS, 630, 650, TypedValues.Transition.TYPE_DURATION, 750, 800, 850, TypedValues.Custom.TYPE_INT}, new int[]{1450, 1420, 1390, 1360, 1330, 1300, 1270, 1240, 1210, 1180, AssembleInfo.STATE_CANCELLED, 1050, 950, 850, 750, 650, 635, 620, TypedValues.Motion.TYPE_ANIMATE_RELATIVE_TO, 590, 575, 560, 545, 530, 515, 500, 490, 480, 470, 460, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 470, 490, 510, 530, 550, 600, 650, TypedValues.Transition.TYPE_DURATION, 750, 800}, new int[]{1475, 1445, 1415, 1385, 1355, 1325, 1295, 1265, 1235, 1205, 1175, 1075, 975, 875, 775, 675, 698, 720, 743, 765, 588, 575, 563, 550, 538, 525, 515, 505, 495, 485, 475, 465, 455, 445, 435, TypedValues.Cycle.TYPE_WAVE_PHASE, 430, 435, 440, 445, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 409, 418, 427, 436, 445, 456, 467, 478, 489, 500, 545, 590, 635, 680, 725}, new int[]{1500, 1470, 1440, 1410, 1380, 1350, 1320, 1290, 1260, 1230, 1200, AssembleInfo.STATE_COMPLETE, 1000, TypedValues.Custom.TYPE_INT, 800, TypedValues.Transition.TYPE_DURATION, 680, 660, 640, 620, 600, 590, 580, 570, 560, 550, 540, 530, IInputController.KEYCODE_KNOB_VOL_UP, 510, 500, 490, 480, 470, 460, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 408, TypedValues.Cycle.TYPE_PATH_ROTATE, TypedValues.Cycle.TYPE_WAVE_OFFSET, 432, 440, 442, 444, 446, 448, 450, 490, 530, 570, TypedValues.Motion.TYPE_QUANTIZE_MOTIONSTEPS, 650}, new int[]{1525, 1495, 1465, 1435, 1405, 1375, 1345, 1315, 1285, 1255, CarConfig.KEYCODE_NEW_LONG_RIGHT_DOWN, 1125, 1025, 925, 825, 725, 710, 695, 680, 665, 650, 635, 620, TypedValues.Motion.TYPE_ANIMATE_RELATIVE_TO, 590, 575, 565, 555, 545, 535, 525, 515, 505, 495, 485, 475, 470, 465, 460, 455, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 445, 440, 435, 430, TypedValues.Cycle.TYPE_WAVE_PHASE, 428, 431, 434, 437, 440, 437, 434, 431, 428, TypedValues.Cycle.TYPE_WAVE_PHASE, 465, 505, 545, 585, 625}, new int[]{1550, 1520, 1490, 1460, 1430, IpcConfig.DeviceCommunicationConfig.SEND_CAR_CONTROL_TOPIC, 1370, 1340, 1310, 1280, 1250, AssembleInfo.STATE_CANCELLED, 1050, 950, 850, 750, 740, 730, 720, 710, TypedValues.Transition.TYPE_DURATION, 680, 660, 640, 620, 600, 590, 580, 570, 560, 550, 540, 530, IInputController.KEYCODE_KNOB_VOL_UP, 510, 500, 490, 480, 470, 460, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 448, 446, 444, 442, 440, 432, TypedValues.Cycle.TYPE_WAVE_OFFSET, TypedValues.Cycle.TYPE_PATH_ROTATE, 408, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 440, 480, IInputController.KEYCODE_KNOB_VOL_UP, 560, 600}, new int[]{1600, 1570, 1540, 1510, 1480, 1450, 1415, 1380, 1345, 1310, 1275, 1175, 1075, 975, 875, 775, 765, 755, 745, 735, 725, TypedValues.Transition.TYPE_INTERPOLATOR, 685, 665, 645, 625, 615, TypedValues.Motion.TYPE_ANIMATE_RELATIVE_TO, 595, 585, 575, 565, 555, 545, 535, 525, 515, 505, 495, 485, 475, 470, 465, 460, 455, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 448, 446, 444, 442, 440, 432, TypedValues.Cycle.TYPE_WAVE_OFFSET, TypedValues.Cycle.TYPE_PATH_ROTATE, 408, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 435, 470, 505, 540, 575}, new int[]{1650, 1620, 1590, 1560, 1530, 1500, 1460, 1420, 1380, 1340, 1300, 1200, AssembleInfo.STATE_COMPLETE, 1000, TypedValues.Custom.TYPE_INT, 800, 790, 780, 770, 760, 750, 730, 710, 690, 670, 650, 640, 630, 620, TypedValues.Motion.TYPE_QUANTIZE_MOTIONSTEPS, 600, 590, 580, 570, 560, 550, 540, 530, IInputController.KEYCODE_KNOB_VOL_UP, 510, 500, 490, 480, 470, 460, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 448, 446, 444, 442, 440, 432, TypedValues.Cycle.TYPE_WAVE_OFFSET, TypedValues.Cycle.TYPE_PATH_ROTATE, 408, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 430, 460, 490, IInputController.KEYCODE_KNOB_VOL_UP, 550}, new int[]{1750, 1720, 1690, 1660, 1630, 1600, 1560, 1520, 1480, 1440, IpcConfig.DeviceCommunicationConfig.SEND_CAR_CONTROL_TOPIC, 1300, 1200, AssembleInfo.STATE_COMPLETE, 1000, TypedValues.Custom.TYPE_INT, 885, 870, 850, 840, 825, 805, 785, 765, 745, 725, 710, 695, 680, 665, 650, 635, 620, TypedValues.Motion.TYPE_ANIMATE_RELATIVE_TO, 590, 575, 565, 555, 545, 535, 525, 515, 505, 495, 485, 475, 475, 475, 475, 475, 475, 475, 475, 475, 475, 475, 468, 461, 454, 447, 440, 436, 432, 428, TypedValues.Cycle.TYPE_WAVE_OFFSET, 420, 441, 462, 483, 504, 525}, new int[]{1850, 1820, 1790, 1760, 1730, 1700, 1660, 1620, 1580, 1540, 1500, IpcConfig.DeviceCommunicationConfig.SEND_CAR_CONTROL_TOPIC, 1300, 1200, AssembleInfo.STATE_COMPLETE, 1000, 980, 960, 940, 920, TypedValues.Custom.TYPE_INT, 880, 860, 840, 820, 800, 780, 760, 740, 720, TypedValues.Transition.TYPE_DURATION, 680, 660, 640, 620, 600, 590, 580, 570, 560, 550, 540, 530, IInputController.KEYCODE_KNOB_VOL_UP, 510, 500, 500, 500, 500, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 500, 500, 500, 500, 500, 500, 488, 476, 464, 452, 440, 440, 440, 440, 440, 440, 452, 464, 476, 488, 500}};
    private static final int[][] OUTER_TABLE = {new int[]{3500, 3300, 3100, 2900, 2700, 2500, 2400, 2300, 2200, 2100, 2000, 1720, 1440, 1160, 880, 600, 560, IInputController.KEYCODE_KNOB_VOL_UP, 480, 440, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 480, 560, 640, 720, 800, 840, 880, 920, 960, 1000, 1140, 1280, 1420, 1560, 1700, 1860, 2020, 2180, 2340, 2500}, new int[]{4000, 3750, 3500, 3250, 3000, 2750, 2650, 2550, 2450, 2350, 2250, 1925, 1600, 1275, 950, 625, 585, 545, 505, 465, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, 430, 435, 440, 445, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 405, 410, 415, 420, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, TypedValues.Cycle.TYPE_WAVE_PHASE, 485, 545, TypedValues.Motion.TYPE_ANIMATE_RELATIVE_TO, 665, 725, 765, 805, 845, 885, 925, 1050, 1175, 1300, 1425, 1550, 1710, 1870, 2030, 2190, 2350}, new int[]{4500, 4200, 3900, 3600, 3300, 3000, 2900, 2800, 2700, 2600, 2500, 2130, 1760, 1390, 1020, 650, TypedValues.Motion.TYPE_QUANTIZE_MOTIONSTEPS, 570, 530, 490, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 450, 450, 450, 450, 450, 490, 530, 570, TypedValues.Motion.TYPE_QUANTIZE_MOTIONSTEPS, 650, 690, 730, 770, 810, 850, 960, 1070, 1180, 1290, IpcConfig.DeviceCommunicationConfig.SEND_CAR_CONTROL_TOPIC, 1560, 1720, 1880, 2040, 2200}, new int[]{4850, 4500, 4150, 3800, 3450, 3100, 3000, 2900, 2800, 2700, 2600, 2225, 1850, 1475, AssembleInfo.STATE_COMPLETE, 725, 675, 625, 575, 525, 475, 475, 475, 475, 475, 475, 475, 475, 475, 475, 475, 460, 445, 430, 415, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 450, 450, 450, 450, 450, 480, 510, 540, 570, 600, 640, 680, 720, 760, 800, TypedValues.Custom.TYPE_INT, 1000, AssembleInfo.STATE_COMPLETE, 1200, 1300, 1450, 1600, 1750, 1900, 2050}, new int[]{5200, 4800, 4400, 4000, 3600, 3200, 3100, 3000, 2900, 2800, 2700, 2320, 1940, 1560, 1180, 800, 740, 680, 620, 560, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 480, 460, 440, 420, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 450, 450, 450, 450, 450, 470, 490, 510, 530, 550, 590, 630, 670, 710, 750, 840, 930, 1020, 1110, 1200, 1340, 1480, 1620, 1760, 1900}, new int[]{5200, 4830, 4460, 4090, 3720, 3350, 3250, 3150, 3050, 2950, 2850, 2460, 2070, 1680, 1290, TypedValues.Custom.TYPE_INT, 830, 760, 690, 620, 550, 545, 540, 535, 530, 525, 525, 525, 525, 525, 525, 510, 495, 480, 465, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 450, 450, 450, 450, 450, 460, 470, 480, 490, 500, 540, 580, 620, 660, TypedValues.Transition.TYPE_DURATION, 780, 860, 940, 1020, AssembleInfo.STATE_COMPLETE, 1240, 1380, 1520, 1660, 1800}, new int[]{5200, 4860, 4520, 4180, 3840, 3500, 3400, 3300, 3200, 3100, 3000, 2600, 2200, 1800, IpcConfig.DeviceCommunicationConfig.SEND_CAR_CONTROL_TOPIC, 1000, 920, 840, 760, 680, 600, 590, 580, 570, 560, 550, 550, 550, 550, 550, 550, 540, 530, IInputController.KEYCODE_KNOB_VOL_UP, 510, 500, 480, 460, 440, 420, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 410, 420, 430, 440, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 490, 530, 570, TypedValues.Motion.TYPE_QUANTIZE_MOTIONSTEPS, 650, 720, 790, 860, 930, 1000, 1140, 1280, 1420, 1560, 1700}, new int[]{5600, 5230, 4860, 4490, 4120, 3750, 3650, 3550, 3450, 3350, 3250, 2840, 2430, 2020, 1610, 1200, AssembleInfo.STATE_COMPLETE, 1000, TypedValues.Custom.TYPE_INT, 800, TypedValues.Transition.TYPE_DURATION, 685, 670, 655, 640, 625, 625, 625, 625, 625, 625, 615, TypedValues.Motion.TYPE_ANIMATE_RELATIVE_TO, 595, 585, 575, 550, 525, 500, 475, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 445, 440, 435, 430, TypedValues.Cycle.TYPE_WAVE_PHASE, 465, 505, 545, 585, 625, 690, 755, 820, 885, 950, 1085, CarConfig.KEYCODE_NEW_LONG_LEFT_UP, 1355, 1490, 1625}, new int[]{HttpInitEventListener.CODE_NOT_INDIV, 5600, 5200, 4800, 4400, 4000, 3900, 3800, 3700, 3600, 3500, 3080, 2660, 2240, 1820, IpcConfig.DeviceCommunicationConfig.SEND_CAR_CONTROL_TOPIC, 1280, 1160, NotificationHelper.SCENE_CAPSULE_SLEEP_OVER_TIME, 920, 800, 780, 760, 740, 720, TypedValues.Transition.TYPE_DURATION, TypedValues.Transition.TYPE_DURATION, TypedValues.Transition.TYPE_DURATION, TypedValues.Transition.TYPE_DURATION, TypedValues.Transition.TYPE_DURATION, TypedValues.Transition.TYPE_DURATION, 690, 680, 670, 660, 650, 620, 590, 560, 530, 500, 490, 480, 470, 460, 450, 450, 450, 450, 450, 450, 440, 430, 420, 410, AssembleRequest.ASSEMBLE_ACTION_CANCEL, 440, 480, IInputController.KEYCODE_KNOB_VOL_UP, 560, 600, 660, 720, 780, 840, TypedValues.Custom.TYPE_INT, 1030, 1160, 1290, 1420, 1550}, new int[]{HttpInitEventListener.CODE_NOT_INDIV, 5650, 5300, 4950, 4600, 4250, 4150, 4050, 3950, 3850, 3750, 3310, 2870, 2430, 1990, 1550, 1430, 1290, 1160, 1030, TypedValues.Custom.TYPE_INT, 880, 860, 840, 820, 800, 785, 770, 755, 740, 725, 715, TypedValues.Transition.TYPE_INTERPOLATOR, 695, 685, 675, 640, TypedValues.Motion.TYPE_ANIMATE_RELATIVE_TO, 570, 535, 500, 490, 480, 470, 460, 450, 450, 450, 450, 450, 450, 444, 438, 432, 426, 420, 451, 483, 513, 544, 575, 630, 685, 740, 795, 850, 970, 1090, 1210, 1300, 1450}, new int[]{HttpInitEventListener.CODE_NOT_INDIV, 5700, 5400, 5100, 4800, 4500, 4400, 4300, 4200, 4100, 4000, 3540, 3080, 2620, 2160, 1700, 1560, 1420, 1280, 1140, 1000, 980, 960, 940, 920, TypedValues.Custom.TYPE_INT, 870, 840, 810, 780, 750, 740, 730, 720, 710, TypedValues.Transition.TYPE_DURATION, 660, 620, 580, 540, 500, 490, 480, 470, 460, 450, 450, 450, 450, 450, 450, 448, 446, 444, 442, 440, 462, 484, TypedValues.Position.TYPE_PERCENT_X, 528, 550, 600, 650, TypedValues.Transition.TYPE_DURATION, 750, 800, 910, 1020, 1130, 1240, 1350}, new int[]{HttpInitEventListener.CODE_NOT_INDIV, 5750, 5500, 5250, 5000, 4750, 4650, 4550, 4450, 4350, 4250, 3770, 3290, 2810, 2330, 1850, 1710, 1570, 1430, 1290, AssembleInfo.STATE_CANCELLED, 1130, AssembleInfo.STATE_COMPLETE, 1090, 1070, 1050, 995, 940, 885, 830, 775, 765, 755, 745, 735, 725, 690, 655, 620, 585, 550, 530, 510, 490, 470, 450, 450, 450, 450, 450, 450, 448, 446, 444, 442, 440, 457, 474, 491, 508, 525, 565, TypedValues.Motion.TYPE_ANIMATE_RELATIVE_TO, 645, 685, 725, 830, 935, NotificationHelper.SCENE_CAPSULE_SLEEP_OVER_TIME, 1145, 1250}, new int[]{HttpInitEventListener.CODE_NOT_INDIV, 5800, 5600, 5400, 5200, 5000, 4900, 4800, 4700, 4600, 4500, 4000, 3500, 3000, 2500, 2000, 1860, 1720, 1580, 1440, 1300, 1280, 1260, 1240, CarConfig.KEYCODE_NEW_LONG_LEFT_UP, 1200, 1120, NotificationHelper.SCENE_CAPSULE_SLEEP_OVER_TIME, 960, 880, 800, 790, 780, 770, 760, 750, 720, 690, 660, 630, 600, 570, 540, 510, 480, 450, 450, 450, 450, 450, 450, 448, 446, 444, 442, 440, 452, 464, 476, 488, 500, 530, 560, 590, 620, 650, 750, 850, 950, 1050, AssembleInfo.STATE_CANCELLED}, new int[]{HttpInitEventListener.CODE_NOT_INDIV, 5900, 5800, 5700, 5600, 5500, 5400, 5300, 5200, 5100, 5000, 4450, 3900, 3350, 2800, 2250, 2110, 1970, 1830, 1690, 1550, 1510, 1470, 1430, 1390, 1350, 1250, AssembleInfo.STATE_CANCELLED, 1050, 950, 850, 830, 810, 790, 770, 750, 725, TypedValues.Transition.TYPE_DURATION, 675, 650, 625, TypedValues.Motion.TYPE_ANIMATE_RELATIVE_TO, 585, 565, 545, 525, IInputController.KEYCODE_KNOB_VOL_UP, 515, 510, 505, 500, 496, 492, 488, 484, 480, 479, 478, 477, 476, 475, 495, 515, 535, 555, 575, 675, 775, 875, 975, 1075}, new int[]{HttpInitEventListener.CODE_NOT_INDIV, HttpInitEventListener.CODE_NOT_INDIV, HttpInitEventListener.CODE_NOT_INDIV, HttpInitEventListener.CODE_NOT_INDIV, HttpInitEventListener.CODE_NOT_INDIV, HttpInitEventListener.CODE_NOT_INDIV, 5900, 5800, 5700, 5600, 5500, 4900, 4300, 3700, 3100, 2500, 2360, 2220, 2080, 1940, 1800, 1740, 1680, 1620, 1560, 1500, 1380, 1260, 1140, 1020, TypedValues.Custom.TYPE_INT, 870, 840, 810, 780, 750, 730, 710, 690, 670, 650, 640, 630, 620, TypedValues.Motion.TYPE_QUANTIZE_MOTIONSTEPS, 600, 590, 580, 570, 560, 550, 544, 538, 532, IInputController.KEYCODE_KNOB_USB_MUSIC, IInputController.KEYCODE_KNOB_VOL_UP, TypedValues.Position.TYPE_PERCENT_X, 492, 478, 464, 450, 460, 470, 480, 490, 500, 600, TypedValues.Transition.TYPE_DURATION, 800, TypedValues.Custom.TYPE_INT, 1000}};

    @Override // com.xiaopeng.carcontrol.viewmodel.endurance.HvacConsumptionTable
    public int getHvacStableConsumption(boolean isInnerCircle, float targetTemp, float outTemp) {
        int checkTargetTempIdx = checkTargetTempIdx(targetTemp);
        int checkOuterTempIdx = checkOuterTempIdx(outTemp);
        try {
            int i = isInnerCircle ? INNER_TABLE[checkTargetTempIdx][checkOuterTempIdx] : OUTER_TABLE[checkTargetTempIdx][checkOuterTempIdx];
            LogUtils.i("HvacConsumptionTable", "Hvac stable consumption: " + i, false);
            return i;
        } catch (IndexOutOfBoundsException unused) {
            LogUtils.w("HvacConsumptionTable", "getHvacStableConsumption failed due to idx out of bound", false);
            return 0;
        }
    }

    private int checkTargetTempIdx(float targetTemp) {
        int ceil;
        if (targetTemp < 18.0f) {
            ceil = 0;
        } else {
            ceil = targetTemp > 32.0f ? 14 : (((int) Math.ceil(targetTemp)) - 18) / 1;
        }
        LogUtils.i("HvacConsumptionTable", "checkTargetTempIdx: " + ceil, false);
        return ceil;
    }

    private int checkOuterTempIdx(float outTemp) {
        int ceil;
        if (outTemp < -25.0f) {
            ceil = 0;
        } else {
            ceil = outTemp > 45.0f ? 70 : (((int) Math.ceil(outTemp)) + 25) / 1;
        }
        LogUtils.i("HvacConsumptionTable", "checkOuterTempIdx: " + ceil, false);
        return ceil;
    }
}
