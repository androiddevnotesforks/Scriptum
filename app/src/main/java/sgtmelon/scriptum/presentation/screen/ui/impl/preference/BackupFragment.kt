package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.Manifest
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.preference.Preference
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.PermissionRequest
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.domain.model.state.PermissionState
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.extension.isGranted
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.presentation.factory.DialogFactory
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IBackupFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IBackupViewModel
import javax.inject.Inject

/**
 * Fragment of backup preferences.
 */
class BackupFragment : ParentPreferenceFragment(), IBackupFragment {

    @Inject internal lateinit var viewModel: IBackupViewModel

    private val openState = OpenState()
    private val readPermissionState by lazy {
        PermissionState(Manifest.permission.READ_EXTERNAL_STORAGE, activity)
    }
    private val writePermissionState by lazy {
        PermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE, activity)
    }

    //region Dialogs

    private val dialogFactory by lazy { DialogFactory.Preference(context, fm) }

    private val exportPermissionDialog by lazy { dialogFactory.getExportPermissionDialog() }
    private val exportDenyDialog by lazy { dialogFactory.getExportDenyDialog() }
    private val importPermissionDialog by lazy { dialogFactory.getImportPermissionDialog() }
    private val importDialog by lazy { dialogFactory.getImportDialog() }
    private val loadingDialog by lazy { dialogFactory.getLoadingDialog() }

    //endregion

    //region Preferences

    private val exportPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_backup_export)) }
    private val importPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_backup_import)) }
    private val importSkipPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_backup_skip)) }

    //endregion

    private val broadcastControl by lazy { BroadcastControl[context] }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_backup, rootKey)

        ScriptumApplication.component.getBackupBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        broadcastControl.initLazy()

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
         * After call [IBackupViewModel.onPause] this dialog will not have any items.
         */
        importDialog.safeDismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        openState.save(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val isGranted = grantResults.firstOrNull()?.isGranted() ?: return
        val result = if (isGranted) PermissionResult.GRANTED else PermissionResult.FORBIDDEN

        when (requestCode) {
            PermissionRequest.EXPORT -> viewModel.onClickExport(result)
            PermissionRequest.IMPORT -> viewModel.onClickImport(result)
        }
    }

    //region Toast functions

    override fun showToast(@StringRes stringId: Int) {
        context?.showToast(stringId)
    }

    override fun showExportPathToast(path: String) {
        val text = getString(R.string.pref_toast_export_result, path)

        context?.showToast(text, Toast.LENGTH_LONG)
    }

    override fun showImportSkipToast(count: Int) {
        val text = getString(R.string.pref_toast_import_result_skip, count)

        context?.showToast(text, Toast.LENGTH_LONG)
    }

    //endregion

    override fun setup() {
        exportPreference?.setOnPreferenceClickListener {
            viewModel.onClickExport(writePermissionState.getResult())
            return@setOnPreferenceClickListener true
        }

        importPreference?.setOnPreferenceClickListener {
            viewModel.onClickImport(readPermissionState.getResult())
            return@setOnPreferenceClickListener true
        }

        exportPermissionDialog.isCancelable = false
        exportPermissionDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@OnClickListener

            requestPermissions(arrayOf(writePermissionState.permission), PermissionRequest.EXPORT)
        }
        exportPermissionDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()
        }

        exportDenyDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()
        }

        importPermissionDialog.isCancelable = false
        importPermissionDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@OnClickListener

            requestPermissions(arrayOf(readPermissionState.permission), PermissionRequest.IMPORT)
        }
        importPermissionDialog.dismissListener = DialogInterface.OnDismissListener {
            openState.clear()
        }

        importDialog.positiveListener = DialogInterface.OnClickListener { _, _ ->
            val name = with(importDialog) { itemArray.getOrNull(check) } ?: return@OnClickListener

            openState.skipClear = true
            viewModel.onResultImport(name)
        }
        importDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }

        loadingDialog.isCancelable = false
        loadingDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    //region Dialog functions

    override fun updateExportEnabled(isEnabled: Boolean) {
        exportPreference?.isEnabled = isEnabled
    }

    override fun showExportPermissionDialog() {
        exportPermissionDialog.show(fm, DialogFactory.Preference.EXPORT_PERMISSION)
    }

    override fun showExportDenyDialog() {
        exportDenyDialog.show(fm, DialogFactory.Preference.EXPORT_DENY)
    }

    override fun showExportLoadingDialog() = openState.tryInvoke {
        loadingDialog.show(fm, DialogFactory.Preference.LOADING)
    }

    override fun hideExportLoadingDialog() = loadingDialog.safeDismiss()

    override fun updateImportEnabled(isEnabled: Boolean) {
        importPreference?.isEnabled = isEnabled
        importSkipPreference?.isEnabled = isEnabled
    }

    override fun showImportPermissionDialog() = openState.tryInvoke {
        importPermissionDialog.show(fm, DialogFactory.Preference.IMPORT_PERMISSION)
    }

    override fun showImportDialog(titleArray: Array<String>) = openState.tryInvoke {
        openState.tag = OpenState.Tag.DIALOG

        importDialog.itemArray = titleArray
        importDialog.show(fm, DialogFactory.Preference.IMPORT)
    }

    override fun showImportLoadingDialog() = openState.tryInvoke(OpenState.Tag.DIALOG) {
        loadingDialog.show(fm, DialogFactory.Preference.LOADING)
    }

    override fun hideImportLoadingDialog() = loadingDialog.safeDismiss()

    //endregion

    //region Broadcast functions

    override fun sendNotifyNotesBroadcast() = broadcastControl.sendNotifyNotesBind()

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    override fun sendNotifyInfoBroadcast(count: Int?) = broadcastControl.sendNotifyInfoBind(count)

    //endregion

}