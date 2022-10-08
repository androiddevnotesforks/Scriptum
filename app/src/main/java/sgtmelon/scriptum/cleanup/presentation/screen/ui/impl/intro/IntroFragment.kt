package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlin.math.max
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.data.IntroData
import sgtmelon.scriptum.databinding.IncInfoBinding
import sgtmelon.scriptum.infrastructure.utils.inflateBinding

/**
 * Fragment page for [IntroActivity].
 */
class IntroFragment : Fragment() {

    private var binding: IncInfoBinding? = null

    private var page: Int = ND_PAGE

    /**
     * Setup manually because after rotation lazy function will return null.
     */
    private var parentContainer: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflater.inflateBinding(R.layout.inc_info, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentContainer = view.findViewById(R.id.info_parent_container)

        page = savedInstanceState?.getInt(PAGE_CURRENT)
                ?: arguments?.getInt(PAGE_CURRENT)
                ?: ND_PAGE

        if (page != ND_PAGE) binding?.onBindingPage(page)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PAGE_CURRENT, page)
    }

    /**
     * Update screen content display.
     */
    fun setChange(offset: Float) {
        val alpha = max(0.2F, offset)
        val scale = max(0.75F, offset)

        parentContainer?.apply {
            this.alpha = alpha
            this.scaleX = scale
            this.scaleY = scale
        }
    }


    private fun IncInfoBinding.onBindingPage(page: Int) {
        icon = IntroData.icon[page]
        title = IntroData.title[page]
        details = IntroData.details[page]

        executePendingBindings()
    }

    companion object {
        private const val PAGE_CURRENT = "ARGUMENT_INTRO_PAGE_CURRENT"
        private const val ND_PAGE = -1

        operator fun get(page: Int) = IntroFragment().apply {
            arguments = Bundle().apply { putInt(PAGE_CURRENT, page) }
        }
    }

}