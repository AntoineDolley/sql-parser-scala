package executionplan

/**
 * Lecture de toutes les données d'une table.
 *
 * @param tableName Nom de la table.
 * @param next      Opération suivante.
 */
case class TableScan(
                      tableName: String,
                      next: Option[OperationTrait]
                    ) extends OperationTrait