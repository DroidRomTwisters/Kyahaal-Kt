/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Databases.Message

import androidx.lifecycle.LiveData
import androidx.room.*
import com.teamdrt.kyahaal.Databases.Contact.Contact

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message)

    @Update
    fun update(message: Message)

    @Delete
    fun delete(message: Message)

    @Query("delete from messages")
    fun deleteAllmessage()

    @Query("select * from messages where fuid=:from_uid and tuid=:to_uid or fuid=:to_uid and tuid=:from_uid order by sent_tstamp ASC")
    fun getAllMessages(from_uid:String,to_uid:String): LiveData<List<Message>>
}