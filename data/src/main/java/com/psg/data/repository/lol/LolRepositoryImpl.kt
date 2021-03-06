package com.psg.data.repository.lol

import com.psg.data.mapper.*
import com.psg.data.model.local.SearchEntity
import com.psg.data.model.local.SummonerEntity
import com.psg.data.repository.key.local.KeyLocalDataSource
import com.psg.data.repository.lol.local.LolLocalDataSource
import com.psg.data.repository.lol.remote.LolRemoteDataSource
import com.psg.data.utils.*
import com.psg.domain.model.*
import com.psg.domain.repository.LolRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class LolRepositoryImpl @Inject constructor(
    private val lolRemoteDataSource: LolRemoteDataSource,
    private val lolLocalDataSource: LolLocalDataSource,
    private val keyLocalDataSource: KeyLocalDataSource,
    private val jsonUtils: JsonUtils
) : LolRepository {
    override suspend fun getSummoner(): Flow<List<Summoner>> = flow {
        val summoner = lolLocalDataSource.getSummoner()
        emit(entityToSummoner(summoner))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteSummoner(summoner: Summoner) = lolLocalDataSource.deleteSummoner(
        summonerToEntity(summoner)
    )

    override suspend fun deleteSummonerAll() = lolLocalDataSource.deleteSummonerAll()

    override suspend fun getProfile(): Flow<Profile> = flow {
        val profile = lolLocalDataSource.getProfile()
        emit(entityToProfile(profile))
    }.flowOn(Dispatchers.IO)

    override suspend fun insertProfile(profile: Profile) = lolLocalDataSource.insertProfile(
        profileToEntity(profile)
    )

    override suspend fun deleteProfile() = lolLocalDataSource.deleteProfile()

    override suspend fun getSearch(): Flow<List<Search>> = flow {
        val search = lolLocalDataSource.getSearch()
        emit(entityToSearch(search))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteSearch(search: Search) = lolLocalDataSource.deleteSearch(
        searchToEntity(search)
    )

    override suspend fun deleteSearchAll() = lolLocalDataSource.deleteSearchAll()

    override suspend fun getSpectator(name: String): Flow<Spectator> = flow {
        var spectator = Spectator("", mutableListOf())
        val list = mutableListOf<Spectator.BanChamp>()
        try {
            keyLocalDataSource.apiKey.let { key ->
                val body = lolRemoteDataSource.searchSummoner(name, key).body()
                val code = lolRemoteDataSource.searchSummoner(name, key).code()
                if (code == 200) {
                    body?.id.let {
                        val res = lolRemoteDataSource.searchSpectator(it, key).body()
                        if (res != null) {
                            for (x in res.bannedChampions) {
                                list.add(
                                    Spectator.BanChamp(
                                        jsonUtils.longToTeam(x.teamId),
                                        jsonUtils.jsonToChampPath(x.championId)
                                    )
                                )
                            }
                            spectator = Spectator(
                                jsonUtils.jsonToMap(res.mapId),
                                list
                            )

                        }
                    }

                } else {
                    AppLogger.p(
                        "????????????????????????:${
                            lolRemoteDataSource.searchSummoner(name, key).errorBody()?.string()
                        }"
                    )
                    AppLogger.p("????????????:${lolRemoteDataSource.searchSummoner(name, key).code()}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        emit(spectator)
    }.flowOn(Dispatchers.IO)

    override suspend fun getSpectatorInfoR(name: String): Flow<List<SpectatorInfo>> = flow {
        val list = mutableListOf<SpectatorInfo>()

        try {
            keyLocalDataSource.apiKey.let { key ->
                val body = lolRemoteDataSource.searchSummoner(name, key).body()
                val code = lolRemoteDataSource.searchSummoner(name, key).code()
                if (code == 200) {
                    body?.id.let {
                        val res = lolRemoteDataSource.searchSpectator(it, key).body()
                        if (res != null) {
                            for (i in res.participants) {
                                if (jsonUtils.longToTeam(i.teamId) == "??????") {
                                    AppLogger.p("????????? ??????")
                                    list.add(
                                        SpectatorInfo(
                                            i.summonerName,
                                            jsonUtils.jsonToChampName(i.championId),
                                            jsonUtils.jsonToChampPath(i.championId),
                                            jsonUtils.longToTeam(i.teamId),
                                            jsonUtils.jsonToSpell(i.spell1Id),
                                            jsonUtils.jsonToSpell(i.spell2Id),
                                            jsonUtils.jsonToRuneStyle(i.perks.perkStyle),
                                            jsonUtils.jsonToRuneStyle(i.perks.perkSubStyle),
                                            jsonUtils.jsonToMainRunes(i.perks.perkStyle, i.perks.perkIds[0]),
                                            jsonUtils.jsonToRunes(
                                                i.perks.perkStyle,
                                                i.perks.perkSubStyle,
                                                i.perks.perkIds
                                            ),
                                        )
                                    )
                                }


                            }

                        }
                    }

                } else {
                    AppLogger.p(
                        "????????????????????????:${
                            lolRemoteDataSource.searchSummoner(name, key).errorBody()?.string()
                        }"
                    )
                    AppLogger.p("????????????:${lolRemoteDataSource.searchSummoner(name, key).code()}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        emit(list)
    }.flowOn(Dispatchers.IO)

    override suspend fun getSpectatorInfoB(name: String): Flow<List<SpectatorInfo>> = flow {
        val list = mutableListOf<SpectatorInfo>()
        try {
            keyLocalDataSource.apiKey.let { key ->
                val body = lolRemoteDataSource.searchSummoner(name, key).body()
                val code = lolRemoteDataSource.searchSummoner(name, key).code()
                if (code == 200) {
                    body?.id.let {
                        val res = lolRemoteDataSource.searchSpectator(it, key).body()
                        if (res != null) {
                            for (i in res.participants) {
                                if (jsonUtils.longToTeam(i.teamId) == "??????") {
                                    AppLogger.p("????????? ??????")
                                    list.add(
                                        SpectatorInfo(
                                            i.summonerName,
                                            jsonUtils.jsonToChampName(i.championId),
                                            jsonUtils.jsonToChampPath(i.championId),
                                            jsonUtils.longToTeam(i.teamId),
                                            jsonUtils.jsonToSpell(i.spell1Id),
                                            jsonUtils.jsonToSpell(i.spell2Id),
                                            jsonUtils.jsonToRuneStyle(i.perks.perkStyle),
                                            jsonUtils.jsonToRuneStyle(i.perks.perkSubStyle),
                                            jsonUtils.jsonToMainRunes(i.perks.perkStyle, i.perks.perkIds[0]),
                                            jsonUtils.jsonToRunes(
                                                i.perks.perkStyle,
                                                i.perks.perkSubStyle,
                                                i.perks.perkIds
                                            ),
                                        )
                                    )
                                }

                            }

                        }
                    }

                } else {
                    AppLogger.p(
                        "????????????????????????:${
                            lolRemoteDataSource.searchSummoner(name, key).errorBody()?.string()
                        }"
                    )
                    AppLogger.p("????????????:${lolRemoteDataSource.searchSummoner(name, key).code()}")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        emit(list)
    }.flowOn(Dispatchers.IO)

    override fun searchLeague(name: String, key: String, date: String): Flow<League> = flow {
        try {
            val body = lolRemoteDataSource.searchSummoner(name, key).body()
            val code = lolRemoteDataSource.searchSummoner(name, key).code()

            if (code == 200 && body != null) {
                AppLogger.p("??????????${body.id}")
                val res = lolRemoteDataSource.searchLeague(body.id, key)
                val resSpectator = lolRemoteDataSource.searchSpectator(body.id, key).body()
                val playing = resSpectator?.gameId != null
                AppLogger.p("??????????$playing")
                if (res.body()?.size != 0) {
                    var soloRank = false
                    val iterator = res.body()?.iterator() ?: iterator { }
                    while (iterator.hasNext()) {
                        val league = iterator.next()
                        if (league.queueType == "RANKED_SOLO_5x5") {
                            soloRank = true
                            AppLogger.p("???????????????:${league.summonerName},??????:${league.tier},???????????????${league.leaguePoints},??????:${league.rank},??????:${league.wins}???,${league.losses}???")
                            if (league.miniSeries != null) {
                                AppLogger.p("?????????id:${body.profileIconId}")
                                val mini = SummonerEntity.MiniSeries(
                                    league.miniSeries.losses!!,
                                    league.miniSeries.target!!,
                                    league.miniSeries.wins!!,
                                    league.miniSeries.progress!!
                                )
                                val icon =
                                    "http://ddragon.leagueoflegends.com/cdn/11.24.1/img/profileicon/${body.profileIconId}.png"

                                CoroutineScope(Dispatchers.IO).launch {
                                    lolLocalDataSource.insertSummoner(
                                        SummonerEntity(
                                            league.summonerName!!,
                                            body.summonerLevel.toString(),
                                            icon,
                                            league.tier!!,
                                            league.leaguePoints!!,
                                            league.rank!!,
                                            league.wins!!,
                                            league.losses!!,
                                            mini,
                                            playing
                                        )
                                    )
                                }

                                AppLogger.p("????????????")
                                AppLogger.p(
                                    "?????????:${
                                        league.miniSeries.progress.replace("L", "???")
                                            .replace("W", "???")
                                    }"
                                )
                                emit(
                                    League(
                                        result = true,
                                        record = true,
                                        miniSeries = true,
                                        code = 0
                                    )
                                )

                            } else {
                                val mini = SummonerEntity.MiniSeries(0, 0, 0, "No")
                                val icon =
                                    "http://ddragon.leagueoflegends.com/cdn/11.24.1/img/profileicon/${body.profileIconId}.png"
                                CoroutineScope(Dispatchers.IO).launch {
                                    lolLocalDataSource.insertSummoner(
                                        SummonerEntity(
                                            league.summonerName!!,
                                            body.summonerLevel.toString(),
                                            icon,
                                            league.tier!!,
                                            league.leaguePoints!!,
                                            league.rank!!,
                                            league.wins!!,
                                            league.losses!!,
                                            mini,
                                            playing
                                        )
                                    )
                                }

                                AppLogger.p("???????????????")
                                emit(
                                    League(
                                        result = true,
                                        record = true,
                                        miniSeries = false,
                                        code = 1
                                    )
                                )
                            }
//                                toastEvent("????????????")
                            CoroutineScope(Dispatchers.IO).launch {
                                lolLocalDataSource.insertSearch(
                                    SearchEntity(
                                        league.summonerName!!,
                                        date
                                    )
                                )
                            }


                        } else {
                            AppLogger.p("??????????????? ??????")
                            continue
//                                emit(League(false, record = false, miniSeries = false, code = 2))
//                                toastEvent("?????? ?????? ???????????? ????????? ?????????\n ????????? ????????? ???????????????.")
                        }
                    }
                    if (!soloRank) {
                        emit(League(false,
                            record = false,
                            miniSeries = false,
                            code = 2)
                        )
                    }
                } else {
//                        toastEvent("?????? ?????? ????????? ???????????? ????????????.")
                    emit(League(
                        result = false,
                        record = false,
                        miniSeries = false,
                        code = 3
                    ))
                }

            } else {
                AppLogger.p(
                    "????????????????????????:${
                        lolRemoteDataSource.searchSummoner(name, key).errorBody()?.string()
                    }"
                )
                AppLogger.p("????????????:${lolRemoteDataSource.searchSummoner(name, key).code()}")
                AppLogger.p("??????")
                emit(League(result = false, record = false, miniSeries = false, code = code))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override fun refreshData(list: List<Summoner>): Flow<League> = flow {
        if (list.isNotEmpty()) {
            for (summoner in list)
                try {
                    keyLocalDataSource.apiKey.let {
                        val body = lolRemoteDataSource.searchSummoner(summoner.name, it).body()
                        val code = lolRemoteDataSource.searchSummoner(summoner.name, it).code()
                        AppLogger.p("id????${body?.id}")
                        if (code == 200 && body != null) {
                            var soloRank = false
                            val resLeague = lolRemoteDataSource.searchLeague(body.id, it)
                            val resSpectator =
                                lolRemoteDataSource.searchSpectator(body.id, it).body()
                            val playing = resSpectator?.gameId != null
                            AppLogger.p("??????????$playing")
                            val iterator = resLeague.body()?.iterator() ?: iterator { }
                            while (iterator.hasNext()) {
                                val league = iterator.next()
                                if (league.queueType == "RANKED_SOLO_5x5") {
                                    soloRank = true
                                    AppLogger.p("???????????????:${league.summonerName},??????:${league.tier},???????????????${league.leaguePoints},??????:${league.rank},??????:${league.wins}???,${league.losses}???")
                                    if (league.miniSeries != null) {
                                        val mini = SummonerEntity.MiniSeries(
                                            league.miniSeries.losses!!,
                                            league.miniSeries.target!!,
                                            league.miniSeries.wins!!,
                                            league.miniSeries.progress!!
                                        )
                                        val icon =
                                            "http://ddragon.leagueoflegends.com/cdn/11.24.1/img/profileicon/${body.profileIconId}.png"

                                        CoroutineScope(Dispatchers.IO).launch {
                                            lolLocalDataSource.updateSummoner(
                                                SummonerEntity(
                                                    league.summonerName!!,
                                                    body.summonerLevel.toString(),
                                                    icon,
                                                    league.tier!!,
                                                    league.leaguePoints!!,
                                                    league.rank!!,
                                                    league.wins!!,
                                                    league.losses!!,
                                                    mini,
                                                    playing
                                                )
                                            )
                                        }

                                        AppLogger.p("????????????")
                                        AppLogger.p(
                                            "?????????:${
                                                league.miniSeries.progress.replace("L", "???")
                                                    .replace("W", "???")
                                            }"
                                        )

                                        emit(
                                            League(
                                                result = true,
                                                record = true,
                                                miniSeries = true,
                                                code = 0
                                            )
                                        )
                                    } else {
                                        val mini = SummonerEntity.MiniSeries(0, 0, 0, "No")
                                        val icon =
                                            "http://ddragon.leagueoflegends.com/cdn/11.24.1/img/profileicon/${body.profileIconId}.png"
                                        CoroutineScope(Dispatchers.IO).launch {
                                            lolLocalDataSource.updateSummoner(
                                                SummonerEntity(
                                                    league.summonerName!!,
                                                    body.summonerLevel.toString(),
                                                    icon,
                                                    league.tier!!,
                                                    league.leaguePoints!!,
                                                    league.rank!!,
                                                    league.wins!!,
                                                    league.losses!!,
                                                    mini,
                                                    playing
                                                )
                                            )
                                        }

                                        AppLogger.p("???????????????")
                                        emit(
                                            League(
                                                result = true,
                                                record = true,
                                                miniSeries = false,
                                                code = 1
                                            )
                                        )
                                    }

                                } else {
                                    AppLogger.p("??????????????? ??????")
                                    continue
//                                        emit(League(false, record = false, miniSeries = false, code = 2))
                                }
                            }
                            if (!soloRank) {
                                emit(
                                    League(
                                        false,
                                        record = false,
                                        miniSeries = false,
                                        code = 2
                                    )
                                )
                            }

                        } else {
                            AppLogger.p(
                                "????????????????????????:${
                                    lolRemoteDataSource.searchSummoner(summoner.name, it)
                                        .errorBody()
                                        ?.string()
                                }"
                            )
                            AppLogger.p(
                                "??????:${
                                    lolRemoteDataSource.searchSummoner(summoner.name, it).code()
                                }"
                            )
                            AppLogger.p("??????")
                            emit(
                                League(
                                    result = false,
                                    record = false,
                                    miniSeries = false,
                                    code = code
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
        }

    }.flowOn(Dispatchers.IO)
}