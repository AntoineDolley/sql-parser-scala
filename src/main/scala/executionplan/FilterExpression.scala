package executionplan

/**
 * Représente une expression de filtre dans une clause WHERE.
 */
enum FilterExpression {
  /** c1 = c2 */
  case Equal(c1: ColumnExpression, c2: ColumnExpression)

  /** c1 != c2 */
  case NotEqual(c1: ColumnExpression, c2: ColumnExpression)

  /** c1 > c2 */
  case Greater(c1: ColumnExpression, c2: ColumnExpression)

  /** c1 >= c2 */
  case GreaterOrEqual(c1: ColumnExpression, c2: ColumnExpression)

  /** c1 < c2 */
  case Less(c1: ColumnExpression, c2: ColumnExpression)

  /** c1 <= c2 */
  case LessOrEqual(c1: ColumnExpression, c2: ColumnExpression)
}