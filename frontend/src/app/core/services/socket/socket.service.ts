import {Injectable} from '@angular/core';
import {RxStomp, RxStompConfig} from '@stomp/rx-stomp'
import {SocketConfig} from '@/app/core/config/socket.config';

// @Injectable({
//   providedIn: 'root'
// })
// export class SocketService extends RxStomp {
//   constructor() {
//     super();
//     this.configure(SocketConfig);
//     this.activate();
//   }
// }
