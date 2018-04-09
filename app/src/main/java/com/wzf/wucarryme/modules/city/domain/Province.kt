package com.wzf.wucarryme.modules.city.domain

import android.os.Parcel
import android.os.Parcelable

class Province : Parcelable {

    lateinit var mProName: String
    var mProSort: Int = 0

    override fun toString(): String {
        return "[mProName] = " + mProName + "\n" +
                "[mProSort] = " + mProSort
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.mProName)
        dest.writeInt(this.mProSort)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.mProName = `in`.readString()
        this.mProSort = `in`.readInt()
    }

    companion object {

        val CREATOR: Parcelable.Creator<Province> = object : Parcelable.Creator<Province> {
            override fun createFromParcel(source: Parcel): Province {
                return Province(source)
            }

            override fun newArray(size: Int): Array<Province?> {
                return arrayOfNulls(size)
            }
        }
    }
}
