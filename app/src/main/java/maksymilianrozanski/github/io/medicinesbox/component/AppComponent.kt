package maksymilianrozanski.github.io.medicinesbox.component

import android.app.Application
import android.content.Context
import dagger.Component
import maksymilianrozanski.github.io.medicinesbox.MyApp
import maksymilianrozanski.github.io.medicinesbox.annotation.ApplicationContext
import maksymilianrozanski.github.io.medicinesbox.data.MedicinesDatabaseHandler
import maksymilianrozanski.github.io.medicinesbox.data.TimeProvider
import maksymilianrozanski.github.io.medicinesbox.module.AppModule
import maksymilianrozanski.github.io.medicinesbox.module.DatabaseModule
import maksymilianrozanski.github.io.medicinesbox.module.TimeProviderModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, TimeProviderModule::class])
interface AppComponent {

    fun inject(myApp: MyApp)

    fun plus(module: MainActivityComponent.Module): MainActivityComponent

    fun plusAddEditActivity(module: AddEditActivityComponent.Module): AddEditActivityComponent

    @ApplicationContext
    fun getContext(): Context

    fun getApplication(): Application

    fun getDatabaseHandler(): MedicinesDatabaseHandler

    fun getTimeProvider(): TimeProvider

}