package maksymilianrozanski.github.io.medicinesbox.module

import dagger.Module
import dagger.Provides
import maksymilianrozanski.github.io.medicinesbox.data.TimeProvider

@Module
class TimeProviderModule {

    @Provides
    fun provideClock(): TimeProvider {
        return TimeProvider()
    }
}