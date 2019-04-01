// BindLocalCallBack.aidl
package com.aidl.myapplication;
import com.aidl.myapplication.bean.FruitBean;

// Declare any non-default types here with import statements

interface TheRemoteCallBack {

     void fruitCallBack(inout FruitBean mFruitBean);
}
