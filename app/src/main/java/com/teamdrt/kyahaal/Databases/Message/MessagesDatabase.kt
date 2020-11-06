/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Databases.Message

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teamdrt.kyahaal.Databases.Contact.Contact

@Database(entities = [Message::class],version = 1)
abstract class MessagesDatabase:RoomDatabase(){
    abstract fun MessageDao():MessageDao

    companion object{
        private var instance:MessagesDatabase?=null

        @Synchronized
        fun getInstance(context: Context):MessagesDatabase{
            if (instance==null){
                instance= Room.databaseBuilder(
                    context.applicationContext,
                    MessagesDatabase::class.java,
                    "Message"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }

    }
}