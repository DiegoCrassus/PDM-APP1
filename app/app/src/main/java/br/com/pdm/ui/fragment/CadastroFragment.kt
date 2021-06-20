package br.com.pdm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.pdm.R
import br.com.pdm.databinding.FragmentCadastroBinding
import br.com.pdm.databinding.FragmentMainBinding


class CadastroFragment : Fragment() {

    private lateinit var binding: FragmentCadastroBinding

    fun irCadastroPMain(v: View) {
        findNavController().navigate(R.id.action_cadastroFragment_to_mainFragment2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCadastroBinding.inflate(inflater, container, false)

        // o código é a própria classe
        binding.codigoFragmento = this
        // o código define o ciclo de vida do fragmento
        binding.lifecycleOwner = this

        return binding.root
    }

}