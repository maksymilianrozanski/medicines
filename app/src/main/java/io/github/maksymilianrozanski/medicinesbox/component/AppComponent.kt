package io.github.maksymilianrozanski.medicinesbox.component

import android.app.Application
import android.content.Context
import dagger.Component
import io.github.maksymilianrozanski.medicinesbox.MyApp
import io.github.maksymilianrozanski.medicinesbox.annotation.ApplicationContext
import io.github.maksymilianrozanski.medicinesbox.data.MedicinesDatabaseHandler
import io.github.maksymilianrozanski.medicinesbox.data.TimeProvider
import io.github.maksymilianrozanski.medicinesbox.module.AppModule
import io.github.maksymilianrozanski.medicinesbox.module.DatabaseModule
import io.github.maksymilianrozanski.medicinesbox.module.QuantityCalculatorModule
import io.github.maksymilianrozanski.medicinesbox.module.TimeProviderModule
import io.github.maksymilianrozanski.medicinesbox.utilities.QuantityCalculator
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class,
    TimeProviderModule::class, QuantityCalculatorModule::class])
interface AppComponent {

    fun inject(myApp: MyApp)

    fun plus(module: MainActivityComponent.Module): MainActivityComponent

    fun plusAddEditActivity(module: AddEditActivityComponent.Module): AddEditActivityComponent

    @ApplicationContext
    fun getContext(): Context

    fun getApplication(): Application

    fun getDatabaseHandler(): MedicinesDatabaseHandler

    fun getTimeProvider(): TimeProvider

    fun getQuantityCalculator(): QuantityCalculator

}