package sgtmelon.scriptum.infrastructure.system.dataSource

import android.content.res.Resources
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import java.util.Locale
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.infrastructure.model.exception.DifferentSizeException
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.utils.record
import sgtmelon.scriptum.testing.getRandomSize
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [SummaryDataSourceImpl].
 */
class SummaryDataSourceImplTest : ParentTest() {

    @MockK lateinit var resources: Resources

    private val provider by lazy { SummaryDataSourceImpl(resources) }

    @Test fun getTheme() {
        val value = Theme.values().random()
        val id = when (value) {
            Theme.LIGHT -> R.string.pref_theme_light
            Theme.DARK -> R.string.pref_theme_dark
            Theme.SYSTEM -> R.string.pref_theme_system
        }
        val summary = nextString()

        every { resources.getString(id) } returns summary

        assertEquals(provider.getTheme(value), summary)

        verifySequence {
            resources.getString(id)
        }
    }

    @Test fun getSort() {
        val value = Sort.values().random()
        val id = when (value) {
            Sort.CHANGE -> R.string.pref_sort_change
            Sort.CREATE -> R.string.pref_sort_create
            Sort.RANK -> R.string.pref_sort_rank
            Sort.COLOR -> R.string.pref_sort_color
        }
        val summary = nextString()

        every { resources.getString(id) } returns summary

        assertEquals(provider.getSort(value), summary)

        verifySequence {
            resources.getString(id)
        }
    }

    @Test fun getColor() {
        val value = Color.values().random()
        val id = when (value) {
            Color.RED -> R.string.pref_color_red
            Color.PURPLE -> R.string.pref_color_purple
            Color.INDIGO -> R.string.pref_color_indigo
            Color.BLUE -> R.string.pref_color_blue
            Color.TEAL -> R.string.pref_color_teal
            Color.GREEN -> R.string.pref_color_green
            Color.YELLOW -> R.string.pref_color_yellow
            Color.ORANGE -> R.string.pref_color_orange
            Color.BROWN -> R.string.pref_color_brown
            Color.BLUE_GREY -> R.string.pref_color_blue_gray
            Color.WHITE -> R.string.pref_color_white
        }
        val summary = nextString()

        every { resources.getString(id) } returns summary

        assertEquals(provider.getColor(value), summary)

        verifySequence {
            resources.getString(id)
        }
    }

    @Test fun getSavePeriod() {
        val value = SavePeriod.values().random()
        val id = when (value) {
            SavePeriod.MIN_1 -> R.string.pref_save_period_1
            SavePeriod.MIN_3 -> R.string.pref_save_period_3
            SavePeriod.MIN_7 -> R.string.pref_save_period_7
        }
        val summary = nextString()

        every { resources.getString(id) } returns summary

        assertEquals(provider.getSavePeriod(value), summary)

        verifySequence {
            resources.getString(id)
        }
    }

    @Test fun getRepeat() {
        val value = Repeat.values().random()
        val id = when (value) {
            Repeat.MIN_10 -> R.string.pref_repeat_0
            Repeat.MIN_30 -> R.string.pref_repeat_1
            Repeat.MIN_60 -> R.string.pref_repeat_2
            Repeat.MIN_180 -> R.string.pref_repeat_3
            Repeat.MIN_1440 -> R.string.pref_repeat_4
        }
        val summary = nextString()

        every { resources.getString(id) } returns summary

        assertEquals(provider.getRepeat(value), summary)

        verifySequence {
            resources.getString(id)
        }
    }

    @Test fun `getSignal exception record`() {
        val valueSize = getRandomSize()
        val valueArray = BooleanArray(valueSize) { Random.nextBoolean() }

        var summarySize = getRandomSize()
        while (summarySize == valueArray.size) {
            summarySize = getRandomSize()
        }
        val summaryArray = Array(summarySize) { nextString() }

        val exception = DifferentSizeException(valueSize, summarySize)

        every { resources.getStringArray(R.array.pref_signal) } returns summaryArray
        FastMock.fireExtensions()
        every { exception.record() } returns Unit

        assertEquals(provider.getSignal(valueArray), "")

        verifySequence {
            resources.getStringArray(R.array.pref_signal)
            exception.record()
        }
    }

    @Test fun getSignal() {
        val size = getRandomSize()
        val valueArray = BooleanArray(size) { Random.nextBoolean() }
        val summaryArray = Array(size) { nextString() }

        every { resources.getStringArray(R.array.pref_signal) } returns summaryArray

        val result = valueArray.asSequence()
            .mapIndexed { i, b ->
                if (!b) return@mapIndexed null

                val summary = summaryArray[i].lowercase(Locale.getDefault())
                return@mapIndexed SummaryDataSourceImpl.SIGNAL_DIVIDER.plus(summary)
            }
            .filterNotNull()
            .joinToString(separator = "")
            .replaceFirst(SummaryDataSourceImpl.SIGNAL_DIVIDER, "")

        assertEquals(provider.getSignal(valueArray), result)

        verifySequence {
            resources.getStringArray(R.array.pref_signal)
        }
    }

    @Test fun getVolume() {
        val value = Random.nextInt()
        val summary = nextString()

        every { resources.getString(R.string.pref_summary_alarm_volume, value) } returns summary

        assertEquals(provider.getVolume(value), summary)

        verifySequence {
            resources.getString(R.string.pref_summary_alarm_volume, value)
        }
    }
}