package com.melyseev.badhabits.domain

abstract class BaseTest {


    class DuplicateCardMapper(private val id: Long, private val newText: String):
        Card.Mapper<Card.Abstract> {
        override fun map(id: Long, text: String, days: Int, editable: Boolean): Card.Abstract {
            return   if(days>0){
                if(editable){
                    Card.NonZeroDaysEdit(id, this.newText, days)
                }else{
                    Card.NonZeroDays(id, this.newText, days)
                }
            }else if (days==0){
                if(editable){
                    Card.ZeroDaysEdit(id, this.newText)
                }else{
                    Card.ZeroDays(id, this.newText)
                }
            }else{
                if(id==0L)
                    Card.Add
                else
                    Card.Make
            }
        }

    }
    class SameCardMapper(private val id: Long): Card.Mapper<Boolean> {
        override fun map(id: Long, text: String, days: Int, editable: Boolean): Boolean {
            return this.id == id
        }
    }
}