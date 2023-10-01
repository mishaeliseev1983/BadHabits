package com.melyseev.badhabits.presentation

import com.melyseev.badhabits.domain.Card

sealed class NewUIState {

    data class Add(val card: Card): NewUIState()
    data class AddAll(val cards: List<Card>): NewUIState()
    data class Replace(val position: Int, val card: Card): NewUIState()
    data class Remove(val position: Int): NewUIState()
}