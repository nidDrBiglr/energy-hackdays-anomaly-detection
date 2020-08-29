import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as moment from 'moment';
import { Moment } from 'moment';
import { ChartComponent } from '../chart/chart.component';
import { AlertsComponent } from '../alerts/alerts.component';
import { PubSubService } from '../service/pubsub.service';

@Component({
  selector: 'app-meter-detail',
  templateUrl: './meter-detail.component.html',
  styleUrls: ['./meter-detail.component.css']
})
export class MeterDetailComponent {
  public meterId: string;
  public from: Moment = moment().startOf('month').subtract(2, 'months');
  public to: Moment = moment().endOf('month');
  public renderContent = true;
  @ViewChild('chart') chartComponent: ChartComponent;
  @ViewChild('alerts') alertsComponent: AlertsComponent;

  constructor(private route: ActivatedRoute, private pubSubService: PubSubService) {
    this.route.paramMap.subscribe(params => {
      this.meterId = params.get('meterId');
      // redraw hackzz
      this.renderContent = false;
      setTimeout(() => {
        this.renderContent = true;
      }, 10);
    });
  }

  public nextPeriod(): void {
    const from: Moment = moment(this.to).add(1, 'month').startOf('month');
    this.to = moment(from).add(2, 'months').endOf('month');
    this.from = from;
    this.notifySubscribers();
  }

  public previousPeriod(): void {
    const to: Moment = moment(this.from).subtract(1, 'month').endOf('month');
    this.from = moment(to).subtract(2, 'months').startOf('month');
    this.to = to;
    this.notifySubscribers();
  }

  private notifySubscribers(): void {
    this.pubSubService.Stream.emit({meterId: this.meterId, from: this.from, to: this.to});
  }

}
