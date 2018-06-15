package maksymilianrozanski.github.io.medicinesbox.module

import dagger.Module
import dagger.Provides
import maksymilianrozanski.github.io.medicinesbox.data.TimeProvider
import javax.inject.Singleton

@Module
@Singleton
open class TimeProviderModule {

    @Provides
    open fun provideClock(): TimeProvider {
        return TimeProvider()
    }
}