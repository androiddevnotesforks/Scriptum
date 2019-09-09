package sgtmelon.scriptum.interactor

import android.content.Context
import sgtmelon.scriptum.repository.room.IRoomRepo
import sgtmelon.scriptum.repository.room.RoomRepo

/**
 * Interactor for backup data from RoomDb
 *
 * @author SerjantArbuz
 */
class BackupInteractor(private val context: Context?) : IBackupInteractor {

    private val iRoomRepo: IRoomRepo? = context?.let { RoomRepo(it) }

    override fun export(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}