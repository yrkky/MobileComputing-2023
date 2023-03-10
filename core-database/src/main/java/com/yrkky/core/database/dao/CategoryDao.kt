package com.yrkky.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yrkky.core.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdate(category: CategoryEntity): Long

    @Query("SELECT * FROM categories WHERE categoryId LIKE :categoryId")
    fun findOne(categoryId: Long): CategoryEntity

    @Query("SELECT * FROM categories")
    suspend fun findAll(): List<CategoryEntity>

    @Query("DELETE FROM categories WHERE categoryId LIKE :categoryId")
    suspend fun delete(categoryId: String)
}