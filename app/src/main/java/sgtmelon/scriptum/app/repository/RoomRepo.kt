package sgtmelon.scriptum.app.repository

import android.content.Context
import sgtmelon.scriptum.app.room.RoomDb

class RoomRepo(private val context: Context): IRoomRepo {

    private val db: RoomDb
        get() = RoomDb.provideDb(context)

    fun beforeClose(function: () -> Unit) {
        function.invoke()
        db.close()
    }



    companion object {
        fun getInstance(context: Context): IRoomRepo = RoomRepo(context)
    }

}