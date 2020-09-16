package lej.happy.retube.data.models.realm

import io.realm.RealmObject

open class User : RealmObject() {
    var startDate: String? = null
    var viewCount = 0
    var down:Int = 0
    var am:Int = 0
    var pm:Int = 0
    var night:Int = 0
    var week:Int = 0
    var holy:Int = 0
}
