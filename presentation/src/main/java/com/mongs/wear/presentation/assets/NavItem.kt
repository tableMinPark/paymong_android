package com.mongs.wear.presentation.assets

sealed class NavItem(val route: String) {

    /* Login */
    data object Login: NavItem("Login")

    /* MainPager */
    data object MainPager: NavItem("MainPager")

    /* Feed */
    data object FeedNested: NavItem("FeedNested")
    data object FeedMenu: NavItem("FeedMenu")
    data object FeedFoodPick: NavItem("FeedFoodPick")
    data object FeedSnackPick: NavItem("FeedSnackPick")

    /* Collection */
    data object CollectionNested: NavItem("CollectionNested")
    data object CollectionMenu: NavItem("CollectionMenu")
    data object CollectionMapPick: NavItem("CollectionMapPick")
    data object CollectionMongPick: NavItem("CollectionMongPick")

    /* SlotPick */
    data object SlotPick: NavItem("SlotPick")

    /* Store */
    data object StoreNested: NavItem("StoreNested")
    data object StoreMenu: NavItem("StoreMenu")
    data object StoreChargeStarPoint: NavItem("StoreChargeStarPoint")
    data object StoreExchangePayPoint: NavItem("StoreExchangePayPoint")

    /* Feedback */
    data object Feedback: NavItem("Feedback")

    /* Training */
    data object TrainingNested: NavItem("TrainingNested")
    data object TrainingMenu: NavItem("TrainingMenu")
    data object TrainingJumping: NavItem("TrainingJumping")

    /* Battle */
    data object BattleNested: NavItem("BattleNested")
    data object BattleMenu: NavItem("BattleMenu")
    data object BattleMatch: NavItem("BattleMatch")

    /* Help */
    data object HelpNested: NavItem("HelpNested")
    data object HelpMenu: NavItem("HelpMenu")

    /* Setting */
    data object Setting: NavItem("Setting")

    /* Exchange Walking */
    data object ExchangeWalking: NavItem("ExchangeWalking")

    /* Inventory */
    data object Inventory: NavItem("Inventory")

    /* Search Map */
    data object SearchMap: NavItem("SearchMap")

    /* Lucky Draw */
    data object LuckyDraw: NavItem("LuckyDraw")

    /* Walking */
    data object Walking: NavItem("Walking")
}
