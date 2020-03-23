package sgtmelon.scriptum.presentation.control.system

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Handler
import sgtmelon.scriptum.presentation.control.system.callback.IMelodyControl

/**
 * Class for help control [MediaPlayer] and [AudioManager]
 */
class MelodyControl(private val context: Context) : IMelodyControl,
        AudioManager.OnAudioFocusChangeListener {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

    private val audioAttributes = if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
    } else {
        null
    }

    private val audioFocusRequest = if (VERSION.SDK_INT >= VERSION_CODES.O) {
        audioAttributes?.let {
            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(it)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(this)
                    .build()
        }
    } else {
        null
    }


    /**
     * This value need for reset in [release]
     */
    private val startVolume = audioManager?.getStreamVolume(AudioManager.STREAM_ALARM)

    private val maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_ALARM)
    private val minVolume = if (VERSION.SDK_INT >= VERSION_CODES.P) {
        audioManager?.getStreamMinVolume(AudioManager.STREAM_ALARM)
    } else {
        0
    }

    private var mediaPlayer: MediaPlayer? = null

    private var increaseCurrent = 0
    private var increaseMax = 0

    private val increaseHandler = Handler()
    private val increaseRunnable: Runnable = object : Runnable {
        override fun run() {
            if (maxVolume == null || increaseCurrent >= increaseMax) return

            setVolume(increaseCurrent++)
            increaseHandler.postDelayed(this, INCREASE_PERIOD)
        }
    }


    override fun setupVolume(volume: Int, increase: Boolean) {
        if (maxVolume == null || minVolume == null) return

        if (increase) {
            increaseCurrent = minVolume
            increaseMax = maxVolume.getPercentOf(volume)

            if (increaseMax < minVolume) increaseMax = minVolume

            setVolume(increaseCurrent++)
            increaseHandler.post(increaseRunnable)
        } else {
            setVolume(maxVolume.getPercentOf(volume))
        }
    }

    override fun setupPlayer(uri: Uri, isLooping: Boolean) {
        mediaPlayer = MediaPlayer().apply {
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                setAudioAttributes(audioAttributes)
            } else {
                setAudioStreamType(AudioManager.STREAM_ALARM)
            }

            setDataSource(context, uri)
            prepare()

            this.isLooping = isLooping
        }
    }


    override fun start() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            audioFocusRequest?.let { audioManager?.requestAudioFocus(it) }
        } else {
            audioManager?.requestAudioFocus(
                    this, AudioManager.STREAM_ALARM, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }

        mediaPlayer?.start()
    }

    override fun stop() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            audioFocusRequest?.let { audioManager?.abandonAudioFocusRequest(it) }
        } else {
            audioManager?.abandonAudioFocus(this)
        }

        mediaPlayer?.stop()
    }

    override fun release() {
        mediaPlayer?.release()

        increaseHandler.removeCallbacksAndMessages(null)

        /**
         * Return volume which was before
         */
        setVolume(startVolume)
    }

    override fun onAudioFocusChange(focusChange: Int) {}


    /**
     * Set volume for device
     */
    private fun setVolume(value: Int?) {
        if (value == null) return

        audioManager?.setStreamVolume(AudioManager.STREAM_ALARM, value, AudioManager.ADJUST_SAME)
    }

    /**
     * [this] - max value, [value] - current value
     */
    private fun Int.getPercentOf(value: Int) = this * value / 100

    companion object {
        private const val INCREASE_PERIOD = 1000L
    }

}