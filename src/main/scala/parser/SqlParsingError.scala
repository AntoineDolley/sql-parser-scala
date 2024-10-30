package parser

/**
 * Représente une erreur survenue lors du parsing SQL.
 *
 * @param message Message d'erreur.
 */
case class SqlParsingError(message: String) extends SqlParsingErrorTrait

