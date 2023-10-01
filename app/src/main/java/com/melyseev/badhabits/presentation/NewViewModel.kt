package com.melyseev.badhabits.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.melyseev.badhabits.core.Init
import com.melyseev.badhabits.domain.Card
import com.melyseev.badhabits.domain.NewMainInteractor

class NewViewModel(
    private val communication: NewMainCommunication.Mutable,
    private val interactor: NewMainInteractor,
    private val changeEditable: Card.Mapper<Card> = Card.Mapper.ChangeEditable(),
) : Init, NewMainCommunication.Observe, Actions {


    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            val listCards = interactor.cards()
            communication.put(NewUIState.AddAll(cards = listCards))
        }
    }



    override fun observe(owner: LifecycleOwner, observer: Observer<NewUIState>) =
        communication.observe(owner, observer)

    override fun addNewCard(position: Int) {
        communication.put(NewUIState.Replace(position, card = Card.Make))
    }

    override fun cancelMakeCard(position: Int) {
        communication.put(NewUIState.Replace(position, card = Card.Add))
    }

    override fun editZeroCard(position: Int, cardZeroDays: Card.ZeroDays) {
        communication.put(NewUIState.Replace(position, cardZeroDays.map(changeEditable)))
    }

    override fun cancelEditZeroCard(position: Int, zeroCard: Card.ZeroDaysEdit) {
        communication.put(NewUIState.Replace(position, zeroCard.map(changeEditable)))
    }

    override fun saveNewCard(position: Int, text: String) {
        val card = interactor.newCard(text = text) as Card.ZeroDays
        communication.put(NewUIState.Replace(position, card))
        if(interactor.canAddNewCard())
            communication.put(NewUIState.Add(Card.Add))
    }

    override fun deleteCard(position: Int, id: Long) {
        interactor.deleteCard(id)
        communication.put(NewUIState.Remove(position))
        if(!interactor.canAddNewCard())
            communication.put(NewUIState.Add(Card.Add))
    }

    override fun saveEditedZeroDaysCard(position: Int, id: Long, text: String) {
        interactor.updateCard(id, text)
        communication.put(NewUIState.Replace(position = 0, Card.ZeroDays(id, text)))
    }

    override fun saveEditedNonZeroDaysCard(position: Int, id: Long, text: String, days: Int) {
        interactor.updateCard(id, text)
        communication.put(NewUIState.Replace(position = 0, Card.NonZeroDays(id, text, days)))
    }

    override fun editNonZeroCard(position: Int, card: Card.NonZeroDays) {
        communication.put(NewUIState.Replace(position,  card.map(changeEditable)))
    }

    override fun cancelEditNonZeroCard(position: Int, card: Card.NonZeroDaysEdit) {
        communication.put(NewUIState.Replace(position,  card.map(changeEditable)))
    }

    override fun resetNonZeroDaysCard(position: Int, card: Card.NonZeroDaysEdit) {
        card.map(Card.Mapper.Reset(interactor))
        communication.put(NewUIState.Replace(position, card.map(Card.Mapper.ResetDays())))
    }


}


interface Actions : AddNewCard, SaveNewCard, CancelMakeCard,
    EditZeroCard, CancelEditZeroCard, DeleteCard, SaveEditedZeroCard, SaveEditedNonZeroCard,
    EditNonZeroCard, CancelEditNonZeroCard, ResetNonZeroDaysCard

interface AddNewCard{
    fun addNewCard(position: Int)
}

interface SaveNewCard{
    fun saveNewCard(position: Int, text: String)
}

interface CancelMakeCard{
    fun cancelMakeCard(position: Int)
}

interface EditZeroCard{
    fun editZeroCard(position: Int,  card: Card.ZeroDays)
}

interface EditNonZeroCard{
    fun editNonZeroCard(position: Int,  card: Card.NonZeroDays)
}

interface CancelEditZeroCard{
    fun cancelEditZeroCard(position: Int, card: Card.ZeroDaysEdit)
}

interface DeleteCard{
    fun deleteCard(position: Int, id: Long)
}


interface SaveEditedZeroCard{
    fun saveEditedZeroDaysCard(position: Int, id: Long, text: String)
}

interface SaveEditedNonZeroCard{
    fun saveEditedNonZeroDaysCard(position: Int, id: Long, text: String, days: Int)
}

interface CancelEditNonZeroCard{
    fun cancelEditNonZeroCard(position: Int, card: Card.NonZeroDaysEdit)
}

interface ResetNonZeroDaysCard{
    fun resetNonZeroDaysCard(position: Int, card: Card.NonZeroDaysEdit)
}