package com.example.project1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project1.api.ApiService
import com.example.project1.model.CafeInfo
import com.example.project1.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CafeListViewModel : ViewModel() {
    private val api: ApiService = RetrofitClient.apiService

    // ① 기존 상태들
    private val _defaultCafes = MutableStateFlow<List<CafeInfo>>(emptyList())
    val defaultCafes: StateFlow<List<CafeInfo>> = _defaultCafes

    private val _cafes = MutableStateFlow<List<CafeInfo>>(emptyList())
    val cafes: StateFlow<List<CafeInfo>> = _cafes

    // ② "요즘 핫한" 카페 리스트 (1번만 고정되게 셔플)
    private val _hotNowCafes = MutableStateFlow<List<CafeInfo>>(emptyList())
    val hotNowCafes: StateFlow<List<CafeInfo>> = _hotNowCafes

    fun fetchDefaultCafes() {
        viewModelScope.launch {
            try {
                val cafes = api.getDefaultCafes()
                Log.d("CafeViewModel", "✅ 가져온 기본 카페 수: ${cafes.size}")
                _defaultCafes.value = cafes

                // ✅ hotNowCafes가 비어있을 때만 초기 셔플
                if (_hotNowCafes.value.isEmpty() && cafes.size >= 9) {
                    _hotNowCafes.value = cafes.shuffled().take(9)
                    Log.d("CafeViewModel", "🔥 요즘 핫한 카페 9개 선정 완료")
                }
            } catch (e: Exception) {
                Log.e("CafeViewModel", "❌ 기본 카페 목록 가져오기 실패", e)
            }
        }
    }

    fun fetchAllCafes() {
        viewModelScope.launch {
            try {
                val response = api.getAllCafes()
                _cafes.value = response
            } catch (e: Exception) {
                Log.e("CafeViewModel", "전체 카페 목록 가져오기 실패", e)
            }
        }
    }
}
