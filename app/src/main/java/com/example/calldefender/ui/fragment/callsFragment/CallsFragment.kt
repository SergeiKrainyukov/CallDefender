package com.example.calldefender.ui.fragment.callsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.R
import com.example.calldefender.data.CallEntity
import com.example.calldefender.databinding.FragmentCallsBinding
import com.example.calldefender.ui.fragment.callsFragment.adapter.ViewPagerAdapter
import com.example.calldefender.ui.model.CallStatus
import com.example.calldefender.ui.model.CallUi
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CallsFragment : Fragment() {

    private lateinit var binding: FragmentCallsBinding
    private val disposables = CompositeDisposable()
    private var data: MutableList<MutableList<CallUi>> = mutableListOf()

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
        initData()
    }

    private fun initData() {
        val dao =
            (requireActivity().application as CallDefenderApp).getAppDatabase().callEntityDao()
        disposables.add(
            dao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ callEntities ->
                    if (callEntities.isEmpty()) {
                        addEntities()
                    } else {
                        callEntities.forEach {
                            if (data.isEmpty()) {
                                data.add(mutableListOf())
                                data.add(mutableListOf())
                            }
                            data[0].add(
                                CallUi(
                                    callNumber = it.callNumber,
                                    callDate = Date(it.callDate).formatToPattern(DatePatterns.DEFAULT.pattern),
                                    callStatus = if (it.rejected) CallStatus.REJECTED else CallStatus.ACCEPTED
                                )
                            )
                        }
                        initTabs()
                    }
                }, { error ->
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                })
        )
    }

    private fun addEntities() {
        val dao =
            (requireActivity().application as CallDefenderApp).getAppDatabase().callEntityDao()
        disposables.add(
            dao.insert(CallEntity(0, "+79991234567", Date().time, false))
                .subscribeOn(Schedulers.io()) // Операции с базой данных выполняются в фоновом потоке
                .observeOn(AndroidSchedulers.mainThread()) // Результаты обрабатываются в UI-потоке
                .subscribe({
                    Toast.makeText(
                        requireContext(),
                        "call inserted successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    initTabs()
                }, {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                })
        )
    }

    private fun initTabs() {
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