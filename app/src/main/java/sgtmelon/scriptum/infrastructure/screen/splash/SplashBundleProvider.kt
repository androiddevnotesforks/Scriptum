package sgtmelon.scriptum.infrastructure.screen.splash

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.model.annotation.AppOpenFrom
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.key.SplashOpen
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

class SplashBundleProvider {

    fun getData(bundle: Bundle?, isFirstStart: Boolean): SplashOpen {
        return when (bundle?.getString(AppOpenFrom.INTENT_KEY)) {
            AppOpenFrom.ALARM -> getAlarmData(bundle)
            AppOpenFrom.BIND_NOTE -> getBindData(bundle)
            AppOpenFrom.NOTIFICATIONS -> SplashOpen.Notifications
            AppOpenFrom.HELP_DISAPPEAR -> SplashOpen.HelpDisappear
            AppOpenFrom.CREATE_TEXT -> SplashOpen.CreateNote(NoteType.TEXT)
            AppOpenFrom.CREATE_ROLL -> SplashOpen.CreateNote(NoteType.ROLL)
            else -> getIntro(isFirstStart)
        }
    }

    private fun getAlarmData(bundle: Bundle): SplashOpen.Alarm {
        val id = bundle.getLong(Note.Intent.ID, Note.Default.ID)
        return SplashOpen.Alarm(id)
    }

    private fun getBindData(bundle: Bundle): SplashOpen.BindNote {
        val id = bundle.getLong(Note.Intent.ID, Note.Default.ID)
        val color = bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR)
        val type = bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE)

        return SplashOpen.BindNote(id, color, type)
    }

    private fun getIntro(isFirstStart: Boolean): SplashOpen {
        return if (isFirstStart) SplashOpen.Intro else SplashOpen.Main
    }
}