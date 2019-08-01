package io.github.maksymilianrozanski.medicinesbox

import android.content.Intent
import android.os.Bundle
import androidx.core.app.NavUtils
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import io.github.maksymilianrozanski.medicinesbox.component.AddEditActivityComponent
import io.github.maksymilianrozanski.medicinesbox.data.MedicinesDatabaseHandler
import io.github.maksymilianrozanski.medicinesbox.data.TimeProvider
import io.github.maksymilianrozanski.medicinesbox.model.KEY_ID
import io.github.maksymilianrozanski.medicinesbox.model.Medicine
import io.github.maksymilianrozanski.medicinesbox.utilities.QuantityCalculator
import io.github.maksymilianrozanski.medicinesbox.utilities.getDoubleFromString
import kotlinx.android.synthetic.main.activity_add_edit.*
import javax.inject.Inject

class AddEditActivity : AppCompatActivity() {

    @Inject
    lateinit var databaseHandler: MedicinesDatabaseHandler

    @Inject
    lateinit var timeProvider: TimeProvider

    @Inject
    lateinit var quantityCalculator: QuantityCalculator

    private var addingQuantityVisibilityKey: String = "visibility_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        (application as MyApp).appComponent
                .plusAddEditActivity(AddEditActivityComponent.Module())
                .inject(this)

        if (savedInstanceState?.get(addingQuantityVisibilityKey) != null) {
            if (savedInstanceState.get(addingQuantityVisibilityKey) == true) {
                displayAddingQuantityViews()
            } else {
                hideAddingQuantityViews()
            }
        }

        var launchIntent: Intent = intent
        var medicineFromIntent = launchIntent.getParcelableExtra<Medicine>(KEY_ID)

        if (medicineFromIntent?.name != null
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
        addMoreMedicineButton.setOnClickListener {
            displayAddingQuantityViews()
            amountOfMedicineToAddEditText.requestFocus()
        }
        acceptQuantityButton.setOnClickListener {
            increaseQuantity()
            hideAddingQuantityViews()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (amountOfMedicineToAddEditText.visibility == View.VISIBLE)
            outState.putBoolean(addingQuantityVisibilityKey, true)
        else outState.putBoolean(addingQuantityVisibilityKey, false)

        super.onSaveInstanceState(outState)
    }

    private fun saveNewMedicine() {
        if (isUserInputValid()) {
            var medicineToSave = Medicine()
            medicineToSave.name = medicineNameEditText.text.toString()
            medicineToSave.quantity = getDoubleFromString(medicineQuantityEditText.text.toString())
            medicineToSave.dailyUsage = getDoubleFromString(medicineDailyUsageEditText.text.toString())
            medicineToSave.savedTime = timeProvider.getCurrentTimeInMillis()
            databaseHandler.createMedicine(medicineToSave)
            NavUtils.navigateUpFromSameTask(this)
        }
    }

    private fun saveEditedMedicine(medicine: Medicine) {
        if (medicine.id != null && isUserInputValid()) {
            var medicineToUpdate = Medicine()
            medicineToUpdate.name = medicineNameEditText.text.toString()
            medicineToUpdate.quantity = getDoubleFromString(medicineQuantityEditText.text.toString())
            medicineToUpdate.dailyUsage = getDoubleFromString(medicineDailyUsageEditText.text.toString())
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

    private fun increaseQuantity() {
        var currentQuantity: Double = if (medicineQuantityEditText.text.isNotBlank()) {
            getDoubleFromString(medicineQuantityEditText.text.toString())
        } else 0.0
        val quantityToAdd: Double = if (amountOfMedicineToAddEditText.text.isNotBlank()) {
            getDoubleFromString(amountOfMedicineToAddEditText.text.toString())
        } else 0.0

        currentQuantity += quantityToAdd

        medicineQuantityEditText.setText(String.format("%.2f", currentQuantity))
        amountOfMedicineToAddEditText.setText("")
    }
}
