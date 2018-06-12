package maksymilianrozanski.github.io.medicinesbox.data

open class TimeProvider {

    open fun getCurrentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }

}