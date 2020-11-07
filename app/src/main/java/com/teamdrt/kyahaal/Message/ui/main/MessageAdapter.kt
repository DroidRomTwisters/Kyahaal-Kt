/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Message.ui.main

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.teamdrt.kyahaal.Databases.Message.Message
import com.teamdrt.kyahaal.R
import com.vanniktech.emoji.EmojiManager
import kotlin.coroutines.coroutineContext

class MessageAdapter(val context: Context, val ml: ArrayList<Message>): RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType==1 || viewType==3){
            MessageViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.chatrightsinglelayout, parent, false))
        }else{
            MessageViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.chaleftsinglelayout, parent, false))
        }
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val messsage=ml[position].msg
        if(getItemViewType(position)==3 || getItemViewType(position)==4){
            holder.iv.visibility= View.INVISIBLE
        }
        when {
            com.vdurmont.emoji.EmojiManager.isOnlyEmojis(messsage) -> {
                holder.message.setEmojiSize(85,false)
                holder.message.text=messsage
            }
            com.vdurmont.emoji.EmojiManager.containsEmoji(messsage) -> {
                holder.message.setEmojiSize(65,false)
                holder.message.text=messsage
            }
            else -> {
                holder.message.setEmojiSize(55,false)
                holder.message.text=messsage
            }
        }
    }


    override fun getItemCount(): Int {
        return ml.size
    }

    override fun getItemViewType(position: Int): Int {
        val message=ml[position]
        var messageprev:Message?=null
        if(position>0){
            messageprev=ml[position-1]
        }

        return if (message.from_uid==FirebaseAuth.getInstance().currentUser?.uid){
            if (messageprev!=null && messageprev.from_uid==message.from_uid){
                3
            }else{
                1
            }

        }else{
            if (messageprev!=null && messageprev.from_uid==message.from_uid){
                4
            }else{
                2
            }
        }

    }
}