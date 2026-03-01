# 📝 Electron Text Editor

Simple desktop text editor built using Electron Framework.

## 📌 Features

- Create new file
- Open existing `.txt` file
- Save changes
- Exit application
- Confirmation dialogs for unsaved changes
- Clean and minimal UI

## 🛠 Technologies Used

- Electron
- Node.js
- HTML
- CSS
- JavaScript

## 🚀 Installation

Clone repository:

```bash
git clone https://github.com/your-username/your-repository.git
cd your-repository
```

Install dependencies:

```bash
npm install
```

Run application:

```bash
npm start
```

## 🧠 Application Logic

The application implements:

- Menu system using Electron Menu API
- File system operations via Node.js (fs module)
- IPC communication between main and renderer process
- Confirmation dialogs using Electron `dialog.showMessageBox`

## 🎯 UI Principles Applied

- ✔ Simplicity
- ✔ Consistency
- ✔ User feedback
- ✔ Protection from data loss
- ✔ Predictable behavior

## 📂 Project Structure

```
text-editor/
│
├── main.js
├── preload.js
├── renderer.js
├── index.html
├── style.css
├── package.json
└── README.md
```

## 📦 Build

To build executable (optional):

```bash
npm install electron-builder --save-dev
```

---

### 👨‍💻 Author

Dmytro Pylypchuk  
Kyiv, 2026
