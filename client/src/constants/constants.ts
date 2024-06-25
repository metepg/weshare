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
  "lightgreen", "pink", "gold", "red", "skyblue", "grey"
];

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
