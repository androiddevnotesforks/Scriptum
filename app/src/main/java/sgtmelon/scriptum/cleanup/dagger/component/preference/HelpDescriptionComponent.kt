package sgtmelon.scriptum.cleanup.dagger.component.preference

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.module.HelpDescriptionModule
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.help.HelpDisappearActivity

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