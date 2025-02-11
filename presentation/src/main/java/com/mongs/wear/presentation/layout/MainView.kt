package com.mongs.wear.presentation.layout

import android.content.Context
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.navigation
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.background.MainBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.dialog.error.NetworkErrorDialog
import com.mongs.wear.presentation.global.constValue.MainPagerConst
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import com.mongs.wear.presentation.pages.battle.match.BattleMatchView
import com.mongs.wear.presentation.pages.battle.menu.BattleMenuView
import com.mongs.wear.presentation.pages.collection.map.CollectionMapPickView
import com.mongs.wear.presentation.pages.collection.menu.CollectionMenuView
import com.mongs.wear.presentation.pages.collection.mong.CollectionMongPickView
import com.mongs.wear.presentation.pages.exchangeWalking.ExchangeWalkingView
import com.mongs.wear.presentation.pages.feed.food.FeedFoodPickView
import com.mongs.wear.presentation.pages.feed.menu.FeedMenuView
import com.mongs.wear.presentation.pages.feed.snack.FeedSnackPickView
import com.mongs.wear.presentation.pages.feedback.FeedbackView
import com.mongs.wear.presentation.pages.help.menu.HelpMenuView
import com.mongs.wear.presentation.pages.inventory.InventoryView
import com.mongs.wear.presentation.pages.login.LoginView
import com.mongs.wear.presentation.pages.luckyDraw.LuckyDrawView
import com.mongs.wear.presentation.pages.main.layout.MainPagerView
import com.mongs.wear.presentation.pages.searchMap.SearchMapView
import com.mongs.wear.presentation.pages.setting.SettingView
import com.mongs.wear.presentation.pages.slotPick.SlotPickView
import com.mongs.wear.presentation.pages.store.chargeStartPoint.StoreChargeStarPointView
import com.mongs.wear.presentation.pages.store.exchangePayPoint.StoreExchangePayPointView
import com.mongs.wear.presentation.pages.store.menu.StoreMenuView
import com.mongs.wear.presentation.pages.training.menu.TrainingMenuView
import com.mongs.wear.presentation.pages.training.runner.TrainingRunnerView
import com.mongs.wear.presentation.pages.walking.WalkingView

