package wook.pool.board

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WalletApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        Logger.addLogAdapter(
                object : AndroidLogAdapter(
                        PrettyFormatStrategy.newBuilder()
                                .showThreadInfo(false)
                                .tag(getString(R.string.app_name))
                                .methodCount(1)
                                .build()
                ) {
                    override fun isLoggable(priority: Int, tag: String?): Boolean = BuildConfig.DEBUG
                }
        )
    }
}