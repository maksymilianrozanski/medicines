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
        medicine.dailyUsage = 2.0
        medicine.quantity = 14.0

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
        medicine.dailyUsage = 2.0
        medicine.quantity = 5.0   //supply for 2.5 days

        var expectedString = "Jan 3, 2018"
        assertTrue(medicine.enoughUntilDate().equals(expectedString))
    }

    @Test
    fun enoughUntilDateTest2() {
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    10:00:00
        medicine.dailyUsage = 3.0
        medicine.quantity = 11.0  //supply for 3.67 days

        var expectedString = "Jan 4, 2018"
        assertTrue(medicine.enoughUntilDate().equals(expectedString))
    }

    @Test
    fun enoughUntilDateTest3() {
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    10:00:00
        medicine.dailyUsage = 3.0
        medicine.quantity = 14.0  //supply for 4.33 days

        var expectedString = "Jan 5, 2018"
        assertTrue(medicine.enoughUntilDate().equals(expectedString))
    }

    @Test
    fun enoughUntilDateTest4(){
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    10:00:00
        medicine.dailyUsage = 0.5
        medicine.quantity = 2.0  //supply for 4.00 days

        var expectedString = "Jan 5, 2018"
        assertTrue(medicine.enoughUntilDate().equals(expectedString))
    }

    @Test
    fun enoughUntilDateTest5(){
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    10:00:00
        medicine.dailyUsage = 0.33
        medicine.quantity = 2.0  //supply for 6.06 days

        var expectedString = "Jan 7, 2018"
        assertTrue(medicine.enoughUntilDate().equals(expectedString))
    }

    @Test
    fun enoughUntilDateTest6(){
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    10:00:00
        medicine.dailyUsage = 0.25
        medicine.quantity = 0.9  //supply for 3.6 days

        var expectedString = "Jan 4, 2018"
        assertTrue(medicine.enoughUntilDate().equals(expectedString))
    }

    @Test
    fun enoughUntilDateTest7(){
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    10:00:00
        medicine.dailyUsage = 2.5
        medicine.quantity = 9.0  //supply for 3.6 days

        var expectedString = "Jan 4, 2018"
        assertTrue(medicine.enoughUntilDate().equals(expectedString))
    }

    @Test
    fun enoughUntilTest() {
        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "Vitamins"
        medicine.savedTime = 1514797200000L //01.01.2018    9:00:00 UTC
        medicine.dailyUsage = 2.0
        medicine.quantity = 5.0

        var expectedValue = 1514970000000L  //03.01.2018    9:00:00 UTC
        assertTrue(medicine.enoughUntil() == expectedValue)
    }
}