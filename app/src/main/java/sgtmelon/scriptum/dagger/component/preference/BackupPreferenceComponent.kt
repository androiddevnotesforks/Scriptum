package sgtmelon.scriptum.dagger.component.preference

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.module.base.ControlModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ParserModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.BackupPreferenceFragment

/**
 * Component for [BackupPreferenceFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class,
    ControlModule::class,
    ParserModule::class
])
interface BackupPreferenceComponent {

    fun inject(fragment: BackupPreferenceFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: BackupPreferenceFragment): Builder

        fun build(): BackupPreferenceComponent
    }
}