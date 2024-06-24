type CategorizedStats = Record<number, number>;

export interface CalculationResult {
  totalOwnAmount: number;
  categorizedTotals: CategorizedStats;
}
