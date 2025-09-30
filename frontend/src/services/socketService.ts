// src/services/socketService.ts
import { RxStomp } from '@stomp/rx-stomp';
import { SocketConfig } from '../config/socket.config'; // <= sửa đường dẫn ở đây
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import type { IMessage } from '@stomp/stompjs'; // optional typing

class SocketService {
  private client: RxStomp;

  constructor() {
    this.client = new RxStomp();
    this.client.configure(SocketConfig);
    this.client.activate();
  }


  watchTopic<T = any>(topic: string): Observable<T> {
    return this.client.watch(topic).pipe(
      map((message: IMessage) => {
        try {
          return JSON.parse(message.body) as T;
        } catch {
          return (message.body as unknown) as T;
        }
      })
    );
  }

  watchOrder(topic: string): Observable<IMessage> {
    return this.client.watch(topic);
  }

  send(destination: string, body: any) {
    this.client.publish({ destination, body: JSON.stringify(body) });
  }

  disconnect() {
    this.client.deactivate();
  }
}


export const socketService = new SocketService();
export default socketService;
