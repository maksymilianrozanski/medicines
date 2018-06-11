package maksymilianrozanski.github.io.medicinesbox

import dagger.Component
import maksymilianrozanski.github.io.medicinesbox.component.AppComponent
import maksymilianrozanski.github.io.medicinesbox.module.AppModule
import maksymilianrozanski.github.io.medicinesbox.module.DatabaseModule
import maksymilianrozanski.github.io.medicinesbox.module.TimeProviderModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, TimeProviderModule::class])
interface TestAppComponent :AppComponent{
    fun inject(test:MainActivityDbTest)
    fun inject(test:AddEditActivitySavingTest)
}