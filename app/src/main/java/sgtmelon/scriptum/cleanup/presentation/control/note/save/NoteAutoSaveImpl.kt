package sgtmelon.scriptum.cleanup.presentation.control.note.save

import android.content.res.Resources
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.state.NoteSaveState
import sgtmelon.scriptum.infrastructure.utils.DelayedJob

/**
 * Class for help control note pause/auto save.
 */
class NoteAutoSaveImpl(
    lifecycle: Lifecycle,
    resources: Resources,
    private val state: NoteSaveState,
    private val callback: Callback
) : NoteAutoSave,
    DefaultLifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    private val saveDelay = DelayedJob(lifecycle)

    private val periodGap: Long = run {
        val array = resources.getIntArray(R.array.pref_note_save_period_array)
        return@run array[state.savePeriod.ordinal].toLong()
    }

    /**
     * [onPause] happen not only if application close (e.g. if we close activity).
     * In some cases we must skip note saving in [onPauseSave].
     */
    private var skipPauseSave = false

    override fun skipPauseSave() {
        skipPauseSave = true
    }

    override fun changeAutoSaveWork(isWork: Boolean) {
        if (!state.isAutoSaveOn) return

        saveDelay.cancel()

        if (isWork) {
            saveDelay.start(periodGap) {
                callback.onAutoSave()
                changeAutoSaveWork(isWork = true)
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        skipPauseSave = false
        changeAutoSaveWork(callback.isAutoSaveAvailable)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        if (callback.isPauseSaveAvailable) {
            onPauseSave()
            changeAutoSaveWork(isWork = false)
        }
    }

    private fun onPauseSave() {
        if (!state.isPauseSaveOn) return

        if (!skipPauseSave) {
            callback.onAutoSave()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        changeAutoSaveWork(isWork = false)
    }

    interface Callback {
        fun onAutoSave()
        val isAutoSaveAvailable: Boolean
        val isPauseSaveAvailable: Boolean
    }
}