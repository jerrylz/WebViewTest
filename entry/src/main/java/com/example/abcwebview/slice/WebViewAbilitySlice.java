package com.example.abcwebview.slice;

import com.example.abcwebview.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.webengine.WebView;

public class WebViewAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_web_view);
        WebView webView = (WebView) findComponentById(ResourceTable.Id_webview);
        final String url = "https://webank.abchina.com/paas-prod-mmsp-xs//webank/html/customer/customerIndex.html?chn=wechat&storeId=109739&employeeOrder=1"; // EXAMPLE_URL由开发者自定义
        webView.getWebConfig().setJavaScriptPermit(true);// 启动JS
        webView.load(url);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
