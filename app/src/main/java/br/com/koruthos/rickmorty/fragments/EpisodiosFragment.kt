package br.com.koruthos.rickmorty.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.koruthos.rickmorty.databinding.FragmentEpisodiosBinding
import br.com.koruthos.rickmorty.databinding.FragmentPersonagensBinding

class EpisodiosFragment : Fragment() {

    // Objeto com a amarração de todos os componentes do layout
    private lateinit var mBinding: FragmentEpisodiosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inicialização da view usando data binding
        mBinding = FragmentEpisodiosBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o recycler view

        // Chamada para o webservice
        // A resposta do webservice vai atualizar o recycler view
    }

    /**
     * Função estática para construção do fragmento
     */
    companion object {

        @JvmStatic
        fun newInstance() = EpisodiosFragment()
    }
}



