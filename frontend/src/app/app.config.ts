import {HttpClientModule, provideHttpClient} from '@angular/common/http';
import {ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {routes} from './app.routes';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {providePrimeNG} from 'primeng/config';
import Lara from '@primeng/themes/lara';
import { MenubarModule } from 'primeng/menubar';
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { InputTextModule } from 'primeng/inputtext';
import { RippleModule } from 'primeng/ripple';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {ToastModule} from 'primeng/toast';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ConfirmationService, MessageService} from 'primeng/api';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    provideAnimationsAsync(),
    providePrimeNG({
      theme: {
        preset: Lara,
      },
    }),
    importProvidersFrom(
      MenubarModule,
      BadgeModule,
      AvatarModule,
      InputTextModule,
      RippleModule,
      HttpClientModule,
      BrowserAnimationsModule,
      ToastModule,
      ConfirmDialogModule
    ),
    MessageService,
    ConfirmationService
  ],
};
