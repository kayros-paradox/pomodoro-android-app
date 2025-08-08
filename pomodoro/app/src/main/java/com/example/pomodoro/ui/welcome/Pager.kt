package com.example.pomodoro.ui.welcome

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.pomodoro.R
import com.example.pomodoro.data.Page
import kotlinx.coroutines.launch

@Composable
fun Pager(
    modifier: Modifier = Modifier,
    pages: List<Page>,
    closeScreen: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { index ->
            OnBoardingPage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
                page = pages[index],
                offsetFraction = pagerState.currentPageOffsetFraction,
                selected = index == pagerState.currentPage
            )
        }
        PagerBottomPanel(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_pre_medium)),
            pagerState = pagerState,
            closeScreen = closeScreen
        )
    }
}

@Composable
fun PagerBottomPanel(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    closeScreen: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var enabled by remember { mutableStateOf(value = true) }

    val closePager = {
        if (enabled) {
            enabled = false
            closeScreen()
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SkipPageButton(
            canScrollForward = pagerState.canScrollForward,
            onSkipPagerClick = closePager
        )
        PagerIndicator(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            pageCount = pagerState.pageCount,
            currentPage = pagerState.currentPage
        )
        SwipePageButton(
            canScrollForward = pagerState.canScrollForward,
            onClosePagerClick = closePager,
            onSwipePageClick = {
                if (!pagerState.isScrollInProgress) {
                    scope.launch {
                        pagerState.scrollToPage(page = pagerState.currentPage + 1)
                    }
                }
            }
        )
    }
}

@Composable
fun SkipPageButton(
    canScrollForward: Boolean = false,
    @DrawableRes skipPagerIconId: Int = R.drawable.skip,
    onSkipPagerClick: () -> Unit
) {
    AnimatedContent(
        targetState = canScrollForward,
        transitionSpec = {
            slideIntoContainer (
                animationSpec = tween(durationMillis = 200),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            ) togetherWith slideOutOfContainer (
                animationSpec = tween(durationMillis = 200),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        label = "анимация кнопки закрытия слайдера"
    ) {
        when {
            it -> PagerButton(iconId = skipPagerIconId, onClick = onSkipPagerClick)
            else -> Spacer(modifier = Modifier.size(56.dp))
        }
    }
}

@Composable
fun SwipePageButton(
    canScrollForward: Boolean = false,
    @DrawableRes closePagerIconId: Int = R.drawable.done,
    @DrawableRes swipePageIconId: Int = R.drawable.next,
    onClosePagerClick: () -> Unit,
    onSwipePageClick: () -> Unit
) {
    AnimatedContent(
        targetState = canScrollForward,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(durationMillis = 200)
            ) togetherWith fadeOut(
                animationSpec = tween(durationMillis = 200)
            )
        },
        label = "анимация кнопки перехода по слайдеру"
    ) {
        when {
            it -> PagerButton(iconId = swipePageIconId, onClick = onSwipePageClick)
            else -> PagerButton(iconId = closePagerIconId, onClick = onClosePagerClick)
        }
    }
}

@Composable
fun PagerButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit = {}
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        contentColor = contentColor,
        containerColor = containerColor,
        elevation = FloatingActionButtonDefaults.elevation(0.dp),
        shape = CircleShape
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "pager button icon"
        )
    }
}

@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { page ->
            IndicatorSingleDot(selected = page == currentPage)
        }
    }
}

@Composable
fun IndicatorSingleDot(
    minDotSizeDp: Dp = 8.dp,
    maxDotSizeDp: Dp = 24.dp,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.onTertiary,
    selected: Boolean = false
) {
    val width by animateDpAsState(
        targetValue = if (selected) maxDotSizeDp else minDotSizeDp,
        label = "анимация ширины индикатора слайдера"
    )

    val background by animateColorAsState(
        targetValue = if (selected) selectedColor else unselectedColor,
        label = "анимация цвета индикатора слайдера"
    )

    Box(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
            .height(minDotSizeDp)
            .width(width)
            .clip(CircleShape)
            .background(background)
            .alpha(0.9F)
    )
}