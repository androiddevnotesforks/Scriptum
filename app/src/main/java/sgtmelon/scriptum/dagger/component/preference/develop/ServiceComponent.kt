package sgtmelon.scriptum.dagger.component.preference.develop

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.ServiceFragment

/**
 * Component for [ServiceFragment]
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface ServiceComponent {

    fun inject(fragment: ServiceFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: ServiceFragment): Builder

        fun build(): ServiceComponent
    }
}