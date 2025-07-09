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

    // 전체 검색 카페 리스트 (예: "전체 검색" 탭)
    private val _defaultCafes = MutableStateFlow<List<CafeInfo>>(emptyList())
    val defaultCafes: StateFlow<List<CafeInfo>> = _defaultCafes

    // (선택) 다른 카페 리스트도 관리할 수 있음
    private val _cafes = MutableStateFlow<List<CafeInfo>>(emptyList())
    val cafes: StateFlow<List<CafeInfo>> = _cafes

    fun fetchDefaultCafes() {
        viewModelScope.launch {
            try {
                val cafes = api.getDefaultCafes()
                Log.d("CafeViewModel", "✅ 가져온 기본 카페 수: ${cafes.size}")
                _defaultCafes.value = cafes
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
