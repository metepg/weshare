import { ChartData } from 'chart.js';
import { CATEGORY_COLORS, MONTHS } from '../constants/constants';
import { BillCategoryCode } from '../constants/Categories';

/**
 * Generates an array of year options going back a specified number of years from the current year.
 * 
 * @param yearsToGoBack - The number of years to generate options for, counting back from the current year.
 * @returns An array of objects.
 * 
 * Example:
 * ```typescript
 * [
 *   { code: 2024, name: "2024" },
 *   { code: 2023, name: "2023" },
 *   { code: 2022, name: "2022" },
 *   // ...
 * ]
 * ```
 */
export function generateYearOptions(yearsToGoBack: number): { name: string, code: number }[] {
  const currentYear = new Date().getFullYear();
  return Array.from({length: yearsToGoBack}, (_, idx) => {
    const year = currentYear - idx;
    return {name: year.toString(), code: year};
  });
}

/**
 * Generates chart data from the provided map of monthly values by category.
 *
 * @param data - The map containing monthly values by category.
 * @returns A ChartData object structured for charting.
 */
export function generateChartData(data: Map<string, number[]>): ChartData {
  // Quick fix for finnish translations for now
  const CATEGORY_TRANSLATIONS: { [key in keyof typeof BillCategoryCode]: string } = {
    Category0: "Auto",
    Category1: "Kissat",
    Category2: "Laskut",
    Category3: "Ravintola",
    Category4: "Ruoka",
    Category5: "Muut"
  };
  
  return {
    labels: MONTHS,
    datasets: Object.values(BillCategoryCode)
    .filter(value => typeof value === 'string')
    .map((category, index) => ({
      label: CATEGORY_TRANSLATIONS[category as keyof typeof CATEGORY_TRANSLATIONS].toString() || category.toString(),
      backgroundColor: CATEGORY_COLORS[index] || "grey",
      data: data.get(category as string) || new Array(MONTHS.length).fill(0) as number[],
    })),
  };
}
