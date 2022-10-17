package sgtmelon.scriptum.cleanup.dagger.component.preference.develop

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.develop.screen.print.ServiceDevelopFragment

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