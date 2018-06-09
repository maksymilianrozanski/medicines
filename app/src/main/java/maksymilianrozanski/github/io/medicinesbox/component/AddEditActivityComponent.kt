package maksymilianrozanski.github.io.medicinesbox.component

import dagger.Subcomponent
import maksymilianrozanski.github.io.medicinesbox.AddEditActivity
import maksymilianrozanski.github.io.medicinesbox.annotation.ActivityScope

@ActivityScope
@Subcomponent(modules = [(AddEditActivityComponent.Module::class)])
interface AddEditActivityComponent {

    fun inject(activity: AddEditActivity)

    @dagger.Module
    class Module {
    }
}