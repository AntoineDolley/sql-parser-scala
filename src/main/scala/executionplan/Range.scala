package executionplan

/**
 * Limite le nombre de lignes à retourner.
 *
 * @param start Index de début.
 * @param count Nombre de lignes à retourner.
 * @param next  Opération suivante.
 */
case class Range(
                  start: Int,
                  count: Int,
                  next: Option[OperationTrait]
                ) extends OperationTrait

