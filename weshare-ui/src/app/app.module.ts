import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { BillComponent } from './bill/bill.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { InputTextModule } from 'primeng/inputtext';
import { MainComponent } from './main/main.component';
import { NgModule } from '@angular/core';
import { SkeletonModule } from 'primeng/skeleton';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { SliderModule } from 'primeng/slider';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { NavbarComponent } from './navbar/navbar.component';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BlockUIModule } from 'primeng/blockui';
import { NewBillFormComponent } from './new-bill-form/new-bill-form.component';
import { ShowBillsComponent } from './show-bills/show-bills.component';
import { SpinnerModule } from 'primeng/spinner';
import { ShowStatisticsComponent } from './show-statistics/show-statistics.component';
import { ChartModule } from 'primeng/chart';

@NgModule({
  declarations: [
    AppComponent,
    BillComponent,
    MainComponent,
    NavbarComponent,
    NewBillFormComponent,
    ShowBillsComponent,
    ShowStatisticsComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    ButtonModule,
    CardModule,
    ConfirmDialogModule,
    DropdownModule,
    FormsModule,
    HttpClientModule,
    InputTextModule,
    ReactiveFormsModule,
    SkeletonModule,
    SliderModule,
    ToastModule,
    ToolbarModule,
    ProgressSpinnerModule,
    BlockUIModule,
    SpinnerModule,
    ChartModule
  ],
  providers: [ConfirmationService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
