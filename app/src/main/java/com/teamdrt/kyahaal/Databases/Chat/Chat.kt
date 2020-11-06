/*
 * Copyright (c) 2020.
 * All rights Reserved
 */

package com.teamdrt.kyahaal.Databases.Chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatList")
data class Chat (

    @PrimaryKey(autoGenerate = false)
    val uid:String,

    val name:String,

    val phone:String,

    val pplink:String,

    val lastmsg:String,

    val fromme:Boolean,

    var unreadcount:Int,

    val latestmsg_time:Long

)
