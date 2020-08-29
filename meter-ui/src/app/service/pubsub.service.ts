
// tslint:disable-next-line:no-any
import { Subject } from 'rxjs';

class CustomEventEmitter extends Subject<any> {
  // constructor
  constructor() {
    super();
  }
  // emits an event
  // tslint:disable-next-line:no-any
  public emit(value: any): void {
    super.next(value);
  }
}

// tslint:disable-next-line:max-classes-per-file
export class PubSubService {
  // class properties
  public Stream: CustomEventEmitter;
  // constructor
  constructor() {
    this.Stream = new CustomEventEmitter();
  }
}
