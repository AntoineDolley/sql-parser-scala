package executionplan

/**
 * Applique une projection et des transformations aux lignes.
 *
 * @param column       Première expression de colonne à projeter.
 * @param otherColumns Autres expressions de colonnes à projeter.
 * @param next         Opération suivante.
 */
case class Projection(
                       column: ColumnExpression | All.type,
                       otherColumns: List[ColumnExpression | All.type],
                       next: Option[OperationTrait]
                     ) extends OperationTrait
