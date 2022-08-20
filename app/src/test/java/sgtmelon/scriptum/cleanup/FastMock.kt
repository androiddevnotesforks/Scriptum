package sgtmelon.scriptum.cleanup

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlin.random.Random
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.test.common.nextString

object FastMock {

    object Dao {

        private const val PATH = "sgtmelon.scriptum.infrastructure.database.dao.safe"

        fun alarmDaoSafe() = mockkStatic("$PATH.AlarmDaoSafeExt")

        fun rollVisibleDaoSafe() = mockkStatic("$PATH.RollVisibleDaoSafeExt")

        fun rankVisibleDaoSafe() = mockkStatic("$PATH.RankDaoSafeExt")
    }

    fun fireExtensions() = mockkStatic("sgtmelon.scriptum.infrastructure.utils.FireExtensionUtils")

    fun timeExtension() = mockkStatic("sgtmelon.common.utils.TimeExtensionUtils")

    fun listExtension() = mockkStatic("sgtmelon.scriptum.cleanup.extension.ListExtensionUtils")

    fun daoExtension() =
        mockkStatic("sgtmelon.scriptum.cleanup.data.room.extension.DaoExtensionUtils")

    fun daoHelpExtension() =
        mockkStatic("sgtmelon.scriptum.cleanup.data.room.extension.DaoHelpExtensionUtils")

    object Note {

        fun deepCopy(
            item: NoteItem.Text,
            id: Long = Random.nextLong(),
            create: String = nextString(),
            change: String = nextString(),
            name: String = nextString(),
            text: String = nextString(),
            color: Color = Color.values().random(),
            rankId: Long = Random.nextLong(),
            rankPs: Int = Random.nextInt(),
            isBin: Boolean = Random.nextBoolean(),
            isStatus: Boolean = Random.nextBoolean(),
            alarmId: Long = Random.nextLong(),
            alarmDate: String = nextString()
        ) {
            every { item.id } returns id
            every { item.create } returns create
            every { item.change } returns change
            every { item.name } returns name
            every { item.text } returns text
            every { item.color } returns color
            every { item.rankId } returns rankId
            every { item.rankPs } returns rankPs
            every { item.isBin } returns isBin
            every { item.isStatus } returns isStatus
            every { item.alarmId } returns alarmId
            every { item.alarmDate } returns alarmDate

            every {
                item.deepCopy(
                    any(), any(), any(), any(), any(), any(),
                    any(), any(), any(), any(), any(), any()
                )
            } returns item
        }

        fun deepCopy(
            item: NoteItem.Roll,
            id: Long = Random.nextLong(),
            create: String = nextString(),
            change: String = nextString(),
            name: String = nextString(),
            text: String = nextString(),
            color: Color = Color.values().random(),
            rankId: Long = Random.nextLong(),
            rankPs: Int = Random.nextInt(),
            isBin: Boolean = Random.nextBoolean(),
            isStatus: Boolean = Random.nextBoolean(),
            alarmId: Long = Random.nextLong(),
            alarmDate: String = nextString(),
            isVisible: Boolean = Random.nextBoolean(),
            list: MutableList<RollItem> = MutableList(getRandomSize()) { mockk() }
        ) {
            every { item.id } returns id
            every { item.create } returns create
            every { item.change } returns change
            every { item.name } returns name
            every { item.text } returns text
            every { item.color } returns color
            every { item.rankId } returns rankId
            every { item.rankPs } returns rankPs
            every { item.isBin } returns isBin
            every { item.isStatus } returns isStatus
            every { item.alarmId } returns alarmId
            every { item.alarmDate } returns alarmDate
            every { item.isVisible } returns isVisible
            every { item.list } returns list

            for (it in list) {
                every { it.copy(any(), any(), any(), any()) } returns it
            }

            every {
                item.deepCopy(
                    any(), any(), any(), any(), any(), any(), any(),
                    any(), any(), any(), any(), any(), any(), any()
                )
            } returns item
        }
    }

}