package lej.happy.retube.data.models

import lej.happy.retube.data.models.comments.Replies

data class repliesData(
    val num: Int,
    val list: List<Replies.Item>?
)