package wook.pool.board.screen.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import wook.pool.board.R
import wook.pool.board.databinding.DialogSetTotalBinding


class SetTotalDialog(context: Context) : Dialog(context, R.style.DialogTheme) {

    private val binding: DialogSetTotalBinding by lazy {
        DialogSetTotalBinding.inflate(LayoutInflater.from(context))
    }

    private var isBackPressDisabled: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window!!.apply {
            setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER_VERTICAL)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onBackPressed() {
        if (isBackPressDisabled) return
        super.onBackPressed()
    }


    class Builder {
        private var onClickCancel: ((SetTotalDialog) -> Unit)? = null
        private var onClickConfirm: ((SetTotalDialog, String) -> Unit)? = null

        fun setOnClickCancel(onClickCancel: (SetTotalDialog) -> Unit): Builder = this.apply {
            this.onClickCancel = onClickCancel
        }

        fun setOnClickConfirm(onClickConfirm: (SetTotalDialog, String) -> Unit): Builder = this.apply {
            this.onClickConfirm = onClickConfirm
        }

        fun create(context: Context): SetTotalDialog = SetTotalDialog(context).apply dialog@{
            binding.apply {
                onClickCancel = this@Builder.onClickCancel
                onClickConfirm = this@Builder.onClickConfirm
                dialog = this@dialog
            }
        }
    }
}