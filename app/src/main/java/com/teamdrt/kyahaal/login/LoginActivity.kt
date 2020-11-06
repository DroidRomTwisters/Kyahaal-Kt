package com.teamdrt.kyahaal.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.teamdrt.kyahaal.R
import com.teamdrt.kyahaal.login.UserDataActivity.UserDataFragment
import com.teamdrt.kyahaal.login.ui.login.LoginFragment


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val loginfragment = LoginFragment()
        if (!sharedPref.getBoolean(resources.getString(R.string.login_completion_state), false)) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
                .add(R.id.fragment, loginfragment, R.string.login_tg.toString())
                .commit()
        }else {
                val userDataFragment = UserDataFragment()
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
                    .replace(R.id.fragment, userDataFragment, R.string.udata_tg.toString())
                    .commit()
        }

    }


}