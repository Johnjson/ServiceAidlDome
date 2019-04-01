// BindInterface.aidl
package com.aidl.myapplication;
import com.aidl.myapplication.BindLocalCallBack;

// Declare any non-default types here with import statements

interface BindInterface {

     //用于UI注册BindLocalCallBack
     void registerCallback(BindLocalCallBack callback);
     //用于UI注销BindLocalCallBack
     void unRegisterCallback();

     //用于UI向service传输数据
     void sendMessage(String name);
}
