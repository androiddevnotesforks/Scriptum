package sgtmelon.scriptum.cleanup.dagger.component.note

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.infrastructure.model.key.NoteState

/**
 * Component for [RollNoteFragment].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface RollNoteComponent {

    fun inject(fragment: RollNoteFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: RollNoteFragment): Builder

        @BindsInstance
        fun set(isEdit: Boolean): Builder

        @BindsInstance
        fun set(noteState: NoteState): Builder

        fun build(): RollNoteComponent
    }
}