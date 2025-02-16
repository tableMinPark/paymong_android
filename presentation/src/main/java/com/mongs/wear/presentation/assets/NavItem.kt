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

    /* ExchangePayPoint */
    data object ExchangeNested: NavItem("ExchangeNested")
    data object ExchangeMenu: NavItem("ExchangeMenu")
    data object ExchangeWalking: NavItem("ExchangeWalking")
    data object ExchangeStarPoint: NavItem("ExchangeStarPoint")

    /* ChargeStarPoint */
    data object ChargeStarPoint: NavItem("ChargeStarPoint")

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

    /* Inventory */
    data object Inventory: NavItem("Inventory")

    /* Search Map */
    data object SearchMap: NavItem("SearchMap")

    /* Lucky Draw */
    data object LuckyDraw: NavItem("LuckyDraw")

    /* Walking */
    data object Walking: NavItem("Walking")

    /* Notice */
    data object Notice: NavItem("Notice")
}
