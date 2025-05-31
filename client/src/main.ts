import { enableProdMode, importProvidersFrom } from '@angular/core';

import { environment } from './environments/environment';
import { AppComponent } from './app/app.component';
import { ChartModule } from 'primeng/chart';
import { BlockUIModule } from 'primeng/blockui';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ToolbarModule } from 'primeng/toolbar';
import { ToastModule } from 'primeng/toast';
import { SliderModule } from 'primeng/slider';
import { SkeletonModule } from 'primeng/skeleton';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { bootstrapApplication, BrowserModule } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app/app-routing.module';
import {
  HttpClient,
  provideHttpClient,
  withInterceptorsFromDi
} from '@angular/common/http';
import { ConfirmationService } from 'primeng/api';
import {
  TranslateLoader,
  TranslateModule,
  TranslateModuleConfig
} from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

if (environment.production) {
  enableProdMode();
}

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

const translateModuleConfig: TranslateModuleConfig = {defaultLanguage: 'fi',
  loader: {
    provide: TranslateLoader,
    useFactory: HttpLoaderFactory,
    deps: [HttpClient]
  }
};

bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(
          AppRoutingModule,
          BrowserModule,
          ButtonModule,
          CardModule,
          ConfirmDialogModule,
          DropdownModule,
          FormsModule, 
          InputTextModule, 
          ReactiveFormsModule, 
          SkeletonModule, 
          SliderModule, 
          ToastModule, 
          ToolbarModule, 
          ProgressSpinnerModule, 
          BlockUIModule, 
          ChartModule),
      importProvidersFrom(BrowserModule, TranslateModule.forRoot(translateModuleConfig) ), 
      ConfirmationService, provideHttpClient(withInterceptorsFromDi()), 
      provideAnimations()
    ]
})
  .catch(err => console.error(err));
