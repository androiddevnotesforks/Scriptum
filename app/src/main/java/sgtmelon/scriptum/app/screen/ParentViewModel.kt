package sgtmelon.scriptum.app.screen

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.app.repository.IRoomRepo
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.office.utils.Preference

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