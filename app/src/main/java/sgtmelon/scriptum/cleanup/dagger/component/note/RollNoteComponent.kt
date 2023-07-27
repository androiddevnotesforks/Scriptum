package sgtmelon.scriptum.cleanup.dagger.component.note

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.ListModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.NoteModule
import sgtmelon.scriptum.cleanup.dagger.module.infrastructure.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragment
import sgtmelon.scriptum.infrastructure.screen.note.save.NoteSaveImpl

/**
 * Component for [RollNoteFragment].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class, ListModule::class, NoteModule::class])
interface RollNoteComponent {

    fun inject(fragment: RollNoteFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        @BindsInstance
        fun set(lifecycle: Lifecycle): Builder

        @BindsInstance
        fun set(callback: NoteSaveImpl.Callback): Builder

        @BindsInstance
        fun set(init: NoteInit): Builder

        fun build(): RollNoteComponent
    }
}