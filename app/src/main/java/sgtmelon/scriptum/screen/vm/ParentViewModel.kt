package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.repository.room.RoomRepo

/**
 * Родительский ViewModel
 *
 * @author SerjantArbuz
 */
abstract class ParentViewModel(application: Application) : AndroidViewModel(application) {

    protected val context: Context = application.applicationContext

    protected val iAlarmRepo by lazy { AlarmRepo.getInstance(context) }

    protected val iPreferenceRepo = PreferenceRepo(context)
    protected val iRoomRepo = RoomRepo.getInstance(context)

}