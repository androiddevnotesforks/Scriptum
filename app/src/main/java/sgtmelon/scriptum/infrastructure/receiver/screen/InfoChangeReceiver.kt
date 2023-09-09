package sgtmelon.scriptum.infrastructure.receiver.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.extensions.decode
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Values
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.receiver.screen.InfoChangeReceiver.Callback

/**
 * Receiver for catch screen changes (of info visibility) and this way prevent awkward animation
 * (hide list and show empty info; and vice versa).
 *
 * How it will work:
 * - We make some changes on the screen and can predict how it affect to another screen. E.g.
 *   BIN screen is empty, and from NOTES screen we delete note -> BIN screen will receive this
 *   item and list will be shown.
 * - When we make an action -> send a broadcast with [ShowListState].
 * - In [Callback] implementation -> change screen state rely on received [ShowListState].
 */
class InfoChangeReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.getStringExtra(Values.COMMAND) != Command.UI.INFO_CHANGE) return

        val state = intent.getStringExtra(IntentData.ShowList.Key.VALUE)?.decode<ShowListState>()
        if (state != null) {
            callback?.onReceiveInfoChange(state)
        }
    }

    interface Callback {
        fun onReceiveInfoChange(state: ShowListState)
    }

    companion object {
        operator fun get(callback: Callback): BroadcastReceiver {
            return InfoChangeReceiver().apply { this.callback = callback }
        }
    }
}