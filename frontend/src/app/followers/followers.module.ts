import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FollowersRoutingModule } from './followers-routing.module';
import { FollowersComponent } from './followers.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({

  declarations: [FollowersComponent],

  imports: [
    CommonModule,
    RouterModule,
    FollowersRoutingModule,
    SharedModule
  ]
})
export class FollowersModule { }
