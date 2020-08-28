import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as moment from 'moment';
import { Moment } from 'moment';

@Component({
  selector: 'app-meter-detail',
  templateUrl: './meter-detail.component.html',
  styleUrls: ['./meter-detail.component.css']
})
export class MeterDetailComponent {
  public meterId: string;
  public from: Moment = moment().startOf('month').subtract(2, 'months');
  public to: Moment = moment().endOf('month');

  constructor(private route: ActivatedRoute) {
    this.route.paramMap.subscribe(params => {
      this.meterId = params.get('meterId');
    });
  }

  public nextPeriod(): void {
    const from: Moment = moment(this.to).add(1, 'month').startOf('month');
    this.to = moment(from).add(2, 'months').endOf('month');
    this.from = from;
  }

  public previousPeriod(): void {
    const to: Moment = moment(this.from).subtract(1, 'month').endOf('month');
    this.from = moment(to).subtract(2, 'months').startOf('month');
    this.to = to;
  }

}
