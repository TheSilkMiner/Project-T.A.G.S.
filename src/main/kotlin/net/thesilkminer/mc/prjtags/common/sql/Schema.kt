@file:JvmName("__S")

package net.thesilkminer.mc.prjtags.common.sql

import org.jetbrains.exposed.sql.Table

internal object StatusVariables : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 25).uniqueIndex()
    val varcharValue = varchar("value_varchar", 100).nullable()
}

internal const val LAST_COMMIT_QUERY = "last_commit_query"
