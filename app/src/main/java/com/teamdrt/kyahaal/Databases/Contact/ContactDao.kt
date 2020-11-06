package com.teamdrt.kyahaal.Databases.Contact

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Update
    fun update(contact: Contact)

    @Delete
    fun delete(contact: Contact)

    @Query("delete from contacts")
    fun deleteAllcontact()

    @Query("select * from contacts order by name ASC")
    fun getAllData(): LiveData<List<Contact>>
}