package lej.happy.retube.data.models.realm

import io.realm.RealmObject

open class Category : RealmObject() {
    var categoryId = 0
    var categoryCount = 0
    var categoryName: String? = null
}
