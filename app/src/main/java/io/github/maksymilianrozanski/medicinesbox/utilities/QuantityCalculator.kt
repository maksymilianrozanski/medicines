package io.github.maksymilianrozanski.medicinesbox.utilities

import android.support.annotation.VisibleForTesting
import io.github.maksymilianrozanski.medicinesbox.data.TimeProvider
import io.github.maksymilianrozanski.medicinesbox.model.Medicine
import java.util.*

class QuantityCalculator(var timeProvider: TimeProvider) {

    @VisibleForTesting
    var dayBeginningHour: Int = 0

    fun calculateQuantityToday(medicine: Medicine): Double {
        val savedQuantity = medicine.quantity
        val dailyUsage = medicine.dailyUsage
        val savedDate = medicine.savedTime
        if (savedQuantity != null && dailyUsage != null) {
            val timeAfterSubtraction: Long = subtractTimeAfterCompleteDays(savedDate)
            val timeSinceLastSave: Long = timeProvider.getCurrentTimeInMillis() - timeAfterSubtraction
            val daysSinceLastSave = (timeSinceLastSave / 86400000L).toInt()
            val medicineUsed: Double = dailyUsage.times(daysSinceLastSave)
            val medicineToday: Double = savedQuantity.minus(medicineUsed)

            return if (medicineToday <= 0) {
                0.0
            } else medicineToday
        }
        return 0.0
    }

    @VisibleForTesting
    fun subtractTimeAfterCompleteDays(inputTime: Long): Long {
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = inputTime

        calendar.set(Calendar.HOUR_OF_DAY, dayBeginningHour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return if (calendar.timeInMillis > inputTime) {
            calendar.timeInMillis = calendar.timeInMillis - 86400000L
            calendar.timeInMillis
        } else {
            calendar.timeInMillis
        }
    }
}