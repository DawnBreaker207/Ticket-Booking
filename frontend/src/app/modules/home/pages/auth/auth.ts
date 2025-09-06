import {Component, inject, OnInit, signal} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthService} from '@/app/core/services/auth/auth.service';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {NzIconDirective} from 'ng-zorro-antd/icon';
import {NzInputDirective, NzInputGroupComponent} from 'ng-zorro-antd/input';
import {NzFormControlComponent, NzFormItemComponent, NzFormLabelComponent} from 'ng-zorro-antd/form';
import {NgClass} from '@angular/common';
import {NzDatePickerComponent} from 'ng-zorro-antd/date-picker';
import {NzOptionComponent, NzSelectComponent} from 'ng-zorro-antd/select';
import {Store} from '@ngrx/store';
import {AuthActions} from '@/app/core/store/state/auth/auth.actions';

@Component({
  selector: 'app-auth',
  imports: [
    FormsModule,
    NzButtonComponent,
    NzFormControlComponent,
    NzFormItemComponent,
    NzInputDirective,
    NzInputGroupComponent,
    ReactiveFormsModule,
    NgClass,
    NzIconDirective,
    NzFormLabelComponent,
    NzDatePickerComponent,
    NzSelectComponent,
    NzOptionComponent
  ],
  templateUrl: './auth.html',
  styleUrl: './auth.css'
})
export class AuthComponent implements OnInit {
  activeTab = signal<'login' | 'register'>('login');
  form!: FormGroup;
  private fb = inject(FormBuilder);
  private store = inject(Store);

  ngOnInit() {

    this.initializeForm();
  }

  initializeForm() {
    this.form = this.fb.group({
      username: ['',],
      email: ['',],
      password: ['',]
    })
  }

  onSubmit() {
    if (!this.form.valid) return;
    if (this.activeTab() === 'login') {
      const {username, password} = this.form.value;
      this.store.dispatch(AuthActions.loadLogin({username, password}));

    } else {
      const {username, email, password} = this.form.value;
      this.store.dispatch(AuthActions.loadRegister({username, email, password}));
    }
  }

}
