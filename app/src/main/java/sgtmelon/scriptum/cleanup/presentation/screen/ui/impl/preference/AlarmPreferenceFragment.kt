package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference

import android.Manifest
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.preference.Preference
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeDismiss
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.annotation.PermissionRequest
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.domain.model.state.OpenState
import sgtmelon.scriptum.cleanup.domain.model.state.PermissionState
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.extension.isGranted
import sgtmelon.scriptum.cleanup.extension.toUriOrNull
import sgtmelon.scriptum.cleanup.presentation.control.system.MelodyControl
import sgtmelon.scriptum.cleanup.presentation.factory.DialogFactory
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IAlarmPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimation

/**
 * Fragment of notification (alarm) preferences.
 */
class AlarmPreferenceFragment : ParentPreferenceFragment(),
    IAlarmPreferenceFragment,
    DotAnimation.Callback {

    @Inject internal lateinit var viewModel: IAlarmPreferenceViewModel

    private val openState = OpenState()
    private val storagePermissionState by lazy {
        PermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE, activity)
    }

    //region Dialogs

    private val dialogFactory by lazy { DialogFactory.Preference.Alarm(context, fm) }

    private val repeatDialog by lazy { dialogFactory.getRepeatDialog() }
    private val signalDialog by lazy { dialogFactory.getSignalDialog() }
    private val melodyPermissionDialog by lazy { dialogFactory.getMelodyPermissionDialog() }
    private val melodyDialog by lazy { dialogFactory.getMelodyDialog() }
    private val volumeDialog by lazy { dialogFactory.getVolumeDialog() }

    //endregion

    //region Preferences

    private val repeatPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_repeat)) }
    private val signalPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_signal)) }
    private val melodyPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_melody)) }
    private val increasePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_increase)) }
    private val volumePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_alarm_volume)) }

    //endregion

    private val melodyControl by lazy { MelodyControl(context) }
    private val dotAnimation = DotAnimation(DotAnimType.COUNT, callback = this)

    //region System

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_alarm, rootKey)

        ScriptumApplication.component.getAlarmPrefBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        melodyControl.initLazy()

        openState.get(savedInstanceState)
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
        melodyControl.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
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

    override fun showToast(@StringRes stringId: Int) = toastControl.show(stringId)

    override fun setup() {
        repeatPreference?.setOnPreferenceClickListener {
            viewModel.onClickRepeat()
            return@setOnPreferenceClickListener true
        }

        repeatDialog.apply {
            onPositiveClick { viewModel.onResultRepeat(repeatDialog.check) }
            onDismiss { openState.clear() }
        }

        signalPreference?.setOnPreferenceClickListener {
            viewModel.onClickSignal()
            return@setOnPreferenceClickListener true
        }

        signalDialog.apply {
            onPositiveClick { viewModel.onResultSignal(signalDialog.check) }
            onDismiss { openState.clear() }
        }

        melodyPreference?.setOnPreferenceClickListener {
            viewModel.onClickMelody(storagePermissionState.getResult())
            return@setOnPreferenceClickListener true
        }

        melodyPermissionDialog.apply {
            isCancelable = false

            onPositiveClick {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@onPositiveClick

                requestPermissions(
                    arrayOf(storagePermissionState.permission), PermissionRequest.MELODY
                )
            }
            onDismiss { openState.clear() }
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
                melodyControl.stop()
                openState.clear()
            }
        }

        volumePreference?.setOnPreferenceClickListener {
            viewModel.onClickVolume()
            return@setOnPreferenceClickListener true
        }

        volumeDialog.apply {
            onPositiveClick { viewModel.onResultVolume(volumeDialog.progress) }
            onDismiss { openState.clear() }
        }
    }

    override fun updateRepeatSummary(summary: String) {
        repeatPreference?.summary = summary
    }

    override fun showRepeatDialog(repeat: Repeat) = openState.tryInvoke {
        repeatDialog.setArguments(repeat.ordinal)
            .safeShow(fm, DialogFactory.Preference.Alarm.REPEAT, owner = this)
    }

    override fun updateSignalSummary(summary: String) {
        signalPreference?.summary = summary
    }

    override fun showSignalDialog(valueArray: BooleanArray) = openState.tryInvoke {
        signalDialog.setArguments(valueArray)
            .safeShow(fm, DialogFactory.Preference.Alarm.SIGNAL, owner = this)
    }

    override fun showMelodyPermissionDialog() = openState.tryInvoke {
        melodyPermissionDialog.safeShow(fm, DialogFactory.Preference.Alarm.MELODY_PERMISSION, owner = this)
    }

    override fun updateMelodyEnabled(isEnabled: Boolean) {
        melodyPreference?.isEnabled = isEnabled
    }

    override fun updateMelodyGroupEnabled(isEnabled: Boolean) {
        melodyPreference?.isEnabled = isEnabled
        increasePreference?.isEnabled = isEnabled
        volumePreference?.isEnabled = isEnabled
    }

    override fun startMelodySummarySearch() {
        val context = context ?: return

        dotAnimation.start(context, R.string.pref_summary_alarm_melody_search)
    }

    override fun stopMelodySummarySearch() = dotAnimation.stop()

    override fun onDotAnimUpdate(text: CharSequence) {
        melodyPreference?.summary = text
    }

    override fun updateMelodySummary(summary: String) {
        melodyPreference?.summary = summary
    }

    override fun updateMelodySummary(@StringRes summaryId: Int) {
        melodyPreference?.summary = getString(summaryId)
    }

    override fun showMelodyDialog(titleArray: Array<String>, value: Int) = openState.tryInvoke {
        melodyDialog.itemArray = titleArray
        melodyDialog.setArguments(value)
            .safeShow(fm, DialogFactory.Preference.Alarm.MELODY, owner = this)
    }

    override fun playMelody(stringUri: String) {
        val uri = stringUri.toUriOrNull() ?: return

        with(melodyControl) {
            stop()
            setupPlayer(uri, isLooping = false)
            start()
        }
    }

    override fun updateVolumeSummary(summary: String) {
        volumePreference?.summary = summary
    }

    override fun showVolumeDialog(value: Int) = openState.tryInvoke {
        volumeDialog.setArguments(value)
            .safeShow(fm, DialogFactory.Preference.Alarm.VOLUME, owner = this)
    }
}