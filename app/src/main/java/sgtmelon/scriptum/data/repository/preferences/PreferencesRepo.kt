package sgtmelon.scriptum.data.repository.preferences

import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.scriptum.infrastructure.model.state.AlarmState
import sgtmelon.scriptum.infrastructure.model.state.NoteSaveState
import sgtmelon.scriptum.infrastructure.model.state.SignalState

interface PreferencesRepo {

    var isFirstStart: Boolean

    // App settings

    var theme: Theme

    // Backup settings

    val isBackupSkipImports: Boolean

    // Note settings

    var sort: Sort

    var defaultColor: Color

    val saveState: NoteSaveState

    var savePeriod: SavePeriod

    // Alarm settings

    var repeat: Repeat

    var signalTypeCheck: BooleanArray

    val signalState: SignalState

    suspend fun getMelodyUri(melodyList: List<MelodyItem>): String?

    suspend fun setMelodyUri(melodyList: List<MelodyItem>, title: String): String?

    suspend fun getMelodyCheck(melodyList: List<MelodyItem>): Int?

    var volumePercent: Int

    val isVolumeIncrease: Boolean

    val alarmState: AlarmState

    // Developer settings

    var isDeveloper: Boolean

    fun clear()
}