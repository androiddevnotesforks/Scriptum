package sgtmelon.scriptum.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.IncludeInfoBinding
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.state.PageState
import sgtmelon.scriptum.office.utils.AppUtils.bind
import sgtmelon.scriptum.office.utils.AppUtils.change

class IntroFragment : Fragment() {

    companion object {
        fun getInstance(pageState: PageState): IntroFragment {
            val introFragment = IntroFragment()
            introFragment.pageState = pageState

            return introFragment
        }
    }

    private val parentContainer: View? by lazy {
        view?.findViewById<View>(R.id.info_parent_container)
    }

    lateinit var pageState: PageState
    lateinit var binding: IncludeInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.include_info, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            pageState.page = savedInstanceState.getInt(IntentDef.STATE_PAGE)
        }

        binding.bind(pageState.page)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(IntentDef.STATE_PAGE, pageState.page)
    }

    fun setChange(alpha: Float, scale: Float) = parentContainer?.change(alpha, scale)

}