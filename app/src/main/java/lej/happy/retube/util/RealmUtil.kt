package lej.happy.retube.util

import android.graphics.Color
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import lej.happy.retube.R
import lej.happy.retube.data.models.realm.*
import lej.happy.retube.data.models.UserStat
import lej.happy.retube.helper.MyValueFormatter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object RealmUtil {

    fun calTag(tags: List<String>?) : List<String>{
        if (tags != null) {
            if (tags.isNotEmpty()) {
                //글자수 작은 것만
                val shortTag: MutableList<String> = ArrayList()
                for (i in tags.indices) {
                    if (tags[i].length < 6) {
                        shortTag.add(tags[i])
                    }
                }

                //태그를 3개만 저장하기 위해서
                if (shortTag.size < 4) {
                    saveViewNoun(shortTag)
                    return shortTag
                } else {
                    val newList: MutableList<String> = ArrayList()
                    newList.add(shortTag[0])
                    newList.add(shortTag[shortTag.size / 2])
                    newList.add(shortTag[shortTag.size - 1])
                    saveViewNoun(newList)
                    return newList

                }
            }
        }

        return listOf()
    }

    fun saveChannel(channelId: String) {
        val realm = Realm.getDefaultInstance() //데이터 넣기(insert)
        val viewChannel =
            realm.where(ViewChannel::class.java).equalTo("channelId", channelId)
                .findFirst()
        if (viewChannel != null) {
            realm.executeTransaction { viewChannel.channelCount = viewChannel.channelCount + 1 }
        } else {
            realm.executeTransaction { realm ->
                val newViewChannel =
                    realm.createObject(ViewChannel::class.java)
                newViewChannel.channelId = channelId
                newViewChannel.channelCount = 1
            }
        }
    }

    fun saveCategory(num: Int) {

        //영화&애니메이션 중복
        var categoryid = num
        if (categoryid == 30 || categoryid == 31) categoryid = 1
        //코미디
        if (categoryid == 34) categoryid = 23
        //공포&스릴러
        if (categoryid == 41) categoryid = 39
        //단편
        if (categoryid == 42) categoryid = 18
        val realm = Realm.getDefaultInstance() //데이터 넣기(insert)
        val category: Category? =
            realm.where(Category::class.java).equalTo("categoryId", categoryid)
                .findFirst()
        if (category != null) {
            realm.executeTransaction {
                category.categoryCount = category.categoryCount + 1
            }
        } else {
            realm.executeTransaction { realm ->
                val newCategory: Category = realm.createObject(Category::class.java)
                newCategory.categoryId = categoryid
                newCategory.categoryCount = 1
                newCategory.categoryName = getCategoryName(categoryid)
            }
        }
    }

    private fun getCategoryName(id: Int): String {
        return when (id) {
            1 -> "영화&애니메이션"
            2 -> "자동차"
            10 -> "음악"
            15 -> "동물"
            17 -> "스포츠"
            18 -> "단편영화"
            19 -> "여행&이벤트"
            20 -> "게임"
            21 -> "브이로그"
            22 -> "인물&블로그"
            23 -> "코미디"
            24 -> "엔터테인먼트"
            25 -> "뉴스&정치"
            26 -> "노하우&스타일"
            27 -> "교육"
            28 -> "과학&기술"
            29 -> "비영리&사회운동"
            32 -> "액션&모험"
            33 -> "클래식"
            35 -> "다큐멘터리"
            36 -> "드라마"
            37 -> "가족"
            38 -> "외국"
            39 -> "공포&스릴러"
            40 -> "공상과학&판타지"
            43 -> "예능"
            44 -> "트레일러"
            else -> "영화&애니메이션"
        }
    }


    fun saveViewTimeCount(){

        val realm = Realm.getDefaultInstance() //데이터 넣기(insert)

        val user =
            realm.where(
                User::class.java
            ).findFirst()
        realm.executeTransaction {
            user!!.viewCount = user.viewCount + 1
            var v = 0
            try {
                v = viewTime()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            when (v) {
                0 -> user.down = user.down + 1
                1 -> user.am = user.am + 1
                2 -> user.pm = user.pm + 1
                3 -> user.night = user.night + 1
                else -> {
                }
            }
            val c1 = Calendar.getInstance()
            if (c1[Calendar.DAY_OF_WEEK] === Calendar.SATURDAY ||
                c1[Calendar.DAY_OF_WEEK] === Calendar.SUNDAY
            ) {
                user.holy = user.holy + 1
            } else {
                user.week = user.week + 1
            }
        }


    }

    @Throws(ParseException::class)
    private fun viewTime(): Int {
        val today = Date()
        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
        val fm = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")

        //dawn
        val dawn: String = dateFormat.format(today).toString() + " 05:59:59"
        if (today.time <= fm.parse(dawn).getTime()) {
            return 0
        }
        //am
        val am: String = dateFormat.format(today).toString() + " 11:59:59"
        if (today.time <= fm.parse(am).getTime()) {
            return 1
        }
        //pm
        val pm: String = dateFormat.format(today).toString() + " 17:59:59"
        if (today.time <= fm.parse(pm).getTime()) {
            return 2
        }
        //night
        val night: String = dateFormat.format(today).toString() + " 23:59:59"
        return if (today.time <= fm.parse(night).getTime()) {
            3
        } else -1
    }

    fun saveSearchNoun(list: List<String>){

            val realm: Realm = Realm.getDefaultInstance()

            for (i in list.indices){
                val isRealmSearch: RealmSearch? =
                    realm.where(RealmSearch::class.java).equalTo("noun", list[i]).findFirst()
                if (isRealmSearch != null){
                    realm.executeTransaction {
                        isRealmSearch.count = isRealmSearch.count + 1
                        isRealmSearch.date = Date()
                    }
                }else{
                    realm.executeTransaction { realm ->
                        val search: RealmSearch = realm.createObject(RealmSearch::class.java)
                        search.noun = list[i]
                        search.count = 1
                        search.date = Date()
                    }
                }
            }
    }

    private fun saveViewNoun(list: List<String>){

        val realm: Realm = Realm.getDefaultInstance()

        for (i in list.indices){
            val tagName: ViewVideo? =
                realm.where(ViewVideo::class.java).equalTo("noun", list[i]).findFirst()
            if (tagName != null){
                realm.executeTransaction {
                    tagName.count = tagName.count + 1
                    tagName.date = Date()
                }
            }else{
                realm.executeTransaction { realm ->
                    val tageName: ViewVideo = realm.createObject(ViewVideo::class.java)
                    tageName.noun = list[i]
                    tageName.count = 1
                    tageName.date = Date()
                }
            }
        }

    }


    fun getUserData() : UserStat{
        val realm: Realm = Realm.getDefaultInstance()

        val user =
            realm.where(
                User::class.java
            ).findFirst()

        val newUser = UserStat()
        newUser.date = user?.startDate + " 부터"
        newUser.viewCount = user?.viewCount.toString()

        val dawn = user?.down
        val am = user?.am
        val pm = user?.pm
        val night = user?.night

        var maxString = "새벽"
        var maxInt: Int? = dawn

        if (maxInt!! < am!!) {
            maxString = "오전"
            maxInt = am
        }
        if (maxInt!! < pm!!) {
            maxString = "오후"
            maxInt = pm
        }
        if (maxInt!! < night!!) {
            maxString = "밤"
            maxInt = night
        }

        val all: Int = dawn!! + am!! + pm!! + night!!
        val percent = (maxInt.toDouble() / all.toDouble() * 100.0).toInt()

        newUser.time_percent = "$percent%"
        newUser.time_text = maxString


        val week = user.week
        val holy = user.holy

        var maxString2 = "주중"
        var maxInt2 = week

        if (holy > maxInt2) {
            maxString2 = "주말"
            maxInt2 = holy
        }
        val all2 = week + holy
        val percent2 = (maxInt2.toDouble() / all2.toDouble() * 100.0).toInt()

        newUser.day_text = maxString2
        newUser.day_percent = "$percent2%"



        return newUser
    }


    fun getPieChartList() : PieData{
        val realm: Realm = Realm.getDefaultInstance()

        val yValues = ArrayList<PieEntry>()


        val categories: RealmResults<Category> =
            realm.where(Category::class.java).sort("categoryCount", Sort.DESCENDING)
                .findAll()


        val colors = ArrayList<Int>()

        if (categories.size > 0) {
            yValues.add(PieEntry(categories[0]?.categoryCount?.toFloat()!!, categories[0]?.categoryName))
            colors.add(Color.rgb(212, 223, 230))
            if (categories.size > 1) {
                yValues.add(PieEntry(categories[1]?.categoryCount?.toFloat()!!, categories[1]?.categoryName))
                colors.add(Color.rgb(142, 192, 228))
                if (categories.size > 2) {
                    yValues.add(PieEntry(categories[2]?.categoryCount?.toFloat()!!, categories[2]?.categoryName))
                    colors.add(Color.rgb(202, 219, 233))
                    if (categories.size > 3) {
                        yValues.add(PieEntry(categories[3]?.categoryCount?.toFloat()!!, categories[3]?.categoryName))
                        colors.add(Color.rgb(106, 175, 230))
                        if (categories.size > 4) {
                            yValues.add(PieEntry(categories[4]?.categoryCount?.toFloat()!!, categories[4]?.categoryName))
                            colors.add(Color.rgb(214, 236, 250))
                        }
                    }
                }
            }
        }

        val dataSet = PieDataSet(yValues, "category")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueTextSize(20f)
        data.setValueFormatter(MyValueFormatter("%"))

        return data
    }


    fun getKeyWordSearch(checkId: Int) : RealmResults<RealmSearch>? {
        val realm: Realm = Realm.getDefaultInstance()
        val cal = Calendar.getInstance()
        cal.time = Date()

        when(checkId){
            R.id.radioDay -> {

            }
            R.id.radioWeek -> {
                cal.add(Calendar.DATE, -7)
            }
            R.id.radioMonth -> {
                cal.add(Calendar.MONTH, -1)
            }
        }

        return realm.where(RealmSearch::class.java)
            .greaterThanOrEqualTo("date", Converter.stringToDate("00:00:00", cal.time))
            .lessThanOrEqualTo("date", Converter.stringToDate("23:59:59", Date()))
            .sort("count", Sort.DESCENDING).findAll()
    }

    fun getKeyWordView(checkId: Int) : RealmResults<ViewVideo>? {
        val realm: Realm = Realm.getDefaultInstance()
        val cal = Calendar.getInstance()
        cal.time = Date()


        when(checkId){
            R.id.radioDay -> {

            }
            R.id.radioWeek -> {
                cal.add(Calendar.DATE, -7)
            }
            R.id.radioMonth -> {
                cal.add(Calendar.MONTH, -1)
            }
        }

        return realm.where(ViewVideo::class.java)
            .greaterThanOrEqualTo("date", Converter.stringToDate("00:00:00", cal.time))
            .lessThanOrEqualTo("date", Converter.stringToDate("23:59:59", Date()))
            .sort("count", Sort.DESCENDING).findAll()
    }


}