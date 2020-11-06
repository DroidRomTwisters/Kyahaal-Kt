package com.teamdrt.kyahaal.login.Finishing

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class FinishingViewMOdelFactory(val application: Application):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FinishingViewModel::class.java)){
            return FinishingViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}