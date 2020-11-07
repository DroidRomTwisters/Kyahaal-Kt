/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Message.ui.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamdrt.kyahaal.R
import com.vanniktech.emoji.EmojiTextView

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val message=itemView.findViewById<EmojiTextView>(R.id.text)
    val iv=itemView.findViewById<ImageView>(R.id.test_arrow)
}