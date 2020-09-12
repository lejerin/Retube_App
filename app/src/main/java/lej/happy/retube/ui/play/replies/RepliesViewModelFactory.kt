package lej.happy.retube.ui.play.replies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lej.happy.retube.data.repositories.YoutubeRepository


class RepliesViewModelFactory(
    private val repository: YoutubeRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RepliesViewModel(repository) as T
    }

}