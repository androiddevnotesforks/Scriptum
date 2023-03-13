package sgtmelon.scriptum.infrastructure.utils.delegators

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * [value] will be reset to [default], after it will be provided by [getValue].
 */
class ResetValueDelegator<V>(
    private var value: V,
    private val default: V = value
) : ReadWriteProperty<Any, V>{

    override fun getValue(thisRef: Any, property: KProperty<*>): V {
        val value = this.value
        setValue(thisRef, property, default)
        return value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: V) {
        this.value = value
    }
}