package br.com.koruthos.cursoandroid.widgets.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.koruthos.rickmorty.R
import br.com.koruthos.rickmorty.databinding.ItemPersonagemBinding
import br.com.koruthos.rickmorty.models.Personagem
import br.com.koruthos.rickmorty.utils.TAG
import com.bumptech.glide.Glide

class PersonagemRecyclerAdapter(
    var personagens: List<Personagem>,
    var evento: PersonagemRecyclerAdapter.Evento
) : RecyclerView.Adapter<PersonagemRecyclerAdapter.ViewHolder>() {

    /**
     * Carrega o layout que será usado nos itens do recycler view. Cria um
     * view holder para cada item que será usado para armazenar os valores de exibição.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonagemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    /**
     * Preenche os dados do view holder com o elemento correspondente da lista de personagens.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ${position}")

        val personagem = personagens[position]
        Log.d(TAG, "onBindViewHolder: ${personagem.nome}")
        Log.d(TAG, "onBindViewHolder: ${holder.binding.personagemTxtNome.text}")

        // Define o personagem do viewholder
        // - os dados são atualizados diretamente na tela devido ao databinding
        holder.binding.personagem = personagem

        // Cadastra os eventos nos botões
        holder.binding.personagemImgShare.setOnClickListener {
            // Chama o método do evento recebido no construtor passando o personagem atual
            evento.onCompartilharClick(personagem)
        }

        holder.binding.personagemCardView.setOnClickListener {
            evento.onPersonagemClick(personagem)
        }

        // Carrega a imagem do personagem
        Glide.with(holder.itemView)
            .load(personagem.urlImagem)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.binding.personagemImgImagem);
    }

    /**
     * Retorna o número de itens da lista.
     */
    override fun getItemCount(): Int {
//        Log.d(TAG, "getItemCount: ${personagens.size}")
        return personagens.size
    }

    // =================================================================================
    // EVENTOS
    // =================================================================================

    /**
     * Definição dos eventos que um item do recycler view pode disparar. A ação executada
     * em cada evento será definida pela classe que criou o recycler view.
     */
    interface Evento {
        fun onCompartilharClick(personagem: Personagem)
        fun onPersonagemClick(personagem: Personagem)
    }

    // =================================================================================
    // VIEW HOLDER
    // =================================================================================

    /**
     * Classe do ViewHolder que armazena os itens de layout do recycle view
     */
    data class ViewHolder(var binding: ItemPersonagemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
