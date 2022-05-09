package com.lahsuak.apps.mycompose

import android.Manifest
import android.os.Bundle
import android.content.res.Configuration
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.lahsuak.apps.mycompose.ui.BottomNavItem
import com.lahsuak.apps.mycompose.ui.isPermanentlyDenied
import com.lahsuak.apps.mycompose.ui.theme.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyComposeTheme {
                PermissionHandle()
//                val navController = rememberNavController()
//                Scaffold(
//                    bottomBar = {
//                        BottomNavigationBar(items = listOf(
//                            BottomNavItem("Home",
//                                "home",
//                                icon = Icons.Default.Home),
//                            BottomNavItem("Chat",
//                                "chat",
//                                icon = Icons.Default.Email),
//                            BottomNavItem("Settings",
//                                "settings",
//                                icon = Icons.Default.Settings)
//                        ),
//                            navController = navController,
//                            onItemClick = {
//                                navController.navigate(it.route)
//                            }
//                        )
//                    }
//                ) {
//                    Navigation(navController = navController)
//                }
            }
        }
    }

    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_NO,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun PreviewApp() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomNavigationBar(items = listOf(
                    BottomNavItem("Home",
                        "home",
                        icon = Icons.Default.Home),
                    BottomNavItem("Chat",
                        "chat",
                        icon = Icons.Default.Email, 67),
                    BottomNavItem("Settings",
                        "settings",
                        icon = Icons.Default.Settings)
                ),
                    navController = navController,
                    onItemClick = {
                        navController.navigate(it.route)
                    }
                )
            }
        ) {
            Navigation(navController = navController)
        }
    }

    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "Home") {
            composable(route = "Home") {
                HomeScreen()
            }
            composable(route = "Chat") {
                ChatScreen()
            }
            composable(route = "Settings") {
                SettingsScreen()
            }
        }
    }

    @Composable
    fun HomeScreen() {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Text(text = "Home screen")
        }
    }

    @Composable
    fun ChatScreen() {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Text(text = "Chat screen")
        }
    }

    @Composable
    fun SettingsScreen() {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Text(text = "Settings screen")
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun BottomNavigationBar(
        items: List<BottomNavItem>,
        navController: NavController,
        modifier: Modifier = Modifier,
        onItemClick: (BottomNavItem) -> Unit,
    ) {
        val backStackEntry = navController.currentBackStackEntryAsState()

        BottomNavigation(modifier = modifier,
            backgroundColor = Color.DarkGray,
            elevation = 5.dp
        ) {

            items.forEach { item ->
                val selected = item.route == backStackEntry.value?.destination?.route
                BottomNavigationItem(
                    selected = selected,
                    onClick = { onItemClick(item) },
                    selectedContentColor = Color.Green,
                    unselectedContentColor = Color.Gray,
                    icon = {
                        Column(horizontalAlignment = CenterHorizontally) {
                            Log.d("TAG", "BottomNavigationBar: ${item.badgeCount}")
                            if (item.badgeCount > 0) {
                                BadgeBox(badgeContent = {
                                    Text(text = item.badgeCount.toString())
                                }) {
                                    Icon(imageVector = item.icon,
                                        contentDescription = item.name)
                                }
                            } else {
                                Icon(imageVector = item.icon,
                                    contentDescription = item.name
                                )
                            }

                            if (selected) {
                                Text(text = item.name,
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp)
                            }
                        }
                    })
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable()
    fun PermissionHandle() {
        val permissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
            )
        )

        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(
            key1 = lifecycleOwner,
            effect = {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START) {
                        permissionsState.launchMultiplePermissionRequest()
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            permissionsState.permissions.forEach { perm ->
                when (perm.permission) {
                    Manifest.permission.CAMERA -> {
                        when {
                            perm.hasPermission -> {
                                Text(text = "Camera permission accepted")
                            }
                            perm.shouldShowRationale -> {
                                Text(text = "Camera permission is needed" +
                                        "to access the camera")
                            }
                            perm.isPermanentlyDenied() -> {
                                Text(text = "Camera permission was permanently" +
                                        "denied. You can enable it in the app" +
                                        "settings.")
                            }
                        }
                    }
                    Manifest.permission.RECORD_AUDIO -> {
                        when {
                            perm.hasPermission -> {
                                Text(text = "Record audio permission accepted")
                            }
                            perm.shouldShowRationale -> {
                                Text(text = "Record audio permission is needed" +
                                        "to access the camera")
                            }
                            perm.isPermanentlyDenied() -> {
                                Text(text = "Record audio permission was permanently" +
                                        "denied. You can enable it in the app" +
                                        "settings.")
                            }
                        }
                    }
                }
            }

        }
    }
}