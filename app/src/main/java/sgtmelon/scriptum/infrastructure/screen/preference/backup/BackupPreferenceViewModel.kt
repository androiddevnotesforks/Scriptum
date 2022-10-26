package sgtmelon.scriptum.infrastructure.screen.preference.backup

import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportDataState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportState

interface BackupPreferenceViewModel {

    fun startExport(): Flow<ExportState>

    val importData: Flow<ImportDataState>

    fun startImport(name: String): Flow<ImportState>

}