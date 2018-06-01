package maksymilianrozanski.github.io.medicinesbox.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import maksymilianrozanski.github.io.medicinesbox.MyApp
import maksymilianrozanski.github.io.medicinesbox.annotation.ApplicationContext

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