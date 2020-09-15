package lej.happy.retube.ui.statistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lej.happy.retube.data.repositories.YoutubeRepository


class StatViewModelFactory(
    private val repository: YoutubeRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StatViewModel(repository) as T
    }

}