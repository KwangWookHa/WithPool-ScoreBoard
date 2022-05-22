package wook.pool.board.bindingadapter

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object CommonBindingAdapter {

    @BindingAdapter("bindVisibleOrGone")
    @JvmStatic
    fun setVisibleOrGone(view: View, visibility: Boolean) {
        view.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    @BindingAdapter("bindVisibleOrInvisible")
    @JvmStatic
    fun setVisibleOrInvisible(view: View, visibility: Boolean) {
        view.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }


    @BindingAdapter("bindImgUrl")
    @JvmStatic
    fun AppCompatImageView.setImageUrl(imgUrl: String?) {
        imgUrl?.let {
            Glide.with(context).load(imgUrl).into(this)
        }
    }

    @BindingAdapter("bindImgResourceId")
    @JvmStatic
    fun AppCompatImageView.setImageResourceId(imgResourceId: Int) {
        Glide.with(context).load(imgResourceId).into(this)
    }

    @BindingAdapter("bindUnderLine")
    @JvmStatic
    fun AppCompatTextView.bindUnderLine(flag: Boolean) {
        if (flag) {
            this.text = SpannableString(this.text.toString()).apply {
                setSpan(UnderlineSpan(), 0, length, 0)
            }
        }
    }
}