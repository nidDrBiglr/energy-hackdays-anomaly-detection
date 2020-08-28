import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class DataService {
  private urlHistoric = 'assets/energy.json';
  private urlAnomalies = 'assets/anomalies.json';

  constructor(private http: HttpClient) {}

  getData(meterId: string, from: string, to: string): Observable<any> {
    return this.http.get(this.urlHistoric);
  }

  getAnomalies(meterId: string, from: string, to: string): Observable<any> {
    return this.http.get(this.urlAnomalies);
  }

}
