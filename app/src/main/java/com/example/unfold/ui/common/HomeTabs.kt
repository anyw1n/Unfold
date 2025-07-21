package com.example.unfold.ui.common

import androidx.annotation.DrawableRes
import com.example.unfold.R
import com.example.unfold.ui.MainDestinations

enum class HomeTabs(@DrawableRes val icon: Int, val route: String) {
    Ribbon(R.drawable.home, HomeDestinations.RibbonRoute),
    Collections(R.drawable.star, HomeDestinations.CollectionsRoute),
    Profile(R.drawable.person, HomeDestinations.ProfileRoute),
}

object HomeDestinations {
    const val RibbonRoute = MainDestinations.HomeRoute + "/ribbon"
    const val CollectionsRoute = MainDestinations.HomeRoute + "/collections"
    const val ProfileRoute = MainDestinations.HomeRoute + "/profile"
}

object RibbonRoutes {
    const val PhotoRoute = HomeDestinations.RibbonRoute + "/photo"
    const val PhotoId = "photoId"
}

object CollectionsRoutes {
    const val CollectionRoute = HomeDestinations.CollectionsRoute + "/collection"
    const val CollectionId = "collectionId"
}
