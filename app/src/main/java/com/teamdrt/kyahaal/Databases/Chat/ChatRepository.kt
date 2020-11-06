/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Databases.Chat

import android.app.Application
import androidx.lifecycle.LiveData
import com.teamdrt.kyahaal.Databases.Chat.Chat
import com.teamdrt.kyahaal.Utils.subscribeOnBackground
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatRepository(application: Application,uid:String?) {
    private var chatDao:ChatDao
    var getAllChat:LiveData<List<Chat>>


    private val database=ChatDatabase.getInstance(application)

    init {
        chatDao=database.chatDao()
        getAllChat=chatDao.getAllChatLists()
    }

    fun insert(Chat: Chat) {
        subscribeOnBackground {
            chatDao.insert(Chat)
        }

    }

    fun update(Chat: Chat) {
        subscribeOnBackground {
            chatDao.update(Chat)
        }
    }

    fun delete(Chat: Chat) {
        subscribeOnBackground {
            chatDao.delete(Chat)
        }
    }

    fun incrementuc(uid:String){
        subscribeOnBackground {
            chatDao.updateUnreadCount(uid)
        }
    }

    fun clearuc(uid: String){
        subscribeOnBackground {
            chatDao.cleaarUnreadCount(uid)
        }
    }



}