package controllers

import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import models.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import tornadofx.*
import util.execute
import java.math.BigDecimal

class UserController : Controller() {
    private val listOfItems: ObservableList<UserEntryModel> = execute {
        UserTbl.selectAll().map {
            UserEntryModel().apply {
                item = it.toUserEntry()
            }
        }.observable()
    }

    var items: ObservableList<UserEntryModel> by singleAssign()

    init {
        items = listOfItems
    }

    fun add(newName: String, newBalance: Double, newId: String): UserEntry {
        val newEntry = execute {
            UserTbl.insert {
                it[name] = newName
                it[balance] = BigDecimal.valueOf(newBalance)
                it[id] = newId
            }
        }

        listOfItems.add(
                UserEntryModel().apply {
                    item = UserEntry(newEntry[UserTbl.uid], newName, newBalance, newId)
                }
        )

        return UserEntry(newEntry[UserTbl.uid], newName, newBalance, newId)
    }

    fun update(updatedUser: UserEntryModel): Int {
        return execute {
            UserTbl.update ({ UserTbl.uid eq(updatedUser.uid.value.toInt())}) {
                it[name] = updatedUser.name.value
                it[balance] = BigDecimal.valueOf(updatedUser.balance.value.toDouble())
                it[id] = updatedUser.id.value
            }
        }
    }

    fun delete(model: UserEntryModel) {
        execute {
            UserTbl.deleteWhere {
                UserTbl.uid eq(model.uid.value.toInt())
            }
        }
    }
}