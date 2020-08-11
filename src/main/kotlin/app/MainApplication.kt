package app

import javafx.stage.Stage
import tornadofx.App

class MainApplication : App(PingWorkspace::class) {
    override fun start(stage: Stage) {
        with(stage){
            width = 1200.0
            height = 600.0
        }
        super.start(stage)
    }
}