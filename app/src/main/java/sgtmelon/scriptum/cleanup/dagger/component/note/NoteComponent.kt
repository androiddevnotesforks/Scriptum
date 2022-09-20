package sgtmelon.scriptum.cleanup.dagger.component.note

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity

/**
 * Component for [NoteActivity].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface NoteComponent {

    fun inject(activity: NoteActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: NoteActivity): Builder

        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): NoteComponent
    }
}