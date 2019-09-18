package com.example.chatpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.example.chatpractice.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var chatData: ObservableField<ChatData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding
        chatData = ObservableField()

        binding.apply {
//            Log.d("mytest", "바인딩 함")
            chatting = chatData
            rclView.adapter = ChatAdapter()
        }



        btn_add.setOnClickListener {
//            Log.d("mytest", "클릭")
            if (edit_id.text.isNotEmpty() && edit_text.text.isNotEmpty()){
//                Log.d("mytest", "dddd" + edit_id.text.toString())
                chatData.set(ChatData(edit_id.text.toString(), edit_text.text.toString()))
            }

        }

    }
}
