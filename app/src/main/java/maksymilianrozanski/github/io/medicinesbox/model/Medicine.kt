package maksymilianrozanski.github.io.medicinesbox.model

import java.text.DateFormat
import java.util.*

class Medicine {
    var id: Int? = null
    var name: String? = null
    var quantity: Int? = null
    var dailyUsage: Int? = null
    var savedTime: Long = 0

    fun showFormattedDate(): String {
        var dateFormat: java.text.DateFormat = DateFormat.getDateInstance()
        return dateFormat.format(Date(savedTime).time)
    }

    override fun toString(): String {
        return "Medicine(id: $id, name: $name," +
                " quantity: $quantity, daily usage: $dailyUsage, time in millis: $savedTime)"
    }
}