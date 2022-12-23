package sgtmelon.scriptum.infrastructure.screen.splash

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.model.annotation.AppOpenFrom
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.key.SplashOpen
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.parent.ParentBundleProvider

class SplashBundleProvider : ParentBundleProvider {

    private var key: String? = null

    private var _open: SplashOpen = SplashOpen.Main
    val open get() = _open

    override fun getData(bundle: Bundle?) {
        key = bundle?.getString(AppOpenFrom.INTENT_KEY)?.ifEmpty { null } ?: return

        _open = when (key) {
            AppOpenFrom.ALARM -> getAlarmData(bundle)
            AppOpenFrom.BIND_NOTE -> getBindData(bundle)
            AppOpenFrom.NOTIFICATIONS -> SplashOpen.Notifications
            AppOpenFrom.HELP_DISAPPEAR -> SplashOpen.HelpDisappear
            AppOpenFrom.CREATE_TEXT -> SplashOpen.CreateNote(NoteType.TEXT)
            AppOpenFrom.CREATE_ROLL -> SplashOpen.CreateNote(NoteType.ROLL)
            else -> SplashOpen.Main
        }
    }

    private fun getAlarmData(bundle: Bundle): SplashOpen.Alarm {
        val id = bundle.getLong(Note.Intent.ID, Note.Default.ID)
        return SplashOpen.Alarm(id)
    }

    private fun getBindData(bundle: Bundle): SplashOpen.BindNote {
        val id = bundle.getLong(Note.Intent.ID, Note.Default.ID)
        val type = bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE)
        val color = bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR)
        val name = bundle.getString(Note.Intent.NAME, Note.Default.NAME) ?: Note.Default.NAME

        return SplashOpen.BindNote(id, type, color, name)
    }

    override fun saveData(outState: Bundle) {
        outState.putString(AppOpenFrom.INTENT_KEY, key)
    }
}