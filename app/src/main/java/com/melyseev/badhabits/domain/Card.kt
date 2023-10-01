package com.melyseev.badhabits.domain


interface Card{


    abstract class Abstract(
        protected open val id: Long,
        protected open val text: String,
        protected open val days: Int,
        protected open val editable: Boolean): Card{

       fun <T> map(mapper: Mapper<T>): T = mapper.map(id, text, days, editable)
    }

    interface Mapper<T> {
        fun map(id: Long, text: String, days: Int, editable: Boolean): T



        class Reset(private val resetCard: ResetCard) : Mapper<Unit> {
            override fun map(id: Long, text: String, days: Int, editable: Boolean) =
                resetCard.resetCard(id)
        }

        class ResetDays : Mapper<Abstract> {
            override fun map(id: Long, text: String, days: Int, editable: Boolean) =
                ZeroDays(id, text)
        }

            class ChangeEditable: Mapper<Card> {
                override fun map(id: Long, text: String, days: Int, editable: Boolean): Card {
                  return  if (days > 0) {
                        if (editable)
                            NonZeroDays(id, text, days)
                        else
                            NonZeroDaysEdit(id, text, days)
                    } else if (days == 0) {
                        if (!editable)
                            ZeroDays(id, text)
                        else
                            ZeroDaysEdit(id, text)
                    } else {
                        if (id == 0L) Add
                        else Make
                    }
            }
        }


    }

    object Add: Abstract(0L, "", -1, false)
    object Make: Abstract(1L, "", -1, false)
    data class ZeroDays(override val id: Long, override val text: String): Abstract(id, text, 0, false)
    data class ZeroDaysEdit(override val id: Long, override val text: String): Abstract(id, text, 0, true)
    data class NonZeroDays(override val id: Long, override val text: String, override val days: Int): Abstract(id, text, days, false)
    data class NonZeroDaysEdit(override val id: Long, override val text: String, override val days: Int): Abstract(id, text, days, true)
}