package app

import javafx.scene.control.TabPane
import models.TransactionsEntryTbl
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import tornadofx.Workspace
import util.createTables
import util.enableConsoleLogger
import util.execute
import util.toDate
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
        // Doc views

        tabContainer.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

    }
}