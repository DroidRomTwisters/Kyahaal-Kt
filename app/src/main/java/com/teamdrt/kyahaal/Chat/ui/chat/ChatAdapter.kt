/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Chat.ui.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamdrt.kyahaal.Contact.ContactClickListener
import com.teamdrt.kyahaal.Databases.Chat.Chat
import com.teamdrt.kyahaal.Message.MessageActivity
import com.teamdrt.kyahaal.R

class ChatAdapter(
    val context: Context,
    val cl: ArrayList<Chat>,
    private val clickListener: ContactClickListener
) :
    RecyclerView.Adapter<ChatViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.singlechatlayout, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = cl[position]
        holder.last_msg.text = chat.lastmsg
        if (chat.unreadcount < 1) {
            holder.uc.visibility = View.GONE
        } else {
            holder.uc.visibility = View.VISIBLE
            holder.uc.text = chat.unreadcount.toString()
        }
        holder.uname.text = chat.name
        Glide.with(context).load(chat.pplink).thumbnail(0.1f).placeholder(R.drawable.ic_default_dp1)
            .into(holder.pp)

        holder.pp.setOnClickListener {
            clickListener.OndpClick(position)
        }

        holder.single_layout.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("cname", chat.name)
            intent.putExtra("dpurl", chat.pplink)
            intent.putExtra("uid", chat.uid)
            intent.putExtra("cphone", chat.phone)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return cl.size
    }


}