package sgtmelon.scriptum.dagger.component.preference

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.module.HelpDescriptionModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.help.HelpDisappearActivity

/**
 * Component for [HelpDisappearActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    HelpDescriptionModule::class
])
interface HelpDescriptionComponent {

    fun inject(activity: HelpDisappearActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: HelpDisappearActivity): Builder

        fun build(): HelpDescriptionComponent
    }
}