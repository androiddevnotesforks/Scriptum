package sgtmelon.scriptum.control.alarm.callback

import android.net.Uri
import sgtmelon.scriptum.control.alarm.MelodyControl

/**
 * Interface for comminication with [MelodyControl]
 */
interface IMelodyControl {

    fun setupVolume(volume: Int, increase: Boolean)

    fun setupPlayer(uri: Uri, isLooping: Boolean)

    fun start()

    fun stop()

    fun release()

}