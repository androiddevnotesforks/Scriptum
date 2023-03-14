package sgtmelon.scriptum.cleanup

import io.mockk.mockkStatic

object FastMock {

    object Dao {

        private const val PATH = "sgtmelon.scriptum.infrastructure.database.dao.safe"

        fun alarmDaoSafe() = mockkStatic("$PATH.AlarmDaoSafeExt")

        fun noteDaoSafe() = mockkStatic("$PATH.NoteDaoSafeExt")

        fun rankDaoSafe() = mockkStatic("$PATH.RankDaoSafeExt")

        fun rollDaoSafe() = mockkStatic("$PATH.RollDaoSafeExt")

        fun rollVisibleDaoSafe() = mockkStatic("$PATH.RollVisibleDaoSafeExt")
    }

    fun fireExtensions() = mockkStatic(
        "sgtmelon.scriptum.infrastructure.utils.extensions.FireExtensionsUtils"
    )

    fun timeExtension() = mockkStatic("sgtmelon.extensions.TimeExtensionsUtils")

    fun noteExtensions() = mockkStatic("sgtmelon.scriptum.infrastructure.utils.extensions.note.NoteExtensionsUtils")

    fun rollExtensions() = mockkStatic(
        "sgtmelon.scriptum.infrastructure.utils.extensions.note.RollExtensionsUtils"
    )

    object Note {


        //        fun deepCopy(
        //            item: NoteItem.Text,
        //            id: Long = Random.nextLong(),
        //            create: String = nextString(),
        //            change: String = nextString(),
        //            name: String = nextString(),
        //            text: String = nextString(),
        //            color: Color = Color.values().random(),
        //            rankId: Long = Random.nextLong(),
        //            rankPs: Int = Random.nextInt(),
        //            isBin: Boolean = Random.nextBoolean(),
        //            isStatus: Boolean = Random.nextBoolean(),
        //            alarmId: Long = Random.nextLong(),
        //            alarmDate: String = nextString()
        //        ) {
        //            every { item.id } returns id
        //            every { item.create } returns create
        //            every { item.change } returns change
        //            every { item.name } returns name
        //            every { item.text } returns text
        //            every { item.color } returns color
        //            every { item.rankId } returns rankId
        //            every { item.rankPs } returns rankPs
        //            every { item.isBin } returns isBin
        //            every { item.isStatus } returns isStatus
        //            every { item.alarm.id } returns alarmId
        //            every { item.alarm.date } returns alarmDate
        //
        //            every {
        //                item.deepCopy(
        //                    any(), any(), any(), any(), any(), any(),
        //                    any(), any(), any(), any(), any(), any()
        //                )
        //            } returns item
        //        }
        //
        //        fun deepCopy(
        //            item: NoteItem.Roll,
        //            id: Long = Random.nextLong(),
        //            create: String = nextString(),
        //            change: String = nextString(),
        //            name: String = nextString(),
        //            text: String = nextString(),
        //            color: Color = Color.values().random(),
        //            rankId: Long = Random.nextLong(),
        //            rankPs: Int = Random.nextInt(),
        //            isBin: Boolean = Random.nextBoolean(),
        //            isStatus: Boolean = Random.nextBoolean(),
        //            alarmId: Long = Random.nextLong(),
        //            alarmDate: String = nextString(),
        //            isVisible: Boolean = Random.nextBoolean(),
        //            list: MutableList<RollItem> = MutableList(getRandomSize()) { mockk() }
        //        ) {
        //            every { item.id } returns id
        //            every { item.create } returns create
        //            every { item.change } returns change
        //            every { item.name } returns name
        //            every { item.text } returns text
        //            every { item.color } returns color
        //            every { item.rankId } returns rankId
        //            every { item.rankPs } returns rankPs
        //            every { item.isBin } returns isBin
        //            every { item.isStatus } returns isStatus
        //            every { item.alarm.id } returns alarmId
        //            every { item.alarm.date } returns alarmDate
        //            every { item.isVisible } returns isVisible
        //            every { item.list } returns list
        //
        //            for (it in list) {
        //                every { it.copy(any(), any(), any(), any()) } returns it
        //            }
        //
        //            every {
        //                item.deepCopy(
        //                    any(), any(), any(), any(), any(), any(), any(),
        //                    any(), any(), any(), any(), any(), any(), any()
        //                )
        //            } returns item
        //        }
    }
}