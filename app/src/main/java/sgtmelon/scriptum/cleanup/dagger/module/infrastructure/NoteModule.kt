package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import android.content.Context
import androidx.lifecycle.Lifecycle
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.screen.note.save.NoteSave
import sgtmelon.scriptum.infrastructure.screen.note.save.NoteSaveImpl

@Module
class NoteModule {

    @Provides
    @ActivityScope
    fun provideNoteSave(
        lifecycle: Lifecycle,
        context: Context,
        preferencesRepo: PreferencesRepo,
        callback: NoteSaveImpl.Callback
    ): NoteSave {
        return NoteSaveImpl(lifecycle, context.resources, preferencesRepo.saveState, callback)
    }
}