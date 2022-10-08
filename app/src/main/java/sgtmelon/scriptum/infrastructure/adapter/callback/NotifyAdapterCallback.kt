package sgtmelon.scriptum.infrastructure.adapter.callback

/**
 * Need copy list inside implementation. Because if you pass same list (without copy) and update
 * some data - data also will be changed inside your adapter. And when you will try
 * submit it (notify adapter) - nothing will happen, because inside your Adapter will be
 * exactly the same list (with no changes).
 */
interface NotifyAdapterCallback<T> {

    fun notifyList(list: List<T>)
}