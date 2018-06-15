package maksymilianrozanski.github.io.medicinesbox.utilities

import maksymilianrozanski.github.io.medicinesbox.data.TimeProvider
import maksymilianrozanski.github.io.medicinesbox.model.Medicine

class QuantityCalculator(var timeProvider: TimeProvider) {

    fun calculateQuantityToday(medicine: Medicine): Double {
        val savedQuantity = medicine.quantity
        val dailyUsage = medicine.dailyUsage
        val savedDate = medicine.savedTime
        if (savedQuantity != null && dailyUsage != null) {

            val timeSinceLastSave = timeProvider.getCurrentTimeInMillis() - savedDate
            val daysSinceLastSave = (timeSinceLastSave / 86400000).toInt()
            val medicineUsed = dailyUsage.times(daysSinceLastSave)
            val medicineToday = savedQuantity.minus(medicineUsed)

            return if (medicineToday <= 0) {
                0.0
            } else medicineToday
        }
        return 0.0
    }
}