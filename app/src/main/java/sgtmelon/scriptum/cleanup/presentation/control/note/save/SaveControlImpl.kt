package sgtmelon.scriptum.cleanup.presentation.control.note.save

import android.content.res.Resources
import androidx.annotation.MainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sgtmelon.common.utils.runMain
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.state.NoteSaveState
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Class for help control note pause/auto save.
 */
class SaveControlImpl(
    resources: Resources,
    private val saveState: NoteSaveState,
    private val callback: Callback
) : SaveControl {

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null

    private val periodTime: Long? = try {
        val timeArray = resources.getIntArray(R.array.pref_note_save_time_array)
        timeArray[saveState.savePeriod.ordinal].toLong()
    } catch (e: Throwable) {
        e.record()
        null
    }

    /**
     * onPause happen not only if application close (e.g. if we close activity).
     * In this cases we must skip note saving.
     */
    override var isNeedSave = true

    override fun changeAutoSaveWork(isWork: Boolean) {
        if (!saveState.isAutoSaveOn) return

        job?.cancel()
        job = null

        if (isWork) {
            val period = periodTime ?: return

            job = ioScope.launch {
                delay(period)
                runMain { makeSave() }
            }
        }
    }

    @MainThread
    private fun makeSave() {
        job = null
        callback.onResultSaveControl()
        changeAutoSaveWork(isWork = true)
    }

    override fun onPauseSave() {
        if (!saveState.isPauseSaveOn) return

        if (isNeedSave) {
            callback.onResultSaveControl()
        }
    }

    interface Callback {
        fun onResultSaveControl()
    }
}