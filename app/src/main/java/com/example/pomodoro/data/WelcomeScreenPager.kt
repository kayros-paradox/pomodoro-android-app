package com.example.pomodoro.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.example.pomodoro.R

@Immutable
data class Page(
    @StringRes val titleId: Int,
    @StringRes val descriptionId: Int,
    @DrawableRes val imageId: Int
)

@Immutable
object WelcomeScreenPager {
    val listOfPages = listOf(
        Page(
            titleId = R.string.welcome_page_title_focus,
            descriptionId = R.string.welcome_page_description_focus,
            imageId = R.drawable.welcome_page_1
        ),
        Page(
            titleId = R.string.welcome_page_title_relax,
            descriptionId = R.string.welcome_page_description_relax,
            imageId = R.drawable.welcome_page_2
        ),
        Page(
            titleId = R.string.welcome_page_title_begin,
            descriptionId = R.string.welcome_page_description_begin,
            imageId = R.drawable.welcome_page_3
        )
    )
}
