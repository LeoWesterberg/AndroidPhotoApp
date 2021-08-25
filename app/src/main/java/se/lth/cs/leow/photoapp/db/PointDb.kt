package se.lth.cs.leow.photoapp.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [PointEntity::class], version = 1,exportSchema = false)
abstract class PointDb:RoomDatabase() {
    abstract fun pointDao(): PointDao

    companion object{
        @Volatile
        private var INSTANCE:PointDb? = null

        fun getDatabase(context: Context): PointDb?{
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            else {
                synchronized(this) {
                    val instance = Room.databaseBuilder<PointDb>(
                        context.applicationContext, PointDb::class.java, "point_database"
                    ).allowMainThreadQueries().build()
                }
                return INSTANCE
            }
        }
    }
}