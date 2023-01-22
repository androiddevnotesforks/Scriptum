package sgtmelon.scriptum.cleanup.dagger.component.note

import androidx.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.control.note.save.NoteSaveImpl
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragmentImpl

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
        fun set(lifecycle: Lifecycle): Builder

        @BindsInstance
        fun set(callback: NoteSaveImpl.Callback): Builder

        @BindsInstance
        fun set(init: NoteInit): Builder

        fun build(): TextNoteComponent
    }
}