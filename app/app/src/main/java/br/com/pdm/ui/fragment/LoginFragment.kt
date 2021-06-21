package br.com.pdm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.pdm.R
import br.com.pdm.databinding.FragmentLoginBinding
import java.io.File

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class LoginFragment : Fragment() {
    private var faceSelfie: String? = ""
    private var email: String? = null

    private lateinit var binding: FragmentLoginBinding

    fun irParaHome(v: View) {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.codigoFragmento = this
        binding.lifecycleOwner = this

        return binding.root
    }


}
