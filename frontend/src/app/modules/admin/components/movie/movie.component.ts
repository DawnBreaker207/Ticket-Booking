import {Component, inject, OnInit} from '@angular/core';
import {NzTableModule} from 'ng-zorro-antd/table';
import {MovieService} from '@/app/core/services/movie/movie.service';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzModalService} from 'ng-zorro-antd/modal';
import {FormMovieComponent} from '@/app/modules/admin/components/movie/form/movie/form.component';
import {displayColumns, headerColumns} from '@/app/core/constants/column';
import {Movie} from '@/app/core/models/movie.model';
import {NzTagComponent} from 'ng-zorro-antd/tag';
import {DatePipe} from '@angular/common';
import {NzSpaceComponent} from 'ng-zorro-antd/space';

@Component({
  selector: 'app-movie',
  imports: [
    NzTableModule,
    NzInputModule,
    NzSelectModule,
    NzButtonModule,
    NzIconModule,
    NzTagComponent,
    DatePipe,
    NzSpaceComponent
  ],
  templateUrl: './movie.component.html',
  styleUrl: './movie.component.css',
  providers: [NzModalService]
})
export class MovieComponent implements OnInit {
  private modalService = inject(NzModalService);
  private movieService = inject(MovieService);
  headerColumn = headerColumns.movie;
  displayColumn = displayColumns.movie;
  movieList: readonly Movie[] = [];

  ngOnInit() {
    this.movieService.getMovieLists().subscribe(data => this.movieList = data);
  }

  openMovieModal(mode: 'add' | 'edit' | 'view', id?: string | number) {
    const modal = this.modalService.create({
      nzContent: FormMovieComponent,
      nzTitle: undefined,
      nzClosable: true,
      nzData: {
        mode: mode,
        movieId: id
      },
      nzWidth: 900,
      nzKeyboard: true,
      nzFooter: mode !== 'view' ? [
        {
          label: 'Confirm',
          type: 'primary',
          onClick: () => {
            const form = modal.getContentComponent() as FormMovieComponent;
            console.log('onClick', form.movieForm.valid)
            if (form.movieForm.valid) {
              form.submit();
              modal.close()
            } else {
              console.log('Form Invalid!');
            }
          }
        }
      ] : null,
      nzOnCancel: () => {
        const formComponent = modal.getContentComponent();
        if (!formComponent.hasData()) {
          return true;
        } else {
          return new Promise((resolve) => {
            this.modalService.confirm({
              nzTitle: 'Are u sure',
              nzOnOk: () => resolve(true),
              nzOnCancel: () => resolve(false),
            })
          })
        }

      }


    });


  }

  onDelete(id: number) {
    return this.movieService.removeMovie(id).subscribe(res => {
      console.log(res)
    })
  }
}
