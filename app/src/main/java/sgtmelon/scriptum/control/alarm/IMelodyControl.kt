package sgtmelon.scriptum.control.alarm

import android.net.Uri

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