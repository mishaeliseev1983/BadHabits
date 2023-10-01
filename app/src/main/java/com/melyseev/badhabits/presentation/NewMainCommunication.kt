package com.melyseev.badhabits.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.melyseev.badhabits.core.Communication

interface   NewMainCommunication {
    interface Put: Communication.Put<NewUIState>

    interface Observe: Communication.Observe<NewUIState>

    interface Mutable : Put, Observe

    class Base: Communication.Abstract<NewUIState>(), Mutable
}