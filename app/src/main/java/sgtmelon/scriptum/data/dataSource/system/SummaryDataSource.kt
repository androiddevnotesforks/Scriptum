package sgtmelon.scriptum.data.dataSource.system

import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat

interface SummaryDataSource {

    fun getTheme(theme: Theme): String

    fun getSort(sort: Sort): String

    fun getColor(color: Color): String

    fun getSavePeriod(savePeriod: SavePeriod): String

    fun getRepeat(repeat: Repeat): String
    
    fun getSignal(valueArray: BooleanArray): String
    
    fun getVolume(value: Int): String
}