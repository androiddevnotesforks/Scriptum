package sgtmelon.scriptum.infrastructure.screen.alarm

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent

/**
 * Bundle data provider for [AlarmActivity] screen.
 */
class AlarmBundleProvider {

    fun getNoteId(bundle: Bundle?): Long? {
        return bundle?.getLong(Intent.ID, Default.ID)?.takeIf { it != Default.ID }
    }
}