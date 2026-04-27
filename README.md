# 🪢 Java Hangman

A classic **Hangman** word-guessing game built entirely in Java — playable straight from your terminal with ASCII art, hints, and a full programming-themed word bank.

---

## ✨ Features

- 🎨 Animated ASCII gallows (7 stages)
- 💡 Hint displayed for every word
- 🔤 Guess letter-by-letter **or** attempt the full word at once
- 🔁 Play-again loop — no need to restart the program
- 📚 20 Java/programming themed words in the word bank
- ⚠️ Duplicate guess detection
- 🖥️ Auto-clears the screen between turns for a clean UI

---

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) **8 or higher**
- A terminal/command prompt

### Compile & Run

```bash
# Compile
javac Hangman.java

# Run
java Hangman
```

---

## 🎮 How to Play

1. A random word is chosen and displayed as underscores (e.g., `_ _ _ _ _ _`)
2. A hint is shown to help you figure out the word
3. Type a **single letter** and press Enter to guess
4. Or type `WORD` and press Enter to guess the entire word at once
5. You have **6 wrong guesses** before the hangman is complete, and the game ends
6. Guess the word before running out of lives to win!

```
  +---+
  |   |
  O   |
 /|\  |
 / \  |
      |
=========
```

---

## 📁 Project Structure

```
Hangman.java        ← Single-file game (no dependencies)
README.md           ← This file
```

---

## 🛠️ Customization

You can easily extend the word bank inside `Hangman.java`:

```java
private static final String[][] WORD_BANK = {
    {"YOURWORD", "Your hint here"},
    // add more entries...
};
```

---

## 👤 Author

**acedevph**

---

## 📄 License

This project is open source and free to use for learning and personal projects.
