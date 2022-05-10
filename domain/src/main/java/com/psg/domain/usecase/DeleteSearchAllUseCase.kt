package com.psg.domain.usecase

import com.psg.domain.repository.LolRepository

class DeleteSearchAllUseCase(private val repository: LolRepository) {
    suspend fun execute() {
        repository.deleteSearchAll()
    }
}