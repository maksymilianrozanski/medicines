package maksymilianrozanski.github.io.medicinesbox.component

import dagger.Subcomponent
import maksymilianrozanski.github.io.medicinesbox.MainActivity
import maksymilianrozanski.github.io.medicinesbox.annotation.ActivityScope

@ActivityScope
@Subcomponent(modules = [(MainActivityComponent.Module::class)])
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    @dagger.Module
    class Module {
    }
}