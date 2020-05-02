/*
 * Copyright (C) 2020  TheSilkMiner
 *
 * This file is part of Project T.A.G.S..
 *
 * Project T.A.G.S. is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Project T.A.G.S. is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Project T.A.G.S..  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact information:
 * E-mail: thesilkminer <at> outlook <dot> com
 */

@file:JvmName("DH")

package net.thesilkminer.mc.prjtags.common.sql

import net.thesilkminer.mc.boson.api.database.databasePath
import net.thesilkminer.mc.boson.api.log.L
import net.thesilkminer.mc.prjtags.MOD_NAME
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.nio.file.Files
import java.sql.Connection

private val l = L(MOD_NAME, "Database Handler")

private val database by lazy {
    l.info("Connecting to SQL database")
    val exists = Files.exists(databasePath)
    val db = Database.connect(url = "jdbc:sqlite:$databasePath", driver = "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    if (!exists) org.jetbrains.exposed.sql.transactions.transaction(db, ::createDatabase)
    l.info("Connection established")
    db
}

internal fun <R> transaction(runnable: Transaction.() -> R): R {
    return org.jetbrains.exposed.sql.transactions.transaction(database) {
        addLogger(Slf4jSqlDebugLogger)
        this.runnable()
    }
}

private fun createDatabase(transaction: Transaction) {
    @Suppress("unused")
    fun Transaction.runCreate() {
        SchemaUtils.create(StatusVariables)

        @Suppress("UNUSED_VARIABLE") // TODO("This is very basic db support: actually create a good SQL database")
        val commitQueryId = StatusVariables.insert {
            it[this.name] = LAST_COMMIT_QUERY
            it[this.varcharValue] = null
        } get StatusVariables.id
    }

    transaction.runCreate()
    l.info("Successfully created SQL database")
}

