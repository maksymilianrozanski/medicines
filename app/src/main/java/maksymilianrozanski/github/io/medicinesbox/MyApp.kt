package maksymilianrozanski.github.io.medicinesbox

import android.app.Application
import android.content.Context
import maksymilianrozanski.github.io.medicinesbox.component.AppComponent
import maksymilianrozanski.github.io.medicinesbox.component.DaggerAppComponent
import maksymilianrozanski.github.io.medicinesbox.module.AppModule
import maksymilianrozanski.github.io.medicinesbox.module.ContextModule
import maksymilianrozanski.github.io.medicinesbox.module.DatabaseModule

class MyApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .contextModule(ContextModule(this))
                .appModule(AppModule(this))
                .databaseModule(DatabaseModule())
                .build()
        appComponent.inject(this)
    }

    companion object {
        fun get(context: Context): MyApp {
            return context.applicationContext as MyApp
        }
    }
}