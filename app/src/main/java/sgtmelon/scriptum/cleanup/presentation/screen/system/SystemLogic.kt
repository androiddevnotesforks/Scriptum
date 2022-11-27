package sgtmelon.scriptum.cleanup.presentation.screen.system

import android.content.Context
import android.content.IntentFilter
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sgtmelon.extensions.runMain
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.domain.model.result.TidyUpResult
import sgtmelon.scriptum.domain.useCase.alarm.TidyUpAlarmUseCase
import sgtmelon.scriptum.domain.useCase.bind.GetBindNoteListUseCase
import sgtmelon.scriptum.domain.useCase.bind.GetNotificationCountUseCase
import sgtmelon.scriptum.domain.useCase.bind.UnbindNoteUseCase
import sgtmelon.scriptum.infrastructure.factory.DelegatorFactory
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData

/**
 * Logic class for working with alarm's and notifications.
 */
class SystemLogic(private val context: Context) : ISystemLogic,
    SystemReceiver.Callback {

    private var _delegators: DelegatorFactory? = null
    private val delegators get() = _delegators

    @Inject lateinit var tidyUpAlarm: TidyUpAlarmUseCase
    @Inject lateinit var getBindNotes: GetBindNoteListUseCase
    @Inject lateinit var getNotificationsCount: GetNotificationCountUseCase
    @Inject lateinit var unbindNote: UnbindNoteUseCase

    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }

    private val receiver = SystemReceiver[this]

    //region cleanup

    override fun setup() {
        ScriptumApplication.component.inject(logic = this)

        _delegators = DelegatorFactory(context, lifecycle = null)
        context.registerReceiver(receiver, IntentFilter(ReceiverData.Filter.SYSTEM))

        /** Update all available data. */
        tidyUpAlarm()
        notifyAllNotes()
        notifyCount(count = null)
    }

    override fun release() {
        context.unregisterReceiver(receiver)
        delegators?.toast?.cancel()
    }

    //endregion

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
        delegators?.alarm?.set(noteId, calendar, showToast)
    }

    override fun cancelAlarm(noteId: Long) {
        delegators?.alarm?.cancel(noteId)
    }

    override fun notifyAllNotes() {
        ioScope.launch {
            val list = getBindNotes()
            runMain { delegators?.bind?.notifyNotes(list) }
        }
    }

    override fun cancelNote(noteId: Long) {
        ioScope.launch { unbindNote(noteId) }
        delegators?.bind?.cancelNote(noteId)
    }

    override fun notifyCount(count: Int?) {
        if (count != null) {
            delegators?.bind?.notifyCount(count)
        } else {
            ioScope.launch {
                val bindCount = getNotificationsCount()
                runMain { notifyCount(bindCount) }
            }
        }
    }

    override fun clearBind() {
        delegators?.bind?.clearRecent()
    }

    override fun clearAlarm() {
        delegators?.alarm?.clear()
    }
}