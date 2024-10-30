package executionplan

/**
 * Le plan d'exécution correspond à l'ensemble des opérations qui seront exécutées
 * par le moteur de la base de données.
 *
 * @param firstOperation L'opération initiale à exécuter.
 */
case class ExecutionPlan(firstOperation: OperationTrait)
