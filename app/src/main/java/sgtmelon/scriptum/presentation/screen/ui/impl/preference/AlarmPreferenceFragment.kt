package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.Manifest
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.preference.Preference
import sgtmelon.safedialog.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.PermissionRequest
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.key.DotAnimType
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.domain.model.state.PermissionState
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.extension.isGranted
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.presentation.control.DotAnimControl
import sgtmelon.scriptum.presentation.control.system.MelodyControl
import sgtmelon.scriptum.presentation.control.system.callback.IMelodyControl
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IAlarmPreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import javax.inject.Inject

/**
 * Fragment of notification (alarm) preferences.
 */
class AlarmPreferenceFragment : ParentPreferenceFragment(),
    IAlarmPreferenceFragment,
    DotAnimControl.Callback {

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

    private val melodyControl: IMelodyControl by lazy { MelodyControl(context) }
    private val dotAnimControl = DotAnimControl(DotAnimType.COUNT, callback = this)

    //region System

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_alarm, rootKey)

        ScriptumApplication.component.getAlarmPrefBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

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
        melodyDialog.safeDismiss()
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

        repeatDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultRepeat(repeatDialog.check)
        }
        repeatDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        signalPreference?.setOnPreferenceClickListener {
            viewModel.onClickSignal()
            return@setOnPreferenceClickListener true
        }

        signalDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultSignal(signalDialog.check)
        }
        signalDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        melodyPreference?.setOnPreferenceClickListener {
            viewModel.onClickMelody(storagePermissionState.getResult())
            return@setOnPreferenceClickListener true
        }

        melodyPermissionDialog.isCancelable = false
        melodyPermissionDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@OnClickListener

            requestPermissions(arrayOf(storagePermissionState.permission), PermissionRequest.MELODY)
        }
        melodyPermissionDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()
        }

        melodyDialog.itemListener = DialogInterface.OnClickListener { _, i ->
            viewModel.onSelectMelody(i)
        }
        melodyDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            val title = with(melodyDialog) { itemArray.getOrNull(check) } ?: return@OnClickListener
            viewModel.onResultMelody(title)
        }
        melodyDialog.dismissListener = DialogInterface.OnDismissListener {
            melodyControl.stop()
            openState.clear()
        }

        volumePreference?.setOnPreferenceClickListener {
            viewModel.onClickVolume()
            return@setOnPreferenceClickListener true
        }

        volumeDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            viewModel.onResultVolume(volumeDialog.progress)
        }
        volumeDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun updateRepeatSummary(summary: String?) {
        repeatPreference?.summary = summary
    }

    override fun showRepeatDialog(@Repeat value: Int) = openState.tryInvoke {
        repeatDialog.setArguments(value).safeShow(fm, DialogFactory.Preference.Alarm.REPEAT)
    }

    override fun updateSignalSummary(summary: String?) {
        signalPreference?.summary = summary
    }

    override fun showSignalDialog(valueArray: BooleanArray) = openState.tryInvoke {
        signalDialog.setArguments(valueArray).safeShow(fm, DialogFactory.Preference.Alarm.SIGNAL)
    }

    override fun showMelodyPermissionDialog() = openState.tryInvoke {
        melodyPermissionDialog.safeShow(fm, DialogFactory.Preference.Alarm.MELODY_PERMISSION)
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

        dotAnimControl.start(context, R.string.pref_summary_alarm_melody_search)
    }

    override fun stopMelodySummarySearch() = dotAnimControl.stop()

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
        melodyDialog.setArguments(value).safeShow(fm, DialogFactory.Preference.Alarm.MELODY)
    }

    override fun playMelody(stringUri: String) {
        val uri = stringUri.toUri() ?: return

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
        volumeDialog.setArguments(value).safeShow(fm, DialogFactory.Preference.Alarm.VOLUME)
    }
}