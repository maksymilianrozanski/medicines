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
}