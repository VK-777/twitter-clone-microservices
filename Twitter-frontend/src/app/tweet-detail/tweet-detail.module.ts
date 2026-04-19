import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { TweetDetailRoutingModule } from './tweet-detail-routing.module';
import { TweetDetailComponent } from './tweet-detail.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [TweetDetailComponent],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    TweetDetailRoutingModule,
    SharedModule
  ]
})
export class TweetDetailModule { }
