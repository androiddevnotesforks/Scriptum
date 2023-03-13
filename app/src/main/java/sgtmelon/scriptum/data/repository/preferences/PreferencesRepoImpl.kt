package sgtmelon.scriptum.data.repository.preferences

import kotlin.math.max
import kotlin.math.min
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import sgtmelon.scriptum.cleanup.extension.validIndexOfFirst
import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.ParentEnumConverter
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.model.state.AlarmState
import sgtmelon.scriptum.infrastructure.model.state.NoteSaveState
import sgtmelon.scriptum.infrastructure.model.state.SignalState

class PreferencesRepoImpl(
    private val dataSource: PreferencesDataSource,
    themeConverter: ThemeConverter,
    sortConverter: SortConverter,
    colorConverter: ColorConverter,
    savePeriodConverter: SavePeriodConverter,
    repeatConverter: RepeatConverter,
    private val signalConverter: SignalConverter
) : PreferencesRepo {

    override var isFirstStart: Boolean
        get() = dataSource.isFirstStart
        set(value) {
            dataSource.isFirstStart = value
        }

    // App settings

    override var theme by PrefEnumDelegator(
        themeConverter, Theme.SYSTEM,
        get = { dataSource.theme },
        set = { dataSource.theme = it }
    )

    // Backup settings

    override val isBackupSkip: Boolean get() = dataSource.isBackupSkip

    // Note settings

    override var sort: Sort by PrefEnumDelegator(
        sortConverter, Sort.CHANGE,
        get = { dataSource.sort },
        set = { dataSource.sort = it }
    )

    override var defaultColor: Color by PrefEnumDelegator(
        colorConverter, Color.WHITE,
        get = { dataSource.defaultColor },
        set = { dataSource.defaultColor = it }
    )

    override val saveState: NoteSaveState
        get() = NoteSaveState(dataSource.isPauseSaveOn, dataSource.isAutoSaveOn, savePeriod)

    override var savePeriod: SavePeriod by PrefEnumDelegator(
        savePeriodConverter, SavePeriod.MIN_1,
        get = { dataSource.savePeriod },
        set = { dataSource.savePeriod = it }
    )

    // Alarm settings

    override var repeat: Repeat by PrefEnumDelegator(
        repeatConverter, Repeat.MIN_10,
        get = { dataSource.repeat },
        set = { dataSource.repeat = it }
    )

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

private class PrefEnumDelegator<E: Enum<E>>(
    private val converter: ParentEnumConverter<E>,
    private val default: E,
    private inline val get: () -> Int,
    private inline val set: (Int) -> Unit
) : ReadWriteProperty<Any, E> {

    override fun getValue(thisRef: Any, property: KProperty<*>): E {
        return converter.toEnum(get()) ?: default.also { setValue(thisRef, property, it) }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: E) {
        set(converter.toInt(value))
    }
}