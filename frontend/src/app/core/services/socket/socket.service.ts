import {Injectable} from '@angular/core';
import {RxStomp} from '@stomp/rx-stomp';
import {SocketConfig} from '@/app/core/config/socket.config';
import {map} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class SocketService extends RxStomp {


  constructor() {
    super();
    this.configure(SocketConfig);
    this.activate();
  }

  watchTopic<T>(topic: string) {
    return this.watch(topic)
      .pipe(map((message) => {
        return JSON.parse(message.body) as T
      }))
  }

  watchOrder(topic: string) {
    return this.watch(topic);
  }

  send(destination: string, body: any) {
    this.publish({destination, body: JSON.stringify(body)});
  }

  disconnect() {
    this.deactivate();
  }
}
