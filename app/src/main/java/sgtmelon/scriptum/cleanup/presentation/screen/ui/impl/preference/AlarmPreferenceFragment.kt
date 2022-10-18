package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference

import android.Manifest
import android.content.DialogInterface
import android.os.Build
import androidx.annotation.StringRes
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeDismiss
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.annotation.PermissionRequest
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.domain.model.state.PermissionState
import sgtmelon.scriptum.cleanup.extension.isGranted
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IAlarmPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceDataBinding
import sgtmelon.scriptum.infrastructure.utils.setOnClickListener
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimation

/**
 * Fragment of notification (alarm) preferences.
 */
class AlarmPreferenceFragment : ParentPreferenceFragment(),
    IAlarmPreferenceFragment,
    DotAnimation.Callback {

    override val xmlId: Int = R.xml.preference_alarm

    private val binding = AlarmPreferenceDataBinding(lifecycle, fragment = this)

    @Inject lateinit var viewModel: IAlarmPreferenceViewModel

    private val permissionState = PermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    //region Dialogs

    private val dialogs by lazy { DialogFactory.Preference.Alarm(context, fm) }

    private val repeatDialog by lazy { dialogs.getRepeat() }
    private val signalDialog by lazy { dialogs.getSignal() }
    private val melodyPermissionDialog by lazy { dialogs.getMelodyPermission() }
    private val melodyDialog by lazy { dialogs.getMelody() }
    private val volumeDialog by lazy { dialogs.getVolume() }

    //endregion

    private val dotAnimation = DotAnimation(DotAnimType.COUNT, callback = this)

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getAlarmPrefBuilder()
            .set(fragment = this)
            .build()
            .inject(fragment = this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onSetup()
    }

    override fun onPause() {
        super.onPause()

        viewModel.onPause()

        /**
         * After call [IAlarmPreferenceViewModel.onPause] this dialog will not have any items.
         */
        melodyDialog.safeDismiss(owner = this)
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val isGranted = grantResults.firstOrNull()?.isGranted() ?: return
        val result = if (isGranted) PermissionResult.GRANTED else PermissionResult.FORBIDDEN

        when (requestCode) {
            PermissionRequest.MELODY -> viewModel.onClickMelody(result)
        }
    }

    //endregion

    override fun showToast(@StringRes stringId: Int) = delegators.toast.show(context, stringId)

    override fun setup() {
        binding.repeatButton?.setOnClickListener { viewModel.onClickRepeat() }

        repeatDialog.apply {
            onPositiveClick { viewModel.onResultRepeat(repeatDialog.check) }
            onDismiss { open.clear() }
        }

        binding.signalButton?.setOnClickListener { viewModel.onClickSignal() }

        signalDialog.apply {
            onPositiveClick { viewModel.onResultSignal(signalDialog.check) }
            onDismiss { open.clear() }
        }

        binding.melodyButton?.setOnClickListener {
            viewModel.onClickMelody(permissionState.getResult(activity))
        }

        melodyPermissionDialog.apply {
            isCancelable = false

            onPositiveClick {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@onPositiveClick

                requestPermissions(
                    arrayOf(permissionState.permission), PermissionRequest.MELODY
                )
            }
            onDismiss { open.clear() }
        }

        melodyDialog.apply {
            melodyDialog.itemListener = DialogInterface.OnClickListener { _, i ->
                viewModel.onSelectMelody(i)
            }
            onPositiveClick {
                val title = with(melodyDialog) { itemArray.getOrNull(check) }
                if (title != null) {
                    viewModel.onResultMelody(title)
                }
            }
            onDismiss {
                delegators.musicPlay.stop()
                open.clear()
            }
        }

        binding.volumeButton?.setOnClickListener { viewModel.onClickVolume() }

        volumeDialog.apply {
            onPositiveClick { viewModel.onResultVolume(volumeDialog.progress) }
            onDismiss { open.clear() }
        }
    }

    override fun updateRepeatSummary(summary: String) {
        binding.repeatButton?.summary = summary
    }

    override fun showRepeatDialog(repeat: Repeat) = open.attempt {
        repeatDialog.setArguments(repeat.ordinal)
            .safeShow(fm, DialogFactory.Preference.Alarm.REPEAT, owner = this)
    }

    override fun updateSignalSummary(summary: String) {
        binding.signalButton?.summary = summary
    }

    override fun showSignalDialog(valueArray: BooleanArray) = open.attempt {
        signalDialog.setArguments(valueArray)
            .safeShow(fm, DialogFactory.Preference.Alarm.SIGNAL, owner = this)
    }

    override fun showMelodyPermissionDialog() = open.attempt {
        melodyPermissionDialog.safeShow(
            fm,
            DialogFactory.Preference.Alarm.MELODY_PERMISSION,
            owner = this
        )
    }

    override fun updateMelodyEnabled(isEnabled: Boolean) {
        binding.melodyButton?.isEnabled = isEnabled
    }

    override fun updateMelodyGroupEnabled(isEnabled: Boolean) {
        binding.melodyButton?.isEnabled = isEnabled
        binding.increaseButton?.isEnabled = isEnabled
        binding.volumeButton?.isEnabled = isEnabled
    }

    override fun startMelodySummarySearch() {
        dotAnimation.start(context, R.string.pref_summary_alarm_melody_search)
    }

    override fun stopMelodySummarySearch() = dotAnimation.stop()

    override fun onDotAnimationUpdate(text: CharSequence) {
        binding.melodyButton?.summary = text
    }

    override fun updateMelodySummary(summary: String) {
        binding.melodyButton?.summary = summary
    }

    override fun updateMelodySummary(@StringRes summaryId: Int) {
        binding.melodyButton?.summary = getString(summaryId)
    }

    override fun showMelodyDialog(titleArray: Array<String>, value: Int) = open.attempt {
        melodyDialog.itemArray = titleArray
        melodyDialog.setArguments(value)
            .safeShow(fm, DialogFactory.Preference.Alarm.MELODY, owner = this)
    }

    override fun playMelody(stringUri: String) {
        val uri = UriConverter().toUri(stringUri) ?: return

        delegators.musicPlay.apply {
            stop()
            setupPlayer(uri, isLooping = false)
            start()
        }
    }

    override fun updateVolumeSummary(summary: String) {
        binding.volumeButton?.summary = summary
    }

    override fun showVolumeDialog(value: Int) = open.attempt {
        volumeDialog.setArguments(value)
            .safeShow(fm, DialogFactory.Preference.Alarm.VOLUME, owner = this)
    }
}