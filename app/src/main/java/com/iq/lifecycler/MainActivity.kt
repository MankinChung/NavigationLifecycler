package com.iq.lifecycler

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iq.lifecycler.ui.theme.LifecyclerIssureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifecyclerIssureTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("other") {
                        OtherScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val destinationListener = NavController.OnDestinationChangedListener { _, destination, _ ->
            Log.d("Lifecycle", "OnDestinationChanged ${destination.route}")
        }
        navController.addOnDestinationChangedListener(destinationListener)
        val observer = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                Log.d("Lifecycle", "onCreate")
            }

            override fun onStart(owner: LifecycleOwner) {
                Log.d("Lifecycle", "onStart")
            }

            override fun onResume(owner: LifecycleOwner) {
                Log.d("Lifecycle", "onResume")
            }

            override fun onPause(owner: LifecycleOwner) {
                Log.d("Lifecycle", "onPause")
            }

            override fun onStop(owner: LifecycleOwner) {
                Log.d("Lifecycle", "onStop")
            }

            override fun onDestroy(owner: LifecycleOwner) {
                Log.d("Lifecycle", "onDestroy")
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            navController.removeOnDestinationChangedListener(destinationListener)
            Log.d("Lifecycle", "onDispose")
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Hello Android")

//            Button(
//                onClick = {
//                    navController.navigate("other")
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(text = "navigate to other")
//            }

            Button(
                onClick = {
                    navController.navigate("other")
                    // Simulate clicking the back button
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "navigate to other and click back immediately")
            }
        }
    }
}


@Composable
fun OtherScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Text(text = "Other!")
    }
}