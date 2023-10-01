package com.melyseev.badhabits

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.melyseev.badhabits.domain.Card
import com.melyseev.badhabits.domain.NewMainInteractor
import com.melyseev.badhabits.presentation.NewMainCommunication
import com.melyseev.badhabits.presentation.NewUIState
import com.melyseev.badhabits.presentation.NewViewModel
import org.junit.Assert
import org.junit.Test

class NewViewModelTest {

    @Test
    fun start() {
        val communication = FakeCommunication()
        val interactor = FakeInteractor(listOf(Card.Add))

        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)
        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(NewUIState.AddAll(listOf(Card.Add)), communication.listCommunication[0])


        newViewModel.init(false)
        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(NewUIState.AddAll(listOf(Card.Add)), communication.listCommunication[0])
    }


    @Test
    fun `add first card`() {
        val communication = FakeCommunication()
        val interactor = FakeInteractor(listOf(Card.Add))

        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(NewUIState.AddAll(listOf(Card.Add)), communication.listCommunication[0])

        newViewModel.addNewCard(position = 0)

        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(position = 0, Card.Make),
            communication.listCommunication[1]
        )
    }

    @Test
    fun `cancel make card`() {
        val communication = FakeCommunication()
        val interactor = FakeInteractor(listOf(Card.Add))

        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(NewUIState.AddAll(listOf(Card.Add)), communication.listCommunication[0])

        newViewModel.addNewCard(position = 0)
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(position = 0, Card.Make),
            communication.listCommunication[1]
        )

        newViewModel.cancelMakeCard(position = 0)
        Assert.assertEquals(3, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(position = 0, Card.Add),
            communication.listCommunication[2]
        )
    }

    @Test
    fun `add first and save new card`() {
        val communication = FakeCommunication()
        val interactor = FakeInteractor(listOf(Card.Add))

        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(NewUIState.AddAll(listOf(Card.Add)), communication.listCommunication[0])

        newViewModel.addNewCard(position = 0)
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(position = 0, Card.Make),
            communication.listCommunication[1]
        )

        interactor.canAddNewCard = true
        newViewModel.saveNewCard(position = 0, text = "habbit 1")
        Assert.assertEquals(4, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.ZeroDays(text = "habbit 1", id = 4L)
            ),
            communication.listCommunication[2]
        )
        Assert.assertEquals(NewUIState.Add(Card.Add), communication.listCommunication[3])
    }

    @Test
    fun `add first and save only one card`() {
        val communication = FakeCommunication()
        val interactor = FakeInteractor(listOf(Card.Add))

        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(NewUIState.AddAll(listOf(Card.Add)), communication.listCommunication[0])

        newViewModel.addNewCard(position = 0)
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(position = 0, Card.Make),
            communication.listCommunication[1]
        )

        interactor.canAddNewCard = false
        newViewModel.saveNewCard(position = 0, text = "habbit2")
        Assert.assertEquals(3, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.ZeroDays(text = "habbit2", id = 4L)
            ),
            communication.listCommunication[2]
        )
    }


    @Test
    fun `test edit zero days card and cancel`() {
        val communication = FakeCommunication()
        val interactor = FakeInteractor(listOf(Card.Add))

        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(NewUIState.AddAll(listOf(Card.Add)), communication.listCommunication[0])

        newViewModel.addNewCard(position = 0)
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(position = 0, Card.Make),
            communication.listCommunication[1]
        )

        interactor.canAddNewCard = true
        newViewModel.saveNewCard(position = 0, text = "habbit 1")
        Assert.assertEquals(4, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.ZeroDays(text = "habbit 1", id = 4L)
            ),
            communication.listCommunication[2]
        )
        Assert.assertEquals(NewUIState.Add(Card.Add), communication.listCommunication[3])


        newViewModel.editZeroCard(
            position = 0,
            cardZeroDays = Card.ZeroDays(id = 4L, text = "habbit 1")
        )
        Assert.assertEquals(5, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.ZeroDays(text = "habbit 1", id = 4L)
            ),
            communication.listCommunication[4]
        )

        newViewModel.cancelEditZeroCard(position = 0, Card.ZeroDaysEdit(id = 4L, text = "habbit 1"))
        Assert.assertEquals(6, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(position = 0, Card.ZeroDaysEdit(text = "habbit 1", id = 4L)),
            communication.listCommunication[5]
        )
    }


    @Test
    fun `test delete zero days card when add card present`() {
        val communication = FakeCommunication()
        val interactor = FakeInteractor(listOf(Card.ZeroDays(id = 5L, text = "habbit3"), Card.Add))

        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.AddAll(
                listOf(
                    Card.ZeroDays(id = 5L, text = "habbit3"),
                    Card.Add
                )
            ), communication.listCommunication[0]
        )


        newViewModel.editZeroCard(
            position = 0,
            cardZeroDays = Card.ZeroDays(id = 5L, text = "habbit3")
        )
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.ZeroDays(id = 5L, text = "habbit3")
            ), communication.listCommunication[1]
        )
        interactor.canAddNewCard = true
        newViewModel.deleteCard(position = 0, id = 5L)
        Assert.assertEquals(3, communication.listCommunication.size)
        Assert.assertEquals(interactor.deleteCardList.size, 1)
        Assert.assertEquals(interactor.deleteCardList[0], 5L)
        Assert.assertEquals(NewUIState.Remove(position = 0), communication.listCommunication[2])

    }


    @Test
    fun `test delete zero days card when add card not present`() {
        val communication = FakeCommunication()
        val interactor = FakeInteractor(
            listOf(
                Card.ZeroDays(id = 5L, text = "habbit1"),
                Card.ZeroDays(id = 6L, text = "habbit2")
            )
        )

        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.AddAll(
                listOf(
                    Card.ZeroDays(id = 5L, text = "habbit1"),
                    Card.ZeroDays(id = 6L, text = "habbit2")
                )
            ),
            communication.listCommunication[0]
        )


        newViewModel.editZeroCard(
            position = 0,
            cardZeroDays = Card.ZeroDays(id = 5L, text = "habbit1")
        )
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.ZeroDays(id = 5L, text = "habbit1")
            ), communication.listCommunication[1]
        )

        interactor.canAddNewCard = false
        newViewModel.deleteCard(position = 0, id = 5L)
        Assert.assertEquals(4, communication.listCommunication.size)
        Assert.assertEquals(interactor.deleteCardList.size, 1)
        Assert.assertEquals(interactor.deleteCardList[0], 5L)
        Assert.assertEquals(NewUIState.Remove(position = 0), communication.listCommunication[2])
        Assert.assertEquals(NewUIState.Add(Card.Add), communication.listCommunication[3])
    }

    @Test
    fun `test edit zero days card and save`() {
        val communication = FakeCommunication()
        val interactor = FakeInteractor(
            listOf(
                Card.ZeroDays(id = 5L, text = "habbit1"),
                Card.ZeroDays(id = 6L, text = "habbit2")
            )
        )

        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.AddAll(
                listOf(
                    Card.ZeroDays(id = 5L, text = "habbit1"),
                    Card.ZeroDays(id = 6L, text = "habbit2")
                )
            ),
            communication.listCommunication[0]
        )


        newViewModel.editZeroCard(
            position = 0,
            cardZeroDays = Card.ZeroDays(id = 5L, text = "habbit1")
        )
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.ZeroDays(id = 5L, text = "habbit1")
            ), communication.listCommunication[1]
        )


        newViewModel.saveEditedZeroDaysCard(position = 0, id = 5L, text = "habbit12")
        Assert.assertEquals(1, interactor.updateCardList.size)
        Assert.assertEquals(5L, interactor.updateCardList[0])
        Assert.assertEquals(3, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.ZeroDays(id = 5L, text = "habbit12")
            ), communication.listCommunication[2]
        )
    }


    @Test
    fun `test edit non-zero days card and cancel`() {
        val communication = FakeCommunication()
        val interactor =
            FakeInteractor(
                listOf(
                    Card.NonZeroDays(id = 5L, text = "habbit1", days = 2),
                    Card.Add
                )
            )
        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.AddAll(
                listOf(
                    Card.NonZeroDays(id = 5L, text = "habbit1", days = 2),
                    Card.Add
                )
            ),
            communication.listCommunication[0]
        )


        newViewModel.editNonZeroCard(
            position = 0,
            card = Card.NonZeroDays(id = 5L, text = "habbit1", days = 2)
        )
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.NonZeroDaysEdit(id = 5L, text = "habbit1", days = 2)
            ), communication.listCommunication[1]
        )

        newViewModel.cancelEditNonZeroCard( position = 0, Card.NonZeroDaysEdit(id = 5L, text = "habbit1", days = 2))
        Assert.assertEquals(3, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.NonZeroDays(id = 5L, text = "habbit1", days = 2)
            ), communication.listCommunication[2]
        )
    }



    @Test
    fun `test delete non-zero days card when add card present`() {
        val communication = FakeCommunication()
        val interactor =
            FakeInteractor(
                listOf(
                    Card.NonZeroDays(id = 5L, text = "habbit1", days = 2),
                    Card.Add
                )
            )
        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.AddAll(
                listOf(
                    Card.NonZeroDays(id = 5L, text = "habbit1", days = 2),
                    Card.Add
                )
            ),
            communication.listCommunication[0]
        )


        newViewModel.editNonZeroCard(position = 0,  Card.NonZeroDays(id = 5L, text = "habbit1", days = 2))
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.NonZeroDaysEdit(id = 5L, text = "habbit1", days = 2)
            ), communication.listCommunication[1]
        )


        interactor.canAddNewCard = true
        newViewModel.deleteCard(position = 0, id = 5L)
        Assert.assertEquals(3, communication.listCommunication.size)
        Assert.assertEquals(interactor.deleteCardList.size, 1)
        Assert.assertEquals(interactor.deleteCardList[0], 5L)
        Assert.assertEquals(NewUIState.Remove(position = 0), communication.listCommunication[2])
    }

    @Test
    fun `test delete non-zero days card when add card not present`() {
        val communication = FakeCommunication()
        val interactor =
            FakeInteractor(
                listOf(
                    Card.NonZeroDays(id = 5L, text = "habbit1", days = 2),
                    Card.NonZeroDays(id = 6L, text = "habbit3", days = 3),
                )
            )
        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.AddAll(
                listOf(
                    Card.NonZeroDays(id = 5L, text = "habbit1", days = 2),
                    Card.NonZeroDays(id = 6L, text = "habbit3", days = 3),
                )
            ),
            communication.listCommunication[0]
        )


        newViewModel.editNonZeroCard(position = 0,  Card.NonZeroDays(id = 5L, text = "habbit1", days = 2))
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.NonZeroDaysEdit(id = 5L, text = "habbit1", days = 2)
            ), communication.listCommunication[1]
        )

        interactor.canAddNewCard = false
        newViewModel.deleteCard(position = 0, id = 5L)
        Assert.assertEquals(4, communication.listCommunication.size)
        Assert.assertEquals(interactor.deleteCardList.size, 1)
        Assert.assertEquals(interactor.deleteCardList[0], 5L)
        Assert.assertEquals(NewUIState.Remove(position = 0), communication.listCommunication[2])
        Assert.assertEquals(NewUIState.Add(Card.Add), communication.listCommunication[3])
    }


    @Test
    fun `test edit non-zero days card and save`() {
        val communication = FakeCommunication()
        val interactor =
            FakeInteractor(
                listOf(
                    Card.NonZeroDays(id = 5L, text = "habbit1", days = 2),
                    Card.NonZeroDays(id = 6L, text = "habbit3", days = 3),
                )
            )
        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.AddAll(
                listOf(
                    Card.NonZeroDays(id = 5L, text = "habbit1", days = 2),
                    Card.NonZeroDays(id = 6L, text = "habbit3", days = 3),
                )
            ),
            communication.listCommunication[0]
        )

        newViewModel.editNonZeroCard(position = 0,  Card.NonZeroDays(id = 5L, text = "habbit1", days = 2))
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.NonZeroDaysEdit(id = 5L, text = "habbit1", days = 2)
            ), communication.listCommunication[1]
        )

        newViewModel.saveEditedNonZeroDaysCard(position = 0, id = 5L, text = "habbit11", days = 2)
        Assert.assertEquals(3, communication.listCommunication.size)
        Assert.assertEquals(1, interactor.updateCardList.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.NonZeroDays(id = 5L, text = "habbit11", days = 2)
            ), communication.listCommunication[2]
        )
    }

    @Test
    fun `test reset non-zero days card`() {
        val communication = FakeCommunication()
        val interactor =
            FakeInteractor(
                listOf(
                    Card.NonZeroDays(id = 5L, text = "habbit1", days = 2),
                    Card.NonZeroDays(id = 6L, text = "habbit3", days = 3),
                )
            )
        val newViewModel = NewViewModel(communication, interactor)
        newViewModel.init(true)

        Assert.assertEquals(1, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.AddAll(
                listOf(
                    Card.NonZeroDays(id = 5L, text = "habbit1", days = 2),
                    Card.NonZeroDays(id = 6L, text = "habbit3", days = 3),
                )
            ),
            communication.listCommunication[0]
        )

        newViewModel.editNonZeroCard(position = 0,  Card.NonZeroDays(id = 5L, text = "habbit1", days = 2))
        Assert.assertEquals(2, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.NonZeroDaysEdit(id = 5L, text = "habbit1", days = 2)
            ), communication.listCommunication[1]
        )

        newViewModel.resetNonZeroDaysCard(position = 0, Card.NonZeroDaysEdit(id = 5L, text = "habbit1", days = 2))
        Assert.assertEquals(3, communication.listCommunication.size)
        Assert.assertEquals(
            NewUIState.Replace(
                position = 0,
                Card.ZeroDays(id = 5L, text = "habbit1")
            ), communication.listCommunication[2]
        )


    }
}


class FakeCommunication : NewMainCommunication.Mutable {

    val listCommunication = mutableListOf<NewUIState>()
    override fun put(value: NewUIState) {
        listCommunication.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<NewUIState>) = Unit
}


class FakeInteractor(private val cards: List<Card>) : NewMainInteractor {

    var canAddNewCard = false
    override fun cards(): List<Card> = cards

    val deleteCardList = mutableListOf<Long>()
    val updateCardList = mutableListOf<Long>()
    val resetCardList = mutableListOf<Long>()
    override fun canAddNewCard(): Boolean {
        return canAddNewCard;
    }

    override fun newCard(text: String): Card {
        return Card.ZeroDays(text = text, id = 4L)
    }


    override fun deleteCard(id: Long) {
        deleteCardList.add(id)
    }

    override fun updateCard(id: Long, newText: String) {
        updateCardList.add(id)
    }

    override fun resetCard(id: Long) {
        resetCardList.add(id)
    }



}

