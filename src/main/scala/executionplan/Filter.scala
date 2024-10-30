package executionplan

/**
 * Filtre les lignes selon des critères.
 *
 * @param filter  Première expression de filtre.
 * @param filters Autres expressions de filtre (liées par AND).
 * @param next    Opération suivante.
 */
case class Filter(
                   filter: FilterExpression,
                   filters: List[FilterExpression],
                   next: Option[OperationTrait]
                 ) extends OperationTrait