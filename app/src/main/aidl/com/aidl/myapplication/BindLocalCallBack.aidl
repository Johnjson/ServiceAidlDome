// BindLocalCallBack.aidl
package com.aidl.myapplication;
import com.aidl.myapplication.bean.StudentBean;
// Declare any non-default types here with import statements

interface BindLocalCallBack {

    void studentCallBack(inout StudentBean mStudentBean);
}
