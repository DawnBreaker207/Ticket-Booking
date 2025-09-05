import {Component, inject, input, OnDestroy, OnInit} from '@angular/core';
// import {SocketService} from '@/app/core/services/socket/socket.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-countdown',
  imports: [],
  templateUrl: './countdown.component.html',
  styleUrl: './countdown.component.css'
})
export class CountdownComponent implements OnInit, OnDestroy {
  orderId = input<any>();
  remainingTime = 0;
  // private socketService = inject(SocketService);
  private subscription!: Subscription;

  ngOnInit() {
    // this.subscription = this.socketService.watch(`/topic/order`).subscribe((message) => {
    //   const body = JSON.parse(message.body);
    //   this.remainingTime = body.ttl;
    // })
  }

  ngOnDestroy() {
    // this.subscription.unsubscribe()
  }
}
