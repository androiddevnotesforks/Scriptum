package sgtmelon.scriptum.infrastructure.screen.data

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note.Intent

/**
 * Data provider for
 */
class AlarmBundleProvider {

    fun getNoteId(bundle: Bundle?): Long? {
        return bundle?.getLong(Intent.ID, Default.ID)?.takeIf { it != Default.ID }
    }
}