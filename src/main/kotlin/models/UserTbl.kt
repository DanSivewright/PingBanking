package models

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import tornadofx.*

fun ResultRow.toUserEntry() = UserEntry(
        this[UserTbl.uid],
        this[UserTbl.name],
        this[UserTbl.balance].toDouble(),
        this[UserTbl.id]
)

object UserTbl: Table() {
    val uid = integer("uid").autoIncrement().primaryKey()
    val name = varchar("name", length = 100)
    val balance = decimal("balance", scale = 2, precision = 9)
    val id = varchar("id", length = 13)
}

class UserEntry(uid: Int, name: String, balance: Double, id: String) {
    val uidProperty = SimpleIntegerProperty(uid)
    var uid: Int by uidProperty

    val nameProperty = SimpleStringProperty(name)
    var name: String by nameProperty

    val balanceProperty = SimpleDoubleProperty(balance)
    var balance: Double by balanceProperty

    val idProperty = SimpleStringProperty(id)
    var id: String by idProperty

//    val depositProperty = SimpleDoubleProperty(deposit)
//    var deposit: Double by depositProperty
//
//    val withdrawProperty = SimpleDoubleProperty(withdraw)
//    var withdraw: Double by withdrawProperty

    override fun toString(): String {
        return "UserEntry(uid=$uid, name=$name, balance=$balance id=$id)"
    }
}

class UserEntryModel: ItemViewModel<UserEntry>() {
    val uid = bind { item?.uidProperty }
    val name = bind { item?.nameProperty }
    val balance = bind { item?.balanceProperty }
    val id = bind { item?.idProperty }
//    var totalDeposit = bind { item?.depositProperty }
//    var totalWithdraw = bind { item?.withdrawProperty }
}