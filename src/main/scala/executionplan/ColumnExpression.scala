package executionplan

/**
 * Représente une expression de colonne dans une requête SQL.
 */
enum ColumnExpression {
  
  /** Représente une colonne.
   *
   * @param name Nom de la colonne.
   */
  case Column(name: String)
  
  /** Valeur littérale entière. */
  case LitInt(value: Int)

  /** Valeur littérale double. */
  case LitDouble(value: Double)

  /** Valeur littérale chaîne de caractères. */
  case LitString(value: String)

  /** Appel de fonction.
   *
   * @param function Nom de la fonction.
   * @param expr     Expression de colonne en argument.
   */
  case FunctionCall(function: String, expr: ColumnExpression)
}