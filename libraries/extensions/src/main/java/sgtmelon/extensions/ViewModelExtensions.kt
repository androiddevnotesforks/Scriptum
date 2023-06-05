package sgtmelon.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun getViewModelFactory(
    crossinline createViewModel: () -> ViewModel
): ViewModelProvider.Factory {
    return viewModelFactory { initializer { createViewModel() } }
}

inline fun <reified T : ViewModel> getViewModel(
    owner: ViewModelStoreOwner,
    getFactory: () -> ViewModelProvider.Factory
): T {
    return ViewModelProvider(owner, getFactory())[T::class.java]
}