package views

import controllers.ItemController
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.text.FontWeight
import models.TransactionsEntryModel
import tornadofx.*

class TransactionsEditor : View("My View") {
    private val model = TransactionsEntryModel()
    private val controller: ItemController by inject()

    var mTableView: TableViewEditModel<TransactionsEntryModel> by singleAssign()
    var totalExpensesLabel: Label by singleAssign()
    private val totalExpensesProp = SimpleDoubleProperty(0.0)

    init {
        updateTotalExpenses()
    }

    override val root = borderpane {
        center = vbox {
            form {
                fieldset {
                    field("Date of Transaction") {
                        maxWidth = 220.0
                        datepicker(model.transDate) {
                            this.required()
                            validator {
                                when {
                                    it?.dayOfMonth.toString().isEmpty() || it?.dayOfMonth.toString().isEmpty() || it?.dayOfYear.toString().isEmpty() ->
                                        error("The date entry cannot be blank")
                                     else -> null
                                }
                            }
                        }
                    }
                }

                fieldset {
                    field ("Item") {
                        maxWidth = 220.0
                        textfield(model.transName) {
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
                    field("Price") {
                        maxWidth = 220.0
                        textfield(model.transAmount) {
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
                                        addTransaction()
                                        model.rollback()
                                    }
                                }
                            }
                        }
                    }
                }

                hbox(10.0) {
                    button("Add Transaction") {
                        enableWhen(model.valid)
                        action {
                            model.commit{
                                addTransaction()
                                model.rollback()
                            }
                        }
                    }

                    button("Delete") {
                        action {
                            val selectedItem = mTableView.tableView.selectedItem
                            when(selectedItem) {
                                null -> return@action
                                else -> {
                                    val diff = totalExpensesProp.value - selectedItem.item.transAmount
                                    totalExpensesProp.value = diff

                                    controller.delete(selectedItem)
                                    updateTotalExpenses()
                                }
                            }
                        }
                    }

                    button ("Reset") {
                        enableWhen(model.valid)
                        action {
                            model.commit{
                                model.rollback()
                            }
                        }
                    }
                }
                fieldset {
                    tableview<TransactionsEntryModel> {
                        items = controller.items
                        mTableView = editModel
                        column("ID", TransactionsEntryModel::id)
                        column("Date", TransactionsEntryModel::transDate).makeEditable()
                        column("Name", TransactionsEntryModel::transName).makeEditable()
                        column("Amount", TransactionsEntryModel::transAmount).makeEditable()

                        onEditCommit {
                            controller.update(it)
                            updateTotalExpenses()
                            controller.updatePie(it)
                        }
                    }
                }
            }
        }
        right = vbox {
            alignment = Pos.CENTER
            paddingBottom = 10.0

            piechart ("Total Expenses") {
                data = controller.pieItemsData
            }
            totalExpensesLabel = label {
                if (totalExpensesProp.doubleValue() != 0.0) {
                    style {
                        fontSize = 19.px
                        padding = box(10.px)
                        fontWeight = FontWeight.EXTRA_BOLD
                        borderRadius = multi(box(8.px))
                        backgroundColor += c("white", 0.8)
                    }
                    bind(Bindings.concat("Total Expenses: ", "R", Bindings.format("%.2f", totalExpensesProp)))
                } else {
                    // Do nothing
                }
            }
        }
    }

    private fun updateTotalExpenses() {
        var total = 0.0
        try {
            controller.items.forEach {
                total += it.transAmount.value.toDouble()
            }
            totalExpensesProp.set(total)
            model.totalExpenses.value = total
        } catch (e: Exception) {
            totalExpensesProp.set(0.0)
        }
    }

    private fun addTransaction() {
        controller.add(model.transDate.value, model.transName.value, model.transAmount.value.toDouble())
        updateTotalExpenses()
    }

}


















