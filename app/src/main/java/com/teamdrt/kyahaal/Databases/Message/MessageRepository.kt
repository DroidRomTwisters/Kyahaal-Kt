/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Databases.Message

import android.app.Application
import androidx.lifecycle.LiveData
import com.teamdrt.kyahaal.Databases.Message.Message
import com.teamdrt.kyahaal.Utils.subscribeOnBackground

class MessageRepository (application: Application, uid1:String?, uid2:String?){
    private var messageDao:MessageDao
    var getAllMessages: LiveData<List<Message>>? =null
    
    private val database=MessagesDatabase.getInstance(application)
    
    init {
        messageDao=database.MessageDao()
        if (uid1!=null && uid2!=null) {
            getAllMessages = messageDao.getAllMessages(uid1, uid2)
        }
    }

    fun insert(Message: Message) {
        subscribeOnBackground {
            messageDao.insert(Message)
        }

    }

    fun update(Message: Message) {
        subscribeOnBackground {
            messageDao.update(Message)
        }
    }

    fun delete(Message: Message) {
        subscribeOnBackground {
            messageDao.delete(Message)
        }
    }

    fun deleteAll(){
        subscribeOnBackground {
            messageDao.deleteAllmessage()
        }
    }
}