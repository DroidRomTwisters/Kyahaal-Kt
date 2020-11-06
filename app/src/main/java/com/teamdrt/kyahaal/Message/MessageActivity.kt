/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.mikhaellopez.circularimageview.CircularImageView
import com.teamdrt.kyahaal.Message.ui.main.MessageFragment
import com.teamdrt.kyahaal.R
import de.hdodenhof.circleimageview.CircleImageView

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_activity)
        val tab=findViewById<Toolbar>(R.id.toolbar3)
        val title=findViewById<TextView>(R.id.toolbar_title1)
        val dp=findViewById<CircleImageView>(R.id.dp_iv)
        val back=findViewById<CircleImageView>(R.id.back)

        setSupportActionBar(tab)
        val ab = supportActionBar
        title.text = intent.getStringExtra("cname")
        ab?.title = null
        Glide.with(this).load(intent.getStringExtra("dpurl")).thumbnail(0.1f).placeholder(R.drawable.ic_default_dp1).into(dp)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MessageFragment.newInstance(intent.getStringExtra("uid")!!,intent.getStringExtra("cname"),intent.getStringExtra("cphone")!!,intent.getStringExtra("dpurl")!!),"chat")
                .commitNow()
        }
        back.setOnClickListener{
            onBackPressed()
        }
        dp.setOnClickListener{
            onBackPressed()
        }
    }
}