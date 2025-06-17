package wook.pool.board.screen.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import wook.pool.board.R
import wook.pool.board.databinding.DialogEditHandicapBinding


class EditHandicapDialog(context: Context) : Dialog(context, R.style.DialogTheme) {

    private val binding: DialogEditHandicapBinding by lazy {
        DialogEditHandicapBinding.inflate(LayoutInflater.from(context))
    }

    private var isBackPressDisabled: Boolean = false
    var selectedHandicap: Int = 5
    var currentHandicap: Int = 5
        private set


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

    fun updateSelectedButton(newHandicap: Int) {
        selectedHandicap = newHandicap
        // 데이터 바인딩의 selectedHandicap 변수도 업데이트
        binding.selectedHandicap = newHandicap
        // 핸디캡 변경 텍스트 업데이트
        updateHandicapChangeText()
        // 데이터 바인딩 새로고침
        binding.invalidateAll()
    }
    
    private fun updateHandicapChangeText() {
        if (currentHandicap == selectedHandicap) {
            // 현재 핸디캡과 수정할 핸디캡이 동일한 경우
            val text = "현재 핸디캡: ${currentHandicap}점"
            val spannableString = SpannableString(text)
            // 색상 적용 없이 기본 회색으로 표시
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#606060")),
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textHandicapChange.text = spannableString
        } else {
            // 현재 핸디캡과 수정할 핸디캡이 다른 경우
            val text = "현재 핸디캡: ${currentHandicap}점 >>> ${selectedHandicap}점 (수정할 핸디캡)"
            val spannableString = SpannableString(text)
            
            // "수정할 핸디캡" 부분의 인덱스 찾기
            val redPartStart = text.indexOf("${selectedHandicap}점 (수정할 핸디캡)")
            if (redPartStart != -1) {
                val redPartEnd = text.length
                spannableString.setSpan(
                    ForegroundColorSpan(Color.RED),
                    redPartStart,
                    redPartEnd,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            
            binding.textHandicapChange.text = spannableString
        }
        
        // 확인 버튼 활성화/비활성화
        updateConfirmButtonState()
    }
    
    private fun updateConfirmButtonState() {
        val isEnabled = currentHandicap != selectedHandicap
        binding.btnConfirm.isEnabled = isEnabled
        binding.btnConfirm.alpha = if (isEnabled) 1.0f else 0.5f
    }


    class Builder {
        private var onClickCancel: ((EditHandicapDialog) -> Unit)? = null
        private var onClickConfirm: ((EditHandicapDialog, Int) -> Unit)? = null
        private var onClickHandicap: ((EditHandicapDialog, Int) -> Unit)? = null
        private var playerName: String = ""
        private var currentHandicap: Int = 5

        fun setOnClickCancel(onClickCancel: (EditHandicapDialog) -> Unit): Builder = this.apply {
            this.onClickCancel = onClickCancel
        }

        fun setOnClickConfirm(onClickConfirm: (EditHandicapDialog, Int) -> Unit): Builder = this.apply {
            this.onClickConfirm = onClickConfirm
        }

        fun setOnClickHandicap(onClickHandicap: (EditHandicapDialog, Int) -> Unit): Builder = this.apply {
            this.onClickHandicap = onClickHandicap
        }

        fun setPlayerName(name: String): Builder = this.apply {
            this.playerName = name
        }

        fun setCurrentHandicap(handicap: Int): Builder = this.apply {
            this.currentHandicap = handicap
        }

        fun create(context: Context): EditHandicapDialog = EditHandicapDialog(context).apply dialog@{
            binding.apply {
                onClickCancel = this@Builder.onClickCancel
                onClickConfirm = this@Builder.onClickConfirm
                onClickHandicap = this@Builder.onClickHandicap ?: { dialog, handicap ->
                    dialog.updateSelectedButton(handicap)
                }
                playerName = this@Builder.playerName
                currentHandicap = this@Builder.currentHandicap
                selectedHandicap = this@Builder.currentHandicap
                dialog = this@dialog
                
                // 초기 선택 상태 설정
                this@dialog.currentHandicap = this@Builder.currentHandicap
                this@dialog.updateSelectedButton(this@Builder.currentHandicap)
            }
        }
    }
}
