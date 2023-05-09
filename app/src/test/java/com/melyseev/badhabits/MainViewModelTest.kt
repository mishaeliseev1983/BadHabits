package com.melyseev.badhabits

import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {

    @Test
    fun test_0_days() {

        val repository = FakeRepository(0)
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

    }

}

private class FakeRepository(private val days: Int) : MainRepository {

    override fun days(): Int {
        return days
    }
}

interface FakeMainCommunication : MainCommunication.Put {

    fun checkCallCount(count: Int): Boolean
    fun isSame(uiState: UIState): Boolean

    class Base : FakeMainCommunication {

        private var callCount = 0
        private lateinit var state: UIState
        override fun put(value: UIState) {
            callCount ++
            state = value
        }

        override fun checkCallCount(count: Int): Boolean {
            return callCount == count
        }
        override fun (uiState: UIState): Boolean{
            return uiState == state
        }

    }
}