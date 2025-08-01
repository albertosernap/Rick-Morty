package com.albertoserna.rickmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.albertoserna.rickmorty.ui.characters.detail.CharacterDetailScreen
import com.albertoserna.rickmorty.ui.characters.list.CharacterListScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CharacterList.route
    ) {
        composable(route = Screen.CharacterList.route) {
            CharacterListScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(Screen.CharacterDetail.createRoute(characterId))
                }
            )
        }
        
        composable(
            route = Screen.CharacterDetail.route + "/{characterId}",
            arguments = listOf(
                navArgument("characterId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
            CharacterDetailScreen(
                characterId = characterId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object CharacterList : Screen("character_list")
    object CharacterDetail : Screen("character_detail") {
        fun createRoute(characterId: Int) = "$route/$characterId"
    }
}