package com.teamdrt.kyahaal

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.ios.IosEmojiProvider

class KyaHaal : Application() {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private val mUsersDatabase: DatabaseReference? = null
    private var mydb: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null

    override fun onCreate() {
        super.onCreate()
        //MobileAds.initialize(this)
        EmojiManager.install(IosEmojiProvider())
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser != null) {
            mydb = mAuth!!.currentUser?.uid?.let {
                FirebaseDatabase.getInstance().getReference("User")
                    .child(it)
            }
            mydb?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    mydb!!.child("Online").onDisconnect().setValue(false)
                    mydb!!.child("seen").onDisconnect().setValue(ServerValue.TIMESTAMP)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    }
}