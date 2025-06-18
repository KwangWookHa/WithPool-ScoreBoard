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
import wook.pool.board.databinding.DialogSelectTableNumberBinding


class SelectTableNumberDialog(context: Context) : Dialog(context, R.style.DialogTheme) {

    private val binding: DialogSelectTableNumberBinding by lazy {
        DialogSelectTableNumberBinding.inflate(LayoutInflater.from(context))
    }

    private var isBackPressDisabled: Boolean = false
    var selectedTableNumber: Int = 1
    var currentTableNumber: Int = 1
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

    fun updateSelectedButton(newTableNumber: Int) {
        selectedTableNumber = newTableNumber
        // 데이터 바인딩의 selectedTableNumber 변수도 업데이트
        binding.selectedTableNumber = newTableNumber
        // 테이블 번호 변경 텍스트 업데이트
        updateTableNumberChangeText()
        // 데이터 바인딩 새로고침
        binding.invalidateAll()
    }
    
    private fun updateTableNumberChangeText() {
        if (currentTableNumber == selectedTableNumber) {
            // 현재 테이블 번호와 수정할 테이블 번호가 동일한 경우
            val text = "현재 테이블: ${currentTableNumber}번"
            val spannableString = SpannableString(text)
            // 색상 적용 없이 기본 회색으로 표시
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#606060")),
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.textTableNumberChange.text = spannableString
        } else {
            // 현재 테이블 번호와 수정할 테이블 번호가 다른 경우
            val text = "현재 테이블: ${currentTableNumber}번 >>> ${selectedTableNumber}번 (변경할 테이블)"
            val spannableString = SpannableString(text)
            
            // "변경할 테이블" 부분의 인덱스 찾기
            val redPartStart = text.indexOf("${selectedTableNumber}번 (변경할 테이블)")
            if (redPartStart != -1) {
                val redPartEnd = text.length
                spannableString.setSpan(
                    ForegroundColorSpan(Color.RED),
                    redPartStart,
                    redPartEnd,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            
            binding.textTableNumberChange.text = spannableString
        }
        
        // 확인 버튼 활성화/비활성화
        updateConfirmButtonState()
    }
    
    private fun updateConfirmButtonState() {
        val isEnabled = currentTableNumber != selectedTableNumber
        binding.btnConfirm.isEnabled = isEnabled
        binding.btnConfirm.alpha = if (isEnabled) 1.0f else 0.5f
    }


    class Builder {
        private var onClickCancel: ((SelectTableNumberDialog) -> Unit)? = null
        private var onClickConfirm: ((SelectTableNumberDialog, Int) -> Unit)? = null
        private var onClickTableNumber: ((SelectTableNumberDialog, Int) -> Unit)? = null
        private var currentTableNumber: Int = 1

        fun setOnClickCancel(onClickCancel: (SelectTableNumberDialog) -> Unit): Builder = this.apply {
            this.onClickCancel = onClickCancel
        }

        fun setOnClickConfirm(onClickConfirm: (SelectTableNumberDialog, Int) -> Unit): Builder = this.apply {
            this.onClickConfirm = onClickConfirm
        }

        fun setOnClickTableNumber(onClickTableNumber: (SelectTableNumberDialog, Int) -> Unit): Builder = this.apply {
            this.onClickTableNumber = onClickTableNumber
        }

        fun setCurrentTableNumber(tableNumber: Int): Builder = this.apply {
            this.currentTableNumber = tableNumber
        }

        fun create(context: Context): SelectTableNumberDialog = SelectTableNumberDialog(context).apply dialog@{
            binding.apply {
                onClickCancel = this@Builder.onClickCancel
                onClickConfirm = this@Builder.onClickConfirm
                onClickTableNumber = this@Builder.onClickTableNumber ?: { dialog, tableNumber ->
                    dialog.updateSelectedButton(tableNumber)
                }
                currentTableNumber = this@Builder.currentTableNumber
                selectedTableNumber = this@Builder.currentTableNumber
                dialog = this@dialog
                
                // 초기 선택 상태 설정
                this@dialog.currentTableNumber = this@Builder.currentTableNumber
                this@dialog.updateSelectedButton(this@Builder.currentTableNumber)
            }
        }
    }
}
