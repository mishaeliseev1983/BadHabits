package com.melyseev.badhabits

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel

class App: Application(), ProvideViewModel {

    lateinit var mainViewModel: MainViewModel
    override fun onCreate() {

        super.onCreate()

        val cacheDataSource = CacheDataSource.Base(getSharedPreferences("main", Context.MODE_PRIVATE))
        val mainRepository = MainRepository.Base(cacheDataSource, Now.Base())

        val communication = MainCommunication.Base()
        mainViewModel = MainViewModel(mainRepository, communication)

    }

    override fun provideViewModel(): MainViewModel{
        return mainViewModel
    }
}

interface ProvideViewModel{
    fun provideViewModel(): MainViewModel
}