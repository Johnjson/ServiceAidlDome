// TheRemoteInterface.aidl
package com.aidl.myapplication;
import com.aidl.myapplication.TheRemoteCallBack;


// Declare any non-default types here with import statements

interface TheRemoteInterface {

     //用于UI注册TheRemoteCallBack
     void registerCallback(TheRemoteCallBack callback);
     //用于UI注销TheRemoteCallBack
     void unRegisterCallback();

     //用于UI向service传输数据
     void sendFruitMessage(String name);
}
