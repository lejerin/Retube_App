package lej.happy.retube.ui.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.ui.search.SearchViewModel


class PlayViewModelFactory(
    private val repository: YoutubeRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayViewModel(repository) as T
    }

}