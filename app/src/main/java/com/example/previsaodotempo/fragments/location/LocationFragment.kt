package com.example.previsaodotempo.fragments.location

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.previsaodotempo.databinding.FragmentLocationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.zip.Inflater

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val localizacaoVisual: LocalizacaoVisual by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstances: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()

    }




    private fun setListeners() {
        binding.imageClose.setOnClickListener { findNavController().popBackStack() }
        binding.inputSearch.editText?.setOnEditorActionListener {_, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard()
                val query = binding.inputSearch.editText?.text
                if(query.isNullOrBlank()) return@setOnEditorActionListener true
                searchLocation(query.toString())
            }
            return@setOnEditorActionListener true
        }
    }

    private fun setObservers() {
        localizacaoVisual.searchResult.observe(viewLifecycleOwner) {
            val searchResultDataState = it ?: return@observe
            if (searchResultDataState.isLoading) {
                binding.locationsRecyclerview.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
            searchResultDataState.locations?.let { remoteLocations ->
                Toast.makeText (
                    requireContext(),
                    "${remoteLocations.size} localizacao encontrada",
                    Toast.LENGTH_SHORT
                ).show()
            }
            searchResultDataState.error?.let { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun searchLocation(query: String) {
        localizacaoVisual.searchLocation(query)
    }

    private fun hideSoftKeyboard() {
        val inputManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            binding.inputSearch.editText?.windowToken, 0
        )
    }
}