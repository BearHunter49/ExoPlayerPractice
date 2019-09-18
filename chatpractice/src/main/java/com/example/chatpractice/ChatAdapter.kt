package com.example.chatpractice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.chatpractice.databinding.ChatItemBinding

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var items = mutableListOf<ChatData>()

    // ViewHolder 만들기
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        // Databinding XML Inflate
        val binding = ChatItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun getItemCount() = items.size

    // bind 행동 양식
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatdata = items[position]
        holder.bind(chatdata)
    }

    // Item 추가 Data binding method
    fun addItem(chatting: ObservableField<ChatData>) {

//        Log.d("mytest", "addItem 실행")
        if (items.size > 11){
            items.removeAt(11)
        }
//        Log.d("mytest", "리스트: " + items.toString())
        items.add(0, chatting.get() as ChatData)
        notifyItemInserted(0)
        notifyItemRemoved(12)


    }

    // 실제 ViewHolder Class (ViewHolder 인자로 View 가 들어감)
    inner class ChatViewHolder(private val binding: ChatItemBinding) : RecyclerView.ViewHolder(binding.root) {

        // variable 에 bind 해주기
        fun bind(item: ChatData) {
            binding.chatitem = item
//            binding.executePendingBindings()
        }
    }

}