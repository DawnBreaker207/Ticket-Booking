import { Component, inject, input, OnInit, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { QuillEditorComponent } from 'ngx-quill';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzUploadModule } from 'ng-zorro-antd/upload';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { Article } from '@domain/article/models/article.model';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-form',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    QuillEditorComponent,
    NzFormModule,
    NzInputModule,
    NzButtonModule,
    NzSelectModule,
    NzUploadModule,
    NzIconModule,
    TranslatePipe,
  ],
  templateUrl: './article-form.component.html',
  styleUrl: './article-form.component.css',
})
export class ArticleFormComponent implements OnInit {
  initialData = input<Article | null>(null);
  saveArticle = output<any>();
  cancelArticle = output<void>();

  private fb = inject(FormBuilder);
  form!: FormGroup;
  isLoading = false;
  isEditMode = false;

  ngOnInit() {
    this.isEditMode = !!this.initialData();
    this.form = this.fb.group({
      title: [this.initialData()?.title || '', [Validators.required]],
      thumbnail: [this.initialData()?.thumbnail || '', [Validators.required]],
      summary: [this.initialData()?.summary || '', [Validators.required]],
      content: [this.initialData()?.content || '', [Validators.required]],
      category: [this.initialData()?.category || 'Tin tức'],
      status: [this.initialData()?.status || 'DRAFT'],
    });
  }

  quillConfig = {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike'], // toggled buttons
      ['blockquote', 'code-block'],
      [{ header: 1 }, { header: 2 }], // custom button values
      [{ list: 'ordered' }, { list: 'bullet' }],
      [{ script: 'sub' }, { script: 'super' }], // superscript/subscript
      [{ indent: '-1' }, { indent: '+1' }], // outdent/indent
      [{ size: ['small', false, 'large', 'huge'] }], // custom dropdown
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
      [{ color: [] }, { background: [] }], // dropdown with defaults from theme
      [{ font: [] }],
      [{ align: [] }],
      ['clean'], // remove formatting button
      ['link', 'image', 'video'], // link and image, video
    ],
  };

  submit() {
    if (this.form.value) {
      this.isLoading = true;

      setTimeout(() => {
        this.saveArticle.emit(this.form.value);
        this.isLoading = false;
      }, 1000);
    } else {
      Object.values(this.form.controls).forEach((control) => {
        if (control.invalid) {
          control.markAsDirty();
          control.updateValueAndValidity({ onlySelf: true });
        }
      });
    }
  }
}
