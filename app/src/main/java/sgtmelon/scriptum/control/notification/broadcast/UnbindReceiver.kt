package sgtmelon.scriptum.control.notification.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.repository.BindRepo

/**
 * Ресивер для открепления заметки в [BindControl]
 *
 * @author SerjantArbuz
 */
class UnbindReceiver(private val callback: Callback? = null) : BroadcastReceiver() {

//    constructor(callback: Callback) : this() {
//        this.callback = callback
//    }
//
//    private var callback: Callback? = null
//

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(NOTE_ID_KEY, ID_UNDEFINED)

        if (id == ID_UNDEFINED) return

        val noteItem = BindRepo.getInstance(context).unbindNoteItem(id)
        BindControl(context, noteItem).cancelBind()

        Log.i("HERE", "callback == null is ${callback == null}")

        callback?.onResultUnbind()
    }

    interface Callback {
        fun onResultUnbind()
    }

    companion object {
        private const val ID_UNDEFINED = -1L

        const val NOTE_ID_KEY = "NOTE_ID_KEY"
    }

}