package at.stefangaller.rxforms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import kotlinx.android.synthetic.main.fragment_first_step.*

class FirstStepFragment : NavigationFragment() {

    override val viewModel: MultiStepViewModel by navGraphViewModels(R.id.main_nav) { defaultViewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_first_step, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registerFirstFragmentInput(button_next.clicks(), username.textChanges(), password.textChanges())

        viewModel.error.observe(viewLifecycleOwner, Observer {
            error.text = it
        })

    }
}