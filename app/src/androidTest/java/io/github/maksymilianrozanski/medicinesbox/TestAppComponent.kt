package io.github.maksymilianrozanski.medicinesbox

import dagger.Component
import io.github.maksymilianrozanski.medicinesbox.component.AppComponent
import io.github.maksymilianrozanski.medicinesbox.module.AppModule
import io.github.maksymilianrozanski.medicinesbox.module.DatabaseModule
import io.github.maksymilianrozanski.medicinesbox.module.QuantityCalculatorModule
import io.github.maksymilianrozanski.medicinesbox.module.TimeProviderModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class,
    TimeProviderModule::class, QuantityCalculatorModule::class])
interface TestAppComponent : AppComponent {
    fun inject(test: MainActivityDbTest)
    fun inject(test: AddEditActivitySavingTest)
}