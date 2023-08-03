package sgtmelon.scriptum.infrastructure.screen.preference.alarm

import android.os.Bundle
import android.view.View
import androidx.annotation.IntRange
import sgtmelon.extensions.collect
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.safedialog.dialog.MultipleDialog
import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.safedialog.utils.DialogStorage
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.dialogs.VolumeDialog
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.model.key.permission.PermissionResult
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.model.state.PermissionState
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.state.MelodySummaryState
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.state.UpdateMelodyState
import sgtmelon.scriptum.infrastructure.utils.extensions.launch
import sgtmelon.scriptum.infrastructure.utils.extensions.registerPermissionRequest
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener
import sgtmelon.scriptum.infrastructure.utils.extensions.toPermissionResult
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimationImpl
import sgtmelon.textDotAnim.DotText
import javax.inject.Inject

/**
 * Fragment of notification (alarm) preferences.
 */
class AlarmPreferenceFragment : PreferenceFragment<AlarmPreferenceBinding>(),
    DotAnimationImpl.Callback {

    override val xmlId: Int = R.xml.preference_alarm

    override fun createBinding() = AlarmPreferenceBinding(fragment = this)

    @Inject lateinit var viewModel: AlarmPreferenceViewModel
    @Inject lateinit var permissionViewModel: PermissionViewModel

    private val writePermissionState = PermissionState(Permission.WriteExternalStorage)
    private val melodyPermissionRequest = registerPermissionRequest {
        onMelodyPermission(it.toPermissionResult())
    }

    private val dialogs by lazy { DialogFactory.Preference.Alarm(resources) }
    private val repeatDialog = DialogStorage(
        DialogFactory.Preference.Alarm.REPEAT, owner = this,
        create = { dialogs.getRepeat() },
        setup = { setupRepeatDialog(it) }
    )
    private val signalDialog = DialogStorage(
        DialogFactory.Preference.Alarm.SIGNAL, owner = this,
        create = { dialogs.getSignal() },
        setup = { setupSignalDialog(it) }
    )
    private val melodyAccessDialog = DialogStorage(
        DialogFactory.Preference.Alarm.MELODY_ACCESS, owner = this,
        create = { dialogs.getMelodyAccess() },
        setup = { setupMelodyAccessDialog(it) }
    )
    private val melodyDialog = DialogStorage(
        DialogFactory.Preference.Alarm.MELODY, owner = this,
        create = { dialogs.getMelody() },
        setup = { setupMelodyDialog(it) }
    )
    private val volumeDialog = DialogStorage(
        DialogFactory.Preference.Alarm.VOLUME, owner = this,
        create = { dialogs.getVolume() },
        setup = { setupVolumeDialog(it) }
    )

    private val dotAnimation = DotAnimationImpl[lifecycle, DotAnimType.COUNT, this]

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

    override fun setupView() {
        binding?.signalButton?.setOnClickListener { showSignalDialog(viewModel.signalTypeCheck) }
        binding?.repeatButton?.setOnClickListener { showRepeatDialog(viewModel.repeat) }

        binding?.melodyButton?.setOnClickListener {
            onMelodyPermission(writePermissionState.getResult(activity, permissionViewModel))
        }

        binding?.volumeButton?.setOnClickListener { showVolumeDialog(viewModel.volumePercent) }
    }

    override fun setupObservers() {
        viewModel.signalSummary.observe(this) { binding?.signalButton?.summary = it }
        viewModel.repeatSummary.observe(this) { binding?.repeatButton?.summary = it }
        viewModel.volumeSummary.observe(this) { binding?.volumeButton?.summary = it }
        viewModel.melodySummaryState.observe(this) { observeMelodySummaryState(it) }
        viewModel.melodyGroupEnabled.observe(this) { observeMelodyGroupEnabled(it) }
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

    private fun observeMelodySummaryState(state: MelodySummaryState) {
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

    private fun updateMelodySummary(summary: CharSequence) {
        binding?.melodyButton?.summary = summary
    }

    private fun observeMelodyGroupEnabled(isEnabled: Boolean) {
        binding?.melodyButton?.isEnabled = isEnabled
        binding?.increaseButton?.isEnabled = isEnabled
        binding?.volumeButton?.isEnabled = isEnabled
    }

    //region Dialogs setup

    override fun setupDialogs() {
        super.setupDialogs()

        repeatDialog.restore()
        signalDialog.restore()
        melodyAccessDialog.restore()
        melodyDialog.restore()
        volumeDialog.restore()
    }

    private fun setupRepeatDialog(dialog: SingleDialog): Unit = with(dialog) {
        onPositiveClick { viewModel.updateRepeat(check) }
        onDismiss {
            repeatDialog.release()
            open.clear()
        }
    }

    private fun setupSignalDialog(dialog: MultipleDialog): Unit = with(dialog) {
        onPositiveClick { viewModel.updateSignal(check) }
        onDismiss {
            signalDialog.release()
            open.clear()
        }
    }

    private fun setupMelodyAccessDialog(dialog: MessageDialog): Unit = with(dialog) {
        isCancelable = false
        onPositiveClick {
            melodyPermissionRequest.launch(writePermissionState, permissionViewModel)
//            requestPermission(PermissionRequest.MELODY, permissionState, permissionViewModel)
        }
        onDismiss {
            melodyAccessDialog.release()
            open.clear()
        }
    }

    private fun setupMelodyDialog(dialog: SingleDialog): Unit = with(dialog) {
        onItemClick { onMelodyClick(it) }
        onPositiveClick { onMelodyApply(itemArray.getOrNull(check)) }
        onDismiss {
            melodyDialog.release()
            open.clear()

            system?.musicPlay?.stop()
        }
    }

    private fun setupVolumeDialog(dialog: VolumeDialog): Unit = with(dialog) {
        onPositiveClick { viewModel.updateVolume(progress) }
        onDismiss {
            melodyDialog.release()
            open.clear()
        }
    }

    //endregion

    //region Dialogs show and actions

    private fun showRepeatDialog(repeat: Repeat) = open.attempt {
        repeatDialog.show { setArguments(repeat.ordinal) }
    }

    private fun showSignalDialog(valueArray: BooleanArray) = open.attempt {
        signalDialog.show { setArguments(valueArray) }
    }

    private fun showMelodyAccessDialog() = open.attempt { melodyAccessDialog.show() }

    private fun showMelodyDialog(titleArray: Array<String>, value: Int) = open.attempt {
        melodyDialog.show {
            itemArray = titleArray
            setArguments(value)
        }
    }

    private fun showVolumeDialog(@IntRange(from = 10, to = 100) value: Int) = open.attempt {
        volumeDialog.show { setArguments(value) }
    }

    private fun onMelodyClick(p: Int) {
        viewModel.getMelody(p).collect(owner = this) { playMelody(it) }
    }

    private fun playMelody(item: MelodyItem) {
        val uri = UriConverter().toUri(item.uri) ?: return
        system?.musicPlay?.stop()
            ?.setupPlayer(uri, isLooping = false)
            ?.start()
    }

    private fun onMelodyApply(title: String?) {
        if (title == null) return

        viewModel.updateMelody(title).collect(owner = this) {
            when (it) {
                UpdateMelodyState.AutoChoose -> {
                    system?.toast?.show(context, R.string.pref_toast_melody_replaced)
                }
            }
        }
    }

    //endregion

    override fun onDotAnimationUpdate(text: DotText) = updateMelodySummary(text.value)

}