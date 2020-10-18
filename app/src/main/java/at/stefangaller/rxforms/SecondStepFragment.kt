package at.stefangaller.rxforms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.jakewharton.rxbinding4.view.clicks
import kotlinx.android.synthetic.main.fragment_first_step.*

class SecondStepFragment : NavigationFragment() {

    override val viewModel: MultiStepViewModel by navGraphViewModels(R.id.main_nav) { defaultViewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_second_step, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registerSecondFragmentClicks(button_next.clicks())


    }

}