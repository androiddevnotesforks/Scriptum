package sgtmelon.scriptum.cleanup.dagger.component.preference.develop

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.develop.screen.print.PrintDevelopActivity

/**
 * Component for [PrintDevelopActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface PrintDevelopComponent {

    fun inject(activity: PrintDevelopActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        @BindsInstance
        fun set(type: PrintType): Builder

        fun build(): PrintDevelopComponent
    }
}