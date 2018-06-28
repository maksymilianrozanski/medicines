package io.github.maksymilianrozanski.medicinesbox.model

import android.os.Parcel
import android.os.Parcelable
import java.text.DateFormat
import java.util.*

class Medicine() : Parcelable {
    var id: Int? = null
    var name: String? = null
    var quantity: Double? = null
    var dailyUsage: Double? = null
    var savedTime: Long = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
        quantity = parcel.readValue(Int::class.java.classLoader) as? Double
        dailyUsage = parcel.readValue(Int::class.java.classLoader) as? Double
        savedTime = parcel.readLong()
    }

    fun showFormattedDate(): String {
        var dateFormat: java.text.DateFormat = DateFormat.getDateInstance()
        return dateFormat.format(Date(savedTime).time)
    }

    fun enoughUntilDate(): String {
        var dateFormat: java.text.DateFormat = DateFormat.getDateInstance()
        return dateFormat.format(Date(enoughUntil()).time)
    }

    fun enoughUntil(): Long {
        if (dailyUsage == 0.0) return savedTime
        val enoughForDays: Int = (quantity!!/dailyUsage!!).toInt()
        return (savedTime + enoughForDays * 86400000L)
    }

    override fun toString(): String {
        return "Medicine(id: $id, name: $name," +
                " quantity: $quantity, daily usage: $dailyUsage, time in millis: $savedTime)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeValue(quantity)
        parcel.writeValue(dailyUsage)
        parcel.writeLong(savedTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Medicine> {
        override fun createFromParcel(parcel: Parcel): Medicine {
            return Medicine(parcel)
        }

        override fun newArray(size: Int): Array<Medicine?> {
            return arrayOfNulls(size)
        }
    }
}