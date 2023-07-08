package com.melyseev.badhabits

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface MainCommunication{

    interface Put{
        fun put(value: UIState)
    }

    interface Observe{
        fun observe(owner: LifecycleOwner, observer: Observer<UIState>)
    }

    interface Mutable: Put, Observe

    class Base(private val liveData: MutableLiveData<UIState> = MutableLiveData<UIState>()): Mutable{
        override fun put(value: UIState) {
            liveData.value = value
        }
        override fun observe(owner: LifecycleOwner, observer: Observer<UIState>) {
            liveData.observe(owner, observer)
        }
    }

}