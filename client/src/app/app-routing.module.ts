import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'create', loadComponent: () => import('./new-bill/new-bill.component').then((m) => m.NewBillComponent) },
  { path: 'bills', loadComponent: () => import('./show-bills/show-bills.component').then((m) => m.ShowBillsComponent) },
  {
    path: 'stats', loadComponent: () => import('./stats/stats.component').then((m) => m.StatsComponent), children: [
      { path: 'chart', loadComponent: () => import('./show-chart/show-chart.component').then((m) => m.ShowChartComponent) },
      { path: 'search', loadComponent: () => import('./search-bills/search-bills.component').then((m) => m.SearchBillsComponent) },
      { path: 'user', loadComponent: () => import('./user-stats/user-stats.component').then((m) => m.UserStatsComponent) }
    ]
  },
  { path: '', redirectTo: 'bills', pathMatch: 'full' },
  { path: '**', redirectTo: 'bills' }
];
