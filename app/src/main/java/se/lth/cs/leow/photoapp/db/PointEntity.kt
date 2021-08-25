package se.lth.cs.leow.photoapp.db

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "point_table")
data class PointEntity(@PrimaryKey(autoGenerate = true)
                       @ColumnInfo(name="id") val id:Int,
                       @ColumnInfo(name ="altitude") val altitude:Double,
                       @ColumnInfo(name ="longitude") val longitude:Double,
                       @ColumnInfo(name="uri") val uri:String
)


