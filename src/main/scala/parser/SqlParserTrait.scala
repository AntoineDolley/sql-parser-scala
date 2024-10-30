package parser

import executionplan.ExecutionPlan

/**
 * Interface du parseur SQL.
 */
trait SqlParserTrait {
  def parseSql(query: String): Either[SqlParsingError, ExecutionPlan]
}
