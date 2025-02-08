package com.mongs.wear.presentation.pages.slot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mongs.wear.core.exception.usecase.BuySlotUseCaseException
import com.mongs.wear.core.exception.usecase.CreateMongUseCaseException
import com.mongs.wear.core.exception.usecase.DeleteMongUseCaseException
import com.mongs.wear.core.exception.usecase.GetSlotCountUseCaseException
import com.mongs.wear.core.exception.usecase.GetSlotsUseCaseException
import com.mongs.wear.core.exception.usecase.GetStarPointUseCaseException
import com.mongs.wear.core.exception.usecase.GraduateMongUseCaseException
import com.mongs.wear.core.exception.usecase.SetCurrentSlotUseCaseException
import com.mongs.wear.domain.management.usecase.CreateMongUseCase
import com.mongs.wear.domain.management.usecase.DeleteMongUseCase
import com.mongs.wear.domain.management.usecase.GetSlotsUseCase
import com.mongs.wear.domain.management.usecase.GraduateMongUseCase
import com.mongs.wear.domain.management.usecase.SetCurrentSlotUseCase
import com.mongs.wear.domain.management.vo.SlotVo
import com.mongs.wear.domain.player.usecase.BuySlotUseCase
import com.mongs.wear.domain.player.usecase.GetSlotCountUseCase
import com.mongs.wear.domain.player.usecase.GetStarPointUseCase
import com.mongs.wear.presentation.global.constValue.SlotConst
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SlotPickViewModel @Inject constructor(
    private val getSlotsUseCase: GetSlotsUseCase,
    private val createMongUseCase: CreateMongUseCase,
    private val deleteMongUseCase: DeleteMongUseCase,
    private val graduateMongUseCase: GraduateMongUseCase,
    private val getStarPointUseCase: GetStarPointUseCase,
    private val getSlotCountUseCase: GetSlotCountUseCase,
    private val setCurrentSlotUseCase: SetCurrentSlotUseCase,
    private val buySlotUseCase: BuySlotUseCase,
): BaseViewModel() {

    val starPoint: LiveData<Int> get() = _starPoint
    private val _starPoint = MediatorLiveData<Int>()

    val slotVoList: LiveData<List<SlotVo>> get() = _slotVoList
    private val _slotVoList = MediatorLiveData<List<SlotVo>>()

    val buySlotPrice: LiveData<Int> get() = _buySlotPrice
    private val _buySlotPrice = MutableLiveData<Int>()

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            _starPoint.addSource(withContext(Dispatchers.IO) { getStarPointUseCase() }) { starPoint ->
                _starPoint.value = starPoint
            }

            _buySlotPrice.postValue(10)

            getSlots()

            uiState.loadingBar = false
        }
    }

    /**
     * 몽 슬롯 목록 조회
     */
    private suspend fun getSlots() {
        withContext(Dispatchers.IO) {

            val slotCount = getSlotCountUseCase()

            val existsSlotVoList = (getSlotsUseCase() as ArrayList).let { slotVoList ->
                repeat((slotCount - slotVoList.size).coerceAtLeast(0)) {
                    slotVoList.add(SlotVo(code = SlotVo.SlotCode.EMPTY))
                }

                repeat((SlotConst.MAX_SLOT_COUNT - slotVoList.size).coerceAtLeast(0)) {
                    slotVoList.add(SlotVo(code = SlotVo.SlotCode.BUY_SLOT))
                }

                slotVoList
            }

            _slotVoList.postValue(existsSlotVoList)
        }
    }

    /**
     * 몽 생성
     */
    fun createMong(name: String, sleepStart: String, sleepEnd: String) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            uiState.loadingBar = true
            uiState.mongCreateDialog = false

            createMongUseCase(
                CreateMongUseCase.Param(
                    name = name, sleepStart = sleepStart, sleepEnd = sleepEnd
                )
            )

            getSlots()

            uiState.loadingBar = false
        }
    }

    /**
     * 몽 삭제
     */
    fun deleteMong(mongId: Long) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            uiState.loadingBar = true
            uiState.mongDeleteDialog = false

            deleteMongUseCase(
                DeleteMongUseCase.Param(
                    mongId = mongId
                )
            )

            getSlots()

            uiState.loadingBar = false
        }
    }

    /**
     * 현재 슬롯 선택
     */
    fun pickMong(mongId: Long) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            uiState.loadingBar = true
            uiState.pickDialog = false

            setCurrentSlotUseCase(
                SetCurrentSlotUseCase.Param(
                    mongId = mongId
                )
            )

            scrollPageMainPagerView()

            uiState.navMainEvent.emit(System.currentTimeMillis())
        }
    }

    /**
     * 몽 졸업
     */
    fun graduateMong(mongId: Long) {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            uiState.loadingBar = true
            uiState.mongGraduateDialog = false

            graduateMongUseCase(
                GraduateMongUseCase.Param(
                    mongId = mongId
                )
            )

            getSlots()

            uiState.loadingBar = false
        }
    }

    /**
     * 추가 슬롯 구매
     */
    fun buySlot() {
        viewModelScopeWithHandler.launch (Dispatchers.IO) {

            uiState.loadingBar = true
            uiState.buySlotDialog = false

            buySlotUseCase()

            getSlots()

            uiState.loadingBar = false
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var navMainEvent = MutableSharedFlow<Long>()
        var mongDetailDialog by mutableStateOf(false)
        var mongCreateDialog by mutableStateOf(false)
        var mongDeleteDialog by mutableStateOf(false)
        var mongGraduateDialog by mutableStateOf(false)
        var buySlotDialog by mutableStateOf(false)
        var pickDialog by mutableStateOf(false)
    }

    override suspend fun exceptionHandler(exception: Throwable) {
        when (exception) {
            is GetSlotsUseCaseException -> {
                uiState.loadingBar = false
                uiState.navMainEvent.emit(System.currentTimeMillis())
            }

            is CreateMongUseCaseException -> {
                uiState.loadingBar = false
                uiState.mongCreateDialog = false
            }

            is DeleteMongUseCaseException -> {
                uiState.loadingBar = false
                uiState.mongDeleteDialog = false
            }

            is GraduateMongUseCaseException -> {
                uiState.loadingBar = false
                uiState.mongGraduateDialog = false
            }

            is GetStarPointUseCaseException -> {
                uiState.loadingBar = false
                uiState.navMainEvent.emit(System.currentTimeMillis())
            }

            is GetSlotCountUseCaseException -> {
                uiState.loadingBar = false
                uiState.navMainEvent.emit(System.currentTimeMillis())
            }

            is SetCurrentSlotUseCaseException -> {
                uiState.loadingBar = false
                uiState.pickDialog = false
            }

            is BuySlotUseCaseException -> {
                uiState.loadingBar = false
                uiState.buySlotDialog = false
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}