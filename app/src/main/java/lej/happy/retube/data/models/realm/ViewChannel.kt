package lej.happy.retube.data.models.realm

import io.realm.RealmObject

open class ViewChannel : RealmObject() {
    var channelId: String? = null
    var channelCount = 0
}
