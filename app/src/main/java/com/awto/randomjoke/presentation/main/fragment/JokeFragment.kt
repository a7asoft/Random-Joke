package com.awto.randomjoke.presentation.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import com.awto.randomjoke.R
import com.awto.randomjoke.data.local.NetworkStateManager
import com.awto.randomjoke.data.remote.model.JokeResponseModel
import com.awto.randomjoke.databinding.FragmentFirstBinding
import com.awto.randomjoke.presentation.main.viewmodel.JokesState
import com.awto.randomjoke.presentation.main.viewmodel.MainViewModel
import com.awto.randomjoke.util.buildChips
import com.awto.randomjoke.util.hideViewWithAnimation
import com.awto.randomjoke.util.showViewWithAnimation
import com.tapadoo.alerter.Alerter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class JokeFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val viewModel: MainViewModel by viewModels()

    private val binding get() = _binding!!
    private var shortAnimationDuration: Int = 0

    //Network state observer
    private val activeNetworkStateObserver: Observer<Boolean> =
        Observer { isConnected -> handleConnection(isConnected) }

    private fun handleConnection(isConnected: Boolean?) {
        if (isConnected == false) {
            Alerter.create(requireActivity())
                .setTitle(getString(R.string.no_connection_title))
                .setText(getString(R.string.no_connection_subtitle))
                .setIcon(R.drawable.ic_baseline_signal_wifi_statusbar_connected_no_internet_4_24)
                .setBackgroundColorRes(R.color.red_error)
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        //Network state observe
        NetworkStateManager.instance?.networkConnectivityStatus
            ?.observe(viewLifecycleOwner, activeNetworkStateObserver)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        observe()
        listeners()
    }

    private fun listeners() {
        binding.fab.setOnClickListener {
            viewModel.getRandomJoke()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.getRandomJoke()
        }
    }

    //handle status and response for Jokes
    private fun observe() {
        observeState()
        observeJokes()
    }

    private fun observeJokes() {
        viewModel.mJoke
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { data ->
                handleJoke(data)
            }
            .launchIn(lifecycle.coroutineScope)
    }

    private fun handleJoke(data: JokeResponseModel) {
        if (data.error) {
            //show error
        } else {
            //handle type of joke
            when (data.type) {
                getString(R.string.single) -> {
                    binding.textviewFirst.apply {
                        text = data.joke
                    }.showViewWithAnimation()

                    binding.textviewSecond.hideViewWithAnimation()
                }
                getString(R.string.twopart) -> {
                    //show second text
                    binding.textviewSecond.apply {
                        text = data.delivery
                    }.showViewWithAnimation()

                    binding.textviewFirst.apply {
                        text = data.setup
                    }.showViewWithAnimation()
                }
            }

            binding.fab.showViewWithAnimation()

            //building Chips categories and flags dynamically
            buildChips(data, binding.chipGroup)

            binding.chipGroup.apply {
                isEnabled = false
                isSelectionRequired = false
            }
            binding.chipGroup.showViewWithAnimation()
        }
    }

    private fun observeState() {
        viewModel.mStateJoke
            .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(lifecycle.coroutineScope)
    }

    private fun handleState(state: JokesState) {
        when (state) {
            is JokesState.IsLoading -> {
                handleLoading(state.isLoading)
            }
            is JokesState.ShowError -> {
                //show error
                binding.textviewError.text = state.error.message
                handleError()
            }
            is JokesState.Init -> {
                //nothing
            }
            is JokesState.ShowException -> {
                //show exception error
                binding.textviewError.text = state.error.message
                handleError()
            }
        }
    }

    private fun handleError() {
        binding.errorView.showViewWithAnimation()
        binding.textviewError.showViewWithAnimation()
        binding.constraintJokes.hideViewWithAnimation()
        binding.fab.hideViewWithAnimation()
        binding.chipGroup.hideViewWithAnimation()
    }

    private fun handleLoading(loading: Boolean) {
        if (loading) {
            binding.chipGroup.hideViewWithAnimation()
            binding.constraintJokes.hideViewWithAnimation()
            binding.lavLoaing.showViewWithAnimation()
            binding.textviewError.hideViewWithAnimation()
            binding.errorView.hideViewWithAnimation()
            binding.fab.hideViewWithAnimation()
        } else {
            binding.chipGroup.showViewWithAnimation()
            binding.constraintJokes.showViewWithAnimation()
            binding.lavLoaing.hideViewWithAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}