package sgtmelon.scriptum.infrastructure.screen.preference.note

import androidx.lifecycle.LiveData
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

interface NotePreferenceViewModel {

    val sort: Sort

    val sortSummary: LiveData<String>

    fun updateSort(value: Int)

    val defaultColor: Color

    val defaultColorSummary: LiveData<String>

    fun updateDefaultColor(value: Int)

    val savePeriod: SavePeriod

    val savePeriodSummary: LiveData<String>

    fun updateSavePeriod(value: Int)

}