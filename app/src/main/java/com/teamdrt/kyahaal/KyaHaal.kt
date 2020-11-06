package com.teamdrt.kyahaal

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.facebook.FacebookEmojiProvider
import com.vanniktech.emoji.ios.IosEmojiProvider

class KyaHaal : Application() {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate() {
        super.onCreate()
        EmojiManager.install(IosEmojiProvider())
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}