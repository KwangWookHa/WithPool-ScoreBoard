package wook.pool.board.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private const val PREFS_NAME = "withpool_scoreboard_prefs"
        private const val KEY_TABLE_NUMBER = "table_number"
        private const val DEFAULT_TABLE_NUMBER = 1
    }
    
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun getTableNumber(): Int {
        return prefs.getInt(KEY_TABLE_NUMBER, DEFAULT_TABLE_NUMBER)
    }
    
    fun setTableNumber(tableNumber: Int) {
        prefs.edit()
            .putInt(KEY_TABLE_NUMBER, tableNumber)
            .apply()
    }
    
    fun isTableNumberSet(): Boolean {
        return prefs.contains(KEY_TABLE_NUMBER)
    }
}
