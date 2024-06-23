import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewBillFormComponent } from './new-bill-form/new-bill-form.component';
import { ShowBillsComponent } from './show-bills/show-bills.component';
import { ShowChartComponent } from './show-chart/show-chart.component';
import { SearchBillsComponent } from './search-bills/search-bills.component';
import { UserStatsComponent } from './user-stats/user-stats.component';
import { StatsComponent } from './stats/stats.component';

const routes: Routes = [
  { path: 'create', component: NewBillFormComponent },
  { path: 'bills', component: ShowBillsComponent },
  { path: 'stats', component: StatsComponent, children: [
      { path: 'chart', component: ShowChartComponent },
      { path: 'search', component: SearchBillsComponent },
      { path: 'user', component: UserStatsComponent }
    ]
  },{ path: '', redirectTo: 'bills', pathMatch: 'full' },
  { path: '**', redirectTo: 'bills' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
