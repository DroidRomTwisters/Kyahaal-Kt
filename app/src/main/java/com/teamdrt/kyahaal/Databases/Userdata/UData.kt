package com.teamdrt.kyahaal.Databases.Userdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UData")
data class UData(
    @PrimaryKey(autoGenerate = false) val uid: String,

    @ColumnInfo(name= "phone")
    var mobile_no: String? = null,

    @ColumnInfo(name = "uname")
    var name: String? = null,

    @ColumnInfo(name = "pplink")
    var profilepic: String? = null,

    @ColumnInfo(name = "about")
    var status: String? = null,

    @ColumnInfo(name = "tid")
    var token_id: String? = null,

    @ColumnInfo(name = "lastseen")
    var seen: Long? = null,

    @ColumnInfo(name = "isonline")
    var Online: Boolean? = null,

    @ColumnInfo(name = "istyping")
    var istyping: Boolean? = null

)