package local.kas.weather.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import local.kas.weather.R
import local.kas.weather.App.Companion.getHistoryWeatherDao
import local.kas.weather.room.HistoryWeatherEntity
import local.kas.weather.room.ID
import local.kas.weather.room.NAME
import local.kas.weather.room.TEMPERATURE

private const val URI_ALL = 1
private const val URI_ID = 2
private const val ENTITY_PATH = "HistoryWeatherEntity"


class EducationContentProvider : ContentProvider() {

    private var authorities: String? = null
    private lateinit var uriMatcher: UriMatcher

    private var entityContentType: String? = null
    private var entityContentItemType: String? = null
    private lateinit var contentUri: Uri

    override fun onCreate(): Boolean {
        authorities = context?.getString(R.string.authorities)
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(authorities, ENTITY_PATH, URI_ALL)
        uriMatcher.addURI(authorities, "${ENTITY_PATH}/#", URI_ID)
        entityContentType = "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        entityContentItemType = "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"
        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val historyWeatherDao = getHistoryWeatherDao()
        val entity = mapper(values)
        historyWeatherDao.insert(entity)
        val resultUri = ContentUris.withAppendedId(contentUri, entity.id)
        context?.contentResolver?.notifyChange(resultUri, null)
        return resultUri
    }

    private fun mapper(values: ContentValues?): HistoryWeatherEntity {
        values?.let {
            val id = it[ID] as Long
            val name = it[NAME] as String
            val temperature = it[TEMPERATURE] as Int
            return HistoryWeatherEntity(id, name, temperature)
        }
        return HistoryWeatherEntity()
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }
}