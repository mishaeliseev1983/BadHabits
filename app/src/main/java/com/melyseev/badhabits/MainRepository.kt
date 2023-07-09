package com.melyseev.badhabits

interface MainRepository {


    fun days(): Int
    fun reset()

    class Base(private val cacheDataSource: CacheDataSource, private val now: Now) : MainRepository {
        override fun days(): Int {

            val saved =  cacheDataSource.time(-100L)
            if(saved == -100L) {
                reset()
                return 0
            }
            val days = now.now() - saved
            return ( days / TO_DAYS_MILLIS).toInt()
        }

        override fun reset() {
            cacheDataSource.save(now.now())
        }

        companion object{
           const val TO_DAYS_MILLIS =  24 * 3600 * 1000
        }
    }
}