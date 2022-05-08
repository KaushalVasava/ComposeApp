package com.lahsuak.apps.mycompose.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lahsuak.apps.mycompose.R
import com.lahsuak.apps.mycompose.ui.ImageWithText

@Composable
fun HomeScreen() {
    InstagramProfile()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InstagramProfile() {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar("kaushal_vasava", modifier = Modifier.padding(top=10.dp, bottom = 10.dp))
        Spacer(modifier = Modifier.padding(2.dp))
        MiddlePart()
        Spacer(modifier = Modifier.padding(2.dp))
        ProfileDescription(displayName = "Android Developer",
            description = "I'm a android developer who loves to make apps",
            url = "https://github.com",
            followedBy = listOf("Kaushal,Mar"),
            otherCount = 34)
        Spacer(modifier = Modifier.height(25.dp))
        ButtonSection(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(25.dp))
        HighlightSection(
            highlights = listOf(
                ImageWithText(
                    image = painterResource(id = R.drawable.ic_music),
                    text = "YouTube"
                ),
                ImageWithText(
                    image = painterResource(id = R.drawable.ic_moon),
                    text = "Q&A"
                ),
                ImageWithText(
                    image = painterResource(id = R.drawable.ic_videocam),
                    text = "Discord"
                ),
                ImageWithText(
                    image = painterResource(id = R.drawable.ic_bubble),
                    text = "Telegram"
                ),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        PostTabView(
            imageWithText = listOf(
                ImageWithText(
                    image = painterResource(id = R.drawable.ic_home),
                    text = "Posts"
                ),
                ImageWithText(
                    image = painterResource(id = R.drawable.ic_search),
                    text = "Reels"
                ),
                ImageWithText(
                    image = painterResource(id = R.drawable.ic_headphone),
                    text = "IGTV"
                ),
                ImageWithText(
                    image = painterResource(id = R.drawable.ic_profile),
                    text = "Profile"
                ),
            )
        ) {
            selectedTabIndex = it
        }
        when (selectedTabIndex) {
            0 -> PostSection(
                posts = listOf(
                    painterResource(id = R.drawable.ic_headphone),
                    painterResource(id = R.drawable.ic_search),
                    painterResource(id = R.drawable.ic_videocam),
                    painterResource(id = R.drawable.ic_headphone),
                    painterResource(id = R.drawable.ic_music),
                    painterResource(id = R.drawable.ic_play),
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TopBar(name: String, modifier: Modifier) {
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Navigate up",
            modifier = Modifier.size(24.dp))
        Text(text = name, fontSize = 20.sp)
        Icon(imageVector = Icons.Default.Notifications,
            contentDescription = "Navigate up",
            modifier = Modifier.size(24.dp))
        Icon(imageVector = Icons.Default.MoreVert,
            contentDescription = "More options", modifier = Modifier.size(24.dp))
    }
}

@Composable
fun MiddlePart() {
    Column(modifier = Modifier.fillMaxWidth()) {

        Row(verticalAlignment = CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)) {
            RoundImage(
                painterResource(id = R.drawable.ic_profile),
                modifier = Modifier
                    .weight(3f)
                    .size(100.dp),
            )
            StateInfo(modifier = Modifier.weight(7f))
        }
    }
}

@Composable
fun RoundImage(image: Painter, modifier: Modifier) {
    Image(painter = image,
        contentDescription = "Profile Image",
        modifier = modifier
            .padding(start = 10.dp)
            .aspectRatio(1f, true)
            .border(width = 1.dp, color = Color.LightGray, CircleShape)
            .padding(2.dp)

    )
}

@Composable
fun StateInfo(modifier: Modifier) {
    StateItem("Posts", "4", modifier)
    StateItem("Followers", "100k", modifier)
    StateItem("Following", "45", modifier)
}

@Composable
fun StateItem(desc: String, count: String, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ) {
        Text(text = desc, fontSize = 16.sp)
        Text(text = count, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Composable
fun ProfileDescription(
    displayName: String,
    description: String,
    url: String,
    followedBy: List<String>,
    otherCount: Int,
) {
    val letterSpacing = 0.5.sp
    val lineHeight = 20.sp

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
        Text(text = displayName, fontWeight = FontWeight.Bold,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight
        )
        Text(text = description,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight
        )
        Text(text = url, fontWeight = FontWeight.Normal,
            color = Color.Blue,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight
        )
        if (followedBy.isNotEmpty()) {
            Text(text = buildAnnotatedString {
                val boldStyle = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)
                append("Followed by ")
                followedBy.forEachIndexed { index, name ->
                    pushStyle(boldStyle)
                    append(name)
                    pop()
                    if (index < followedBy.size - 1) {
                        append(", ")
                    }
                    if (otherCount > 2) {
                        append(" and ")
                        pushStyle(boldStyle)
                        append("$otherCount others")
                    }
                }
            })
        }

    }
}

@Composable
fun ButtonSection(
    modifier: Modifier = Modifier,
) {
    val minWidth = 95.dp
    val height = 30.dp
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        ActionButton(
            text = "Following",
            icon = Icons.Default.KeyboardArrowDown,
            modifier = Modifier
                .defaultMinSize(minWidth = minWidth)
                .height(height)
        )
        ActionButton(
            text = "Message",
            modifier = Modifier
                .defaultMinSize(minWidth = minWidth)
                .height(height)
        )
        ActionButton(
            text = "Email",
            modifier = Modifier
                .defaultMinSize(minWidth = minWidth)
                .height(height)
        )
        ActionButton(
            icon = Icons.Default.KeyboardArrowDown,
            modifier = Modifier
                .size(height)
        )
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(6.dp)
    ) {
        if (text != null) {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}

@Composable
fun HighlightSection(
    modifier: Modifier = Modifier,
    highlights: List<ImageWithText>,
) {
    LazyRow(modifier = modifier) {
        items(highlights.size) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(end = 15.dp)
            ) {
                RoundImage(
                    image = highlights[it].image,
                    modifier = Modifier.size(70.dp)
                )
                Text(
                    text = highlights[it].text,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PostTabView(
    modifier: Modifier = Modifier,
    imageWithText: List<ImageWithText>,
    onTabSelected: (selectedIndex: Int) -> Unit,
) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }

    val inactiveColor = Color.Gray
    TabRow(selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.Transparent,
        contentColor = Color.Black,
        modifier = modifier
    ) {
        imageWithText.forEachIndexed { index, item ->
            Tab(selected = selectedTabIndex == index,
                selectedContentColor = Color.Black,
                unselectedContentColor = inactiveColor,
                onClick = {
                    selectedTabIndex = index
                    onTabSelected(index)
                }
            ) {
                Icon(painter = item.image,
                    contentDescription = item.text,
                    tint = if (selectedTabIndex == index)
                        Color.Black
                    else
                        inactiveColor,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(20.dp)
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun PostSection(
    posts: List<Painter>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        modifier = modifier
            .scale(1.01f)
    ) {
        items(posts.size) {
            Image(
                painter = posts[it],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(
                        width = 1.dp,
                        color = Color.White
                    )
            )
        }
    }
}
