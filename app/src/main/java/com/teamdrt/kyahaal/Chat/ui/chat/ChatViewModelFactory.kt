/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

@file:Suppress("UNCHECKED_CAST")

package com.teamdrt.kyahaal.Chat.ui.chat

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teamdrt.kyahaal.Message.ui.main.MainViewModel

class ChatViewModelFactory(val application: Application,val fm: FragmentManager):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)){
            return ChatViewModel(application,fm) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}