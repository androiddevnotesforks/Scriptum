package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.repository.Preference
import sgtmelon.scriptum.repository.room.IRoomRepo
import sgtmelon.scriptum.repository.room.RoomRepo

/**
 * Родительский ViewModel
 *
 * @author SerjantArbuz
 */
abstract class ParentViewModel(application: Application) : AndroidViewModel(application) {

    protected val context: Context = application.applicationContext

    protected val preference = Preference(context)
    protected val iRoomRepo: IRoomRepo = RoomRepo.getInstance(context)

}