import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class DataService {
  private url = 'assets/energy.json';

  constructor(private http: HttpClient) {}

  getData(meterId: string, from: string, to: string): Observable<any> {
    return this.http.get(this.url);
  }

}
