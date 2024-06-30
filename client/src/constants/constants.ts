import { BillCategoryCode } from './Categories';

export const MONTHS = [
  "Tammikuu",
  "Helmikuu",
  "Maaliskuu",
  "Huhtikuu",
  "Toukokuu",
  "Kesäkuu",
  "Heinäkuu",
  "Elokuu",
  "Syyskuu",
  "Lokakuu",
  "Marraskuu",
  "Joulukuu",
];

export const CATEGORY_COLORS = [
  "lightgreen", "pink", "gold", "red", "skyblue", "grey", "orange", "black", 
];

export const CATEGORY_COLOR_MAP: { [key in BillCategoryCode]: string } = {
  [BillCategoryCode.Category0]: CATEGORY_COLORS[0],
  [BillCategoryCode.Category1]: CATEGORY_COLORS[1],
  [BillCategoryCode.Category2]: CATEGORY_COLORS[2],
  [BillCategoryCode.Category3]: CATEGORY_COLORS[3],
  [BillCategoryCode.Category4]: CATEGORY_COLORS[4],
  [BillCategoryCode.Category5]: CATEGORY_COLORS[5],
  [BillCategoryCode.Category6]: CATEGORY_COLORS[6],
  [BillCategoryCode.Category7]: CATEGORY_COLORS[7]
};

export const BAR_CHART_OPTIONS  = {
  maintainAspectRatio: false,
  plugins: {
    legend: {
      labels: {
        color: "#000000"
      }
    }
  },
  scales: {
    x: {
      stacked: true,
      ticks: {
        color: "#000000"
      },
      grid: {
        color: "rgba(255,255,255,0.2)"
      }
    },
    y: {
      stacked: true,
      ticks: {
        color: "#000000"
      },
      grid: {
        color: "rgba(255,255,255,0.2)"
      }
    }
  }
};
