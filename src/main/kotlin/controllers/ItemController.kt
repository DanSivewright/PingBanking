package controllers

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.chart.PieChart
import models.TransactionEntry
import models.TransactionsEntryModel
import models.TransactionsEntryTbl
import models.toTransactionEntry
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import tornadofx.*
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

    var items: ObservableList<TransactionsEntryModel> by singleAssign()
    var pieItemsData: ObservableList<PieChart.Data> = FXCollections.observableArrayList<PieChart.Data>()

    init {
        items = listOfItems
        items.forEach {
            pieItemsData.add(PieChart.Data(it.transName.value, it.transAmount.value.toDouble()))
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

        listOfItems.add(
                TransactionsEntryModel().apply {
                    item = TransactionEntry(newEntry[TransactionsEntryTbl.id], newTransDate, newTransName, newTransAmount)
                }
        )
        pieItemsData.add(PieChart.Data(newTransName, newTransAmount))

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
        listOfItems.remove(model)
        removeModelFromPie(model)
    }

    fun updatePie(model: TransactionsEntryModel)  {
        val modelId = model.id
        var currentIndex: Int
        items.forEachIndexed{ index, data ->
            if (modelId == data.id) {
                currentIndex = index
                pieItemsData[currentIndex].name = data.transName.value
                pieItemsData[currentIndex].pieValue = data.transAmount.value.toDouble()
            } else {

            }
        }
    }

    private fun removeModelFromPie(model: TransactionsEntryModel) {
        var currentIndex = 0
        pieItemsData.forEachIndexed{ index, data ->
            if (data.name ==  model.transName.value && index != -1) {
                currentIndex = index
            }
        }
        pieItemsData.removeAt(currentIndex)
    }
}