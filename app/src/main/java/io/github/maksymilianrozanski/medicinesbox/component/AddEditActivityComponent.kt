package io.github.maksymilianrozanski.medicinesbox.component

import dagger.Subcomponent
import io.github.maksymilianrozanski.medicinesbox.AddEditActivity
import io.github.maksymilianrozanski.medicinesbox.annotation.ActivityScope

@ActivityScope
@Subcomponent(modules = [(AddEditActivityComponent.Module::class)])
interface AddEditActivityComponent {

    fun inject(activity: AddEditActivity)

    @dagger.Module
    class Module {
    }
}