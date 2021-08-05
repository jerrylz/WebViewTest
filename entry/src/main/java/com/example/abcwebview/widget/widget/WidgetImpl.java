package com.example.abcwebview.widget.widget;

import com.example.abcwebview.MainAbility;
import com.example.abcwebview.utils.LogUtils;
import com.example.abcwebview.widget.controller.FormController;
import ohos.aafwk.ability.*;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.utils.zson.ZSONObject;

/**
 * The main class related to immersion service widget.
 * When adding an immersion card in launcher, it will display a graphic and text interface in form widget.
 * When clicking the immersion card, it will route to {@link WidgetSlice} and show a operation page used to
 * display data of the immersion card.
 * immersion card supports running in phone/tablet/wearable devices.
 */
public class WidgetImpl extends FormController {

    private static final String TAG = WidgetImpl.class.getSimpleName();
    private int count = 0;
    public WidgetImpl(Context context, String formName, Integer dimension) {
        super(context, formName, dimension);
    }

    @Override
    public ProviderFormInfo bindFormData() {
        // 封装数据
        ZSONObject zsonObject = new ZSONObject();
        zsonObject.put("title", "农行-" + count);
        ProviderFormInfo providerFormInfo = new ProviderFormInfo();
        providerFormInfo.setJsBindingData(new FormBindingData(zsonObject));
        return providerFormInfo;
    }

    @Override
    public void updateFormData(long formId, Object... vars) {
        LogUtils.info(TAG, "=====执行updateFormData======");
        // 封装数据
        ZSONObject zsonObject = new ZSONObject();
        zsonObject.put("title", "农行-" + count++);

        FormBindingData formBindingData = new FormBindingData(zsonObject);
        Ability context = (Ability) this.context;
        try {
            context.updateForm(formId, formBindingData);
        } catch (FormException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTriggerFormEvent(long formId, String message) {
    }

    @Override
    public Class<? extends AbilitySlice> getRoutePageSlice(Intent intent) {
        return WidgetSlice.class;
    }
}
