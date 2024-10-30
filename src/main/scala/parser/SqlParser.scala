package parser

import executionplan.*
import fastparse.*
import NoWhitespace.*

/**
 * Implémentation du parseur SQL 
 */
class SqlParser extends SqlParserTrait {
  def parseSql(sqlString: String): Either[SqlParsingError, ExecutionPlan] = {
    fastparse.parse(sqlString, selectStatement(_)) match {
      case Parsed.Success(plan, _) => Right(plan)
      case f: Parsed.Failure       => Left(SqlParsingError(f.msg))
    }
  }

  // Définition des parseurs FastParse

  // Mot-clé insensible à la caste
  private def keyword[$: P](s: String): P[Unit] = P(IgnoreCase(s))


  // Identifiant (nom de table, colonne, etc.)
  private def identifier[$: P]: P[String] = P(CharIn("a-zA-Z_") ~ CharsWhileIn("a-zA-Z0-9_", 0)).!

  // Espace optionnel
  private def ws[$: P]: P[Unit] = P(CharsWhileIn(" \t\r\n").?)

  // Valeurs littérales
  private def intLiteral[$: P]: P[Int] = P(CharIn("0-9").rep(1).!).map(_.toInt)
  private def doubleLiteral[$: P]: P[Double] = P((CharIn("0-9").rep(1) ~ "." ~ CharIn("0-9").rep(1)).!).map(_.toDouble)
  private def stringLiteral[$: P]: P[String] = P("'" ~ CharsWhile(_ != '\'', min = 0).! ~ "'")

  // Expressions de colonne
  private def columnExpression[$: P]: P[ColumnExpression] = P(
    functionCall | literal | columnName
  )

  // Liste d'expressions de colonnes
  private def columnList[$: P]: P[List[ColumnExpression | All.type]] = P(
    ("*".!.map(_ => All) | columnExpression).rep(1, sep = ", ").map(_.toList)
  )

  private def columnName[$: P]: P[ColumnExpression.Column] = P(identifier).map(ColumnExpression.Column.apply)

  private def literal[$: P]: P[ColumnExpression] = P(
    (doubleLiteral.map(ColumnExpression.LitDouble.apply)) |
      (intLiteral.map(ColumnExpression.LitInt.apply)) |
      (stringLiteral.map(ColumnExpression.LitString.apply))
  )

  private def functionCall[$: P]: P[ColumnExpression.FunctionCall] = P(
    identifier ~ "(" ~ columnExpression ~ ")"
  ).map { case (funcName, expr) =>
    ColumnExpression.FunctionCall(funcName.toUpperCase, expr)
  }

  // Clause SELECT
  private def selectClause[$: P]: P[List[ColumnExpression | All.type]] = P(
    keyword("SELECT") ~/ ws ~/ columnList
  )

  // Clause FROM
  private def fromClause[$: P]: P[String] = P(
    ws ~ keyword("FROM") ~/ ws ~ identifier
  )

  // Opérateurs de comparaison
  private def comparisonOperator[$: P]: P[String] = P(
    StringIn("=", "!=", "<>", ">=", "<=", ">", "<").!
  )

  // Expressions de filtre
  private def filterExpression[$: P]: P[FilterExpression] = P(
    columnExpression ~ ws ~ comparisonOperator ~ ws ~ columnExpression
  ).map {
    case (left, op, right) =>
      op match {
        case "="  => FilterExpression.Equal(left, right)
        case "!=" => FilterExpression.NotEqual(left, right)
        case "<>" => FilterExpression.NotEqual(left, right)
        case ">"  => FilterExpression.Greater(left, right)
        case ">=" => FilterExpression.GreaterOrEqual(left, right)
        case "<"  => FilterExpression.Less(left, right)
        case "<=" => FilterExpression.LessOrEqual(left, right)
      }
  }

  // Combinaison d'expressions de filtre avec AND
  private def whereCondition[$: P]: P[List[FilterExpression]] = P(
    filterExpression.rep(1, sep = ws ~ keyword("AND") ~ ws).map(_.toList)
  )

  // Clause WHERE
  private def whereClause[$: P]: P[List[FilterExpression]] = P(
    ws ~ keyword("WHERE") ~/ ws ~ whereCondition
  )

  // Clause LIMIT
  private def limitClause[$: P]: P[(Int, Int)] = P(
    ws ~ keyword("LIMIT") ~/ ws ~ intLiteral
  ).map(n => (0, n))

  // Clause RANGE
  private def rangeClause[$: P]: P[(Int, Int)] = P(
    ws ~ keyword("RANGE") ~/ ws ~ intLiteral ~ "," ~ ws ~ intLiteral
  ).map { case (start, count) => (start, count) }

  // Clause LIMIT ou RANGE
  private def limitOrRangeClause[$: P]: P[(Int, Int)] = P(limitClause | rangeClause)

  // Parseur principal pour les requêtes SELECT
  private def selectStatement[$: P]: P[ExecutionPlan] = P(
    selectClause ~ fromClause ~ whereClause.? ~ limitOrRangeClause.? ~ End
  ).map {
    case (cols, tableName, whereOpt, limitOpt) =>
      // Construction des opérations
      val tableScanOp = TableScan(tableName, next = None)

      val projectionOp = Projection(
        column = cols.head,
        otherColumns = cols.tail,
        next = Some(tableScanOp)
      )

      val filterOp = whereOpt match {
        case Some(conditions) =>
          Filter(
            filter = conditions.head,
            filters = conditions.tail,
            next = Some(projectionOp)
          )
        case None => projectionOp
      }

      val rangeOp = limitOpt match {
        case Some((start, count)) =>
          Range(
            start = start,
            count = count,
            next = Some(filterOp)
          )
        case None => filterOp
      }

      ExecutionPlan(firstOperation = rangeOp)
  }
}
