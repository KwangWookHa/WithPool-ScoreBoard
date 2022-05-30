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
import wook.pool.board.databinding.DialogDefaultBinding


class DefaultDialog(context: Context) : Dialog(context, R.style.DialogTheme) {

    private val binding: DialogDefaultBinding by lazy {
        DialogDefaultBinding.inflate(LayoutInflater.from(context))
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
        private var onClickLeft: ((DefaultDialog) -> Unit)? = null
        private var onClickRight: ((DefaultDialog) -> Unit)? = null
        private lateinit var type: DialogType
        private var title: String = ""
        private var message: String = ""
        private var imgResourceId: Int = 0
        private var isIconVisible: Boolean = false
        private var btnLeftText: String = ""
        private var btnRightText: String = ""
        private var gravity: Int = Gravity.CENTER
        private var isBackDisabled: Boolean = false

        fun setType(type: DialogType): Builder = this.apply {
            this.type = type
        }

        fun setImgResourceId(imgResourceId: Int): Builder = this.apply {
            this.imgResourceId = imgResourceId
        }

        fun setIsIconVisible(isIconVisible: Boolean): Builder = this.apply {
            this.isIconVisible = isIconVisible
        }

        fun setTitle(title: String): Builder = this.apply {
            this.title = title
        }

        fun setMessage(message: String): Builder = this.apply {
            this.message = message
        }

        fun setGravity(gravity: Int): Builder = this.apply {
            this.gravity = gravity
        }

        fun setOnClickLeft(onClickLeft: (DefaultDialog) -> Unit): Builder = this.apply {
            this.onClickLeft = onClickLeft
        }

        fun setOnClickRight(onClickRight: (DefaultDialog) -> Unit): Builder = this.apply {
            this.onClickRight = onClickRight
        }

        fun setRightButtonText(btnRightText: String): Builder = this.apply {
            this.btnRightText = btnRightText
        }

        fun setLeftButtonText(btnLeftText: String): Builder = this.apply {
            this.btnLeftText = btnLeftText
        }

        fun setBackPressDisabled(disabled: Boolean = false): Builder = this.apply {
            this.isBackDisabled = disabled
        }

        fun create(context: Context): DefaultDialog = DefaultDialog(context).apply dialog@{
            binding.apply {
                onClickLeft = this@Builder.onClickLeft
                onClickRight = this@Builder.onClickRight
                type = this@Builder.type
                title = this@Builder.title
                isIconVisible = this@Builder.isIconVisible
                imgResourceId = this@Builder.imgResourceId
                message = this@Builder.message
                gravity = this@Builder.gravity
                btnLeftText = this@Builder.btnLeftText
                btnRightText = this@Builder.btnRightText
                isBackPressDisabled = this@Builder.isBackDisabled
                dialog = this@dialog
            }
        }
    }

    enum class DialogType {
        DIALOG_OK, DIALOG_OK_CANCEL
    }
}