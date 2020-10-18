package at.stefangaller.rxforms

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable

abstract class NavigationFragment : Fragment() {

    private lateinit var disposable: Disposable

    protected abstract val viewModel: NavigationViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable = viewModel.directions
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                println(it)
                findNavController().navigate(it)
            }
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }

}