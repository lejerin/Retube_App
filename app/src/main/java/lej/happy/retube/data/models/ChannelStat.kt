package lej.happy.retube.data.models

//댓글 있는지, 댓글 선택했는지, 번역 있는지, 번역 선택했는지, 댓글 리스트, 번역 리스트
data class ChannelStat(
    val name: String,
    val viewCount: Int = 0,
    val url: String
)