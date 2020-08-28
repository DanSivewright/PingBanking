package app

import controllers.ItemController
import controllers.UserController
import javafx.scene.control.TabPane
import models.TransactionsEntryTbl
import models.UserTbl
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import tornadofx.Workspace
import util.createTables
import util.enableConsoleLogger
import util.execute
import util.toDate
import views.TransactionsEditor
import views.UserEditor
import java.math.BigDecimal
import java.time.LocalDate
import javax.swing.text.View

class PingWorkspace : Workspace("Ping Banking Workspace", NavigationMode.Tabs) {

    init {
        // TODO: Initialize db
        enableConsoleLogger()
        Database.connect("jdbc:sqlite:./app-ping-banking.db", "org.sqlite.JDBC")
        createTables()

        // Controllers
        ItemController()
        UserController()

        // Doc views
        dock<TransactionsEditor>()
        dock<UserEditor>()

//        val newEntry = execute {
//            UserTbl.insert {
//                it[name] = "Tony Sivewright"
//                it[balance] = BigDecimal.valueOf(0.0)
//                it[id] = "6402209985712"
//            }
//        }
        tabContainer.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

    }
}