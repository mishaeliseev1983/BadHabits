package com.melyseev.badhabits

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.melyseev.badhabits.core.Init

class MainViewModel(
    private val repository: MainRepository,
    private val communication: MainCommunication.Mutable
) : Init{

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            val days = repository.days()
            if (days == 0) {
                communication.put(UIState.ZeroDays)
            }else
                communication.put(UIState.NDays(days = days))
        }
    }

    fun reset(){
        repository.reset()
        communication.put(UIState.ZeroDays)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<UIState>){
        communication.observe(owner, observer)
    }
}







