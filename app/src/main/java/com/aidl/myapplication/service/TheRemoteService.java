package com.aidl.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.aidl.myapplication.TheRemoteCallBack;
import com.aidl.myapplication.bean.FruitBean;
import com.aidl.myapplication.bean.StudentBean;

public class TheRemoteService extends Service {

    public static final int SERVICE_DEFAULT = 0;
    public static final int SERVICE_BINDING = 1;
    public static final int SERVICE_STARTING = 2;
    public static final int SERVICE_DESTROY = 3;

    private int mServiceState = SERVICE_DEFAULT;

    private TheRemoteCallBack theRemoteCallBack;
    private String fruitName;
    private String fruitAge;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "TAG VideoLiveS onCreate");
        mServiceState = SERVICE_DEFAULT;

    }


    class TheRemoteInterface extends com.aidl.myapplication.TheRemoteInterface.Stub {

        @Override
        public void registerCallback(TheRemoteCallBack callback) throws RemoteException {
            theRemoteCallBack = callback;
        }

        @Override
        public void unRegisterCallback() throws RemoteException {
            theRemoteCallBack = null;
        }

        @Override
        public void sendFruitMessage(String name) throws RemoteException {
            Log.e("sendFruitMessage", "sendFruitMessage name " + name);
//            Toast mToast = Toast.makeText(TheRemoteService.this, name, Toast.LENGTH_LONG);
//            mToast.setGravity(Gravity.CENTER, 0, 0);
//            mToast.show();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG", "TAG  onBind");
        parseBundle(intent.getExtras());
        mServiceState = SERVICE_BINDING;
        return new TheRemoteInterface().asBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.e("TAG", "TAG  onRebind");
        parseBundle(intent.getExtras());
        mServiceState = SERVICE_BINDING;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG", "TAG  onStartCommand");
        parseBundle(intent.getExtras());
        if (mServiceState != SERVICE_BINDING) {
            mServiceState = SERVICE_STARTING;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void parseBundle(Bundle bundle) {
        Log.e("TAG", "TAG  parseBundle");
        if (null == bundle) {
            return;
        }
        fruitName = bundle.getString("Fruitname", "");
        fruitAge = bundle.getString("Fruitage", "");
        Log.e("TAG", "TAG  parseBundle fruitName  " + fruitName);
        Log.e("TAG", "TAG  parseBundle fruitAge   " + fruitAge);
        callBack();
    }


    private void callBack() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "TAG  onRebind");
                if (theRemoteCallBack != null) {
                    Log.e("TAG", "TAG  onRebind");
                    FruitBean mFruitBean = new FruitBean();
                    mFruitBean.setFruit1("嚣张的苹果");
                    mFruitBean.setFruit2("5月份");
                    try {
                        theRemoteCallBack.fruitCallBack(mFruitBean);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, 3000);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("TAG", "TAG  onUnbind");
        mServiceState = SERVICE_DEFAULT;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "TAG VideoLiveS onDestroy");
        mServiceState = SERVICE_DESTROY;
        theRemoteCallBack = null;
    }
}
