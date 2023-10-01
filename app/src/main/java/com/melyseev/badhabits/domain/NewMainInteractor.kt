package com.melyseev.badhabits.domain

interface NewMainInteractor : CRUDCards {
    fun canAddNewCard(): Boolean

    class Base(val repository: NewRepository, val maxItemsCount: Int) : NewMainInteractor {
        override fun canAddNewCard(): Boolean = maxItemsCount > repository.cards().size


        override fun cards(): List<Card> {
            val list = mutableListOf<Card>()

            if (canAddNewCard()) {
                list.addAll(repository.cards())
                list.add(Card.Add)
            } else {
                list.addAll(repository.cards())
            }
            return list
        }

        override fun newCard(text: String): Card {
            return repository.newCard(text)
        }

        override fun updateCard(id: Long, newText: String) {
            repository.updateCard(id, newText)
        }

        override fun deleteCard(id: Long) {
            repository.deleteCard(id)
        }

        override fun resetCard(id: Long) {
            repository.resetCard(id)
        }

    }
}


interface ResetCard {
    fun resetCard(id: Long)
}

interface CRUDCards : ResetCard {
    fun cards(): List<Card>

    fun newCard(text: String): Card

    fun updateCard(id: Long, newText: String)

    fun deleteCard(id: Long)
}