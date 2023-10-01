package com.melyseev.badhabits.domain

import org.junit.Assert
import org.junit.Test

class NewInteractorTest: BaseTest() {

    @Test
    fun `test initial less max count`() {

        val initList = listOf(Card.NonZeroDays(id = 5L, text = "123", 2))
        val interactor = NewMainInteractor.Base(FakeRepository(initList), 2)

        val actual = interactor.cards()
        val expected = listOf<Card>(Card.NonZeroDays(id = 5L, text = "123", 2), Card.Add)
        Assert.assertEquals(2, actual.size)
        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `test initial equals max count`() {
        val initList = listOf(
            Card.NonZeroDays(id = 5L, text = "123", 2), Card.ZeroDays(id = 5L, text = "123")
        )
        val interactor = NewMainInteractor.Base(FakeRepository(initList), 2)
        val actual = interactor.cards()
        val expected = listOf<Card>(
            Card.NonZeroDays(id = 5L, text = "123", 2), Card.ZeroDays(id = 5L, text = "123")
        )
        Assert.assertEquals(2, actual.size)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test can add new card`() {

        val initList = listOf(
            Card.NonZeroDays(id = 5L, text = "123", 2)
        )
        val interactor = NewMainInteractor.Base(FakeRepository(initList), 2)
        val actual = interactor.canAddNewCard()
        val expected = true
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test cannot add new card`() {
        val initList = listOf(
            Card.NonZeroDays(id = 5L, text = "123", 2), Card.ZeroDays(id = 5L, text = "123")
        )

        val interactor = NewMainInteractor.Base(FakeRepository(initList), 2)
        val actual = interactor.canAddNewCard()
        val expected = false
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test new card`() {

        val interactor = NewMainInteractor.Base(
            FakeRepository( listOf(
            Card.NonZeroDays(id = 5L, text = "123", 2),
        )), 2)

        val card = interactor.newCard("222")
        Assert.assertEquals(card, Card.ZeroDays(id = 6L, text = "222"))

        val actual = interactor.cards()
        val expected = listOf<Card>(
            Card.NonZeroDays(id = 5L, text = "123", 2), Card.ZeroDays(id = 6L, text = "222")
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test delete card`() {
        val repository = FakeRepository(
            listOf(Card.ZeroDays(id = 1L, "a"), Card.ZeroDays(id = 2L, "b"))
        )
        val interactor = NewMainInteractor.Base(repository, maxItemsCount = 2)

        interactor.deleteCard(2L)
        val actual = interactor.cards()
        val expected =  listOf(Card.ZeroDays(id = 1L, "a"), Card.Add)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test update card`() {
        val initList = listOf(
            Card.NonZeroDays(id = 5L, text = "123", 2), Card.ZeroDays(id = 6L, text = "123")
        )

        val interactor = NewMainInteractor.Base(FakeRepository(initList), 2)
        interactor.updateCard(6L, "222")

        val actual = interactor.cards()
        val expected =  listOf(
            Card.NonZeroDays(id = 5L, text = "123", 2), Card.ZeroDays(id = 6L, text = "222"))

        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `test reset card`() {
        val initList = listOf(
            Card.NonZeroDays(id = 5L, text = "123", 2), Card.ZeroDays(id = 6L, text = "123")
        )

        val interactor = NewMainInteractor.Base(FakeRepository(initList), 2)
        interactor.resetCard(5L)

        val actual = interactor.cards()
        val expected =  listOf(
            Card.ZeroDays(id = 5L, text = "123"), Card.ZeroDays(id = 6L, text = "123"))

        Assert.assertEquals(expected, actual)

    }
    class FakeRepository(items: List<Card.Abstract>) : NewRepository {

        private val cards: MutableList<Card.Abstract> = ArrayList()

        init {
            cards.addAll(items)
        }

        override fun cards(): List<Card> {
            return cards
        }

        override fun newCard(text: String): Card {
            val card = Card.ZeroDays(text = text, id = 6L)
            cards.add(card)
            return card
        }

        override fun updateCard(id: Long, newText: String) {


            val card = cards.find { it.map(SameCardMapper(id)) }!!
            val index = cards.indexOf(card)
            val newCard = card.map(DuplicateCardMapper(id, newText))
            cards.set(index, newCard)
        }

        override fun deleteCard(id: Long) {
           val card = cards.find { it.map(SameCardMapper(id)) }
           cards.remove(card)
        }

        override fun resetCard(id: Long) {
            val card = cards.find { it.map(SameCardMapper(id)) }!!
            val index = cards.indexOf(card)
            val newCard = card!!.map(Card.Mapper.ResetDays())
            cards.set(index, newCard)
        }

    }


}