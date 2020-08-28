import { AfterViewInit, Component, Inject, Input, NgZone, OnDestroy, OnInit, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

import * as am4core from '@amcharts/amcharts4/core';
import * as am4charts from '@amcharts/amcharts4/charts';
import am4themes_animated from '@amcharts/amcharts4/themes/animated';
import { DataService } from '../service/data.service';
import { Moment } from 'moment';
import * as moment from 'moment';


@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})

export class ChartComponent implements OnDestroy, AfterViewInit {
  private chart: am4charts.XYChart;
  private data: am4charts.LineSeries;
  private anomalies: am4charts.LineSeries;
  @Input() meterId: string;
  @Input() from: Moment;
  @Input() to: Moment;

  constructor(
    @Inject(PLATFORM_ID) private platformId,
    private zone: NgZone,
    private dataService: DataService) {
  }

  ngAfterViewInit(): void {
    this.browserOnly(() => {
      this.initializeChart();
      this.dataService.getData(this.meterId, this.from.toISOString(), this.to.toISOString()).subscribe((data: any[]) => {
        // somehow amcharts is not able to parse the ISO string directly
        data.forEach((value) => {
          value.date = moment(value.time).toDate();
        });
        this.data.data = data;
      });
      this.dataService.getAnomalies(this.meterId, this.from.toISOString(), this.to.toISOString()).subscribe((anomalies: any[]) => {
        anomalies.forEach((value) => {
          value.date = moment(value.time).toDate();
        });
        this.anomalies.data = anomalies;
      });
    });
  }

  ngOnDestroy(): void {
    this.browserOnly(() => {
      if (this.chart) {
        this.chart.dispose();
      }
    });
  }

  private initializeChart(): void {
    am4core.useTheme(am4themes_animated);
    // chart config
    const chart = am4core.create('chart', am4charts.XYChart);
    chart.paddingRight = 20;
    chart.cursor = new am4charts.XYCursor();
    chart.cursor.lineX.disabled = true;
    chart.cursor.lineY.disabled = true;
    // x-axis configs
    const dateAxis = chart.xAxes.push(new am4charts.DateAxis());
    dateAxis.renderer.grid.template.location = 0;
    dateAxis.dateFormats.setKey('year', 'dd.MM.yyyy');
    dateAxis.dateFormats.setKey('month', 'dd.MM.yyyy');
    dateAxis.dateFormats.setKey('week', 'dd.MM.yyyy');
    dateAxis.dateFormats.setKey('day', 'dd.MM.yyyy');
    dateAxis.dateFormats.setKey('hour', 'dd.MM.yyyy hh:mm:ss');
    dateAxis.periodChangeDateFormats.setKey('year', 'dd.MM.yyyy');
    dateAxis.periodChangeDateFormats.setKey('month', 'dd.MM.yyyy');
    dateAxis.periodChangeDateFormats.setKey('week', 'dd.MM.yyyy');
    dateAxis.periodChangeDateFormats.setKey('day', 'dd.MM.yyyy');
    dateAxis.periodChangeDateFormats.setKey('hour', 'dd.MM.yyyy');
    dateAxis.tooltip.disabled = true;
    // y-axis config
    const valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
    valueAxis.title.text = 'consumption (kWh)';
    valueAxis.renderer.minWidth = 35;
    valueAxis.tooltip.disabled = true;
    // series config
    this.data = chart.series.push(new am4charts.LineSeries());
    this.data.dataFields.dateX = 'date';
    this.data.dataFields.valueY = 'kWh';
    this.data.tooltipText = '{valueY.value} kWh';
    this.anomalies = chart.series.push(new am4charts.LineSeries());
    this.anomalies.dataFields.dateX = 'date';
    this.anomalies.dataFields.valueY = 'kWh';
    this.anomalies.tooltip.disabled = true;
    this.anomalies.connect = false;
    this.anomalies.stroke = am4core.color('#ff0000');
    this.anomalies.fill = am4core.color('#ff0000');
    this.anomalies.strokeWidth = 20;
    const bullet = this.anomalies.bullets.push(new am4charts.Bullet());
    const circle = bullet.createChild(am4core.Circle);
    circle.radius = 6;
// todo: add forecasts, fetch data from api

    this.chart = chart;
  }

  private browserOnly(f: () => void): void {
    if (isPlatformBrowser(this.platformId)) {
      this.zone.runOutsideAngular(() => {
        f();
      });
    }
  }
}
