import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { AppModule } from './app/app.module';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import * as ng2Charts from 'ng2-charts';
console.log(ng2Charts);

(window as any).global = window;
platformBrowserDynamic().bootstrapModule(AppModule)
  .catch((err: any) => console.error(err));

  


