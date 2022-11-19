package br.com.koruthos.rickmorty.widgets.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.koruthos.cursoandroid.widgets.adapters.PersonagemRecyclerAdapter
import br.com.koruthos.rickmorty.databinding.ItemEpisodioBinding
import br.com.koruthos.rickmorty.databinding.ItemPersonagemBinding
import br.com.koruthos.rickmorty.models.Episodio

class EpisodiosRecyclerAdapter(
    var episodios: List<Episodio>,
    var evento: Evento
) : RecyclerView.Adapter<EpisodiosRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemEpisodioBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episodio = episodios[position]
        holder.binding.episodio = episodio

        holder.binding.episodioCardView.setOnClickListener {
            evento.onEpisodioClick(episodio)
        }

    }

    override fun getItemCount(): Int {
        return episodios.size
    }

    // =================================================================================
    // EVENTOS
    // =================================================================================

    interface Evento {
        fun onEpisodioClick(episodio: Episodio)
    }

    // =================================================================================
    // VIEW HOLDER
    // =================================================================================

    data class ViewHolder(var binding: ItemEpisodioBinding) :
        RecyclerView.ViewHolder(binding.root)
}
