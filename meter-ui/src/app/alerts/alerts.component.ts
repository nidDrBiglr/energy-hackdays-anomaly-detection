import { Component, Input, OnInit } from '@angular/core';
import { Moment } from 'moment';
import { DataService } from '../service/data.service';
import { PubSubService } from '../service/pubsub.service';

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

  constructor(private dataService: DataService, private pubSubService: PubSubService) {
    this.pubSubService.Stream.subscribe(event => {
      this.updateData(event.meterId, event.from, event.to);
    });
  }

  ngOnInit(): void {
    this.updateData(this.meterId, this.from, this.to);
  }

  public updateData(meterId: string, from: Moment, to: Moment): void {
    this.dataSet = [];
    this.dataService.getAnomalies(meterId, from.toISOString(), to.toISOString()).subscribe(data => {
      this.dataSet = data;
    });
  }

}
