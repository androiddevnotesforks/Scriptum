package sgtmelon.scriptum.cleanup.dagger.component.note

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragmentImpl

/**
 * Component for [RollNoteFragmentImpl].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface RollNoteComponent {

    fun inject(fragment: RollNoteFragmentImpl)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: RollNoteFragmentImpl): Builder

        @BindsInstance
        fun set(isEdit: Boolean): Builder

        @BindsInstance
        fun set(noteState: NoteState): Builder

        @BindsInstance
        fun set(id: Long): Builder

        @BindsInstance
        fun set(color: Color): Builder

        fun build(): RollNoteComponent
    }
}