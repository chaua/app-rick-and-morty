package br.com.koruthos.rickmorty.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.koruthos.cursoandroid.networks.NetworkManager
import br.com.koruthos.cursoandroid.widgets.adapters.PersonagemRecyclerAdapter
import br.com.koruthos.rickmorty.R
import br.com.koruthos.rickmorty.databinding.FragmentPersonagensBinding
import br.com.koruthos.rickmorty.models.Personagem
import br.com.koruthos.rickmorty.models.Response
import br.com.koruthos.rickmorty.utils.TAG
import retrofit2.Call
import retrofit2.Callback


class PersonagensFragment : Fragment() {

    // Objeto com a amarração de todos os componentes do layout
    private lateinit var mBinding: FragmentPersonagensBinding

    // Lista com todos os personagens
    private var mPersonagens: MutableList<Personagem> = mutableListOf()

    // Controle da paginação do webservice
    private var mPagina = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inicialização da view usando data binding
        mBinding = FragmentPersonagensBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // -- Configuração do recycler view
        // Define o gerenciador de layout do recycler view
        mBinding.personagensRecycler.layoutManager = LinearLayoutManager(activity)

        // Carrega o adaptador do recycler view
        // - Primeiro parametro: lista com os personagens
        // - Segundo parametro: implementação dos eventos dos itens do recycler view
        mBinding.personagensRecycler.adapter = PersonagemRecyclerAdapter(
            mPersonagens,
            object : PersonagemRecyclerAdapter.Evento {
                override fun onCompartilharClick(personagem: Personagem) {
                    this@PersonagensFragment.onCompartilharClick(personagem)
                }

                override fun onPersonagemClick(personagem: Personagem) {
                    this@PersonagensFragment.onSelecionarPersonagemClick(personagem)
                }
            }
        )

        // Captura o evento de scroll para carregar mais itens na página
        mBinding.personagensRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Se não for mais possível realizar o scroll para baixo, chama o webservice
                if (!recyclerView.canScrollVertically(1)) { //1 for down
                    carregarPersonagens()
                }
            }
        })

        // -- Webservice
        // Cria a chamada para o endpoint do webservice
        carregarPersonagens()
    }

    private fun onSelecionarPersonagemClick(personagem: Personagem) {
        // Cria a caixa de alerta
        val alerta = AlertDialog.Builder(requireContext())
            .setTitle(personagem.nome)
            .setMessage(personagem.genero)
            .setPositiveButton(R.string.app_ok) { dialog, i ->
                // Ação ao selecionar o botão de ok!
                dialog.dismiss()
            }
            .setNegativeButton(R.string.app_nao) { dialog, i ->
                // Ação ao selecionar o botão de não!
                dialog.dismiss()
            }
            .create()

        // Exibe a caixa de alerta
        alerta.show()
    }

    override fun onStop() {
        super.onStop()

        // Termina todos as chamadas de webservice
        NetworkManager.stop()
    }

    /**
     * Chama o webservice para carregar mais personagens.
     */
    private fun carregarPersonagens() {
        Log.d(TAG, "carregarPersonagens: ")

        // Verifica se chegou na última página
        if (mPagina < 0) {
            Log.i(TAG, "carregarPersonagens: Última página - sem dados para carregar")
            return
        }

        Log.d(TAG, "carregarPersonagens: Carregando dados da página $mPagina")
        val call = NetworkManager.service.listarPersonagens(mPagina)

        // Enfileira a execução do webservice e trata a resposta
        call.enqueue(object : Callback<Response<Personagem>> {

            // Retorno de sucesso
            override fun onResponse(
                call: Call<Response<Personagem>>,
                response: retrofit2.Response<Response<Personagem>>
            ) {
                onResponseSuccess(response.body())
            }

            // Retorno de falha
            override fun onFailure(call: Call<Response<Personagem>>, t: Throwable) {
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
    private fun onResponseSuccess(body: Response<Personagem>?) {
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
            val ini = mPersonagens.size
            val qtd = mPersonagens.size + body.resultados.size

            // Concatena as duas listas
            mPersonagens.addAll(body.resultados)
            Log.d(TAG, "onResponseSuccess: ${mPersonagens.size}")

            // Atualiza o recycler view
            Log.d(TAG, "onResponseSuccess: Atualizando adapter")
            Log.d(TAG, "onResponseSuccess: ${mBinding.personagensRecycler.adapter}")
            mBinding.personagensRecycler.adapter?.notifyItemRangeInserted(ini, qtd)

            // Esconde a tela de loading e exibe o recycler
            mBinding.personagensProgress.visibility = View.GONE
            mBinding.personagensRecycler.visibility = View.VISIBLE
        }
    }

    /**
     * Executa a ação de compartilhar um personagem.
     */
    fun onCompartilharClick(personagem: Personagem) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "${personagem.nome}: ${personagem.urlImagem}")
        }
        startActivity(Intent.createChooser(intent, getText(R.string.personagens_chooser)))
    }

    /**
     * Função estática para construção do fragmento
     */
    companion object {

        @JvmStatic
        fun newInstance() = PersonagensFragment()
    }
}



