package executionplan

/** 
 * Objet representant la Fin du plan d'exécution. 
 * */
case object EndExec extends OperationTrait {
  val next: Option[OperationTrait] = None
}
