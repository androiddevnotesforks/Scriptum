package sgtmelon.scriptum.cleanup.dagger.component.preference

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceFragment

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
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): NotePreferenceComponent
    }
}