package executionplan

/**
 * Représente une opération dans le plan d'exécution.
 *
 * @param next L'opération suivante à exécuter.
 */
trait OperationTrait {
  def next: Option[OperationTrait]
}

