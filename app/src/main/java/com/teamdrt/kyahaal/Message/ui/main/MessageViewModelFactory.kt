/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Message.ui.main

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.StorageReference

@Suppress("UNCHECKED_CAST")
class MessageViewModelFactory(val application: Application,val from_uid:String,val to_uid:String,val fm:FragmentManager):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(application,from_uid,to_uid,fm) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}