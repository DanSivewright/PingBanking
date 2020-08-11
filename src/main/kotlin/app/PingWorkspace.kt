package app

import javafx.scene.control.TabPane
import tornadofx.Workspace
import javax.swing.text.View

class PingWorkspace : Workspace("Ping Banking Workspace", NavigationMode.Tabs) {

    init {
        // TODO: Initialize db
        // Controllers
        // Doc views

        tabContainer.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

    }
}