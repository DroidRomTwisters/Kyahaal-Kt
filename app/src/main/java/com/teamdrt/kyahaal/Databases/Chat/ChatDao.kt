/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Databases.Chat

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chat: Chat)

    @Update
    fun update(chat: Chat)

    @Delete
    fun delete(chat: Chat)

    @Query("update chatList set unreadcount=unreadcount+1 where uid=:uid")
    fun updateUnreadCount(uid:String)

    @Query("select * from chatList order by latestmsg_time DESC")
    fun getAllChatLists():LiveData<List<Chat>>

    @Query("update chatList set unreadcount=0 where uid=:uid")
    fun cleaarUnreadCount(uid:String)

    @Query("select unreadcount from chatList where uid=:uid")
    suspend fun getuc(uid:String):Int?


}