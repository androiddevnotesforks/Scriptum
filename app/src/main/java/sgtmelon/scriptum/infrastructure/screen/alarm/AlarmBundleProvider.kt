package sgtmelon.scriptum.infrastructure.screen.alarm

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent

/**
 * Bundle data provider for [AlarmActivity] screen.
 */
class AlarmBundleProvider {

    // TODO record exception if noteId is null after getData

    private var _noteId: Long? = null
    val noteId: Long? get() = _noteId

    fun getData(bundle: Bundle?) {
        _noteId = bundle?.getLong(Intent.ID, Default.ID)?.takeIf { it != Default.ID }
    }

    fun saveData(outState: Bundle) {
        val noteId = noteId ?: return
        outState.putLong(Intent.ID, noteId)
    }
}