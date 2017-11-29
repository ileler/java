import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';

import { environment } from '../environments/environment';
import { SharedModule } from './shared';
import { CoreModule } from './core';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    SharedModule,
    CoreModule.forRoot({
      context: environment['SERVER'] || ''
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
