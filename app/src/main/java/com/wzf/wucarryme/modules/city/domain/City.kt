package com.wzf.wucarryme.modules.city.domain

import android.os.Parcel
import android.os.Parcelable

class City : Parcelable {

    lateinit var mCityName: String
    var mProID: Int = 0
    var mCitySort: Int = 0

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.mCityName)
        dest.writeInt(this.mProID)
        dest.writeInt(this.mCitySort)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.mCityName = `in`.readString()
        this.mProID = `in`.readInt()
        this.mCitySort = `in`.readInt()
    }

    companion object {

        val CREATOR: Parcelable.Creator<City> = object : Parcelable.Creator<City> {
            override fun createFromParcel(source: Parcel): City {
                return City(source)
            }

            override fun newArray(size: Int): Array<City?> {
                return arrayOfNulls(size)
            }
        }
    }
}
