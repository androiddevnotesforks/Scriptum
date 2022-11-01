package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.backup

import sgtmelon.scriptum.cleanup.ui.logic.preference.BackupPreferenceLogic
import sgtmelon.scriptum.cleanup.ui.screen.preference.BackupPreferenceScreen
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Interface for all [BackupPreferenceScreen] tests.
 */
interface IBackupPreferenceTest {

    fun getLogic() = BackupPreferenceLogic()

    fun ParentUiTest.runTest(before: () -> Unit = {}, func: BackupPreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreferences { openBackup(func) } } }
        }
    }
}