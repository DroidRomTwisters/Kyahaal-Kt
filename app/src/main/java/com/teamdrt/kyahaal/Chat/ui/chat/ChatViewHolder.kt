/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Chat.ui.chat

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamdrt.kyahaal.R
import com.vanniktech.emoji.EmojiTextView
import de.hdodenhof.circleimageview.CircleImageView

class ChatViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
    val pp=itemView.findViewById<CircleImageView>(R.id.profileimage)
    val uname=itemView.findViewById<TextView>(R.id.username)
    val uc=itemView.findViewById<TextView>(R.id.unread_count)
    val last_msg=itemView.findViewById<EmojiTextView>(R.id.last_msg)
    val single_layout=itemView.findViewById<RelativeLayout>(R.id.single_layout)
}