package com.aidl.myapplication.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FruitBean implements Parcelable {

    private String fruit1;
    private String fruit2;
    private String fruit3;

    public String getFruit1() {
        return fruit1;
    }

    public void setFruit1(String fruit1) {
        this.fruit1 = fruit1;
    }

    public String getFruit2() {
        return fruit2;
    }

    public void setFruit2(String fruit2) {
        this.fruit2 = fruit2;
    }

    public String getFruit3() {
        return fruit3;
    }

    public void setFruit3(String fruit3) {
        this.fruit3 = fruit3;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fruit1);
        dest.writeString(this.fruit2);
        dest.writeString(this.fruit3);
    }

    public FruitBean() {
    }

    protected FruitBean(Parcel in) {
        this.fruit1 = in.readString();
        this.fruit2 = in.readString();
        this.fruit3 = in.readString();
    }

    public void readFromParcel(Parcel in) {
        this.fruit1 = in.readString();
        this.fruit2 = in.readString();
        this.fruit3 = in.readString();
    }

    public static final Creator<FruitBean> CREATOR = new Creator<FruitBean>() {
        @Override
        public FruitBean createFromParcel(Parcel source) {
            return new FruitBean(source);
        }

        @Override
        public FruitBean[] newArray(int size) {
            return new FruitBean[size];
        }
    };
}
