package controllers

import javafx.collections.ObservableList
import models.TransactionEntry
import models.TransactionsEntryModel
import models.TransactionsEntryTbl
import models.toTransactionEntry
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import tornadofx.Controller
import tornadofx.observable
import util.execute
import util.toDate
import java.math.BigDecimal
import java.time.LocalDate

class ItemController : Controller() {
    // Get all items!!
    private val listOfItems: ObservableList<TransactionsEntryModel> = execute {
        TransactionsEntryTbl.selectAll().map {
            TransactionsEntryModel().apply {
                item = it.toTransactionEntry()
            }
        }.observable()
    }

    var transactionModel = TransactionsEntryModel()

    init {
        listOfItems.forEach {
            println("Item::: ${it.transName.value}")
        }
    }

    fun add(newTransDate: LocalDate, newTransName: String, newTransAmount: Double): TransactionEntry {
        val newEntry = execute {
            TransactionsEntryTbl.insert {
                it[transDate] = newTransDate.toDate()
                it[transName] = newTransName
                it[transAmount] = BigDecimal.valueOf(newTransAmount)
            }
        }
        return TransactionEntry(newEntry[TransactionsEntryTbl.id], newTransDate, newTransName, newTransAmount)
    }

    fun update(updatedTrans: TransactionsEntryModel): Int {
        return execute {
            TransactionsEntryTbl.update ({TransactionsEntryTbl.id eq(updatedTrans.id.value.toInt())}) {
                it[transDate] = updatedTrans.transDate.value.toDate()
                it[transName] = updatedTrans.transName.value
                it[transAmount] = BigDecimal.valueOf(updatedTrans.transAmount.value.toDouble())
            }
        }
    }

    fun delete(model: TransactionsEntryModel) {
        execute {
            TransactionsEntryTbl.deleteWhere {
                TransactionsEntryTbl.id eq(model.id.value.toInt())
            }
        }
    }
}