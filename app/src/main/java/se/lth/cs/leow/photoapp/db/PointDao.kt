package se.lth.cs.leow.photoapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PointDao {
    @Query("SELECT * FROM point_table ORDER BY id DESC")
    fun getAllPoint(): LiveData<List<PointEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPoint(pointEntity:PointEntity)

    @Delete
    fun deletePoint(pointEntity: PointEntity)




}