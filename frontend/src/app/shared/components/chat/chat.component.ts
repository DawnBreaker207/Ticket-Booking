import {
  Component,
  effect,
  ElementRef,
  signal,
  viewChild,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzFloatButtonModule } from 'ng-zorro-antd/float-button';

interface Message {
  content: string;
  sender: 'user' | 'bot';
  time: Date;
}

@Component({
  selector: 'app-chat',
  imports: [
    CommonModule,
    FormsModule,
    NzButtonModule,
    NzInputModule,
    NzAvatarModule,
    NzIconModule,
    NzFloatButtonModule,
  ],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css',
})
export class ChatComponent {
  scrollBottom = viewChild<ElementRef>('scrollBottom');

  isOpen = signal<boolean>(false);
  inputValue = signal<string>('');
  messages = signal<Message[]>([
    {
      content: 'Chào bạn! Đây là chat box',
      sender: 'bot',
      time: new Date(),
    },
  ]);

  constructor() {
    effect(() => {
      const msgs = this.messages();
      const open = this.isOpen();
      if (open && msgs.length > 0) {
        setTimeout(() => this.scrollBottom(), 50);
      }
    });
  }

  toggleChat() {
    this.isOpen.update((v) => !v);
  }

  sendMessage() {
    const text = this.inputValue().trim();
    if (!text) return;

    this.messages.update((prev) => [
      ...prev,
      { content: text, sender: 'user', time: new Date() },
    ]);
    this.inputValue.set('');

    setTimeout(() => {
      this.messages.update((prev) => [
        ...prev,
        {
          content: `Bot nhận được: "${text}"`,
          sender: 'bot',
          time: new Date(),
        },
      ]);
    }, 800);
  }

  scrollToBottom() {
    const el = this.scrollBottom()?.nativeElement;
    if (el) {
      el.scrollTop = el.scrollHeight;
    }
  }
}
