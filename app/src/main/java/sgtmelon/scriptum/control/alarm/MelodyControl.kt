package sgtmelon.scriptum.control.alarm

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import sgtmelon.scriptum.control.alarm.callback.IMelodyControl

/**
 * Class for help control [MediaPlayer] and [AudioManager]
 */
class MelodyControl(private val context: Context) : IMelodyControl,
        AudioManager.OnAudioFocusChangeListener {

    // TODO #RELEASE2 coroutine
    // TODO #RELEASE2 when melody ends return melody which play before

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

    /**
     * [startVolume] it is start volume for reset in [release]
     */
    private val startVolume = audioManager?.getStreamVolume(AudioManager.STREAM_ALARM)

    private val maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_ALARM)
    private val minVolume = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        audioManager?.getStreamMinVolume(AudioManager.STREAM_ALARM)
    } else {
        0
    }

    private var mediaPlayer: MediaPlayer? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()

            audioManager?.requestAudioFocus(AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(audioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(this).build())
        } else {
            audioManager?.requestAudioFocus(
                    this, AudioManager.STREAM_ALARM, AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val audioAttributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()

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
        mediaPlayer?.start()
    }

    override fun stop() {
        mediaPlayer?.stop()
    }

    override fun release() {
        mediaPlayer?.release()

        increaseHandler.removeCallbacks(increaseRunnable)

        /**
         * Return volume which was before
         */
        setVolume(startVolume)
    }

    // TODO #RELEASE2
    override fun onAudioFocusChange(focusChange: Int) = Unit


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