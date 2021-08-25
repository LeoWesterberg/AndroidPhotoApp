package se.lth.cs.leow.photoapp.db
import androidx.lifecycle.LiveData
import se.lth.cs.leow.photoapp.Point

class PointRepository(private val pointDao: PointDao) {
    val readAllData: LiveData<List<PointEntity>> = pointDao.getAllPoint()

    suspend fun addPoint(point:PointEntity){
        pointDao.addPoint(point)
    }
    suspend fun removePoint(point:PointEntity){
        pointDao.deletePoint(point)
    }

}