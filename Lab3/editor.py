import sys
from PyQt5.QtWidgets import (
    QApplication,
    QMainWindow,
    QTextEdit,
    QFileDialog,
    QMessageBox,
    QAction
)
from PyQt5.QtGui import QKeySequence


class TextEditor(QMainWindow):

    def __init__(self):
        super().__init__()

        self.current_file = None

        self.setWindowTitle("Текстовий редактор")
        self.setGeometry(200, 200, 800, 600)

        self.text_area = QTextEdit()
        self.setCentralWidget(self.text_area)

        self.text_area.document().modificationChanged.connect(self.update_title)

        self.create_menu()
        self.create_statusbar()

    # ---------- MENU ----------
    def create_menu(self):

        menu = self.menuBar().addMenu("Файл")

        new_action = QAction("Новий", self)
        new_action.setShortcut(QKeySequence.New)
        new_action.triggered.connect(self.new_file)
        menu.addAction(new_action)

        open_action = QAction("Відкрити", self)
        open_action.setShortcut(QKeySequence.Open)
        open_action.triggered.connect(self.open_file)
        menu.addAction(open_action)

        save_action = QAction("Зберегти", self)
        save_action.setShortcut(QKeySequence.Save)
        save_action.triggered.connect(self.save_file)
        menu.addAction(save_action)

        save_as_action = QAction("Зберегти як", self)
        save_as_action.setShortcut(QKeySequence.SaveAs)
        save_as_action.triggered.connect(self.save_file_as)
        menu.addAction(save_as_action)

        menu.addSeparator()

        exit_action = QAction("Вийти", self)
        exit_action.setShortcut(QKeySequence.Quit)
        exit_action.triggered.connect(self.close)
        menu.addAction(exit_action)

    # ---------- STATUS BAR ----------
    def create_statusbar(self):
        self.statusBar().showMessage("Готово")

    # ---------- TITLE ----------
    def update_title(self):
        name = "Без назви"

        if self.current_file:
            name = self.current_file.split("/")[-1]

        if self.text_area.document().isModified():
            name += " *"

        self.setWindowTitle(f"{name} - Текстовий редактор")

    # ---------- SAVE CHECK ----------
    def maybe_save(self):

        if self.text_area.document().isModified():

            reply = QMessageBox.question(
                self,
                "Незбережені зміни",
                "Файл було змінено. Зберегти зміни?",
                QMessageBox.Yes | QMessageBox.No | QMessageBox.Cancel
            )

            if reply == QMessageBox.Yes:
                return self.save_file()

            if reply == QMessageBox.Cancel:
                return False

        return True

    # ---------- NEW FILE ----------
    def new_file(self):

        if not self.maybe_save():
            return

        self.text_area.clear()
        self.current_file = None
        self.text_area.document().setModified(False)

        self.statusBar().showMessage("Створено новий файл")

    # ---------- OPEN FILE ----------
    def open_file(self):

        if not self.maybe_save():
            return

        file_name, _ = QFileDialog.getOpenFileName(
            self,
            "Відкрити файл",
            "",
            "Text Files (*.txt);;All Files (*)"
        )

        if file_name:

            with open(file_name, "r", encoding="utf-8") as file:
                self.text_area.setText(file.read())

            self.current_file = file_name
            self.text_area.document().setModified(False)

            self.statusBar().showMessage("Файл відкрито")

    # ---------- SAVE ----------
    def save_file(self):

        if self.current_file is None:
            return self.save_file_as()

        with open(self.current_file, "w", encoding="utf-8") as file:
            file.write(self.text_area.toPlainText())

        self.text_area.document().setModified(False)

        self.statusBar().showMessage("Файл збережено")

        return True

    # ---------- SAVE AS ----------
    def save_file_as(self):

        file_name, _ = QFileDialog.getSaveFileName(
            self,
            "Зберегти файл",
            "",
            "Text Files (*.txt);;All Files (*)"
        )

        if not file_name:
            return False

        self.current_file = file_name

        return self.save_file()

    # ---------- CLOSE EVENT ----------
    def closeEvent(self, event):

        if self.maybe_save():
            event.accept()
        else:
            event.ignore()


# ---------- RUN PROGRAM ----------
def main():

    app = QApplication(sys.argv)

    editor = TextEditor()
    editor.show()

    sys.exit(app.exec_())


if __name__ == "__main__":
    main()