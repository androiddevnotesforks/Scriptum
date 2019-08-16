package sgtmelon.scriptum.control.alarm.callback

import android.net.Uri
import sgtmelon.scriptum.control.alarm.MelodyControl

/**
 * Interface for [MelodyControl]
 *
 * @author SerjantArbuz
 */
interface IMelodyControl {

    fun setupVolume()

    fun setupPlayer(uri: Uri, isLooping: Boolean)

    fun start()

    fun stop()

    fun release()

}