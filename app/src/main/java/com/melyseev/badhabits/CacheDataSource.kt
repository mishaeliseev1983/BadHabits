package com.melyseev.badhabits

import android.content.SharedPreferences

interface CacheDataSource {
    fun save(value: Long)
    fun time(default: Long): Long


    class Base(private val sharedPreferences: SharedPreferences) : CacheDataSource {
        override fun save(value: Long) {
            sharedPreferences.edit().putLong(KEY, value).apply()
        }

        override fun time(default: Long): Long {
            val saved = sharedPreferences.getLong(KEY, default)
            return saved
        }

        companion object {
            private const val KEY = "SavedTime"
        }
    }
}