@Composable
fun MainView (
    context: Context = LocalContext.current,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    Box {
        if (mainViewModel.uiState.loadingBar || mainViewModel.uiState.permissionLoadingBar) {
            MainBackground()
            MainLoadingBar()
        } else {
            NavContent(modifier = Modifier.zIndex(0f))
        }

        val networkFlag = mainViewModel.network.observeAsState(true)

        if (!networkFlag.value) {
            NetworkErrorDialog(
                errorDialogLoadingBar = mainViewModel.uiState.errorDialogLoadingBar,
                retryNetwork = mainViewModel::retryNetwork,
                modifier = Modifier.zIndex(1f),
            )
        }
    }

    /**
     * 예외 발생 시 에러 Toast
     */
    LaunchedEffect(Unit) {
        BaseViewModel.errorEvent.collect { errorMessage ->
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * 성공 Toast
     */
    LaunchedEffect(Unit) {
        BaseViewModel.successEvent.collect { successMessage ->
            Toast.makeText(
                context,
                successMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * 필수 권한 요청
     */
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
        mainViewModel.checkPermission()
    }

    LaunchedEffect(mainViewModel.uiState.requestPermissionEvent) {
        mainViewModel.uiState.requestPermissionEvent.collect { permissions ->
            permissionLauncher.launch(permissions)
        }
    }
}

/**
 * 라우터
 */
@Composable
fun NavContent(
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier.zIndex(0f)
) {
    val navController = rememberSwipeDismissableNavController()

    val emptyPagerState = rememberPagerState(MainPagerConst.EMPTY_PAGER_STATE_INIT, 0f) { MainPagerConst.EMPTY_PAGER_STATE_SIZE }
    val pagerState = rememberPagerState(MainPagerConst.NORMAL_PAGER_STATE_INIT, 0f) { MainPagerConst.NORMAL_PAGER_STATE_SIZE }

    /**
     * 메인 페이지 스크롤 이벤트
     */
    LaunchedEffect(BaseViewModel.animatePageScrollMainPagerViewEvent) {
        BaseViewModel.animatePageScrollMainPagerViewEvent.collect {
            emptyPagerState.animateScrollToPage(MainPagerConst.EMPTY_PAGER_STATE_INIT)
            pagerState.animateScrollToPage(MainPagerConst.NORMAL_PAGER_STATE_INIT)
        }
    }

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = NavItem.Login.route,
        modifier = modifier
    ) {
        composable(route = NavItem.Login.route) {
            LoginView(navController = navController)
        }

        composable(route = NavItem.MainPager.route) {

            DisposableEffect(Unit) {
                val window = (context as ComponentActivity).window
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                onDispose {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }

            MainPagerView(
                navController = navController,
                emptyPagerState = emptyPagerState,
                pagerState = pagerState,
            )
        }

        navigation(
            startDestination = NavItem.CollectionMenu.route,
            route = NavItem.CollectionNested.route
        ) {
            composable(route = NavItem.CollectionMenu.route) {
                CollectionMenuView(navController = navController)
            }
            composable(route = NavItem.CollectionMapPick.route) {
                CollectionMapPickView(navController = navController)
            }
            composable(route = NavItem.CollectionMongPick.route) {
                CollectionMongPickView(navController = navController)
            }
        }

        navigation(
            startDestination = NavItem.FeedMenu.route,
            route = NavItem.FeedNested.route
        ) {
            composable(route = NavItem.FeedMenu.route) {
                FeedMenuView(navController = navController)
            }
            composable(route = NavItem.FeedFoodPick.route) {
                FeedFoodPickView(navController = navController)
            }
            composable(route = NavItem.FeedSnackPick.route) {
                FeedSnackPickView(navController = navController)
            }
        }

        composable(route = NavItem.SlotPick.route) {
            SlotPickView(navController = navController)
        }

        navigation(
            startDestination = NavItem.StoreMenu.route,
            route = NavItem.StoreNested.route
        ) {
            composable(route = NavItem.StoreMenu.route) {
                StoreMenuView(navController = navController)
            }
            composable(route = NavItem.StoreChargeStarPoint.route) {

                DisposableEffect(Unit) {
                    val window = (context as ComponentActivity).window
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                    onDispose {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }

                StoreChargeStarPointView(navController = navController)
            }
            composable(route = NavItem.StoreExchangePayPoint.route) {

                DisposableEffect(Unit) {
                    val window = (context as ComponentActivity).window
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                    onDispose {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }

                StoreExchangePayPointView(navController = navController)
            }
        }

        composable(route = NavItem.Feedback.route) {
            FeedbackView()
        }

        navigation(
            startDestination = NavItem.TrainingMenu.route,
            route = NavItem.TrainingNested.route
        ) {
            composable(route = NavItem.TrainingMenu.route) {
                TrainingMenuView(navController = navController)
            }
            composable(route = NavItem.TrainingJumping.route) {

                DisposableEffect(Unit) {
                    val window = (context as ComponentActivity).window
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                    onDispose {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }

                TrainingRunnerView(navController = navController)
            }
        }

        navigation(
            startDestination = NavItem.BattleMenu.route,
            route = NavItem.BattleNested.route
        ) {
            composable(route = NavItem.BattleMenu.route) {

                DisposableEffect(Unit) {
                    val window = (context as ComponentActivity).window
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                    onDispose {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }

                BattleMenuView(navController = navController)
            }
            composable(route = NavItem.BattleMatch.route) {

                DisposableEffect(Unit) {
                    val window = (context as ComponentActivity).window
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                    onDispose {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }

                BattleMatchView(navController = navController)
            }
        }

        navigation(
            startDestination = NavItem.HelpMenu.route,
            route = NavItem.HelpNested.route
        ) {
            composable(route = NavItem.HelpMenu.route) {
                HelpMenuView()
            }
        }

        composable(route = NavItem.Setting.route) {
            SettingView(navController = navController)
        }

        composable(route = NavItem.ExchangeWalking.route) {
            ExchangeWalkingView(navController = navController)
        }

        composable(route = NavItem.Inventory.route) {
            InventoryView()
        }

        composable(route = NavItem.SearchMap.route) {
            SearchMapView()
        }

        composable(route = NavItem.LuckyDraw.route) {
            LuckyDrawView()
        }

        composable(route = NavItem.Walking.route) {
            WalkingView()
        }
    }
}


@Composable
fun MainLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}
