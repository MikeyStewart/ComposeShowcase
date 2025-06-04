package com.mikeystewart.composeshowcase

import kotlinx.serialization.Serializable

interface NavigationDestination

@Serializable
object ShowcaseList : NavigationDestination

@Serializable
object IPod : NavigationDestination

@Serializable
object CornerFold : NavigationDestination

@Serializable
object CustomTopBar : NavigationDestination

@Serializable
object Expressive : NavigationDestination