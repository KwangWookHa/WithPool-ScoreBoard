package wook.withpool.board.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class BaseFragment<VB : ViewDataBinding?> : Fragment() {

    abstract val layoutResId: Int
    var binding: VB? = null


    var fragmentActivity: FragmentActivity? = null
    var hostActivityContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<VB>(inflater, layoutResId, container, false)?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding!!.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}