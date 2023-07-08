package com.melyseev.badhabits

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {

    @Test
    fun test_0_days() {

        val repository = FakeRepository.Base(0)
        val communication = FakeMainCommunication.Base()
        val viewModel = MainViewModel(repository, communication)
        viewModel.init(isFirstRun = true)
        assertEquals(true, communication.checkCallCount(1))
        assertEquals(true, communication.isSame(UIState.ZeroDays))

        viewModel.init(isFirstRun = false)
        assertEquals(true, communication.checkCallCount(1))
    }

    @Test
    fun test_N_days() {
        val repository = FakeRepository.Base(6)
        val communication = FakeMainCommunication.Base()
        val viewModel = MainViewModel(repository, communication)
        viewModel.init(isFirstRun = true)
        assertEquals(true, communication.checkCallCount(1))
        assertEquals(true, communication.isSame(UIState.NDays(6)))
        viewModel.init(isFirstRun = false)
        assertEquals(true, communication.checkCallCount(1))
    }

    @Test
    fun test_reset(){
        val repository = FakeRepository.Base(6)
        val communication = FakeMainCommunication.Base()
        val viewModel = MainViewModel(repository, communication)
        viewModel.init(isFirstRun = true)
        assertEquals(true, communication.checkCallCount(1))
        assertEquals(true, communication.isSame(UIState.NDays(6)))

        viewModel.reset()

        assertEquals(true, communication.checkCallCount(2))
        assertEquals(true, communication.isSame(UIState.ZeroDays))
        assertEquals(true, repository.resetCalledCount(1))
    }

}

private interface FakeRepository: MainRepository{


    fun resetCalledCount(count: Int): Boolean

    class Base(private var days: Int) : FakeRepository {
        var resetCount: Int =  0
        override fun resetCalledCount(count: Int) = resetCount == count

        override fun days(): Int {
            return days
        }
        override fun reset() {
            days = 0
            resetCount++
        }
    }
}

interface FakeMainCommunication : MainCommunication.Mutable {

    fun checkCallCount(count: Int): Boolean
    fun isSame(uiState: UIState): Boolean

    class Base : FakeMainCommunication {


        private var callCount = 0
        private lateinit var state: UIState

        override fun put(value: UIState) {
            callCount ++
            state = value
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<UIState>) = Unit

        override fun checkCallCount(count: Int): Boolean {
            return callCount == count
        }


        override fun isSame(uiState: UIState): Boolean = state == uiState

    }
}