package sgtmelon.scriptum.cleanup.dagger.component.note

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Component for [TextNoteFragmentImpl].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface TextNoteComponent {

    fun inject(fragment: TextNoteFragmentImpl)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: TextNoteFragmentImpl): Builder

        @BindsInstance
        fun set(isEdit: Boolean): Builder

        @BindsInstance
        fun set(noteState: NoteState): Builder

        @BindsInstance
        fun set(id: Long): Builder

        @BindsInstance
        fun set(color: Color): Builder

        fun build(): TextNoteComponent
    }
}