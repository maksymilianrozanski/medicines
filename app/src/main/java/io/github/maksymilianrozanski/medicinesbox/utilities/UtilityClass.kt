package io.github.maksymilianrozanski.medicinesbox.utilities

fun getDoubleFromString(value: String): Double {
    return value.replace(',', '.').toDouble()
}