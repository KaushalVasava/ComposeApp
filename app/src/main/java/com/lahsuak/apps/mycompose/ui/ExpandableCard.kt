package com.lahsuak.apps.mycompose.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lahsuak.apps.mycompose.ui.theme.Green
import com.lahsuak.apps.mycompose.ui.theme.LightGreen3

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableCard() {
    var expandableState by remember {
        mutableStateOf(false)
    }
    val rotateState by animateFloatAsState(
        targetValue = if (expandableState) 180f else 0f
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = shapes.medium,
        onClick = {
            expandableState = !expandableState
        }
    ) {
        Column(modifier = Modifier
            .background(Green)
            .fillMaxWidth()
            .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(6f),
                    text = "My Title",
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(modifier = Modifier
                    .weight(1f)
                    .alpha(ContentAlpha.medium)
                    .rotate(rotateState),
                    onClick = { expandableState = !expandableState }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Up and down")
                }
            }
            if (expandableState) {
                Text(text = "jffkfggfgfkggkffkkfdkgfkggkgkfgkfgkfkgfkgfgkfgkfgkgkfgjffkfggfgfkggkffkkfdkgfkggkgkfgkfgkfkgfkgfgkfgkfgkgkfg",
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
@Preview
fun ExpandableCardPreview() {
    ExpandableCard()
}