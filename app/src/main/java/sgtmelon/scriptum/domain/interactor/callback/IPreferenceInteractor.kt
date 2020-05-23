package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.PreferenceInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme

/**
 * Interface for [PreferenceInteractor].
 */
interface IPreferenceInteractor {

    @Theme val theme: Int

    fun getThemeSummary(): String?

    fun updateTheme(@Theme value: Int): String?


    @Sort val sort: Int

    fun getSortSummary(): String?

    fun updateSort(@Sort value: Int): String?


    @Color val defaultColor: Int

    fun getDefaultColorSummary(): String?

    fun updateDefaultColor(@Color value: Int): String?


    val savePeriod: Int

    fun getSavePeriodSummary(): String?

    fun updateSavePeriod(value: Int): String?


    @Repeat val repeat: Int

    fun getRepeatSummary(): String?

    fun updateRepeat(@Repeat value: Int): String?


    fun getSignalSummary(signalCheck: BooleanArray): String?

    fun updateSignal(valueArray: BooleanArray): String?


    val volume: Int

    fun getVolumeSummary(): String?

    fun updateVolume(value: Int): String?


}