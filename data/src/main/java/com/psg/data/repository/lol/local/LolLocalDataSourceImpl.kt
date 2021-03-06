package com.psg.data.repository.lol.local

import com.psg.data.db.LoLDao
import com.psg.data.model.local.ProfileEntity
import com.psg.data.model.local.SearchEntity
import com.psg.data.model.local.SummonerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LolLocalDataSourceImpl(private val dao:LoLDao): LolLocalDataSource {
    // Room Main
    override suspend fun getSummoner() = dao.getSummoner()

    override suspend fun insertSummoner(summonerEntity: SummonerEntity){
        dao.insertSummoner(summonerEntity)
    }

    override suspend fun updateSummoner(summonerEntity: SummonerEntity){
        dao.updateSummoner(summonerEntity)
    }

    override suspend fun deleteSummoner(summonerEntity: SummonerEntity){
        dao.deleteSummoner(summonerEntity)
    }

    override suspend fun deleteSummonerAll(){
        dao.deleteSummonerAll()
    }

    // Search
    override suspend fun getSearch():List<SearchEntity> = dao.getSearch()

    override suspend fun insertSearch(searchEntity: SearchEntity){
        dao.insertSearch(searchEntity)
    }

    override suspend fun deleteSearch(searchEntity: SearchEntity){
        dao.deleteSearch(searchEntity)
    }

    override suspend fun deleteSearchAll(){
        dao.deleteSearchAll()
    }
    // Search
    override suspend fun getProfile(): ProfileEntity = dao.getProfile()


    override suspend fun insertProfile(profileEntity: ProfileEntity){
        dao.insertProfile(profileEntity)
    }

    override suspend fun updateProfile(profileEntity: ProfileEntity){
        dao.updateProfile(profileEntity)
    }

    override suspend fun deleteProfile(){
        dao.deleteProfile()
    }
}