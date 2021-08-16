package com.gulshansutey.mycomposeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.google.gson.Gson
import com.gulshansutey.mycomposeplayground.model.OptionModel
import com.gulshansutey.mycomposeplayground.viewmodel.HomeScreenViewModel
import com.gulshansutey.mycomposeplayground.viewmodel.ThemeListViewModel

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentView()
        }
    }

}

val items = listOf(
    NavItems.Home,
    NavItems.Data,
    NavItems.Anchor,
    NavItems.Bag,
    NavItems.Theme
)

@Preview
@ExperimentalFoundationApi
@Composable
fun ContentView() {
    val viewModel = ThemeListViewModel()
    val homeViewModel = HomeScreenViewModel()
    viewModel.MyAppTheme {
        MainScreen(viewModel, homeViewModel)
    }
}

@ExperimentalFoundationApi
@Composable
fun MainScreen(viewModel: ThemeListViewModel, homeViewModel: HomeScreenViewModel) {
    val navController = rememberNavController()
    Scaffold(topBar = { CreateToolbar() }, bottomBar = { CreateBottomNav(navController) }) {
        ScreenNavigation(navController, viewModel, homeViewModel)
    }
}

@Composable
fun CreateToolbar() {
    TopAppBar(
        title = {
            Text(text = "Amway", fontSize = 18.sp)
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White
    )
}

@Composable
fun CreateBottomNav(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(selected = item.path == currentRoute,
                onClick = {
                    navController.navigate(item.path) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(item.title) },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = MaterialTheme.colors.secondary.copy(0.5f),
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        painterResource(id = item.icon), contentDescription = item.title
                    )
                }
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ScreenNavigation(
    navHostController: NavHostController,
    viewModel: ThemeListViewModel,
    homeViewModel: HomeScreenViewModel
) {
    NavHost(navController = navHostController, startDestination = NavItems.Home.path) {
        items.forEach { item ->
            composable(NavItems.Theme.path) {
                val viewState = viewModel.viewStateStream().collectAsState()
                ThemeScreen(viewState.value.themeList, onCheckedChange = { item ->
                    viewModel.onAction(ThemeListViewModel.UiAction.ThemeSelected(item))
                })
            }

            composable(NavItems.Home.path) {
                val options = homeViewModel.optionsStateFlow.value
                HomeScreen(options) { opt ->
                    navHostController.navigate("${NavItems.Details.path}/${Gson().toJson(opt)}")
                }
            }

            composable("${NavItems.Details.path}/{option}",
                arguments = listOf(
                    navArgument("option") { type = NavType.StringType }
                )) { entry ->
                entry.arguments?.getString("option")?.let {
                    DetailsScreen(option = Gson().fromJson(it, OptionModel::class.java))
                }
            }

            composable(NavItems.Anchor.path) {
                BasicScreen(centerTitle = item.title)
            }
            composable(NavItems.Bag.path) {
                BasicScreen(centerTitle = item.title)
            }
            composable(NavItems.Data.path) {
                BasicScreen(centerTitle = item.title)
            }
        }
    }
}
