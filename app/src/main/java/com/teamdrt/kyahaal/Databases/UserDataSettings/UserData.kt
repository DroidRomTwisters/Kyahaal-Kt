package com.teamdrt.kyahaal.Databases.UserDataSettings

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserData")
data class UserData(

    @ColumnInfo(name = "dpname")
    var dpname: String?,

    @ColumnInfo(name = "ts")
    val timestamp: Long,

    @ColumnInfo(name = "isuploading")
    var isUploading: Boolean = false,

    @PrimaryKey(autoGenerate = false) val uid: String

)