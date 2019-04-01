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

import com.aidl.myapplication.BindLocalCallBack;
import com.aidl.myapplication.bean.StudentBean;


public class LocalService extends Service {

    public static final int SERVICE_DEFAULT = 0;
    public static final int SERVICE_BINDING = 1;
    public static final int SERVICE_STARTING = 2;
    public static final int SERVICE_DESTROY = 3;

    private int mServiceState = SERVICE_DEFAULT;

    private BindLocalCallBack bindLocalCallBack;

    private String name;
    private String age;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "TAG VideoLiveS onCreate");
        mServiceState = SERVICE_DEFAULT;

    }


    class BindInterface extends com.aidl.myapplication.BindInterface.Stub {

        @Override
        public void registerCallback(BindLocalCallBack callback) throws RemoteException {
            bindLocalCallBack = callback;
        }

        @Override
        public void unRegisterCallback() throws RemoteException {
            bindLocalCallBack = null;
        }

        @Override
        public void sendMessage(String name) throws RemoteException {
            Toast mToast = Toast.makeText(LocalService.this, name, Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG", "TAG  onBind");
        parseBundle(intent.getExtras());
        mServiceState = SERVICE_BINDING;
        return new BindInterface().asBinder();
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
        name = bundle.getString("name", "");
        age = bundle.getString("age", "");
        Log.e("TAG", "TAG  parseBundle name  " + name);
        Log.e("TAG", "TAG  parseBundle age   " + age);
        callBack();
    }


    private void callBack() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "TAG  onRebind");
                if (bindLocalCallBack != null) {
                    Log.e("TAG", "TAG  onRebind");
                    StudentBean mStudentBean = new StudentBean();
                    mStudentBean.setName("张飞打凹槽");
                    mStudentBean.setAge("11314179");
                    try {
                        bindLocalCallBack.studentCallBack(mStudentBean);
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
        bindLocalCallBack = null;
    }
}
