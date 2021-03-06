package com.psg.domain.model

data class Summoner(
    val name: String, // 소환사 이름
    val level: String, // 레벨
    val icon: String, // 소환사 아이콘
    val tier: String, // 티어 (GOLD)
    val leaguePoints: Int, // 점수
    val rank: String, // 랭크 (I)
    val wins: Int, // 승리
    val losses: Int, // 패배
    val miniSeries: MiniSeries?, // 승급전
    val isPlaying: Boolean,
) {
    data class MiniSeries(
        var losses: Int,
        var target: Int,
        var wins: Int,
        var progress: String
    )

    fun getLevels() = "LV: $level"
//
    fun getLeaguePoint() = "$leaguePoints LP / ${wins}승 ${losses}패"
//
    fun getTierRank() = "$tier $rank"

    fun getMiniVisible():String = if (miniSeries?.progress != "No") "visible" else "gone"
//
//    fun miniImage1() = getMiniImage(0)
//    fun miniImage2() = getMiniImage(1)
//    fun miniImage3() = getMiniImage(2)
//    fun miniImage4() = getMiniImage(3)
//    fun miniImage5() = getMiniImage(4)

//    private fun getMiniImage(index: Int):Int{
//        var result = 0
//        if (miniSeries != null && miniSeries.progress != "No"){
//            result = when(miniSeries.progress[index]){
//                'W' -> R.drawable.ic_baseline_check_24
//                'L' -> R.drawable.ic_baseline_close_24
//                'N' -> R.drawable.ic_baseline_horizontal_rule_24
//                else -> R.drawable.ic_baseline_horizontal_rule_24
//            }
//        }
//        return result
//    }

//    fun playing() = if (isPlaying) R.color.green_new else R.color.color_red
}

