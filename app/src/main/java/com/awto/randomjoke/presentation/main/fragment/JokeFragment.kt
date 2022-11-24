package com.awto.randomjoke.presentation.main.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import com.awto.randomjoke.R
import com.awto.randomjoke.data.remote.model.JokeResponseModel
import com.awto.randomjoke.databinding.FragmentFirstBinding
import com.awto.randomjoke.presentation.main.viewmodel.JokesState
import com.awto.randomjoke.presentation.main.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class JokeFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val viewModel: MainViewModel by viewModels()

    private val binding get() = _binding!!
    private var shortAnimationDuration: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
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
    }

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
            when(data.type) {
                getString(R.string.single) -> {
                    binding.textviewFirst.apply {
                        text = data.joke
                        alpha = 0f
                        visibility = View.VISIBLE
                        animate()
                            .alpha(1f)
                            .setDuration(shortAnimationDuration.toLong())
                            .setListener(null)
                    }
                }
                getString(R.string.twopart) -> {
                    //show second text
                    binding.textviewSecond.apply {
                        text = data.delivery
                        alpha = 0f
                        visibility = View.VISIBLE
                        animate()
                            .alpha(1f)
                            .setDuration(shortAnimationDuration.toLong())
                            .setListener(null)
                    }
                    binding.textviewFirst.apply {
                        text = data.setup
                        alpha = 0f
                        visibility = View.VISIBLE
                        animate()
                            .alpha(1f)
                            .setDuration(shortAnimationDuration.toLong())
                            .setListener(null)
                    }
                }
            }
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

            }
            is JokesState.Init -> {

            }
        }
    }

    private fun handleLoading(loading: Boolean) {
        if (loading) {

        } else {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}