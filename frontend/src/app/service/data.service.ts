import { Injectable } from '@angular/core';

@Injectable()
export class DataService {

  constructor() {}

  getData(): {}[] {
    const data = [];
    let visits = 10;
    for (let i = 1; i < 366; i++) {
      visits += Math.round((Math.random() < 0.5 ? 1 : -1) * Math.random() * 10);
      data.push({ date: new Date(2020, 0, i), value: visits });
    }

    return data;
  }

}
