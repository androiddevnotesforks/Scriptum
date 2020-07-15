package sgtmelon.scriptum.domain.interactor.impl

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.data.room.converter.type.StringConverter
import sgtmelon.scriptum.domain.interactor.callback.IDevelopInteractor
import sgtmelon.scriptum.presentation.screen.vm.callback.IDevelopViewModel

/**
 * Interactor for [IDevelopViewModel].
 */
class DevelopInteractor(
        private val developRepo: IDevelopRepo,
        private val preferenceRepo: IPreferenceRepo
): ParentInteractor(),
        IDevelopInteractor {

    override suspend fun getNoteTablePrint() = StringBuilder().apply {
        val list = developRepo.getNoteList()

        append("Note table:")

        list.forEach {
            val text = it.text.substring(0, kotlin.math.min(it.text.length, b = 40))
                    .replace("\n", " ")

            append("\n\n")
            append("ID: ${it.id} | CR: ${it.create} | CH: ${it.change}\n")

            if (it.name.isNotEmpty()) append("NM: ${it.name}\n")

            append("TX: $text ${if (it.text.length > 40) "..." else ""}\n")
            append("CL: ${it.color} | TP: ${it.type} | BN: ${it.isBin}\n")
            append("RK ID: ${it.rankId}\n")
            append("RK PS: ${it.rankPs}\n")
            append("ST: ${it.isStatus}")
        }
    }.toString()

    override suspend fun getRollTablePrint() = StringBuilder().apply {
        val list = developRepo.getRollList()

        append("Roll table:")

        list.forEach {
            val text = it.text.substring(0, it.text.length.coerceAtMost(maximumValue = 40))
                    .replace("\n", " ")

            append("\n\n")
            append("ID: ${it.id} | ID_NT: ${it.noteId} | PS: ${it.position} | CH: ${it.isCheck}")
            append("\n")
            append("TX: $text ${if (it.text.length > 40) "..." else ""}")
        }
    }.toString()

    override suspend fun getRankTablePrint() = StringBuilder().apply {
        val list = developRepo.getRankList()

        append("Rank table:")

        list.forEach {
            append("\n\n")
            append("ID: ${it.id} | PS: ${it.position} | VS: ${it.isVisible}\n")
            append("NM: ${it.name}\n")
            append("CR: ${StringConverter().toString(it.noteId)}")
        }
    }.toString()

    override suspend fun getPreferencePrint() = StringBuilder().apply {
        with(preferenceRepo) {
            append("Preference:\n\n")
            append("Theme: $theme\n")
            append("Repeat: $repeat\n")
            append("Signal: $signal\n")
            append("Melody: $melodyUri\n")
            append("Volume: $volume\n")
            append("VolumeIncrease: $volumeIncrease\n")

            append("Sort: $sort\n")
            append("DefaultColor: $defaultColor\n")
            append("PauseSave: $pauseSaveOn\n")
            append("AutoSave: $autoSaveOn\n")
            append("SaveTime: $savePeriod")
        }
    }.toString()

}