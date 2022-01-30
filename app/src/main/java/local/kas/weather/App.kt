package local.kas.weather

import android.app.Application
import androidx.room.Room
import local.kas.weather.room.HistoryDataBase
import local.kas.weather.room.HistoryWeatherDao

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private const val DB_NAME = "history.db"
        private var db: HistoryDataBase? = null

        fun getHistoryWeatherDao(): HistoryWeatherDao {
            if (db == null) {
                db = Room.databaseBuilder(appInstance!!, HistoryDataBase::class.java, DB_NAME)
//                    .allowMainThreadQueries()
                    .build()
            }
            return db!!.historyWeatherDao()
        }
    }
}