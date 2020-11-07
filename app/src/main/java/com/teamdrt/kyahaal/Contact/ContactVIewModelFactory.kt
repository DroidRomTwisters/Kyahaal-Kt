/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Contact

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ContactVIewModelFactory(val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
            return ContactViewModel(application = application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}