package sgtmelon.scriptum.infrastructure.screen.splash

import android.os.Bundle
import sgtmelon.scriptum.cleanup.domain.model.annotation.OpenFrom
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.key.SplashOpen
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

// TODO tests
class SplashBundleProvider {

    fun getData(bundle: Bundle?, isFirstStart: Boolean): SplashOpen {
        return when (bundle?.getString(OpenFrom.INTENT_KEY)) {
            OpenFrom.ALARM -> getAlarmData(bundle)
            OpenFrom.BIND -> getBindData(bundle)
            OpenFrom.NOTIFICATIONS -> SplashOpen.Notifications
            OpenFrom.HELP_DISAPPEAR -> SplashOpen.HelpDisappear
            OpenFrom.CREATE_TEXT -> SplashOpen.CreateNote(NoteType.TEXT)
            OpenFrom.CREATE_ROLL -> SplashOpen.CreateNote(NoteType.ROLL)
            else -> getIntro(isFirstStart)
        }
    }

    private fun getAlarmData(bundle: Bundle): SplashOpen.Alarm {
        val id = bundle.getLong(Note.Intent.ID, Note.Default.ID)
        return SplashOpen.Alarm(id)
    }

    private fun getBindData(bundle: Bundle): SplashOpen.Note {
        val id = bundle.getLong(Note.Intent.ID, Note.Default.ID)
        val color = bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR)
        val type = bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE)

        return SplashOpen.Note(id, color, type)
    }

    private fun getIntro(isFirstStart: Boolean): SplashOpen {
        return if (isFirstStart) SplashOpen.Intro else SplashOpen.Simple
    }
}