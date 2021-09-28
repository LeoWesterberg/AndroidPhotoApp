package se.lth.cs.leow.photoapp.db
import androidx.lifecycle.LiveData

class PointRepository(private val pointDao: PointDao) {
    val readAllData: LiveData<List<PointEntity>> = pointDao.getAllPoint()

    suspend fun addPoint(point:PointEntity){
        pointDao.addPoint(point)
    }
    suspend fun removePoint(id:Int){
        pointDao.deletePointById(id)
    }

}