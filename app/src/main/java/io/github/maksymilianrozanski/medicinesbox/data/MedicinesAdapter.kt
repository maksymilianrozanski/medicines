package io.github.maksymilianrozanski.medicinesbox.data

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import io.github.maksymilianrozanski.medicinesbox.AddEditActivity
import io.github.maksymilianrozanski.medicinesbox.MyApp
import io.github.maksymilianrozanski.medicinesbox.R
import io.github.maksymilianrozanski.medicinesbox.model.KEY_ID
import io.github.maksymilianrozanski.medicinesbox.model.Medicine

class MedicinesAdapter(private var list: ArrayList<Medicine>, private val context: Context) :
        RecyclerView.Adapter<MedicinesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.list_row, parent, false)
        return ViewHolder(view, context, list)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(list[position])
    }

    fun setList(newList: ArrayList<Medicine>) {
        this.list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View, context: Context, list: ArrayList<Medicine>)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var quantityCalculator = (context.applicationContext as MyApp).appComponent.getQuantityCalculator()

        var medicineName = itemView.findViewById(R.id.medicineName) as TextView
        var expectedQuantity = itemView.findViewById(R.id.expectedQuantity) as TextView
        var medicineDailyUsage = itemView.findViewById(R.id.medicineDailyUsage) as TextView
        var deleteButton = itemView.findViewById(R.id.deleteButton) as Button
        var editButton = itemView.findViewById(R.id.editButton) as Button
        var calendarIntentButton = itemView.findViewById(R.id.calendarIntentButton) as Button

        fun bindViews(medicine: Medicine) {
            medicineName.text = medicine.name
            expectedQuantity.text = "Expected quantity today: ${String.format("%.2f", quantityCalculator.calculateQuantityToday(medicine))}"
            medicineDailyUsage.text = "Daily usage: ${String.format("%.2f", medicine.dailyUsage)}"
            setMedicineEnoughUntil(medicine)
            medicineName.setOnClickListener(this)
            deleteButton.setOnClickListener(this)
            editButton.setOnClickListener(this)
            calendarIntentButton.setOnClickListener(this)
        }

        private fun setMedicineEnoughUntil(medicine: Medicine) {
            var medicineEnoughUntil = itemView.findViewById(R.id.enoughUntil) as TextView
            if (medicine.dailyUsage?.equals(0)!!) {
                medicineEnoughUntil.text = "Daily usage is 0."
            } else
                medicineEnoughUntil.text = "Enough until: ${medicine.enoughUntilDate()}"
        }

        override fun onClick(view: View?) {
            var medicine = list[adapterPosition]

            when {
                view?.id == deleteButton.id -> {
                    var alertDialog = deletionAlertDialog(context, medicine)
                    alertDialog.show()
                }
                view?.id == editButton.id -> {
                    var intent = Intent(context, AddEditActivity::class.java)
                    intent.putExtra(KEY_ID, medicine)
                    context.startActivity(intent)
                }
                view?.id == calendarIntentButton.id -> {
                    sendCalendarIntent(medicine, context)
                }
            }
        }

        private fun deletionAlertDialog(context: Context, medicine: Medicine): AlertDialog {
            return AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete ${medicine.name}?")
                    .setPositiveButton("Delete") { _, _ ->
                        run {
                            list.removeAt(adapterPosition)
                            deleteItem(medicine.id!!)
                            notifyItemRemoved(adapterPosition)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .create()
        }

        private fun deleteItem(id: Int) {
            var databaseHandler = MedicinesDatabaseHandler(context)
            databaseHandler.deleteMedicine(id)
        }

        private fun sendCalendarIntent(medicine: Medicine, context: Context) {
            val lastDayOf = context.getString(R.string.last_day_of)
            val supply = context.getString(R.string.supply)
            val intent = Intent(Intent.ACTION_INSERT).apply {
                setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, "$lastDayOf ${medicine.name} $supply.")
                        .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, medicine.enoughUntil())
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }
    }
}