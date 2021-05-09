package sgtmelon.scriptum.dagger.component.preference

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.HelpDescriptionModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.HelpDescriptionActivity

/**
 * Component for [HelpDescriptionActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    HelpDescriptionModule::class
])
interface HelpDescriptionComponent {

    fun inject(activity: HelpDescriptionActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: HelpDescriptionActivity): Builder

        fun build(): HelpDescriptionComponent
    }
}