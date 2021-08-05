package com.example.abcwebview;

import com.example.abcwebview.service.TimerAbility;
import com.example.abcwebview.utils.LogUtils;
import com.example.abcwebview.widget.controller.FormController;
import com.example.abcwebview.widget.controller.FormControllerManager;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.ace.ability.AceAbility;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbility extends AceAbility {
    private static final int ON_CREATE_EVENT = 0;
    private static final int ON_DELETE_EVENT = 1;
    public static final int DEFAULT_DIMENSION_2X2 = 2;
    private static final int INVALID_FORM_ID = -1;
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, MainAbility.class.getName());
    private String topWidgetSlice;
    private FormUpdateEventHandler handler = new FormUpdateEventHandler(EventRunner.create());

    @Override
    public void onStart(Intent intent) {
        HiLog.info(TAG, "onStart=====");
        super.onStart(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected ProviderFormInfo onCreateForm(Intent intent) {
        HiLog.info(TAG, "onCreateForm");
        FormControllerManager formControllerManager =  FormControllerManager.getInstance(this);
        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        String formName = intent.getStringParam(AbilitySlice.PARAM_FORM_NAME_KEY);
        int dimension = intent.getIntParam(AbilitySlice.PARAM_FORM_DIMENSION_KEY, DEFAULT_DIMENSION_2X2);
        HiLog.info(TAG, "onCreateForm: formId=" + formId + ",formName=" + formName);
        FormController formController = formControllerManager.getController(formId);
        formController = (formController == null) ? formControllerManager.createFormController(formId,
                formName, dimension) : formController;
        if (formController == null) {
            HiLog.error(TAG, "Get null controller. formId: " + formId + ", formName: " + formName);
            return null;
        }
        handler.sendEvent(ON_CREATE_EVENT);
        return formController.bindFormData();
    }

    @Override
    protected void onUpdateForm(long formId) {
        HiLog.info(TAG, "onUpdateForm");
        FormControllerManager formControllerManager =  FormControllerManager.getInstance(this);
        super.onUpdateForm(formId);
        FormController formController = formControllerManager.getController(formId);
        formController.updateFormData(formId);
    }

    @Override
    protected void onDeleteForm(long formId) {
        handler.sendEvent(ON_DELETE_EVENT);
        HiLog.info(TAG, "onDeleteForm: formId=" + formId);
        FormControllerManager formControllerManager =  FormControllerManager.getInstance(this);
        super.onDeleteForm(formId);
        formControllerManager.deleteFormController(formId);
    }

    @Override
    protected void onTriggerFormEvent(long formId, String message) {
        HiLog.info(TAG, "onTriggerFormEvent: " + message);
        FormControllerManager formControllerManager =  FormControllerManager.getInstance(this);
        super.onTriggerFormEvent(formId, message);
        FormController formController = formControllerManager.getController(formId);
        formController.onTriggerFormEvent(formId, message);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intentFromWidget(intent)) { // Only response to it when starting from a service widget.
            String newWidgetSlice = getRoutePageSlice(intent);
            if (topWidgetSlice == null || !topWidgetSlice.equals(newWidgetSlice)) {
                topWidgetSlice = newWidgetSlice;
                restart();
            }
        }
    }

    private boolean intentFromWidget(Intent intent) {
        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        return formId != INVALID_FORM_ID;
    }

    private String getRoutePageSlice(Intent intent) {
        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        if (formId == INVALID_FORM_ID) {
            return null;
        }
        FormControllerManager formControllerManager =  FormControllerManager.getInstance(this);
        FormController formController = formControllerManager.getController(formId);
        if (formController == null) {
            return null;
        }
        Class<? extends AbilitySlice> clazz = formController.getRoutePageSlice(intent);
        if (clazz == null) {
            return null;
        }
        return clazz.getName();
    }
    /**
     * 启动service
     */
    private void startTimerService() {
        Intent serviceIntent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(getBundleName())
                .withAbilityName(TimerAbility.class)
                .build();
        serviceIntent.setOperation(operation);
        startAbility(serviceIntent);
    }

    private void stopTimerService(){
        FormControllerManager instance = FormControllerManager.getInstance(this);
        if(!instance.hasMoreForm()){
            Intent serviceIntent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName(getBundleName())
                    .withAbilityName(TimerAbility.class)
                    .build();
            serviceIntent.setOperation(operation);
            stopAbility(serviceIntent);
        }

    }


    class FormUpdateEventHandler extends EventHandler {

        public FormUpdateEventHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            int eventId = event.eventId;
            switch (eventId){
                case ON_CREATE_EVENT:
                    HiLog.info(TAG, "===创建卡片====" );
                    startTimerService();
                    break;
                case ON_DELETE_EVENT:
                    HiLog.info(TAG, "===删除卡片===" );
                    stopTimerService();
                    break;
                default:
                    break;
            }
        }
    }
}
