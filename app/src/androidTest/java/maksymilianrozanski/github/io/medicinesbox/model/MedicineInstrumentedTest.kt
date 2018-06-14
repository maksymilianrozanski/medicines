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

    @Test
    fun enoughUntilDateTest1() {
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    10:00:00
        medicine.dailyUsage = 2
        medicine.quantity = 5   //supply for 2.5 days

        var expectedString = "Jan 3, 2018"
        assertTrue(medicine.enoughUntilDate().equals(expectedString))
    }

    @Test
    fun enoughUntilDateTest2() {
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    10:00:00
        medicine.dailyUsage = 3
        medicine.quantity = 11  //supply for 3.67 days

        var expectedString = "Jan 4, 2018"
        assertTrue(medicine.enoughUntilDate().equals(expectedString))
    }

    @Test
    fun enoughUntilDateTest3() {
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    10:00:00
        medicine.dailyUsage = 3
        medicine.quantity = 14  //supply for 4.33 days

        var expectedString = "Jan 5, 2018"
        assertTrue(medicine.enoughUntilDate().equals(expectedString))
    }

    @Test
    fun enoughUntilTest() {
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    9:00:00 UTC
        medicine.dailyUsage = 2
        medicine.quantity = 5

        var expectedValue = 1514970000000L  //03.01.2018    9:00:00 UTC
        assertTrue(medicine.enoughUntil() == expectedValue)
    }
}