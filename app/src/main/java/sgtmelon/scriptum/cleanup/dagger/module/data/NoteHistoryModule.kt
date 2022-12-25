package sgtmelon.scriptum.cleanup.dagger.module.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl

@Module
class NoteHistoryModule {

    @Provides
    @Singleton
    fun provideNoteHistory(): IInputControl {
        return InputControl()
    }
}