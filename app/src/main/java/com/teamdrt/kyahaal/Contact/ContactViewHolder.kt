/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Contact

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularimageview.CircularImageView
import com.teamdrt.kyahaal.R

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val pp:CircularImageView=itemView.findViewById(R.id.pp)
    val uname:TextView=itemView.findViewById(R.id.uname2)
    val ustatus:TextView=itemView.findViewById(R.id.ustatus)
    val single_layout=itemView.findViewById<RelativeLayout>(R.id.single_layout)
}