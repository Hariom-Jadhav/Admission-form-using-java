# 📝 Coder Class Student Information Form

A simple and efficient **Data Collection Form** built for "Coder Class".

This desktop application is designed to help the class administration quickly collect and save information from new students. Instead of writing on paper, the admin can fill out this digital form, which automatically calculates the student's age and saves their details (Name, Mobile, Languages) directly into a MySQL database.

## 🚀 Features
* **Collect Data:** Takes First Name, Middle Name, Last Name, and Mobile Number.
* **Auto-Age Calculation:** Select a Date of Birth and instantly see the calculated Age.
* **Smart Selection List:** Click-to-toggle language selection (Java, Python, etc.) without holding the `Ctrl` key.
* **Database Integration:** Securely saves all records into MySQL.
* **User-Friendly UI:** Clean, professional light-colored interface.

## 🛠️ Technology Stack
* **Language:** Java (Swing/AWT)
* **Database:** MySQL
* **IDE:** Eclipse
* **Connector:** MySQL JDBC Driver

---

## 🗄️ Database Setup (SQL Query)

Before running the project, you must create the database. Open your **MySQL Workbench** or **Command Line** and run this code block:

```sql
-- 1. Create the Database
CREATE DATABASE admission_db;

-- 2. Select the Database
USE admission_db;

-- 3. Create the Students Table
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    middle_name VARCHAR(50),
    last_name VARCHAR(50),
    mobile_number VARCHAR(20),
    dob DATE,
    age INT,
    languages VARCHAR(255)
);
