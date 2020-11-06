package com.teamdrt.kyahaal.Databases.UserDataSettings

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userData: UserData)

    @Update
    fun update(userData: UserData)

    @Delete
    fun delete(userData: UserData)

    @Query("delete from UserData")
    fun deleteAllUserData()


    @Query("select * from UserData order by ts")
    fun getAllData(): LiveData<UserData>

    @Query("Select count() from UserData where uid =:uid")
    fun count(uid: String): Int


}