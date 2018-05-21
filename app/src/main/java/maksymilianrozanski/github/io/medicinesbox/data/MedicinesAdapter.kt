package maksymilianrozanski.github.io.medicinesbox.data

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import maksymilianrozanski.github.io.medicinesbox.R
import maksymilianrozanski.github.io.medicinesbox.model.Medicine

class MedicinesAdapter(private val list: ArrayList<Medicine>, private val context: Context) :
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

    inner class ViewHolder(itemView: View, context: Context, list: ArrayList<Medicine>)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var medicineName = itemView.findViewById(R.id.medicineName) as TextView
        var medicineQuantity = itemView.findViewById(R.id.medicineQuantity) as TextView
        var medicineDailyUsage = itemView.findViewById(R.id.medicineDailyUsage) as TextView
        var medicineSavedTime = itemView.findViewById(R.id.medicineSaveDate) as TextView
        var deleteButton  = itemView.findViewById(R.id.deleteButton) as Button

        fun bindViews(medicine: Medicine) {
            medicineName.text = medicine.name
            medicineQuantity.text = "Quantity: ${medicine.quantity.toString()}"
            medicineDailyUsage.text = "Daily usage: ${medicine.dailyUsage.toString()}"
            medicineSavedTime.text = medicine.showFormattedDate()

            medicineName.setOnClickListener(this)
            deleteButton.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            var medicine = list[adapterPosition]

           if (view?.id == deleteButton.id) {
               Toast.makeText(context, "Clicked delete button, Deleting! : ${medicine.id}", Toast.LENGTH_SHORT).show()
               list.removeAt(adapterPosition)
               deleteItem(medicine.id!!)
               notifyItemRemoved(adapterPosition)
           }else{
               Toast.makeText(context, "Clicked something else", Toast.LENGTH_SHORT).show()
           }
        }

        private fun deleteItem(id: Int){
            var databaseHandler = MedicinesDatabaseHandler(context)
            databaseHandler.deleteMedicine(id)
        }
    }
}