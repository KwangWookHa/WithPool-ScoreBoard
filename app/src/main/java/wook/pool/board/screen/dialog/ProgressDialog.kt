package wook.pool.board.screen.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import wook.pool.board.R
import wook.pool.board.databinding.DialogProgressBinding

class ProgressDialog(context: Context) : Dialog(context, R.style.Progress) {

    private val binding: DialogProgressBinding by lazy {
        DialogProgressBinding.inflate(LayoutInflater.from(context), null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(binding.root)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
        setCanceledOnTouchOutside(false)
        initLayout()
    }

    override fun onBackPressed() {
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnimation()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            WindowInsetsControllerCompat(it, it.decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    private fun initLayout() {
        setOnShowListener(onShowListener)
        setOnDismissListener(onDismissListener)
    }

    private fun startAnimation() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
        binding.imageProgress.startAnimation(anim)
    }

    private fun clearAnimation() {
        binding.imageProgress.clearAnimation()
    }

    private fun showProgress() {
        startAnimation()
    }

    /**
     * 프로그래스 애니메이션 종료
     */
    private fun hideProgress() {
        clearAnimation()
        dismiss()
    }

    /**
     * 다이얼 로그 활성화 시
     */
    private val onShowListener = DialogInterface.OnShowListener {
        showProgress()
    }

    /**
     * 다이얼로그 비활성화 시
     */
    private val onDismissListener = DialogInterface.OnDismissListener {
        hideProgress()
    }
}