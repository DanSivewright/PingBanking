package models

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime
import org.joda.time.LocalDate
import tornadofx.*

object TransactionsEntryTbl : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val transDate = date("trans_date")
    val transName = varchar("name", length = 50)
    val transAmount = decimal("price", scale = 2, precision = 9)
}

class TransactionEntry(id: Int, transDate: LocalDate, transName: String, transAmount: Double) {
    val idProperty = SimpleIntegerProperty(id)
    var id by idProperty

    val transDateProperty = SimpleObjectProperty<LocalDate>(transDate)
    var transDate by transDateProperty

    val transNameProperty = SimpleStringProperty(transName)
    var transName by transNameProperty

    val transAmountProperty = SimpleDoubleProperty(transAmount)
    var transAmount by transAmountProperty

    override fun toString(): String {
        return "TransactionEntry(id=$id, transDate=$transDate, transName=$transName, transAmount=$transAmount"
    }
}