import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import * as moment from 'moment';

@Injectable()
export class AlertService {
  private url = 'assets/energy.json';
  private data = [
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

  constructor(private http: HttpClient) {}

  getData(meterId: string, from: string, to: string): Observable<any> {
    return of(this.data);
  }
}
