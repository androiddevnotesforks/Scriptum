package sgtmelon.scriptum.infrastructure.system.delegators.melody

import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build

/**
 * Setup parameters for [MelodyPlayDelegator].
 */
class MelodyPlayParams(
    audioManager: AudioManager,
    streamType: Int,
    listener: AudioManager.OnAudioFocusChangeListener
) {

    val attributes: AudioAttributes = AudioAttributes.Builder()
        .setUsage(getAudioAttributes(streamType))
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    private fun getAudioAttributes(streamType: Int): Int {
        return when (streamType) {
            AudioManager.STREAM_ALARM -> AudioAttributes.USAGE_ALARM
            AudioManager.STREAM_MUSIC -> AudioAttributes.USAGE_MEDIA
            else -> AudioAttributes.USAGE_MEDIA
        }
    }

    val focusRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
            .setAudioAttributes(attributes)
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(listener)
            .build()
    } else {
        null
    }

    /** This value needed for save initial volume level. */
    val initialVolume = audioManager.getStreamVolume(streamType)

    private val maxVolume = audioManager.getStreamMaxVolume(streamType)
    private val minVolume = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        audioManager.getStreamMinVolume(streamType)
    } else {
        0
    }

    val minMaxVolumePair = minVolume to maxVolume

}