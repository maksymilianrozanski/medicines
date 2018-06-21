package io.github.maksymilianrozanski.medicinesbox

import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import io.github.maksymilianrozanski.medicinesbox.component.MainActivityComponent
import io.github.maksymilianrozanski.medicinesbox.data.MedicinesAdapter
import io.github.maksymilianrozanski.medicinesbox.data.MedicinesDatabaseHandler
import io.github.maksymilianrozanski.medicinesbox.model.Medicine
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var adapter: MedicinesAdapter? = null
    private var medicineListFromDb: ArrayList<Medicine> = ArrayList()
    private var layoutManger: RecyclerView.LayoutManager? = null

    @Inject
    lateinit var databaseHandler: MedicinesDatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        (application as MyApp).appComponent
                .plus(MainActivityComponent.Module())
                .inject(this)

        layoutManger = LinearLayoutManager(this)
        adapter = MedicinesAdapter(medicineListFromDb, this)

        recyclerViewId.layoutManager = layoutManger
        recyclerViewId.adapter = adapter

        reloadAdapterDataFromDb()
    }

    override fun onResume() {
        super.onResume()
        reloadAdapterDataFromDb()
    }

    private fun reloadAdapterDataFromDb() {
        medicineListFromDb = databaseHandler.readMedicines()
        adapter!!.setList(medicineListFromDb)
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
            R.id.addNewItem -> {
                runNewItemActivity(); return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @VisibleForTesting
    fun runNewItemActivity() {
        var intent = Intent(this, AddEditActivity::class.java)
        startActivity(intent)
    }
}
