package io.github.maksymilianrozanski.medicinesbox.data

class TestTimeProvider(var time: Long) : TimeProvider() {

    override fun getCurrentTimeInMillis(): Long {
        return time
    }
}