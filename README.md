# 📚 Library Management System

A Spring Boot-based Library Management System that allows users to borrow and return books, while admins manage inventory, track lost items, and export records. Features include borrowing limits, penalty rules, advanced filtering, and Excel export functionality, all backed by PostgreSQL and secured with JWT authentication.

## 🔍 Key Features

- Users must return borrowed books within 1 month. Unreturned books are marked as lost.
- Only admins can view lost book records.
- Books can be filtered by author, genre, publisher, and title.
- Admins can add or remove books, authors, genres, and publishers.
- Users who lose books face borrowing restrictions (first offense: half duration; second offense: access revoked).
- Admins can export book records (available, borrowed, lost) to Excel.
- A book can have multiple authors, genres, and publishers.
- Users can borrow up to 3 books at once.
- Users can view their borrowing history.

## 🛠️ Tech Stack

- **Backend:** Java 17, Spring Boot
- **Database:** PostgreSQL
- **Security:** Spring Security, JWT
- **Other Tools:** Liquibase, Lombok

## 📂 Installation

```bash
git clone https://github.com/your-username/library-management.git
cd library-management
./mvnw spring-boot:run
```

## 📄 License

This is an exercise project and is open for everyone to use in any way. No formal license applied to this project at the moment.

