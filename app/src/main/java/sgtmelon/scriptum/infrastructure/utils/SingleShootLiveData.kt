package sgtmelon.scriptum.infrastructure.utils

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Class needed for preventing notify observers by [LiveData] (if event already happened).
 *
 * Every [postValue] call may notify observers only once.
 */
class SingleShootLiveData<T>(data: T? = null) {

    init {
        if (data != null) {
            postValue(data)
        }
    }

    private val liveData = MutableLiveData<Value<T>?>()

    fun postValue(value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            liveData.setValue(Value(value))
        } else {
            liveData.postValue(Value(value))
        }
    }

    fun observe(owner: LifecycleOwner, onEvent: (T) -> Unit) {
        liveData.value = null
        liveData.observe(owner, Observer {
            if (it != null && it.receive(onEvent.hashCode())) {
                onEvent(it.message ?: return@Observer)
            }
        })
    }

    class Value<T>(val message: T? = null) {

        private val received = mutableListOf<Int>()

        fun receive(hash: Int): Boolean {
            if (!received.contains(hash)) {
                received.add(hash)
                return true
            }
            return false
        }
    }
}