package com.melyseev.badhabits

import org.junit.Assert
import org.junit.Test

class MainRepositoryTest {
    val TO_MILLIS = 24 * 3600 * 1000

    interface FakeNow: Now{

        fun addTime(value: Long)

        class Base: FakeNow {
            var now = 0L

            override fun addTime(value: Long){
                now += value
            }

            override fun now(): Long {
                return now
            }
        }
    }

    class FakeCacheDataSource: CacheDataSource{

        var saveValue = 100L
        override fun save(value: Long) {
            saveValue = value
        }

        override fun time(default: Long): Long {
            return if(saveValue == -100L) default
            else
                saveValue
        }

    }

    @Test
    fun test_0_days() {

        val fakeCacheDataSource = FakeCacheDataSource()
        val fakeNow = FakeNow.Base()

        fakeNow.addTime(1459)
        val repository = MainRepository.Base(fakeCacheDataSource, fakeNow)

        val days = repository.days()
        Assert.assertEquals(true,  days == 0)
    }



    @Test
    fun test_5_days() {
        val fakeCacheDataSource = FakeCacheDataSource()
        val fakeNow = FakeNow.Base()

        fakeCacheDataSource.save(2L * TO_MILLIS)
        fakeNow.addTime(7L * TO_MILLIS)
        val repository = MainRepository.Base(fakeCacheDataSource, fakeNow)

        val days = repository.days()
        Assert.assertEquals(true,  days == 5)
    }

    @Test
    fun test_reset() {

        val TO_MILLIS = 24 * 3600 * 1000
        val fakeCacheDataSource = FakeCacheDataSource()
        val fakeNow = FakeNow.Base()
        fakeNow.addTime(8L * TO_MILLIS)

        val repository = MainRepository.Base(fakeCacheDataSource, fakeNow)
        repository.reset()

        val days0 = repository.days()
        Assert.assertEquals(true,  days0 == 0)
        Assert.assertEquals(8L * TO_MILLIS, fakeCacheDataSource.time(-1))
    }
}