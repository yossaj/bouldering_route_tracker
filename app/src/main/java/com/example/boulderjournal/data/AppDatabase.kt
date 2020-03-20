package com.example.boulderjournal.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [RouteEntry::class], version = 18, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun routeDao(): RouteDao

    companion object {
        private val DATABASE_NAME = "routDb"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
                synchronized(this) {
                    var sInstance = INSTANCE

                    if(sInstance != null) {
                        sInstance = Room.databaseBuilder(
                                context.applicationContext,
                                AppDatabase::class.java,
                                DATABASE_NAME)
                                .addMigrations(
                                MIGRATION_17_18, MIGRATION_16_18
                        ).build()
                    }
                    return sInstance
                }

        }

        internal val MIGRATION_17_18: Migration = object : Migration(17, 18) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }

        internal val MIGRATION_16_18: Migration = object : Migration(16, 18) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }


}




