package sgtmelon.scriptum.cleanup.dagger.module.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.presentation.control.note.input.INoteHistory
import sgtmelon.scriptum.cleanup.presentation.control.note.input.NoteHistory

@Module
class NoteHistoryModule {

    @Provides
    @Singleton
    fun provideNoteHistory(): INoteHistory {
        return NoteHistory()
    }
}