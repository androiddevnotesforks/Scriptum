package sgtmelon.scriptum.repository.preference

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Интерфейс для общения с [PreferenceRepo]
 *
 * @author SerjantArbuz
 */
interface IPreferenceRepo {

    fun isFirstStart(): Boolean

    fun setFirstStart(value: Boolean)

    //region Preference screen

    @Theme fun getTheme(): Int

    fun setTheme(@Theme value: Int)

    fun getSort(): String

    fun setSort(value: String)

    @Color fun getDefaultColor(): Int

    fun setDefaultColor(@Color value: Int)

    fun isPauseSaveOn(): Boolean

    fun isAutoSaveOn(): Boolean

    fun getSavePeriod(): Int

    fun setSavePeriod(value: Int)

    //endregion

    fun getSortNoteOrder(): String

    fun getSortSummary(): String

    fun getData(): String

}