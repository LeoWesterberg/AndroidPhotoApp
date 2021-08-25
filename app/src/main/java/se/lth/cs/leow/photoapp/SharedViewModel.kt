package se.lth.cs.leow.photoapp

import android.Manifest
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel:ViewModel() {
    //NewPhotoFragment
    var permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
    lateinit var currentPhotoPath:String
    var lastLatitude: Double? = null
    var lastLongitude: Double? = null
    var lastUri: String? = null



    private var listOfPoints:MutableLiveData<MutableList<Point>> = MutableLiveData(mutableListOf())

    fun setLastCoordinates(latitude:Double,longitude:Double){
        lastLatitude = latitude
        lastLongitude = longitude
    }
    fun getListOfPoints(): LiveData<MutableList<Point>> {
        return listOfPoints
    }
    fun printPoints(){
        listOfPoints.value?.forEach {
            Log.d("Test","${it.latitude} ${it.longitude} ${it.uri}")
        }
    }

    fun addPoint(point:Point){
        listOfPoints.value!!.add(point)
    }

    fun removePoint(point:Point){
        listOfPoints.value?.remove(point)
    }

    fun setLastUriValue(uri: String?) {
        lastUri = uri
    }
}