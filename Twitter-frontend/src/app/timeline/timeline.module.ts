import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { TimelineRoutingModule } from './timeline-routing.module';
import { TimelineComponent } from './timeline.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [TimelineComponent],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    TimelineRoutingModule,
    SharedModule
  ]
})

export class TimelineModule { }
