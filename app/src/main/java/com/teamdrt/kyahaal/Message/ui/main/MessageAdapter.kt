/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Message.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.teamdrt.kyahaal.Databases.Message.Message
import com.teamdrt.kyahaal.R

class MessageAdapter(context: Context, val ml: ArrayList<Message>): RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        if (viewType==1){
            return MessageViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.chatrightsinglelayout, parent, false))
        }else{
            return MessageViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.chaleftsinglelayout, parent, false))
        }
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.message.text=ml[position].msg
    }


    override fun getItemCount(): Int {
        return ml.size
    }

    override fun getItemViewType(position: Int): Int {
        val message=ml[position]
        return if (message.from_uid==FirebaseAuth.getInstance().currentUser?.uid){
            1
        }else{
            2
        }

    }
}