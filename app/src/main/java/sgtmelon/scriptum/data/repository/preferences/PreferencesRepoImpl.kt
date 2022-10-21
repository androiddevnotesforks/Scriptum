package sgtmelon.scriptum.data.repository.preferences

import kotlin.math.max
import kotlin.math.min
import sgtmelon.scriptum.cleanup.extension.validIndexOfFirst
import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.state.AlarmState
import sgtmelon.scriptum.infrastructure.model.state.NoteSaveState
import sgtmelon.scriptum.infrastructure.model.state.SignalState

class PreferencesRepoImpl(
    private val dataSource: PreferencesDataSource,
    private val themeConverter: ThemeConverter,
    private val sortConverter: SortConverter,
    private val colorConverter: ColorConverter,
    private val savePeriodConverter: SavePeriodConverter,
    private val repeatConverter: RepeatConverter,
    private val signalConverter: SignalConverter
) : PreferencesRepo {

    override var isFirstStart: Boolean
        get() = dataSource.isFirstStart
        set(value) {
            dataSource.isFirstStart = value
        }

    // App settings

    override var theme: Theme
        get() = themeConverter.toEnum(dataSource.theme) ?: Theme.SYSTEM.also {
            theme = it
        }
        set(value) {
            dataSource.theme = themeConverter.toInt(value)
        }

    // Backup settings

    override val isBackupSkipImports: Boolean get() = dataSource.isBackupSkipImports

    // Note settings

    override var sort: Sort
        get() = sortConverter.toEnum(dataSource.sort) ?: Sort.CHANGE.also {
            sort = it
        }
        set(value) {
            dataSource.sort = sortConverter.toInt(value)
        }

    override var defaultColor: Color
        get() = colorConverter.toEnum(dataSource.defaultColor) ?: Color.WHITE.also {
            defaultColor = it
        }
        set(value) {
            dataSource.defaultColor = colorConverter.toInt(value)
        }

    override val saveState: NoteSaveState
        get() = NoteSaveState(dataSource.isPauseSaveOn, dataSource.isAutoSaveOn, savePeriod)

    override var savePeriod: SavePeriod
        get() = savePeriodConverter.toEnum(dataSource.savePeriod) ?: SavePeriod.MIN_1.also {
            savePeriod = it
        }
        set(value) {
            dataSource.savePeriod = savePeriodConverter.toInt(value)
        }

    // Alarm settings

    override var repeat: Repeat
        get() = repeatConverter.toEnum(dataSource.repeat) ?: Repeat.MIN_10.also {
            repeat = it
        }
        set(value) {
            dataSource.repeat = repeatConverter.toInt(value)
        }

    override var signalTypeCheck: BooleanArray
        get() = signalConverter.toArray(dataSource.signal)
        set(value) {
            dataSource.signal = signalConverter.toString(value)
        }

    override val signalState: SignalState
        get() = signalConverter.toState(dataSource.signal) ?: run {
            dataSource.signal = "0, 1"
            return@run SignalState(isMelody = true, isVibration = true)
        }


    /**
     * If melody wasn't set before or not found by [PreferencesDataSource.melodyUri] inside
     * [melodyList] (not init or was deleted) when we will set first [MelodyItem.uri]
     * from [melodyList].
     *
     * returns [MelodyItem.uri] if all good, otherwise - null.
     */
    override suspend fun getMelodyUri(melodyList: List<MelodyItem>): String? {
        var value = dataSource.melodyUri

        /** Check uri exist. */
        if (value.isEmpty() || !melodyList.any { it.uri == value }) {
            value = melodyList.firstOrNull()?.uri ?: return null
            dataSource.melodyUri = value
        }

        return value
    }

    /**
     * If melody not found by [title] inside [melodyList] (not init or was deleted) when
     * we will set first [MelodyItem.uri] from [melodyList].
     *
     * returns [MelodyItem.title] if all good, otherwise - null.
     */
    override suspend fun setMelodyUri(melodyList: List<MelodyItem>, title: String): String? {
        /** Check uri exist. */
        val item = melodyList.firstOrNull { it.title == title } ?: melodyList.firstOrNull()
        dataSource.melodyUri = item?.uri ?: return null

        return item.title
    }

    /**
     * Returns index of current [MelodyItem.uri] inside [melodyList] if all good,
     * otherwise - null.
     */
    override suspend fun getMelodyCheck(melodyList: List<MelodyItem>): Int? {
        val uri = getMelodyUri(melodyList)
        return melodyList.validIndexOfFirst { it.uri == uri }
    }

    override var volumePercent: Int
        get() = dataSource.volumePercent
        set(value) {
            /** @IntRange(from = 10, to = 100) */
            dataSource.volumePercent = min(max(value, b = 10), b = 100)
        }

    override val isVolumeIncrease: Boolean get() = dataSource.isVolumeIncrease

    override val alarmState: AlarmState
        get() = AlarmState(signalState, volumePercent, isVolumeIncrease)

    // Developer settings

    override var isDeveloper: Boolean
        get() = dataSource.isDeveloper
        set(value) {
            dataSource.isDeveloper = value
        }

    override fun clear() = dataSource.clear()
}