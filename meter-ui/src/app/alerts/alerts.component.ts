import { Component, Input, OnInit } from '@angular/core';
import { Moment } from 'moment';
import { AlertService } from '../service/alert.service';

@Component({
  selector: 'app-alerts',
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.css']
})
export class AlertsComponent implements OnInit {
  @Input() meterId: string;
  @Input() from: Moment;
  @Input() to: Moment;
  dataSet: any[] = [];

  constructor(private alertService: AlertService) { }

  ngOnInit(): void {
    this.alertService.getData(this.meterId, this.from.toISOString(), this.to.toISOString()).subscribe(data => {
      this.dataSet = data;
    });
  }

}
