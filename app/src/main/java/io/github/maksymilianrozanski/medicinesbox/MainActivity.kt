package io.github.maksymilianrozanski.medicinesbox

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        var observer = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                displayOrHideEmptyView()
                super.onChanged()
            }
        }

        adapter?.registerAdapterDataObserver(observer)
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

    private fun displayOrHideEmptyView() {
        if (adapter?.itemCount == 0) {
            emptyView.visibility = View.VISIBLE
            recyclerViewId.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recyclerViewId.visibility = View.VISIBLE
        }
    }
}
