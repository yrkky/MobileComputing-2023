package com.yrkky.core.domain.repository

import com.yrkky.core.domain.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun addCategory(category: Category): Long
    suspend fun loadCategories(): Flow<List<Category>>
    suspend fun loadCategory(categoryId: Long): Category
}