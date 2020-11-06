/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Databases.Message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(

    @PrimaryKey(autoGenerate = false)
    val push_id:String,

    @ColumnInfo(name = "type")
    val msg_type:String,

    @ColumnInfo(name = "msg")
    val msg:String,

    @ColumnInfo(name = "fuid")
    val from_uid:String,

    @ColumnInfo(name = "tuid")
    val to_uid:String,

    @ColumnInfo(name = "media_loc")
    val media_location:String?,

    @ColumnInfo(name = "sent_tstamp")
    val sent_timestamp: Long?,

    @ColumnInfo(name = "received_tstamp")
    var received_timestamp:Long?=null

){
    constructor():this("","","","","","",null,null)
}

