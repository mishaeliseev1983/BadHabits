package com.melyseev.badhabits

class MainViewModel(
    private val repository: MainRepository,
    private val communication: MainCommunication.Mutable
) {

    fun init(isFirstRun: Boolean) {
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
}







