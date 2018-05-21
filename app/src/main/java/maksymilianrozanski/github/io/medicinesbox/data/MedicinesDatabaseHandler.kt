package maksymilianrozanski.github.io.medicinesbox.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import maksymilianrozanski.github.io.medicinesbox.model.*

class MedicinesDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(database: SQLiteDatabase?) {
        var createMedicineTable = "CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_NAME TEXT, $KEY_QUANTITY INTEGER," +
                " $KEY_DAILY_USAGE INTEGER, $KEY_SAVED_TIME LONG); "
        database?.execSQL(createMedicineTable)
    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
        database?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }

    fun createMedicine(medicine: Medicine) {
        var database: SQLiteDatabase = writableDatabase

        var values = ContentValues()
        values.put(KEY_NAME, medicine.name)
        values.put(KEY_QUANTITY, medicine.quantity)
        values.put(KEY_DAILY_USAGE, medicine.dailyUsage)
        values.put(KEY_SAVED_TIME, medicine.savedTime)

        var insert = database.insert(TABLE_NAME, null, values)
        println("Data inserted, success: $insert")
        database.close()
    }

    fun readMedicine(id: Int): Medicine {
        var database: SQLiteDatabase = writableDatabase
        var cursor: Cursor = database.query(TABLE_NAME,
                arrayOf(KEY_ID, KEY_NAME, KEY_QUANTITY, KEY_DAILY_USAGE, KEY_SAVED_TIME),
                "$KEY_ID =?", arrayOf(id.toString())
                , null, null, null, null)


        cursor.moveToFirst()

        var medicine = Medicine()
        medicine.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
        medicine.name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
        medicine.quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY))
        medicine.dailyUsage = cursor.getInt(cursor.getColumnIndex(KEY_DAILY_USAGE))
        medicine.savedTime = cursor.getLong(cursor.getColumnIndex(KEY_SAVED_TIME))

        cursor.close()
        return medicine
    }

    fun readMedicines(): ArrayList<Medicine> {
        var database: SQLiteDatabase = readableDatabase
        var list: ArrayList<Medicine> = ArrayList()

        var selectAll = "SELECT * FROM $TABLE_NAME"

        var cursor: Cursor = database.rawQuery(selectAll, null)

        if (cursor.moveToFirst()) {
            do {
                var medicine = Medicine()
                medicine.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                medicine.name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                medicine.quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY))
                medicine.dailyUsage = cursor.getInt(cursor.getColumnIndex(KEY_DAILY_USAGE))
                medicine.savedTime = cursor.getLong(cursor.getColumnIndex(KEY_SAVED_TIME))

                list.add(medicine)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return list
    }

    fun updateMedicine(medicine: Medicine): Int {
        var database: SQLiteDatabase = writableDatabase
        var values = ContentValues()
        values.put(KEY_NAME, medicine.name)
        values.put(KEY_QUANTITY, medicine.quantity)
        values.put(KEY_DAILY_USAGE, medicine.dailyUsage)
        values.put(KEY_SAVED_TIME, medicine.savedTime)
        try {
            return database.update(TABLE_NAME, values,
                    "$KEY_ID =?", arrayOf(medicine.id.toString()))
        } finally {
            database.close()
        }
    }

    fun deleteMedicine(id: Int) {
        var database: SQLiteDatabase = writableDatabase
        database.delete(TABLE_NAME, "$KEY_ID =?", arrayOf(id.toString()))
        database.close()
    }

    fun getMedicinesCount(): Int {
        var database: SQLiteDatabase = readableDatabase
        var countQuery = "SELECT * FROM $TABLE_NAME"
        var cursor: Cursor = database.rawQuery(countQuery, null)

        try {
            return cursor.count
        } finally {
            database.close()
            cursor.close()
        }
    }
}