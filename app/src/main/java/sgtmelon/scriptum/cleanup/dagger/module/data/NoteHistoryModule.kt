package sgtmelon.scriptum.cleanup.dagger.module.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.presentation.control.note.input.NoteHistory
import sgtmelon.scriptum.cleanup.presentation.control.note.input.NoteHistoryImpl

@Module
class NoteHistoryModule {

    @Provides
    @Singleton
    fun provideNoteHistory(): NoteHistory {
        return NoteHistoryImpl()
    }
}