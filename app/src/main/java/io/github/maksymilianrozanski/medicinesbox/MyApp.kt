package io.github.maksymilianrozanski.medicinesbox

import android.app.Application
import android.content.Context
import io.github.maksymilianrozanski.medicinesbox.component.AppComponent
import io.github.maksymilianrozanski.medicinesbox.component.DaggerAppComponent
import io.github.maksymilianrozanski.medicinesbox.module.AppModule
import io.github.maksymilianrozanski.medicinesbox.module.ContextModule
import io.github.maksymilianrozanski.medicinesbox.module.DatabaseModule

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