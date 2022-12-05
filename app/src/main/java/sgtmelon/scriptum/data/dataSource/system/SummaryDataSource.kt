package sgtmelon.scriptum.data.dataSource.system

import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme

interface SummaryDataSource {

    fun getTheme(theme: Theme): String

    fun getSort(sort: Sort): String

    fun getColor(color: Color): String

    fun getSavePeriod(savePeriod: SavePeriod): String

    fun getRepeat(repeat: Repeat): String
    
    fun getSignal(valueArray: BooleanArray): String
    
    fun getVolume(value: Int): String
}