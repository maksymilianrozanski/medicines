package io.github.maksymilianrozanski.medicinesbox.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.maksymilianrozanski.medicinesbox.MyApp
import io.github.maksymilianrozanski.medicinesbox.annotation.ApplicationContext

@Module
class AppModule(var myApp: MyApp) {

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return myApp
    }

    @Provides
    fun provideApplication(): Application {
        return myApp
    }
}