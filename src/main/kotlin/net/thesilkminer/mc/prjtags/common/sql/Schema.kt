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

@file:JvmName("__S")

package net.thesilkminer.mc.prjtags.common.sql

import org.jetbrains.exposed.sql.Table

internal object StatusVariables : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 25).uniqueIndex()
    val varcharValue = varchar("value_varchar", 100).nullable()
}

internal const val LAST_COMMIT_QUERY = "last_commit_query"
