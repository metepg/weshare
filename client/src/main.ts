import { provideZonelessChangeDetection } from '@angular/core';
import { AppComponent } from './app/app.component';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient, withFetch, withXsrfConfiguration } from '@angular/common/http';
import { ConfirmationService, MessageService } from 'primeng/api';
import { providePrimeNG } from 'primeng/config';
import Lara from '@primeng/themes/lara';
import { provideRouter, withHashLocation } from '@angular/router';
import { routes } from './app/app-routing.module';

import { provideTranslateService } from '@ngx-translate/core';
import { provideTranslateHttpLoader } from '@ngx-translate/http-loader';

bootstrapApplication(AppComponent, {
  providers: [
    provideZonelessChangeDetection(),
    provideHttpClient(
      withFetch(),
      withXsrfConfiguration({})
    ),
    // eslint-disable-next-line @typescript-eslint/no-deprecated
    provideAnimations(),
    providePrimeNG({
      theme: {
        preset: Lara,
        options: { darkModeSelector: false }
      }
    }),
    provideRouter(routes, withHashLocation()),
    provideTranslateService({
      fallbackLang: 'fi',
      loader: provideTranslateHttpLoader({
        prefix: './assets/i18n/',
        suffix: '.json'
      })
    }),
    ConfirmationService,
    MessageService
  ]
})
  .catch((err) => { console.error(err); });
