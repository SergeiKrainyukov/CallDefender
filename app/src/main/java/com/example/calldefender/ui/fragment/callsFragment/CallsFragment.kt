package com.example.calldefender.ui.fragment.callsFragment

//import com.example.calldefender.di.DaggerMainComponent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.R
import com.example.calldefender.databinding.FragmentCallsBinding
import com.example.calldefender.ui.fragment.callsFragment.adapter.ViewPagerAdapter
import com.example.calldefender.ui.model.CallUi
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.disposables.CompositeDisposable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CallsFragment : Fragment() {

    @Inject
    lateinit var viewModel: CallsFragmentViewModel
    private lateinit var binding: FragmentCallsBinding
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as CallDefenderApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        viewModel.init()
    }

    private fun bindViewModel() {
        viewModel.callsDataLiveData().observe(viewLifecycleOwner) {
            initTabs(it)
        }
    }

    private fun initTabs(data: List<List<CallUi>>) {
        val adapter = ViewPagerAdapter(data)
        with(binding) {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.all_calls)
                    else -> getString(R.string.rejected_calls)
                }
            }.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}

fun Date.formatToPattern(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(
        time
    )

enum class DatePatterns(val pattern: String) {
    DEFAULT("dd.MM.yyyy hh:mm:ss")
}