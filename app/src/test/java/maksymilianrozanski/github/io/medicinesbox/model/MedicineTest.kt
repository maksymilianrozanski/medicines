package maksymilianrozanski.github.io.medicinesbox.model

import junit.framework.Assert
import org.junit.Test

class MedicineTest {

    @Test
    fun showFormattedDate() {
        var medicine = Medicine()
        medicine.id = 1
        medicine.name = "Paracetamol"
        medicine.quantity = 20.0
        medicine.dailyUsage = 1.0
        medicine.savedTime = 1514761200000L

        var formattedDate:String = medicine.showFormattedDate()

        Assert.assertTrue(formattedDate.equals("2018-01-01"))
    }
}