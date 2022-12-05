package sgtmelon.scriptum.infrastructure.screen.preference.backup

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ExportSummaryState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportState
import sgtmelon.scriptum.infrastructure.screen.preference.backup.state.ImportSummaryState

interface BackupPreferenceViewModel {

    val exportSummary: LiveData<ExportSummaryState>

    val exportEnabled: LiveData<Boolean>

    val importSummary: LiveData<ImportSummaryState>

    val importEnabled: LiveData<Boolean>

    fun startExport(): Flow<ExportState>

    val importData: Flow<Array<String>>

    fun startImport(name: String): Flow<ImportState>

}