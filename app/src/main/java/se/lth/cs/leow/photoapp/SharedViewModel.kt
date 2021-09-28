package se.lth.cs.leow.photoapp

import android.Manifest
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import se.lth.cs.leow.photoapp.db.PointDao
import se.lth.cs.leow.photoapp.db.PointDb
import se.lth.cs.leow.photoapp.db.PointEntity
import se.lth.cs.leow.photoapp.db.PointRepository

class SharedViewModel(application:Application): AndroidViewModel(application) {
    var permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
    lateinit var currentPhotoPath:String
    var lastLatitude: Double? = null
    var lastLongitude: Double? = null
    var lastUri: String? = null

    private val repository:PointRepository
    private var readAllPoints: LiveData<List<PointEntity>>

     init {
        val pointDao = PointDb.getDatabase(application)!!.pointDao()
        repository = PointRepository(pointDao)
        readAllPoints = repository.readAllData
    }

    fun addPoint(point:PointEntity){
        viewModelScope.launch(Dispatchers.IO){
            repository.addPoint(point)
        }
    }
    fun removePoint(id:Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.removePoint(id)
        }
    }
    fun getListOfPoints(): LiveData<List<PointEntity>> {
        return readAllPoints
    }

    fun setLastCoordinates(latitude:Double,longitude:Double){
        lastLatitude = latitude
        lastLongitude = longitude
    }

    fun setLastUriValue(uri: String?) {
        lastUri = uri
    }

}