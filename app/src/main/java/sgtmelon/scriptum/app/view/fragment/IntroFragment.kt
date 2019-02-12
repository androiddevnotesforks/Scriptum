package sgtmelon.scriptum.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.IncludeInfoBinding
import sgtmelon.scriptum.office.annot.IntroAnn
import sgtmelon.scriptum.office.annot.def.IntentDef
import sgtmelon.scriptum.office.st.PageSt

class IntroFragment : Fragment() {

    companion object {
        fun getInstance(pageSt: PageSt): IntroFragment{
            val introFragment = IntroFragment()
            introFragment.pageSt = pageSt

            return introFragment
        }
    }

    private val parentContainer by lazy { view!!.findViewById<View>(R.id.info_parent_container) }

    lateinit var pageSt: PageSt
    lateinit var binding: IncludeInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        if (savedInstanceState != null){
            pageSt.page = savedInstanceState.getInt(IntentDef.STATE_PAGE)
        }

        binding = DataBindingUtil.inflate(
                inflater, R.layout.include_info, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(IntentDef.STATE_PAGE, pageSt.page)
    }

    fun setChange(alpha: Float, scale: Float) {
        parentContainer.alpha = alpha
        parentContainer.scaleX = scale
        parentContainer.scaleY = scale
    }

    private fun bind() {
        val page = pageSt.page

        binding.icon = IntroAnn.icon[page]
        binding.title = IntroAnn.title[page]
        binding.details = IntroAnn.details[page]

        binding.executePendingBindings()
    }

}