package sgtmelon.scriptum.infrastructure.service

import android.content.Context
import android.content.IntentFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sgtmelon.extensions.runMain
import sgtmelon.scriptum.domain.model.result.TidyUpResult
import sgtmelon.scriptum.domain.useCase.alarm.TidyUpAlarmUseCase
import sgtmelon.scriptum.domain.useCase.bind.GetBindNoteListUseCase
import sgtmelon.scriptum.domain.useCase.bind.GetNotificationCountUseCase
import sgtmelon.scriptum.domain.useCase.bind.UnbindNoteUseCase
import sgtmelon.scriptum.infrastructure.factory.SystemDelegatorFactory
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.receiver.service.EternalServiceReceiver
import sgtmelon.scriptum.infrastructure.screen.ScriptumApplication
import java.util.Calendar
import javax.inject.Inject

/**
 * Logic class for working with alarm's and notifications.
 */
class EternalServiceLogicImpl(private val context: Context) : EternalServiceLogic,
    EternalServiceReceiver.Callback {

    @Inject lateinit var tidyUpAlarm: TidyUpAlarmUseCase
    @Inject lateinit var getBindNotes: GetBindNoteListUseCase
    @Inject lateinit var getNotificationsCount: GetNotificationCountUseCase
    @Inject lateinit var unbindNote: UnbindNoteUseCase

    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }

    private val receiver = EternalServiceReceiver[this]

    private val system = SystemDelegatorFactory(context, lifecycle = null)

    override fun setup() {
        ScriptumApplication.component.inject(logic = this)

        context.registerReceiver(receiver, IntentFilter(ReceiverData.Filter.SYSTEM))

        /** Update all available data. */
        tidyUpAlarm()
        notifyAllNotes()
        notifyCount(count = null)
    }

    override fun release() {
        context.unregisterReceiver(receiver)
        system.toast.cancel()
    }

    override fun tidyUpAlarm() {
        ioScope.launch {
            tidyUpAlarm.invoke().collect {
                when (it) {
                    is TidyUpResult.Cancel -> cancelAlarm(it.noteId)
                    is TidyUpResult.Update -> setAlarm(it.noteId, it.calendar, showToast = false)
                }
            }
        }
    }

    override fun setAlarm(noteId: Long, calendar: Calendar, showToast: Boolean) {
        system.alarm.set(noteId, calendar, showToast)
    }

    override fun cancelAlarm(noteId: Long) = system.alarm.cancel(noteId)

    override fun notifyAllNotes() {
        ioScope.launch {
            val list = getBindNotes()
            runMain { system.bind.notifyNotes(list) }
        }
    }

    override fun cancelNote(noteId: Long) {
        ioScope.launch { unbindNote(noteId) }
        system.bind.cancelNote(noteId)
    }

    override fun notifyCount(count: Int?) {
        if (count != null) {
            system.bind.notifyCount(count)
        } else {
            ioScope.launch {
                val bindCount = getNotificationsCount()
                runMain { notifyCount(bindCount) }
            }
        }
    }

    override fun clearBind() = system.bind.clearRecent()

    override fun clearAlarm() = system.alarm.clear()

}