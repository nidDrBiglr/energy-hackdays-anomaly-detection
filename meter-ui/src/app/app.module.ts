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
import { MeterDetailComponent } from './meter-detail/meter-detail.component';
import {
  NzBreadCrumbModule,
  NzButtonModule, NzDividerModule, NzEmptyModule,
  NzIconModule,
  NzLayoutModule,
  NzMenuModule,
  NzPageHeaderModule,
  NzTableModule
} from 'ng-zorro-antd';
import { HttpClientModule } from '@angular/common/http';
import { IconModule } from '@ant-design/icons-angular';
import { AppRoutingModule } from './app.routing';
import { NotFoundComponent } from './not-found/not-found.component';

@NgModule({
  declarations: [
    AppComponent,
    ChartComponent,
    AlertsComponent,
    MeterListComponent,
    MeterDetailComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    IconModule,
    NzLayoutModule,
    NzBreadCrumbModule,
    NzMenuModule,
    NzIconModule,
    NzPageHeaderModule,
    AppRoutingModule,
    NzButtonModule,
    NzTableModule,
    NzDividerModule,
    NzEmptyModule
  ],
  providers: [
    AlertService,
    DataService,
    MeterService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
