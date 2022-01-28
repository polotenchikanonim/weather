package local.kas.weather.room

import android.app.Application
import androidx.room.Room

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