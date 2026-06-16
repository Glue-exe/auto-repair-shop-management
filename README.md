# 🔧 Auto Repair Shop Management System

A Java console application for managing an auto repair shop: customers, their cars, parts/services inventory, and repair orders — with all data persisted to local JSON files.

## Overview

This is an object-oriented, menu-driven CLI (command-line interface) tool built around a single active `Mechanic` who registers customers and their cars, manages a parts/services catalog, opens repair orders, and prints receipts. All state is loaded from and saved back to JSON files on disk, so data survives between runs without needing a database.

## Features

- 👤 Register, edit, and list customers (with multiple cars per customer)
- 🚗 Add, edit, and delete cars linked to a customer
- 🧾 Open repair orders, attach products/services as line items, and track repair + payment status
- 🧰 Manage a parts/products catalog (brand, car type, price) and a services/labor catalog
- 🧮 Auto-calculated order totals and a printable receipt/bill view
- 📜 Bill history — browse past orders and reprint receipts
- 💾 JSON-based persistence (no external database required)

## Tech Stack

- **Language:** Java (Scanner-based console I/O)
- **JSON library:** [json-simple 1.1.1](https://code.google.com/archive/p/json-simple/) (bundled as `json-simple-1.1.1.jar`)
- **Data storage:** Flat JSON files (`customers.json`, `cars.json`, `products.json`, `services.json`, `orders.json`, `completed_orders.json`)

## Project Structure

```
.
├── Main.java                 # Entry point — menu loop
├── Mechanic.java              # Core controller: all menu actions (register, orders, stock, etc.)
├── Person.java                # Base class for people
├── Customer.java               # extends Person — adds customerId
├── Car.java                   # Car linked to an owning Customer
├── Item.java                  # Base class for billable items
├── Product.java                # extends Item — parts (brand, car type)
├── Service.java                # extends Item — labor/services
├── Order.java                  # Repair order: customer, car, mechanic, line items, status
├── JsonStorageManager.java     # Save/load helpers for every entity, backed by json-simple
├── json-simple-1.1.1.jar       # Third-party JSON dependency
└── *.json                      # Persisted data (customers, cars, products, services, orders)
```

## Class Design

- `Person` → `Customer`, `Mechanic` (shared name/phone/address fields)
- `Item` → `Product`, `Service` (shared name/price fields)
- `Order` ties together a `Customer`, a `Car`, the `Mechanic`, and a list of `Item`s (products and/or services), with repair status and payment status fields

## Getting Started

### Prerequisites
- JDK 8 or later
- The bundled `json-simple-1.1.1.jar` on the classpath (already referenced in `.vscode/settings.json` for VS Code's Java extension)

### Compile & Run

```bash
javac -cp json-simple-1.1.1.jar *.java
java -cp .:json-simple-1.1.1.jar Main      # macOS/Linux
java -cp .;json-simple-1.1.1.jar Main      # Windows
```

The app starts with a single active mechanic session and loads existing data from the `.json` files in the working directory automatically.

### Menu options

| # | Action |
|---|---|
| 1 | Register new customer & car |
| 2 | Edit/delete customer info |
| 3 | View all members |
| 4 | Register additional car for an existing customer |
| 5 | Edit car info |
| 6 | Delete car |
| 7 | Manage orders & repair status |
| 8 | Bill history & view receipts |
| 9 | Product & stock management |
| 10 | Service management |
| 0 | Exit |

## Future Improvements

- Support multiple mechanics / multi-user accounts instead of a single fixed mechanic
- Migrate from flat JSON files to a proper database (e.g. SQLite or MySQL)
- Add input validation and automated tests

> The bundled `*.json` files contain sample/seed data — delete or edit them to start from a clean slate.

## License

No license specified.
