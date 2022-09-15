package sgtmelon.scriptum.infrastructure.system.delegators.melody

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.math.ceil
import kotlin.math.max
import sgtmelon.extensions.getAudioService
import sgtmelon.extensions.getPercent
import sgtmelon.scriptum.infrastructure.screen.DelayJobDelegator

/**
 * Class, which delegates work of [MediaPlayer] and [AudioManager].
 *
 * [streamType] must be one of [AudioManager].STREAM_ values.
 */
class MelodyPlayDelegator(
    context: Context?,
    lifecycle: Lifecycle,
    private val streamType: Int
) : DefaultLifecycleObserver,
    AudioManager.OnAudioFocusChangeListener {

    init {
        lifecycle.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        release()
    }

    //region Variables

    private val audioManager = context?.getAudioService()

    private val params: MelodyPlayParams? = audioManager?.let {
        MelodyPlayParams(it, streamType, this)
    }

    /**
     * [increaseCurrent] - from there need start volume increase.
     * [increaseMax] - maximum, which may be reached from [increaseCurrent].
     * [isVolumeChanged] - detect was changed volume or not.
     */
    private var increaseCurrent = 0
    private var increaseMax = 0
    private var isVolumeChanged = false

    private val increaseDelayJob = DelayJobDelegator(lifecycle = null)

    private var mediaPlayer: MediaPlayer? = null

    //endregion

    fun setupVolume(volumePercent: Int, isIncrease: Boolean) {
        val (minVolume, maxVolume) = params?.minMaxVolumePair ?: return

        /** Round result volume towards greater value. */
        val volume = ceil(maxVolume.getPercent(volumePercent)).toInt()

        if (isIncrease) {
            /** Preparation before [start] call. */
            increaseCurrent = minVolume
            increaseMax = max(minVolume, volume)

            setVolume(minVolume)
        } else {
            setVolume(volume)
        }
    }

    fun setupPlayer(context: Context?, uri: Uri, isLooping: Boolean) {
        if (context == null) return

        val attributes = params?.attributes ?: return
        mediaPlayer = createMediaPlayer(context, attributes, uri, isLooping)
    }

    private fun createMediaPlayer(
        context: Context,
        attributes: AudioAttributes,
        uri: Uri,
        isLooping: Boolean
    ): MediaPlayer {
        val player = MediaPlayer()

        player.setAudioAttributes(attributes)
        player.setDataSource(context, uri)
        player.prepare()
        player.isLooping = isLooping

        return player
    }

    /**
     * Set volume for alarm.
     */
    private fun setVolume(value: Int) {
        isVolumeChanged = true
        audioManager?.setStreamVolume(streamType, value, AudioManager.ADJUST_SAME)
    }

    fun start(isIncrease: Boolean = false) {
        val mediaPlayer = mediaPlayer ?: return

        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val focusRequest = params?.focusRequest
            if (focusRequest != null) {
                audioManager?.requestAudioFocus(focusRequest)
            }
        } else {
            val durationHint = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            audioManager?.requestAudioFocus(this, streamType, durationHint)
        }

        mediaPlayer.start()

        if (isIncrease) {
            startVolumeIncrease()
        }
    }

    private fun startVolumeIncrease() {
        /** If reach maximum - break this loop. */
        if (increaseCurrent >= increaseMax) return

        setVolume(increaseCurrent++)
        increaseDelayJob.run(INCREASE_GAP) {
            startVolumeIncrease()
        }
    }

    fun stop() {
        val mediaPlayer = mediaPlayer ?: return

        if (!mediaPlayer.isPlaying) return

        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val focusRequest = params?.focusRequest
            if (focusRequest != null) {
                audioManager?.abandonAudioFocusRequest(focusRequest)
            }
        } else {
            audioManager?.abandonAudioFocus(this)
        }

        mediaPlayer.stop()
        increaseDelayJob.cancel()
    }

    fun release() {
        val mediaPlayer = mediaPlayer ?: return

        stop()
        mediaPlayer.release()

        /** Setup volume, which was during initialization (only if it was changed). */
        val initialVolume = params?.initialVolume
        if (initialVolume != null && isVolumeChanged) {
            setVolume(initialVolume)
        }

        /** If note set null when may get error after next call to mediaPlayer. */
        this.mediaPlayer = null
    }

    override fun onAudioFocusChange(focusChange: Int) = Unit

    companion object {
        private const val INCREASE_GAP = 3000L
    }
}