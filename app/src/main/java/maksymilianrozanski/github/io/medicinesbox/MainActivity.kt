package maksymilianrozanski.github.io.medicinesbox

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import maksymilianrozanski.github.io.medicinesbox.data.MedicinesAdapter
import maksymilianrozanski.github.io.medicinesbox.data.MedicinesDatabaseHandler
import maksymilianrozanski.github.io.medicinesbox.model.Medicine

class MainActivity : AppCompatActivity() {

    private var adapter: MedicinesAdapter? = null
    private var medicineListFromDb: ArrayList<Medicine>? = null
    private var medicineListForAdapter: ArrayList<Medicine>? = null

    private var layoutManger: RecyclerView.LayoutManager? = null
    var databaseHandler: MedicinesDatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        databaseHandler = MedicinesDatabaseHandler(this)

        //insert example data
        var exampleMedicine = Medicine()
        exampleMedicine.name = "Paracetamol"
        exampleMedicine.quantity = 10
        exampleMedicine.dailyUsage = 1
        exampleMedicine.savedTime = 1526835161326L
        databaseHandler!!.createMedicine(exampleMedicine)

        medicineListFromDb = ArrayList()
        medicineListForAdapter = ArrayList()
        layoutManger = LinearLayoutManager(this)
        adapter = MedicinesAdapter(medicineListForAdapter!!, this)

        recyclerViewId.layoutManager = layoutManger
        recyclerViewId.adapter = adapter

        medicineListFromDb = databaseHandler!!.readMedicines()
        medicineListFromDb!!.reverse()

        for (medicine in medicineListFromDb!!.iterator()) {
            val currentMedicine = Medicine()
            currentMedicine.id = medicine.id
            currentMedicine.name = medicine.name
            currentMedicine.quantity = medicine.quantity
            currentMedicine.dailyUsage = medicine.dailyUsage
            currentMedicine.savedTime = medicine.savedTime

            medicineListForAdapter!!.add(currentMedicine)
        }
        adapter!!.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
