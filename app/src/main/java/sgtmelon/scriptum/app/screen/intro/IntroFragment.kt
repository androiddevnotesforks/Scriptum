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
 *
 * @author SerjantArbuz
 * @version 1.0
 */
class IntroFragment : Fragment() {

    private var binding: IncludeInfoBinding? = null

    private var page: Int = UNDEFINED

    private var parentContainer: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.include_info, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentContainer = view.findViewById(R.id.info_parent_container)

        page = savedInstanceState?.getInt(PAGE_CURRENT)
                ?: arguments?.getInt(PAGE_CURRENT)
                ?: UNDEFINED

        if (page != UNDEFINED) binding?.bind(page)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PAGE_CURRENT, page)
    }

    /**
     * Обновление отображения контента страницы
     */
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