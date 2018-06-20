package maksymilianrozanski.github.io.medicinesbox.utilities

import junit.framework.Assert
import maksymilianrozanski.github.io.medicinesbox.data.TimeProvider
import maksymilianrozanski.github.io.medicinesbox.model.Medicine
import org.junit.Ignore
import org.junit.Test

class QuantityCalculatorTest {

    @Ignore
    class TimeProviderMock : TimeProvider() {
        override fun getCurrentTimeInMillis(): Long {
            return 1514970000000L   //03 Jan 2018 9:00:00 UTC
        }
    }

    @Test
    fun calculateQuantityTodayTest() {
        var timeProvider = TimeProviderMock()
        var medicine = Medicine()
        medicine.name = "Paracetamol"
        medicine.dailyUsage = 1.0
        medicine.quantity = 3.0
        medicine.savedTime = 1514797200000L     //01 Jan 2018 9:00:00 UTC

        var quantityCalculator = QuantityCalculator(timeProvider)
        var expectedValue = 1.0

        Assert.assertTrue(quantityCalculator.calculateQuantityToday(medicine).equals(expectedValue))
    }

    @Test
    fun calculateQuantityTodayTest2() {
        var timeProvider = TimeProviderMock()
        var medicine = Medicine()
        medicine.name = "Paracetamol"
        medicine.dailyUsage = 0.5
        medicine.quantity = 3.0
        medicine.savedTime = 1514797200000L     //01 Jan 2018 9:00:00 UTC

        var quantityCalculator = QuantityCalculator(timeProvider)
        var expectedValue = 2.0

        Assert.assertTrue(quantityCalculator.calculateQuantityToday(medicine).equals(expectedValue))
    }

    @Test
    fun calculateQuantityTodayTest3() {
        var timeProvider = TimeProviderMock()
        var medicine = Medicine()
        medicine.name = "Paracetamol"
        medicine.dailyUsage = 0.25
        medicine.quantity = 3.0
        medicine.savedTime = 1514797200000L     //01 Jan 2018 9:00:00 UTC

        var quantityCalculator = QuantityCalculator(timeProvider)
        var expectedValue = 2.5

        Assert.assertTrue(quantityCalculator.calculateQuantityToday(medicine).equals(expectedValue))
    }

    @Test
    fun calculateQuantityTodayTest4(){
        var timeProvider = TimeProviderMock()
        var medicine = Medicine()
        medicine.name = "Paracetamol"
        medicine.dailyUsage = 1.0
        medicine.quantity = 3.0
        medicine.savedTime = 1514775600000L //01 Jan 2018, 04:00:00

        var quantityCalculator = QuantityCalculator(timeProvider)
        var expectedValue = 1.0
        Assert.assertTrue(quantityCalculator.calculateQuantityToday(medicine).equals(expectedValue))
    }

    @Test
    fun calculateQuantityTodayTest5(){
        var timeProvider = TimeProviderMock()
        var medicine = Medicine()
        medicine.name = "Paracetamol"
        medicine.dailyUsage = 1.0
        medicine.quantity = 3.0
        medicine.savedTime  = 1514840400000L //01 Jan 2018, 22:00

        var quantityCalculator = QuantityCalculator(timeProvider)
        var expectedValue = 1.0
        Assert.assertTrue(quantityCalculator.calculateQuantityToday(medicine).equals(expectedValue))
    }

    @Test
    fun subtractTimeAfterCompleteDaysTest() {
        var timeProvider = TimeProviderMock()
        var quantityCalculator = QuantityCalculator(timeProvider)
        quantityCalculator.dayBeginningHour = 0

        val inputTime: Long = 1529504725000L //20 Jun 2018, 16:25:25
        val outputTime: Long = quantityCalculator.subtractTimeAfterCompleteDays(inputTime)
        val expectedOutput: Long = 1529445600000L   //20 Jun 2018, 00:00:00
        Assert.assertTrue(outputTime == expectedOutput)
    }

    @Test
    fun subtractTimeAfterCompleteDaysTest2() {
        var timeProvider = TimeProviderMock()
        var quantityCalculator = QuantityCalculator(timeProvider)
        quantityCalculator.dayBeginningHour = 10


        val inputTime: Long = 1529504725000L //20 Jun 2018, 16:25:25
        val outputTime: Long = quantityCalculator.subtractTimeAfterCompleteDays(inputTime)
        val expectedOutput: Long = 1529481600000L    //20 Jun 2018, 10:00:00
        Assert.assertTrue(outputTime == expectedOutput)
    }

    @Test
    fun subtractTimeAfterCompleteDaysTest3() {
        var timeProvider = TimeProviderMock()
        var quantityCalculator = QuantityCalculator(timeProvider)
        quantityCalculator.dayBeginningHour = 22

        val inputTime: Long = 1529504725000L //20 Jun 2018, 16:25:25
        val outputTime: Long = quantityCalculator.subtractTimeAfterCompleteDays(inputTime)
        val expectedOutput: Long = 1529438400000L   //19 Jun 2018, 22:00:00

        Assert.assertTrue(outputTime == expectedOutput)
    }
}