import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewBillFormComponent } from './new-bill-form/new-bill-form.component';
import { ShowBillsComponent } from './show-bills/show-bills.component';
import {
  ShowStatisticsComponent
} from './show-statistics/show-statistics.component';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';

const routes: Routes = [
  { path: 'create', component: NewBillFormComponent },
  { path: 'bills', component: ShowBillsComponent },
  { path: 'stats', component: ShowStatisticsComponent },
  { path: '', redirectTo: 'bills', pathMatch: 'full' },
  { path: '**', redirectTo: 'bills' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [
    { provide: LocationStrategy, useClass: HashLocationStrategy }
  ]
})
export class AppRoutingModule { }
