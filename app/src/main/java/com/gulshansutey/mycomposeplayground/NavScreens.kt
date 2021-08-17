package com.gulshansutey.mycomposeplayground

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.gulshansutey.mycomposeplayground.components.chart.Pie
import com.gulshansutey.mycomposeplayground.components.chart.PieChart
import com.gulshansutey.mycomposeplayground.model.*
import com.gulshansutey.mycomposeplayground.ui.theme.*
import com.gulshansutey.mycomposeplayground.ultis.LogCompositions
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun BasicScreen(centerTitle: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = centerTitle,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.color_primary),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Composable
fun DetailsScreen(option: OptionModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        OptionItemView(option)
    }
}

@ExperimentalFoundationApi
@Composable
fun HomeScreen(optionsList: List<BaseUiModel>, onClicks: ((OptionModel) -> Unit)?) {
    val toolbarHeight = 326.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    var text by rememberSaveable { mutableStateOf("") }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Column {
            LazyVerticalGrid(
                contentPadding = PaddingValues(top = toolbarHeight),
                cells = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                items(optionsList.filterIsInstance<OptionModel>()) {
                    OptionItemView(it, onClicks)
                }
            }
        }

        Box(
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(ColorHomeInfoHeadBg)
                    .fillMaxSize()
            ) {

                LogCompositions(tag = "Text")
                Text(
                    "Title $text",
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Input Field") })
                val tracks = listOf(
                    Pie(72f, ColorTrackOne),
                    Pie(13f, ColorTrackTwo),
                    Pie(5f, ColorTrackThree),
                    Pie(3f, ColorTrackFour),
                    Pie(2f, ColorTrackFive),
                )
                Card(modifier = Modifier.padding(20.dp)) {
                    PieChart(
                        pies = tracks,
                        modifier = Modifier
                            .size(180.dp)
                            .padding(20.dp),
                        trackThickness = 30f
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeScreen(
    list: List<BaseUiModel>,
    onCheckedChange: (theme: ThemeItemModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .fillMaxSize()
    ) {
        items(list) { item ->
            when (item) {
                is HeaderModel -> HeaderItemView(item.title)
                is ThemeItemModel -> ThemeItemView(item, onCheckedChange)
                else -> HeaderItemView("Unknown")
            }
        }
    }
}

@Composable
fun ThemeItemView(
    item: ThemeItemModel,
    onCheckedChange: (theme: ThemeItemModel) -> Unit
) {
    Card(modifier = Modifier.padding(8.dp, 4.dp)) {
        Row(modifier = Modifier.padding(12.dp)) {
            Text(text = item.title)
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(
                selected = item.isSelected,
                onClick = { onCheckedChange(item) }
            )
        }
    }
}

@Composable
fun HeaderItemView(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Light,
        textAlign = TextAlign.Left,
        fontSize = 12.sp, modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(12.dp)
    )
}

@Composable
fun OptionItemView(option: OptionModel, onClicks: ((OptionModel) -> Unit)? = null) {
    Card(
        elevation = 8.dp,
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(6.dp),
    ) {
        Column(modifier = Modifier.clickable(onClick = { onClicks?.let { it(option) } })) {
            Image(
                painter = painterResource(id = R.drawable.profile_pic),
                contentDescription = option.title,
                modifier = Modifier
                    .padding(16.dp, 24.dp, 16.dp, 0.dp)
                    .size(40.dp, 40.dp)
                    .clip(CircleShape)
            )
            Text(
                text = option.title,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                fontSize = 19.sp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 26.dp, 16.dp, 0.dp)
            )
            Text(
                text = option.subTitle,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Left,
                fontSize = 16.sp, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp, 16.dp, 16.dp)
            )
        }
    }
}

@Composable
fun RitualItemView(data: RitualModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp, 24.dp)
    ) {
        Text(
            text = data.title.capitalize(LocaleList()),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Left,
            fontSize = 12.sp, modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = data.message,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            fontSize = 15.sp, modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp)
        )
    }

}

@ExperimentalPagerApi
@Composable
fun TabViewPagerScreen() {
    val tabData = listOf(
        "India",
        "America",
        "Germany",
        "Russia",
    )
    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0
    )
    val coroutineScope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage
    Column {
        TabRow(selectedTabIndex = currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState = pagerState, tabPositions),
                    height = 2.5.dp,
                    color = Color.White,
                )
            }
        ) {
            tabData.forEachIndexed { index, tab ->
                Tab(
                    selected = currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = tab) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.LightGray
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) { index ->
            BasicScreen(tabData[index])
        }
    }
}

