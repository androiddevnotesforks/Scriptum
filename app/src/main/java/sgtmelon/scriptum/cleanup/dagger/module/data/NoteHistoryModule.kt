package sgtmelon.scriptum.cleanup.dagger.module.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.data.noteHistory.NoteHistoryImpl

@Module
class NoteHistoryModule {

    @Provides
    @Singleton
    fun provideNoteHistory(): NoteHistory {
        return NoteHistoryImpl()
    }
}