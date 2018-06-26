package io.github.maksymilianrozanski.medicinesbox

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.github.maksymilianrozanski.medicinesbox.component.AddEditActivityComponent
import io.github.maksymilianrozanski.medicinesbox.data.MedicinesDatabaseHandler
import io.github.maksymilianrozanski.medicinesbox.data.TimeProvider
import io.github.maksymilianrozanski.medicinesbox.model.KEY_ID
import io.github.maksymilianrozanski.medicinesbox.model.Medicine
import io.github.maksymilianrozanski.medicinesbox.utilities.QuantityCalculator
import kotlinx.android.synthetic.main.activity_add_edit.*
import javax.inject.Inject

class AddEditActivity : AppCompatActivity() {

    @Inject
    lateinit var databaseHandler: MedicinesDatabaseHandler

    @Inject
    lateinit var timeProvider: TimeProvider

    @Inject
    lateinit var quantityCalculator: QuantityCalculator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        (application as MyApp).appComponent
                .plusAddEditActivity(AddEditActivityComponent.Module())
                .inject(this)

        var launchIntent: Intent = intent
        var medicineFromIntent = launchIntent.getParcelableExtra<Medicine>(KEY_ID)

        if (medicineFromIntent != null
                && medicineFromIntent.name != null
                && medicineFromIntent.id != null
                && medicineFromIntent.quantity != null
                && medicineFromIntent.dailyUsage != null) {
            medicineNameEditText.setText(medicineFromIntent.name)
            medicineQuantityEditText.setText(String.format("%.2f", quantityCalculator.calculateQuantityToday(medicineFromIntent)))
            medicineDailyUsageEditText.setText(String.format("%.2f", medicineFromIntent.dailyUsage))
        }

        saveButton.setOnClickListener {
            if (medicineFromIntent == null) {
                saveNewMedicine()
            } else {
                saveEditedMedicine(medicineFromIntent)
            }
        }

        cancelButton.setOnClickListener { NavUtils.navigateUpFromSameTask(this) }
        addMoreMedicineButton.setOnClickListener { displayAddingQuantityViews() }
        acceptQuantityButton.setOnClickListener { hideAddingQuantityViews() }
    }

    private fun saveNewMedicine() {
        if (isUserInputValid()) {
            var medicineToSave = Medicine()
            medicineToSave.name = medicineNameEditText.text.toString()
            medicineToSave.quantity = medicineQuantityEditText.text.toString().toDouble()
            medicineToSave.dailyUsage = medicineDailyUsageEditText.text.toString().toDouble()

            medicineToSave.savedTime = timeProvider.getCurrentTimeInMillis()
            databaseHandler.createMedicine(medicineToSave)
            NavUtils.navigateUpFromSameTask(this)
        }
    }

    private fun saveEditedMedicine(medicine: Medicine) {
        if (medicine.id != null && isUserInputValid()) {
            var medicineToUpdate = Medicine()

            medicineToUpdate.name = medicineNameEditText.text.toString()
            medicineToUpdate.quantity = medicineQuantityEditText.text.toString().toDouble()
            medicineToUpdate.dailyUsage = medicineDailyUsageEditText.text.toString().toDouble()
            medicineToUpdate.savedTime = timeProvider.getCurrentTimeInMillis()
            medicineToUpdate.id = medicine.id

            databaseHandler.updateMedicine(medicineToUpdate)
            NavUtils.navigateUpFromSameTask(this)
        }
    }

    private fun isUserInputValid(): Boolean {
        val medicineName = medicineNameEditText.text.toString()
        val medicineQuantity = medicineQuantityEditText.text.toString()
        val medicineDailyUsage = medicineDailyUsageEditText.text.toString()

        if (medicineName.isBlank()) {
            medicineNameEditText.error = getString(R.string.medicine_name_cannot_be_blank)
            return false
        }

        if (medicineQuantity.isBlank()) {
            medicineQuantityEditText.error = getString(R.string.quantity_cannot_be_blank)
            return false
        }

        if (medicineDailyUsage.isBlank()) {
            medicineDailyUsageEditText.error = getString(R.string.daily_usage_cannot_be_blank)
            return false
        }
        return true
    }

    private fun displayAddingQuantityViews() {
        medicineQuantityEditText.visibility = View.GONE
        addMoreMedicineButton.visibility = View.GONE
        amountOfMedicineToAddEditText.visibility = View.VISIBLE
        acceptQuantityButton.visibility = View.VISIBLE
    }

    private fun hideAddingQuantityViews() {
        medicineQuantityEditText.visibility = View.VISIBLE
        addMoreMedicineButton.visibility = View.VISIBLE
        amountOfMedicineToAddEditText.visibility = View.GONE
        acceptQuantityButton.visibility = View.GONE
    }
}
