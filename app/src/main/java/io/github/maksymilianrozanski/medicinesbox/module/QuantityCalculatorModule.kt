package io.github.maksymilianrozanski.medicinesbox.module

import dagger.Module
import dagger.Provides
import io.github.maksymilianrozanski.medicinesbox.data.TimeProvider
import io.github.maksymilianrozanski.medicinesbox.utilities.QuantityCalculator

@Module(includes = [TimeProviderModule::class])
class QuantityCalculatorModule {

    @Provides
    fun provideQuantityCalculator(timeProvider: TimeProvider): QuantityCalculator {
        return QuantityCalculator(timeProvider)
    }
}