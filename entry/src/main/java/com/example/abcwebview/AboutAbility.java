package com.example.abcwebview;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.rpc.*;

import java.io.OutputStream;
import java.net.Socket;

public class AboutAbility extends Ability {
    private static final int OPEN_WEBVIEW = 1001;
    private AboutAbility.AboutRemote aboutRemote;

    @Override
    public IRemoteObject onConnect(Intent intent) {
        Context context = getContext();
        aboutRemote = new AboutAbility.AboutRemote(context);
        return aboutRemote.asObject();
    }

    class AboutRemote extends RemoteObject implements IRemoteBroker {

        private Context context;
        private Socket socket = null;
        private OutputStream os = null;

        public AboutRemote(Context context) {
            super("AboutRemote");
            this.context = context;
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) throws RemoteException {
            switch (code) {
                case OPEN_WEBVIEW:{
                    System.out.println("开启webview");

                    //主要代码

                    Intent intent = new Intent();
                    Operation operation = new Intent.OperationBuilder().withBundleName(getBundleName())
                            .withAbilityName(WebViewAbility.class.getName()).build();
                    intent.setOperation(operation);
                    startAbility(intent);

                    //主要代码

                    break;
                }
                default: {
                    reply.writeString("service not defined");
                    return false;
                }
            }
            return true;
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }
    }
}