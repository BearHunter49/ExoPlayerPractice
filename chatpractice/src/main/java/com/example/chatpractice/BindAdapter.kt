package com.example.chatpractice

import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView

object BindAdapter {

    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, chatting: ObservableField<ChatData>) {
//        Log.d("mytest", "setItems 실행")

//        Log.d("mytest2", chatting.get().toString())

        if (chatting.get() != null){
            val adapter = recyclerView.adapter as ChatAdapter
            adapter.addItem(chatting)
            if (adapter.itemCount > 0) {
//                Log.d("mytest2", adapter.itemCount.toString())
//                recyclerView.smoothScrollToPosition(0)
            }
        }


    }


}