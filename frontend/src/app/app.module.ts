import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ChartComponent } from './chart/chart.component';
import { AlertsComponent } from './alerts/alerts.component';
import { MeterListComponent } from './meter-list/meter-list.component';
import { DataService } from './service/data.service';
import { AlertService } from './service/alert.service';
import { MeterService } from './service/meter.service';

@NgModule({
  declarations: [
    AppComponent,
    ChartComponent,
    AlertsComponent,
    MeterListComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule
  ],
  providers: [
    AlertService,
    DataService,
    MeterService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
