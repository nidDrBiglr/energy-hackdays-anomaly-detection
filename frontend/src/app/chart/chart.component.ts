import { AfterViewInit, Component, Inject, Input, NgZone, OnDestroy, OnInit, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

import * as am4core from '@amcharts/amcharts4/core';
import * as am4charts from '@amcharts/amcharts4/charts';
import am4themes_animated from '@amcharts/amcharts4/themes/animated';
import { DataService } from '../service/data.service';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})

export class ChartComponent implements OnDestroy, AfterViewInit {
  private chart: am4charts.XYChart;
  @Input() meterId: string;

  constructor(
    @Inject(PLATFORM_ID) private platformId,
    private zone: NgZone,
    private dataService: DataService) {
  }

  ngAfterViewInit(): void {
    this.browserOnly(() => {
      this.initializeChart();
      this.dataService.getData(this.meterId).subscribe((data: any[]) => {
        // somehow amcharts is not able to parse the ISO string direclty
        data.forEach((value) => {
          // todo: use moment instead
          value.date = new Date(value.time);
        });
        this.chart.data = data;
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
    //dateAxis.tooltip.disabled = true;
    // y-axis config
    const valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
    valueAxis.title.text = 'consumption (kW/h)';
    valueAxis.renderer.minWidth = 35;
    valueAxis.tooltip.disabled = true;
    // series config
    const series = chart.series.push(new am4charts.LineSeries());
    series.dataFields.dateX = 'date';
    series.dataFields.valueY = 'kWh';
    series.tooltipText = '{valueY.value} kW/h';

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
