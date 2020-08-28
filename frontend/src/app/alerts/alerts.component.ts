import { Component, Input, OnInit } from '@angular/core';
import { Moment } from 'moment';
import * as moment from 'moment';

@Component({
  selector: 'app-alerts',
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.css']
})
export class AlertsComponent implements OnInit {
  @Input() meterId: string;
  @Input() from: Moment;
  @Input() to: Moment;
  dataSet: {value: number, severity: string, date: any }[] = [
    {
      severity: 'HIGH',
      value: 120,
      date: moment(),
    },
    {
      severity: 'MEDIUM',
      value: 70,
      date: moment(),
    },
    {
      severity: 'LOW',
      value: 20,
      date: moment(),
    }
  ];

  constructor() { }

  ngOnInit(): void {
  }

}
