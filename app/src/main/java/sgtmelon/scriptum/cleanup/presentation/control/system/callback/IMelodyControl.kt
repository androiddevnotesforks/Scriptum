package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import android.net.Uri
import sgtmelon.scriptum.cleanup.presentation.control.system.MelodyControl

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