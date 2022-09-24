package wook.pool.board.global.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    abstract val layoutResId: Int
    lateinit var binding: VB


    var fragmentActivity: FragmentActivity? = null
    var hostActivityContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<VB>(inflater, layoutResId, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        fragmentActivity = activity
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hostActivityContext = context
        fragmentActivity = activity
    }

    override fun onDetach() {
        super.onDetach()
        hostActivityContext = null
        fragmentActivity = null
    }

}