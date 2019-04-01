package com.aidl.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aidl.myapplication.bean.FruitBean;
import com.aidl.myapplication.bean.StudentBean;
import com.aidl.myapplication.service.LocalService;
import com.aidl.myapplication.service.TheRemoteService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.localButton).setOnClickListener(this);
        findViewById(R.id.localButton1).setOnClickListener(this);

        findViewById(R.id.localButton2).setOnClickListener(this);

        findViewById(R.id.theRemoteButton).setOnClickListener(this);
        findViewById(R.id.theRemoteButton1).setOnClickListener(this);

        findViewById(R.id.theRemoteButton2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 本地服务
             */
            case R.id.localButton:
                bindLocalService(this, serviceLocalConnection);
                break;
            case R.id.localButton1:
                unBindLocalService(this, serviceLocalConnection);
                break;
            case R.id.localButton2:
                sendMessage();
                break;
            /**
             * 远程服务
             */
            case R.id.theRemoteButton:
                bindTheRemoteService(this, serviceTheRemoteConnection);
                break;
            case R.id.theRemoteButton1:
                unBindTheRemoteService(this, serviceTheRemoteConnection);
                break;
            case R.id.theRemoteButton2:
                sendFruitMessage();
                break;
        }
    }


    /**************************************** 本地服务  start ********************************************/
    private BindInterface bindInterface;

    //创建ServiceConnection的匿名类
    ServiceConnection serviceLocalConnection = new ServiceConnection() {
        //重写onServiceConnected()方法和onServiceDisconnected()方法


        //在Activity与Service建立关联时调用
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //使用BindInterface.Stub.asInterface()方法将传入的IBinder对象传换成了BindInterface对象
            bindInterface = BindInterface.Stub.asInterface(service);
            try {
                //通过该对象调用在bindInterface文件中定义的接口方法,从而实现跨进程通信
                bindInterface.registerCallback(new BindLocalCallBack());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        //在Activity与Service解除关联的时候调用
        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                if (null != bindInterface) {
                    bindInterface.unRegisterCallback();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            bindInterface = null;
        }
    };


    private void showUserInfo(String name, String age) {
        ((TextView) findViewById(R.id.tvName)).setText(MainActivity.this.getString(R.string.app_user_name, name));
        ((TextView) findViewById(R.id.tvAge)).setText(MainActivity.this.getString(R.string.app_user_age, age));
    }

    private void sendMessage() {
        if (bindInterface != null) {
            try {
                bindInterface.sendMessage("我是activity传到service的数据");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    class BindLocalCallBack extends com.aidl.myapplication.BindLocalCallBack.Stub {


        @Override
        public void studentCallBack(StudentBean mStudentBean) throws RemoteException {

            showUserInfo(mStudentBean.getName(), mStudentBean.getAge());
            Log.e("MainActivity", "studentCallBack getName " + mStudentBean.getName());
            Log.e("MainActivity", "studentCallBack getAge " + mStudentBean.getAge());

        }
    }

    /**
     * 解绑服务
     *
     * @param context
     */

    public static void bindLocalService(Context context, ServiceConnection serviceConnection) {
        if (context == null || null == serviceConnection) {
            return;
        }
        //通过Intent指定服务端的服务名称和所在包，与远程Service进行绑定
        Intent intent = new Intent();
        intent.setClass(context, LocalService.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", "张三");
        bundle.putString("age", "25");
        intent.putExtras(bundle);
        //绑定服务,传入intent和serviceConnection对象
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解绑服务
     *
     * @param context
     */
    public static void unBindLocalService(Context context, ServiceConnection serviceConnection) {
        if (context == null || null == serviceConnection) {
            return;
        }
        try {
            // 当需要多次调用doSomething()方法的时候，如果直接bindService是会报错的
            context.unbindService(serviceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**************************************** 本地服务  end ********************************************/


    /**************************************** 远程服务（新开一个进程）  start ********************************************/
    private TheRemoteInterface theRemoteInterface;

    //创建ServiceConnection的匿名类
    ServiceConnection serviceTheRemoteConnection = new ServiceConnection() {
        //重写onServiceConnected()方法和onServiceDisconnected()方法


        //在Activity与Service建立关联时调用
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //使用BindInterface.Stub.asInterface()方法将传入的IBinder对象传换成了BindInterface对象
            theRemoteInterface = TheRemoteInterface.Stub.asInterface(service);
            try {
                //通过该对象调用在bindInterface文件中定义的接口方法,从而实现跨进程通信
                theRemoteInterface.registerCallback(new TheRemoteCallBack());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        //在Activity与Service解除关联的时候调用
        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                if (null != theRemoteInterface) {
                    theRemoteInterface.unRegisterCallback();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            theRemoteInterface = null;
        }
    };


    private void showFruitUserInfo(String fruitName, String fruitAge) {
        ((TextView) findViewById(R.id.tvFruitName)).setText(MainActivity.this.getString(R.string.app_fruit_name, fruitName));
        ((TextView) findViewById(R.id.tvFruitAge)).setText(MainActivity.this.getString(R.string.app_fruit_age, fruitAge));
    }

    private void sendFruitMessage() {
        if (theRemoteInterface != null) {
            try {
                theRemoteInterface.sendFruitMessage("我是activity水果大王传到远程service的数据");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * service回调
     */
    class TheRemoteCallBack extends com.aidl.myapplication.TheRemoteCallBack.Stub {
        @Override
        public void fruitCallBack(FruitBean mFruitBean) throws RemoteException {

            showFruitUserInfo(mFruitBean.getFruit1(), mFruitBean.getFruit2());
        }

    }

    /**
     * 绑定远程服务
     *
     * @param context
     * @param serviceConnection
     */

    public static void bindTheRemoteService(Context context, ServiceConnection serviceConnection) {
        if (context == null || null == serviceConnection) {
            return;
        }
        //通过Intent指定服务端的服务名称和所在包，与远程Service进行绑定
        Intent intent = new Intent();
        intent.setClass(context, TheRemoteService.class);
        Bundle bundle = new Bundle();
        bundle.putString("Fruitname", "苹果");
        bundle.putString("Fruitage", "今年");
        intent.putExtras(bundle);
        //绑定服务,传入intent和serviceConnection对象
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解绑远程服务
     *
     * @param context
     * @param serviceConnection
     */
    public static void unBindTheRemoteService(Context context, ServiceConnection serviceConnection) {
        if (context == null || null == serviceConnection) {
            return;
        }
        try {
            // 当需要多次调用doSomething()方法的时候，如果直接bindService是会报错的
            context.unbindService(serviceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**************************************** 远程服务  end ********************************************/


}
