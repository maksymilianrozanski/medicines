package maksymilianrozanski.github.io.medicinesbox.module

import dagger.Module
import dagger.Provides
import maksymilianrozanski.github.io.medicinesbox.data.TimeProvider
import maksymilianrozanski.github.io.medicinesbox.utilities.QuantityCalculator

@Module(includes = [TimeProviderModule::class])
class QuantityCalculatorModule {

    @Provides
    fun provideQuantityCalculator(timeProvider: TimeProvider): QuantityCalculator {
        return QuantityCalculator(timeProvider)
    }
}