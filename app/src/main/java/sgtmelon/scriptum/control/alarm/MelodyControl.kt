package sgtmelon.scriptum.control.alarm

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import sgtmelon.scriptum.control.alarm.callback.IMelodyControl

/**
 * Class for help control [MediaPlayer] and [AudioManager]
 *
 * @author SerjantArbuz
 */
class MelodyControl(private val context: Context) : IMelodyControl,
        AudioManager.OnAudioFocusChangeListener {

    // TODO #RELEASE1 coroutine

    // TODO #RELEASE1 when melody ends return melody which play before

    private var mediaPlayer: MediaPlayer? = null
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

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

    override fun setupVolume() {
        val maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_ALARM)

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupPlayer(uri: Uri, isLooping: Boolean) {
        mediaPlayer = MediaPlayer.create(context, uri).apply {
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
    }

    override fun onAudioFocusChange(focusChange: Int) {
        // TODO #RELEASE1
    }

}