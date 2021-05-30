package sgtmelon.scriptum.test.auto.screen.preference.backup

import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.logic.preference.BackupPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.BackupPreferenceScreen

/**
 * Interface for all [BackupPreferenceScreen] tests.
 */
interface IBackupPreferenceTest {

    fun getLogic() = BackupPreferenceLogic()

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: BackupPreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openBackup(func) } } }
        }
    }
}