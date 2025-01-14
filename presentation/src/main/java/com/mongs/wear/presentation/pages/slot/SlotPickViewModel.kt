package com.mongs.wear.presentation.pages.slot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mongs.wear.domain.management.exception.CreateMongException
import com.mongs.wear.domain.management.exception.DeleteMongException
import com.mongs.wear.domain.management.exception.GetSlotsException
import com.mongs.wear.domain.management.exception.GraduateMongException
import com.mongs.wear.domain.management.exception.SetCurrentSlotException
import com.mongs.wear.domain.management.usecase.CreateMongUseCase
import com.mongs.wear.domain.management.usecase.DeleteMongUseCase
import com.mongs.wear.domain.management.usecase.GetSlotsUseCase
import com.mongs.wear.domain.management.usecase.GraduateMongUseCase
import com.mongs.wear.domain.management.usecase.SetCurrentSlotUseCase
import com.mongs.wear.domain.management.vo.SlotVo
import com.mongs.wear.domain.player.exception.BuySlotException
import com.mongs.wear.domain.player.exception.GetSlotCountException
import com.mongs.wear.domain.player.exception.GetStarPointException
import com.mongs.wear.domain.player.usecase.BuySlotUseCase
import com.mongs.wear.domain.player.usecase.GetSlotCountUseCase
import com.mongs.wear.domain.player.usecase.GetStarPointUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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

    companion object {
        private const val MAX_SLOT_COUNT = 3
    }

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

    private suspend fun getSlots() {
        withContext(Dispatchers.IO) {

            val slotCount = getSlotCountUseCase()

            val existsSlotVoList = (getSlotsUseCase() as ArrayList).let { slotVoList ->
                repeat((slotCount - slotVoList.size).coerceAtLeast(0)) {
                    slotVoList.add(SlotVo(code = SlotVo.SlotCode.EMPTY))
                }

                repeat((MAX_SLOT_COUNT - slotVoList.size).coerceAtLeast(0)) {
                    slotVoList.add(SlotVo(code = SlotVo.SlotCode.BUY_SLOT))
                }

                slotVoList
            }

            _slotVoList.postValue(existsSlotVoList)
        }
    }

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

    override fun exceptionHandler(exception: Throwable) {

        CoroutineScope(Dispatchers.IO).launch {
            when (exception) {
                is GetSlotsException -> {
                    uiState.loadingBar = false
                    uiState.navMainEvent.emit(System.currentTimeMillis())
                }

                is CreateMongException -> {
                    uiState.loadingBar = false
                    uiState.mongCreateDialog = false
                }

                is DeleteMongException -> {
                    uiState.loadingBar = false
                    uiState.mongDeleteDialog = false
                }

                is GraduateMongException -> {
                    uiState.loadingBar = false
                    uiState.mongGraduateDialog = false
                }

                is GetStarPointException -> {
                    uiState.loadingBar = false
                    uiState.navMainEvent.emit(System.currentTimeMillis())
                }

                is GetSlotCountException -> {
                    uiState.loadingBar = false
                    uiState.navMainEvent.emit(System.currentTimeMillis())
                }

                is SetCurrentSlotException -> {
                    uiState.loadingBar = false
                    uiState.pickDialog = false
                }

                is BuySlotException -> {
                    uiState.loadingBar = false
                    uiState.buySlotDialog = false
                }

                else -> {
                    uiState.loadingBar = false
                }
            }
        }
    }
}