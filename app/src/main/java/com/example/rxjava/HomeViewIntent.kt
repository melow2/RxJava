package com.example.rxjava

sealed class HomeViewIntent : MviIntent {
  object Initial : HomeViewIntent()
  object Refresh : HomeViewIntent()
  object LoadNextPageUpdatedComic : HomeViewIntent()
  object RetryNewest : HomeViewIntent()
  object RetryMostViewed : HomeViewIntent()
  object RetryUpdate : HomeViewIntent()
}