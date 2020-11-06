package com.teamdrt.kyahaal.Databases.Contact

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contact::class],version = 1)
abstract class ContactDatabase:RoomDatabase() {
    abstract fun ContactDao():ContactDao

    companion object{
        private var instace:ContactDatabase?=null

        @Synchronized
        fun getInstance(ctx:Context):ContactDatabase{
            if (instace==null){
                instace= Room.databaseBuilder(
                    ctx.applicationContext,
                    ContactDatabase::class.java,
                    "Contacts"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instace!!
        }

    }
}