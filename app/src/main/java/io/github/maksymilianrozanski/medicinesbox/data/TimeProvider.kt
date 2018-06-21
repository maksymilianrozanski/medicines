package io.github.maksymilianrozanski.medicinesbox.data

open class TimeProvider {

    open fun getCurrentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }

}