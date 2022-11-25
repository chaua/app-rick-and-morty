package br.com.koruthos.rickmorty.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import br.com.koruthos.rickmorty.R
import br.com.koruthos.rickmorty.databinding.ActivityMainBinding
import br.com.koruthos.rickmorty.fragments.EpisodiosFragment
import br.com.koruthos.rickmorty.fragments.PersonagensFragment

class MainActivity : AppCompatActivity() {

    // Objeto com a amarração de todos os componentes do layout
    lateinit var mBinding: ActivityMainBinding

    // Fragmentos da tela inicial
    lateinit var mPersonagensFragment: PersonagensFragment
    lateinit var mEpisodiosFragment: EpisodiosFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialização de layout tradicional
        // setContentView(R.layout.activity_main)

        // Inicialização de layout com DataBinding:
        //   - 1. Habilitar o databiding no build.gradle
        //   - 2. Alterar o arquivo de layout com a tag <layout> na raíz do arquivo
        //   - 3. Substituir a chamada do setContentView
        //   !!! Precisa recompilar o projeto para que a classe ActivityMainBinding seja gerada
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Cadastra os eventos da bottom navigation
        mBinding.mainBottomNavigation.setOnItemSelectedListener(::onItemSelected)

        // Cadastra a toolbar
        setSupportActionBar(mBinding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Inicializa os fragmentos
        mPersonagensFragment = PersonagensFragment.newInstance()
        mEpisodiosFragment = EpisodiosFragment.newInstance()

        // Define qual é o primeiro fragmento a ser carregado no container
        trocarFragmento(mPersonagensFragment)
    }

    /**
     * Troca o fragmento quando um item da bottom navigation é selecionado
     */
    private fun onItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_personagens -> trocarFragmento(mPersonagensFragment)
            R.id.action_episodios -> trocarFragmento(mEpisodiosFragment)
        }
        return true
    }

    /**
     * Função para realizar a troca dinâmica dos fragmentos.
     */
    private fun trocarFragmento(fragmento: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE)
        fragmentTransaction.replace(R.id.main_container, fragmento)
        fragmentTransaction.commit()
    }
}
