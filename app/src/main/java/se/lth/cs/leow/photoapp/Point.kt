package se.lth.cs.leow.photoapp

import android.net.Uri
import android.provider.MediaStore

data class Point(val latitude: Double, val longitude: Double,  val uri: Uri) {

}