package maksymilianrozanski.github.io.medicinesbox.model

import android.os.Parcel
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MedicineInstrumentedTest {

    @Test
    fun parcelableImplementationTest() {
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1527281990668L
        medicine.dailyUsage = 2
        medicine.quantity = 14

        var parcel: Parcel = Parcel.obtain()
        medicine.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        var medicineFromParcel = Medicine.createFromParcel(parcel)

        assertTrue(medicine.id!!.equals(medicineFromParcel.id))
        assertTrue(medicine.name!!.equals(medicineFromParcel.name))
        assertTrue(medicine.savedTime.equals(medicineFromParcel.savedTime))
        assertTrue(medicine.dailyUsage!!.equals(medicineFromParcel.dailyUsage))
        assertTrue(medicine.quantity!!.equals(medicineFromParcel.quantity))
    }
}