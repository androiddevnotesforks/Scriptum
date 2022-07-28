package sgtmelon.scriptum.infrastructure.provider

import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.infrastructure.model.key.Theme

interface SummaryProvider {

    fun getTheme(theme: Theme): String
    
    fun getSort(sort: Sort): String
    
    fun getColor(color: Color): String
    
    fun getSavePeriod(savePeriod: SavePeriod): String
    
    fun getRepeat(repeat: Repeat): String
    
    fun getSignal(valueArray: BooleanArray): String
    
    fun getVolume(value: Int): String
}