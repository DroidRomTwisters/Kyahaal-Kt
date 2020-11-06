package com.teamdrt.kyahaal.Databases.Userdata

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(uData: UData)

    @Update
    fun update(uData: UData)

    @Delete
    fun delete(uData: UData)

    @Query("delete from UData")
    fun deleteAllUserData()

    @Query("select * from UData")
    fun getAllData(): LiveData<UData>


}