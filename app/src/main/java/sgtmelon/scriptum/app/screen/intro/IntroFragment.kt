package sgtmelon.scriptum.app.screen.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.IncludeInfoBinding
import sgtmelon.scriptum.office.utils.AppUtils.bind
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding

/**
 * Фрагмент страницы для вступления [IntroActivity]
 */
class IntroFragment : Fragment() {

    lateinit var binding: IncludeInfoBinding

    private var page: Int = UNDEFINED

    private val parentContainer: View? by lazy {
        view?.findViewById<View>(R.id.info_parent_container)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = inflater.inflateBinding(R.layout.include_info, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        page = savedInstanceState?.getInt(PAGE_CURRENT)
                ?: arguments?.getInt(PAGE_CURRENT)
                ?: UNDEFINED

        if (page != UNDEFINED) binding.bind(page)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PAGE_CURRENT, page)
    }

    fun setChange(alpha: Float, scale: Float) = parentContainer?.apply {
        this.alpha = alpha
        scaleX = scale
        scaleY = scale
    }

    companion object {
        private const val PAGE_CURRENT = "ARGUMENT_INTRO_PAGE_CURRENT"
        private const val UNDEFINED = -1

        fun getInstance(page: Int) = IntroFragment().apply {
            arguments = Bundle().apply { putInt(PAGE_CURRENT, page) }
        }
    }

}