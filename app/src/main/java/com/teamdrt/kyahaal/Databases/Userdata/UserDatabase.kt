package com.teamdrt.kyahaal.Databases.Userdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UData::class],version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun UDao():UDao

    companion object{
        private var instance:UserDatabase?=null

        @Synchronized
        fun getInstance(ctx:Context):UserDatabase{
            if (instance==null){
                instance= Room.databaseBuilder(
                    ctx.applicationContext,
                    UserDatabase::class.java,
                    "loggedIn_user"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return instance!!
        }
    }
}