package com.example.abcwebview.service;

import com.example.abcwebview.utils.LogUtils;
import com.example.abcwebview.widget.controller.FormController;
import com.example.abcwebview.widget.controller.FormControllerManager;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimerAbility extends Ability {
    private static final String TAG = TimerAbility.class.getSimpleName();
    private static final long DELAY = 3000;
    private static final long PERIOD =1000;
    private Timer timer;
    private FormControllerManager formControllerManager;
    @Override
    public void onStart(Intent intent) {
        formControllerManager = FormControllerManager.getInstance(this);
        startTask();
        super.onStart(intent);
    }

    private void startTask() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateForms();
            }
        }, DELAY, PERIOD);
    }

    private void updateForms(){
        List<Long> formIdList = formControllerManager.getAllFormIdFromMap();
        LogUtils.info(TAG, "=======updateForms:" + formIdList.size());
        for(Long formId : formIdList){
            FormController controller = formControllerManager.getController(formId);
            controller.updateFormData(formId);
        }
    }

    @Override
    public void onStop() {
        LogUtils.error(TAG, "onStop=====");
        super.onStop();
        timer.cancel();
        timer = null;
    }



}