/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Contact

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamdrt.kyahaal.Databases.Contact.Contact
import com.teamdrt.kyahaal.Message.MessageActivity
import com.teamdrt.kyahaal.R


class ContactAdapter(
    val context: Context,
    val tv: ArrayList<Contact>,
    private val clickListener: ContactClickListener
) : RecyclerView.Adapter<ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.single_contact_view, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact: Contact = tv[position]
        if (contact.pplink != "default") {
            Glide.with(context).load(contact.pplink).thumbnail(0.1f)
                .placeholder(R.drawable.ic_default_dp1).into(
                    holder.pp
                )
        }
        holder.uname.text = contact.name
        holder.ustatus.text = contact.status
        holder.pp.setOnClickListener {
            clickListener.OndpClick(position)
        }
        holder.single_layout.setOnClickListener{
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("cname",contact.name)
            intent.putExtra("dpurl",contact.pplink)
            intent.putExtra("uid",contact.uid)
            intent.putExtra("cphone",contact.number)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return tv.size
    }

}