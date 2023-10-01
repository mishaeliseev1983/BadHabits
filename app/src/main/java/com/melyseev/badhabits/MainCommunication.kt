package com.melyseev.badhabits

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.melyseev.badhabits.core.Communication

interface MainCommunication{
    interface Put :  Communication.Put<UIState>
    interface Observe: Communication.Observe<UIState>
    interface Mutable: Put, Observe

    open class Base: Communication.Abstract<UIState>(), Mutable

}