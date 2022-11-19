package br.com.koruthos.rickmorty.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.koruthos.cursoandroid.networks.NetworkManager
import br.com.koruthos.rickmorty.databinding.FragmentEpisodiosBinding
import br.com.koruthos.rickmorty.models.Episodio
import br.com.koruthos.rickmorty.models.Personagem
import br.com.koruthos.rickmorty.models.Response
import br.com.koruthos.rickmorty.utils.TAG
import br.com.koruthos.rickmorty.widgets.adapters.EpisodiosRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback

class EpisodiosFragment : Fragment() {

    // Objeto com a amarração de todos os componentes do layout
    private lateinit var mBinding: FragmentEpisodiosBinding

    // Lista de episodios
    private var mEpisodios = mutableListOf<Episodio>()

    // Controle da paginação do webservice
    private var mPagina = 1

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
        mBinding.episodiosRecycler.layoutManager = GridLayoutManager(activity, 2)

        // Inicializa o adapter
        mBinding.episodiosRecycler.adapter = EpisodiosRecyclerAdapter(
            mEpisodios,
            object : EpisodiosRecyclerAdapter.Evento {
                override fun onEpisodioClick(episodio: Episodio) {
                    this@EpisodiosFragment.onEpisodioClick(episodio)
                }
            }
        )

        // Captura o evento de scroll para carregar mais itens na página
        mBinding.episodiosRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Se não for mais possível realizar o scroll para baixo, chama o webservice
                if (!recyclerView.canScrollVertically(1)) { //1 for down
                    carregarEpisodios()
                }
            }
        })

        // Chamada para o webservice
        // A resposta do webservice vai atualizar o recycler view
        carregarEpisodios()

    }

    private fun onEpisodioClick(episodio: Episodio) {
        Toast.makeText(activity, episodio.nome, Toast.LENGTH_LONG).show()
    }

    /**
     * Chama o webservice para carregar mais episódios.
     */
    private fun carregarEpisodios() {
        Log.d(TAG, "carregarEpisodios: ")

        // Verifica se chegou na última página
        if (mPagina < 0) {
            Log.i(TAG, "carregarEpisodios: Última página - sem dados para carregar")
            return
        }

        Log.d(TAG, "carregarEpisodios: Carregando dados da página $mPagina")
        val call = NetworkManager.service.listarEpisodios(mPagina)

        // Enfileira a execução do webservice e trata a resposta
        call.enqueue(object : Callback<Response<Episodio>> {

            // Retorno de sucesso
            override fun onResponse(
                call: Call<Response<Episodio>>,
                response: retrofit2.Response<Response<Episodio>>
            ) {
                onResponseSuccess(response.body())
            }

            // Retorno de falha
            override fun onFailure(call: Call<Response<Episodio>>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
                if (context != null) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    /**
     * Função para tratar a mensagem de sucesso do webservice.
     */
    private fun onResponseSuccess(body: Response<Episodio>?) {
        Log.d(TAG, "onResponseSuccess: ")

        // Atualiza qual é a próxima página a ser carregada
        // - Se for a última página, invalida o contador de páginas para evitar novas chamadas
        // - Senão, atualiza o contador de páginas
        if (body?.info?.next == null) {
            mPagina = -1
        } else {
            mPagina++
        }

        // Adiciona os resultados obtidos na lista de personagens
        if (body?.resultados != null) {
            // Recupera o range da lista
            val ini = mEpisodios.size
            val qtd = body.resultados.size

            // Concatena as duas listas
            mEpisodios.addAll(body.resultados)
            Log.d(TAG, "onResponseSuccess: ${mEpisodios.size}")

            // Atualiza o recycler view
            Log.d(TAG, "onResponseSuccess: Atualizando adapter")
            Log.d(TAG, "onResponseSuccess: ${mBinding.episodiosRecycler.adapter}")
            mBinding.episodiosRecycler.adapter?.notifyItemRangeInserted(ini, qtd)

        }
    }





    /**
     * Função estática para construção do fragmento
     */
    companion object {

        @JvmStatic
        fun newInstance() = EpisodiosFragment()
    }
}



