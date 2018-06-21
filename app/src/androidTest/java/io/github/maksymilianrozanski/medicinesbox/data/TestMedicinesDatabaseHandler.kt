package io.github.maksymilianrozanski.medicinesbox.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import io.github.maksymilianrozanski.medicinesbox.model.Medicine

class TestMedicinesDatabaseHandler(context: Context) : MedicinesDatabaseHandler(context) {

    private var medicines = ArrayList<Medicine>()

    init {
        var medicine1 = Medicine()
        medicine1.id = 3
        medicine1.name = "Paracetamol"
        medicine1.quantity = 10.0
        medicine1.dailyUsage = 2.0
        medicine1.savedTime = 1527284036691L

        medicines = ArrayList<Medicine>()
        medicines.add(medicine1)
    }

    override fun onCreate(database: SQLiteDatabase?) {
    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    override fun createMedicine(medicine: Medicine) {
    }

    override fun readMedicine(id: Int): Medicine {
        return medicines[id]
    }

    override fun readMedicines(): ArrayList<Medicine> {
        return medicines
    }

    override fun updateMedicine(medicine: Medicine): Int {
        return 0
    }

    override fun deleteMedicine(id: Int) {

    }

    override fun getMedicinesCount(): Int {
        return medicines.size
    }
}