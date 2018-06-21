package io.github.maksymilianrozanski.medicinesbox.module

import dagger.Module
import dagger.Provides
import io.github.maksymilianrozanski.medicinesbox.data.TimeProvider
import javax.inject.Singleton

@Module
@Singleton
open class TimeProviderModule {

    @Provides
    open fun provideClock(): TimeProvider {
        return TimeProvider()
    }
}