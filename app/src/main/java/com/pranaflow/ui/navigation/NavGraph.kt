package com.pranaflow.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pranaflow.datastore.SettingsDataStore
import com.pranaflow.ui.breathing.BreathingScreen
import com.pranaflow.ui.category.CategoryScreen
import com.pranaflow.ui.home.HomeScreen
import com.pranaflow.ui.onboarding.OnboardingScreen
import com.pranaflow.ui.settings.SettingsScreen
import com.pranaflow.ui.splash.SplashScreen
import kotlinx.coroutines.launch
import javax.inject.Inject

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val CATEGORY = "category/{categoryName}"
    const val BREATHING = "breathing/{techniqueId}"
    const val SETTINGS = "settings"

    fun category(name: String) = "category/$name"
    fun breathing(techniqueId: String) = "breathing/$techniqueId"
}

@Composable
fun PranaNavHost(settingsDataStore: SettingsDataStore) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val onboardingDone by settingsDataStore.onboardingCompleted.collectAsState(initial = true)

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = { fadeIn(animationSpec = tween(400)) },
        exitTransition = { fadeOut(animationSpec = tween(400)) }
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    val destination = if (onboardingDone) Routes.HOME else Routes.ONBOARDING
                    navController.navigate(destination) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    scope.launch {
                        settingsDataStore.setOnboardingCompleted()
                    }
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onCategorySelected = { categoryName ->
                    navController.navigate(Routes.category(categoryName))
                },
                onTechniqueSelected = { techniqueId ->
                    navController.navigate(Routes.breathing(techniqueId))
                },
                onSettingsClicked = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        composable(
            route = Routes.CATEGORY,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "RELAX"
            CategoryScreen(
                categoryName = categoryName,
                onTechniqueSelected = { navController.navigate(Routes.breathing(it)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.BREATHING,
            arguments = listOf(navArgument("techniqueId") { type = NavType.StringType })
        ) { backStackEntry ->
            val techniqueId = backStackEntry.arguments?.getString("techniqueId") ?: "box"
            BreathingScreen(
                techniqueId = techniqueId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
