package sgtmelon.scriptum.presentation.control.alarm.callback

import android.net.Uri
import sgtmelon.scriptum.presentation.control.alarm.MelodyControl

/**
 * Interface for communicate with [MelodyControl]
 */
interface IMelodyControl {

    fun setupVolume(volume: Int, increase: Boolean)

    fun setupPlayer(uri: Uri, isLooping: Boolean)

    fun start()

    fun stop()

    fun release()

}