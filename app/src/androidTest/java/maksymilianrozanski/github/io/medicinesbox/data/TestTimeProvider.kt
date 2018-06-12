package maksymilianrozanski.github.io.medicinesbox.data

class TestTimeProvider(var time: Long) : TimeProvider() {

    override fun getCurrentTimeInMillis(): Long {
        return time
    }
}