package sgtmelon.scriptum.cleanup.dagger.component.preference.develop

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.ServiceDevelopFragment

/**
 * Component for [ServiceDevelopFragment]
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface ServiceDevelopComponent {

    fun inject(fragment: ServiceDevelopFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: ServiceDevelopFragment): Builder

        fun build(): ServiceDevelopComponent
    }
}