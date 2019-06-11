package sgtmelon.scriptum.repository.preference

import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Интерфейс для общения с [PreferenceRepo]
 *
 * @author SerjantArbuz
 */
interface IPreferenceRepo {

    var firstStart: Boolean

    @Theme var theme: Int

    var repeat: Int

    var signal: Int

    var sort: Int

    @Color var defaultColor: Int

    val pauseSaveOn: Boolean

    val autoSaveOn: Boolean

    var savePeriod: Int

    fun getSignalSummary(): String

    fun getData(): String

}