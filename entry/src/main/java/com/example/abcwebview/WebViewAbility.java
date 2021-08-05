package com.example.abcwebview;

import com.example.abcwebview.slice.WebViewAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class WebViewAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(WebViewAbilitySlice.class.getName());
    }
}
