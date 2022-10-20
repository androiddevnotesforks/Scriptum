package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.IntRange
import javax.inject.Inject
import sgtmelon.extensions.collect
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.annotation.PermissionRequest
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.domain.model.state.PermissionState
import sgtmelon.scriptum.cleanup.extension.isGranted
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.AlarmPreferenceViewModel
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceDataBinding
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.MelodySummaryState
import sgtmelon.scriptum.infrastructure.utils.setOnClickListener
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimation

/**
 * Fragment of notification (alarm) preferences.
 */
class AlarmPreferenceFragment : ParentPreferenceFragment(),
    DotAnimation.Callback {

    override val xmlId: Int = R.xml.preference_alarm

    private val binding = AlarmPreferenceDataBinding(fragment = this)

    @Inject lateinit var viewModel: AlarmPreferenceViewModel

    private val permissionState = PermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val dialogs by lazy { DialogFactory.Preference.Alarm(context, fm) }
    private val repeatDialog by lazy { dialogs.getRepeat() }
    private val signalDialog by lazy { dialogs.getSignal() }
    private val melodyAccessDialog by lazy { dialogs.getMelodyAccess() }
    private val melodyDialog by lazy { dialogs.getMelody() }
    private val volumeDialog by lazy { dialogs.getVolume() }

    private val dotAnimation = DotAnimation(DotAnimType.COUNT, callback = this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialogs()
    }

    override fun inject(component: ScriptumComponent) {
        component.getAlarmPrefBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val isGranted = grantResults.firstOrNull()?.isGranted() ?: return
        val result = if (isGranted) PermissionResult.GRANTED else PermissionResult.FORBIDDEN

        when (requestCode) {
            PermissionRequest.MELODY -> onMelodyPermission(result)
        }
    }

    override fun setup() {
        binding.repeatButton?.setOnClickListener { showRepeatDialog(viewModel.repeat) }
        binding.signalButton?.setOnClickListener { showSignalDialog(viewModel.signalTypeCheck) }

        binding.melodyButton?.setOnClickListener {
            onMelodyPermission(permissionState.getResult(activity))
        }

        binding.volumeButton?.setOnClickListener { showVolumeDialog(viewModel.volumePercent) }
    }

    override fun setupObservers() {
        viewModel.repeatSummary.observe(this) { binding.repeatButton?.summary = it }
        viewModel.signalSummary.observe(this) { binding.signalButton?.summary = it }
        viewModel.volumeSummary.observe(this) { binding.volumeButton?.summary = it }
        viewModel.melodySummaryState.observe(this) { updateMelodySummary(it) }
        viewModel.melodyGroupEnabled.observe(this) { updateMelodyGroupEnabled(it) }
    }

    private fun onMelodyPermission(result: PermissionResult?) {
        if (result == null) return

        /**
         * Show permission request only on [PermissionResult.ASK] because we can display
         * melodies which not located on SD card.
         */
        when (result) {
            PermissionResult.ASK -> showMelodyAccessDialog()
            else -> viewModel.selectMelodyData.collect(owner = this) {
                showMelodyDialog(it.first, it.second)
            }
        }
    }

    //region Melody state changed

    private fun updateMelodySummary(state: MelodySummaryState) {
        when (state) {
            is MelodySummaryState.Loading -> {
                dotAnimation.start(context, R.string.pref_summary_alarm_melody_search)
            }
            is MelodySummaryState.Finish -> {
                dotAnimation.stop()
                updateMelodySummary(state.title)
            }
            is MelodySummaryState.Empty -> {
                dotAnimation.stop()
                updateMelodySummary(getString(R.string.pref_summary_alarm_melody_empty))
            }
        }
    }

    private fun updateMelodySummary(summary: String) {
        binding.melodyButton?.summary = summary
    }

    private fun updateMelodyGroupEnabled(isEnabled: Boolean) {
        binding.melodyButton?.isEnabled = isEnabled
        binding.increaseButton?.isEnabled = isEnabled
        binding.volumeButton?.isEnabled = isEnabled
    }

    //endregion

    private fun setupDialogs() {
        repeatDialog.apply {
            onPositiveClick { viewModel.updateRepeat(repeatDialog.check) }
            onDismiss { open.clear() }
        }

        signalDialog.apply {
            onPositiveClick { viewModel.updateSignal(signalDialog.check) }
            onDismiss { open.clear() }
        }

        volumeDialog.apply {
            onPositiveClick { viewModel.updateVolume(volumeDialog.progress) }
            onDismiss { open.clear() }
        }

        melodyAccessDialog.apply {
            isCancelable = false

            onPositiveClick {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@onPositiveClick
                requestPermissions(arrayOf(permissionState.permission), PermissionRequest.MELODY)
            }
            onDismiss { open.clear() }
        }

        melodyDialog.apply {
            onItemClick { onMelodyClick(it) }
            onPositiveClick { onMelodyApply(with(melodyDialog) { itemArray.getOrNull(check) }) }
            onDismiss {
                delegators.musicPlay.stop()
                open.clear()
            }
        }
    }

    private fun showRepeatDialog(repeat: Repeat) = open.attempt {
        repeatDialog.setArguments(repeat.ordinal)
            .safeShow(fm, DialogFactory.Preference.Alarm.REPEAT, owner = this)
    }

    private fun showSignalDialog(valueArray: BooleanArray) = open.attempt {
        signalDialog.setArguments(valueArray)
            .safeShow(fm, DialogFactory.Preference.Alarm.SIGNAL, owner = this)
    }

    private fun showVolumeDialog(@IntRange(from = 10, to = 100) value: Int) = open.attempt {
        volumeDialog.setArguments(value)
            .safeShow(fm, DialogFactory.Preference.Alarm.VOLUME, owner = this)
    }

    private fun showMelodyAccessDialog() = open.attempt {
        melodyAccessDialog.safeShow(fm, DialogFactory.Preference.Alarm.MELODY_ACCESS, owner = this)
    }

    private fun showMelodyDialog(titleArray: Array<String>, value: Int) = open.attempt {
        melodyDialog.itemArray = titleArray
        melodyDialog.setArguments(value)
            .safeShow(fm, DialogFactory.Preference.Alarm.MELODY, owner = this)
    }

    private fun onMelodyClick(p: Int) {
        viewModel.getMelody(p).collect(owner = this) { playMelody(it) }
    }

    private fun playMelody(item: MelodyItem) {
        val uri = UriConverter().toUri(item.uri) ?: return
        delegators.musicPlay.stop()
            .setupPlayer(uri, isLooping = false)
            .start()
    }

    private fun onMelodyApply(title: String?) {
        if (title == null) return

        viewModel.updateMelody(title).collect(owner = this) { isSuccess ->
            if (!isSuccess) {
                delegators.toast.show(context, R.string.pref_toast_melody_replace)
            }
        }
    }

    override fun onDotAnimationUpdate(text: CharSequence) = updateMelodySummary(text.toString())

}