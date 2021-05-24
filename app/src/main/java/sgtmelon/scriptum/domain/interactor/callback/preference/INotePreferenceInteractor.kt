package sgtmelon.scriptum.domain.interactor.callback.preference

import sgtmelon.scriptum.domain.interactor.impl.preference.NotePreferenceInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.INotePreferenceViewModel

/**
 * Interface for communication [INotePreferenceViewModel] with [NotePreferenceInteractor].
 */
interface INotePreferenceInteractor {

    @Sort val sort: Int

    fun getSortSummary(): String?

    fun updateSort(@Sort value: Int): String?


    @Color val defaultColor: Int

    fun getDefaultColorSummary(): String?

    fun updateDefaultColor(@Color value: Int): String?


    @SavePeriod val savePeriod: Int

    fun getSavePeriodSummary(): String?

    fun updateSavePeriod(value: Int): String?

}