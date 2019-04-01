package com.aidl.myapplication.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentBean implements Parcelable {

    private String name;
    private String age;
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.age);
        dest.writeString(this.sex);
    }

    public StudentBean() {
    }

    protected StudentBean(Parcel in) {
        this.name = in.readString();
        this.age = in.readString();
        this.sex = in.readString();
    }

    public void readFromParcel(Parcel in) {
        this.name = in.readString();
        this.age = in.readString();
        this.sex = in.readString();
    }

    public static final Creator<StudentBean> CREATOR = new Creator<StudentBean>() {
        @Override
        public StudentBean createFromParcel(Parcel source) {
            return new StudentBean(source);
        }

        @Override
        public StudentBean[] newArray(int size) {
            return new StudentBean[size];
        }
    };
}
