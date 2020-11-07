package com.teamdrt.kyahaal.Chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.teamdrt.kyahaal.Chat.ui.pagerAdapter.SectionsPagerAdapter
import com.teamdrt.kyahaal.R
import com.teamdrt.kyahaal.login.LoginActivity

class ChatActivity : AppCompatActivity() {

    //private var mAdView: AdView? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)
        //mAdView = findViewById(R.id.adView)
        //val adRequest: AdRequest = AdRequest.Builder().build()
        //mAdView!!.loadAd(adRequest)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            auth = FirebaseAuth.getInstance()
            if (auth.currentUser != null) {
                if (!sharedPref.getBoolean(
                        resources.getString(R.string.login_completion_state),
                        false
                    )) {
                    val sectionsPagerAdapter = SectionsPagerAdapter(
                        this,
                        supportFragmentManager
                    )
                    val viewPager = findViewById<ViewPager>(R.id.view_pager)
                    viewPager.offscreenPageLimit = 2
                    viewPager.adapter = sectionsPagerAdapter
                    val tabs = findViewById<TabLayout>(R.id.tabs)
                    tabs.setupWithViewPager(viewPager)
                } else {
                    startLoginActivityInent()
                }
            } else {
                startLoginActivityInent()
            }

    }

    private fun startLoginActivityInent() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}