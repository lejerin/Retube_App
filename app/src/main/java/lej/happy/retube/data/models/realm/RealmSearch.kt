package lej.happy.retube.data.models.realm

import io.realm.RealmObject
import java.util.*

open class RealmSearch : RealmObject() {
    var noun: String? = null
    var count = 0
    var date: Date? = null
}
