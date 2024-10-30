# SQL Parser Scala

## Overview

**SqlParserProject** is a Scala-based SQL parser that transforms `SELECT` queries into an executable plan. Leveraging the FastParse library, it efficiently parses SQL statements and constructs a structured `ExecutionPlan`.

### Packages

- **executionplan**: Defines the components of the execution plan, including operations (`TableScan`, `Projection`, `Filter`, `Range`) and expressions for columns and filters.

- **parser**: Contains the SQL parser implementation using FastParse, responsible for analyzing SQL queries and generating the corresponding `ExecutionPlan`.

## How It Works

1. **Parsing SQL Queries**: The `SqlParser` class utilizes FastParse to interpret SQL `SELECT` statements, handling clauses such as `SELECT`, `FROM`, `WHERE`, `LIMIT`, and `RANGE`.

2. **Building Execution Plans**: As the parser processes the SQL query, it constructs an `ExecutionPlan` comprising a sequence of operations:
    - **TableScan**: Reads data from the specified table.
    - **Projection**: Selects and transforms the required columns.
    - **Filter**: Applies `WHERE` conditions to filter rows.
    - **Range**: Limits the number of rows based on `LIMIT` or `RANGE`.

3. **Result Handling**: The parser returns either a successfully constructed `ExecutionPlan` or a `SqlParsingError` detailing any syntax issues encountered during parsing.

### Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/AntoineDolley/sql-parser-scala.git
   cd sql-parser-scala
   ```
2. **Build the Project**
    ```bash
   sbt compile
   ```
3. **Run the Parser**
   Create a `Main.scala` to test the parser or use the provided example below.
    ```Scala
   package main
   import parser.SqlParser
    
   object Main extends App {
    val sqlQuery = "SELECT col1, ABS(col2) FROM my_table WHERE col1 >= 18 LIMIT 10"
    
    val parser = new SqlParser()
    val result = parser.parseSql(sqlQuery)
    
    result match {
      case Right(plan) =>
        println("Execution Plan:")
        println(plan)
      case Left(error) =>
        println("Parsing Error:")
        println(error.message)
      }
    }
   ```
   
### Documentation 
Generate Scaladoc using sbt:
```bash
   sbt doc
 ```
Open the generated documentation in a browser:
```bash
   target/scala-3.3.4/api/index.html
 ```

