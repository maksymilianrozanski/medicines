package maksymilianrozanski.github.io.medicinesbox

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_edit.*
import maksymilianrozanski.github.io.medicinesbox.model.KEY_ID
import maksymilianrozanski.github.io.medicinesbox.model.Medicine

class AddEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        var launchIntent: Intent = intent
        var medicine = launchIntent.getParcelableExtra<Medicine>(KEY_ID)

        if (medicine != null
                && medicine.name != null
                && medicine.id != null
                && medicine.quantity != null
                && medicine.dailyUsage != null) {
            medicineNameEditText.setText(medicine.name)
            medicineQuantityEditText.setText(medicine.quantity!!.toString())
            medicineDailyUsageEditText.setText(medicine.dailyUsage!!.toString())
        }
    }
}
