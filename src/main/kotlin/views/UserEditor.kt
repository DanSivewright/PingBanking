package views

import controllers.ItemController
import controllers.UserController
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.text.FontWeight
import models.TransactionsEntryModel
import models.UserEntryModel
import tornadofx.*
import java.math.BigDecimal
import kotlin.error

class UserEditor: View("Users") {
    private val model = UserEntryModel()
    val controller: UserController by inject()

    var mTableView: TableViewEditModel<UserEntryModel> by singleAssign()

    override val root = borderpane {
        center = vbox {
            form {
                fieldset {
                    field ("Name") {
                        maxWidth = 220.0
                        textfield(model.name) {
                            this.required()
                            validator {
                                when {
                                    it.isNullOrEmpty() -> error("Field cannot be empty")
                                    it!!.length < 3 -> error("Too short")
                                    else -> null
                                }
                            }
                        }
                    }
                }

                fieldset {
                    field("Id Number") {
                        maxWidth = 220.0
                        textfield(model.id) {
                            this.required()
                            validator {
                                when (it) {
                                    null -> error("Price cannot be blank")
                                    else -> null
                                }
                            }
                            setOnKeyPressed {
                                if (it.code == KeyCode.ENTER) {
                                    model.commit {
                                        addUser()
                                        model.rollback()
                                    }
                                }
                            }
                        }
                    }
                }

                hbox(10.0) {
                    button("Add User") {
                        enableWhen(model.valid)
                        action {
                            addUser()
                            model.rollback()
                        }
                    }
                }
                fieldset {
                    tableview<UserEntryModel> {
                        items = controller.items
                        mTableView = editModel
                        column("Full Name", UserEntryModel::name).makeEditable()
                        column("Id Number", UserEntryModel::id).makeEditable()
                        column("Balance", UserEntryModel::balance).makeEditable()

                        onEditCommit {
                            controller.update(it)
                        }
                    }
                }
            }
        }
        right = vbox {
            minWidth = 400.0
            alignment = Pos.TOP_CENTER

            label ("Manage Funds") {
                style {
                    fontSize = 19.px
                    padding = box(15.px)
                    fontWeight = FontWeight.EXTRA_BOLD
                }
            }


            form {
                fieldset {
                    field {
                        maxWidth = 220.0
                        textfield {
                            action {
                            }
                        }
                    }
                    hbox(10.0) {
                        button("Deposit") {
                            action {
                                handleDeposit()
                            }
                        }
                    }
                }

                fieldset {
                    field {
                        maxWidth = 220.0
                    }
                    hbox(10.0) {
                        button("Withdraw") {
                            action {
                                handleWithdraw()
                            }
                        }
                    }
                }

            }
        }
    }

    private fun handleDeposit() {
        // This functionality is handled in the table... Double the Balance column to deposit and withdraw fundsc
    }
    private fun handleWithdraw() {
        // This functionality is handled in the table... Double the Balance column to deposit and withdraw fundsc
    }

    private fun addUser() {
        controller.add(model.name.value, 0.0, model.id.value)
    }



}