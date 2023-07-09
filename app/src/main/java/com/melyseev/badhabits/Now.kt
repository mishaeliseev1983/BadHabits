package com.melyseev.badhabits

interface Now {
    fun now(): Long

    class Base: Now{
        override fun now(): Long {
            return  System.currentTimeMillis()
        }

    }
}