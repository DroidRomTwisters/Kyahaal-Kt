package com.teamdrt.kyahaal.Databases.Contact

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = false)
    val uid: String,

    @ColumnInfo(name = "phone")
    val number: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "pplink")
    val pplink: String,

    @ColumnInfo(name = "about")
    val status: String

)
