package io.github.maksymilianrozanski.medicinesbox.component

import dagger.Subcomponent
import io.github.maksymilianrozanski.medicinesbox.MainActivity
import io.github.maksymilianrozanski.medicinesbox.annotation.ActivityScope

@ActivityScope
@Subcomponent(modules = [(MainActivityComponent.Module::class)])
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    @dagger.Module
    class Module
}