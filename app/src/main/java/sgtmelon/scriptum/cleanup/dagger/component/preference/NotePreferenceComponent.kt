package sgtmelon.scriptum.cleanup.dagger.component.preference

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment

/**
 * Component for [NotePreferenceFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface NotePreferenceComponent {

    fun inject(fragment: NotePreferenceFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: NotePreferenceFragment): Builder

        fun build(): NotePreferenceComponent
    }
}