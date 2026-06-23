package com.pranaflow.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Main Hilt module.
 * Most dependencies use constructor injection with @Inject + @Singleton,
 * so this module is currently a marker. Extend as needed for
 * interface bindings or third-party dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule
