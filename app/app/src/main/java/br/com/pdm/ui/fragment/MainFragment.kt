package br.com.pdm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.pdm.R
import br.com.pdm.databinding.FragmentLoginBinding
import br.com.pdm.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    fun irParaLogin(v: View) {
        findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
    }

    fun irParaCadastro(v: View) {
        findNavController().navigate(R.id.action_mainFragment_to_cadastroFragment)
    }

    fun irParaHome(v: View) {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    fun irParaCarrossel(v: View) {
        findNavController().navigate(R.id.action_mainFragment_to_carrosselActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.codigoFragmento = this
        binding.lifecycleOwner = this

        return binding.root
    }

}