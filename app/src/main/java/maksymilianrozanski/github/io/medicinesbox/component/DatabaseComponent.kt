package maksymilianrozanski.github.io.medicinesbox.component

import dagger.Component
import maksymilianrozanski.github.io.medicinesbox.data.MedicinesDatabaseHandler
import maksymilianrozanski.github.io.medicinesbox.module.DatabaseModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(DatabaseModule::class)])
interface DatabaseComponent {

    fun getDatabaseHandler(): MedicinesDatabaseHandler
}