// src/app/shared/shared.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { AvatarComponent } from './avatar/avatar.component';
import { FollowBtnComponent } from './follow-btn/follow-btn.component';
import { TweetCardComponent } from './tweet-card/tweet-card.component';
import { SidebarComponent } from './sidebar/sidebar.component';

@NgModule({
  declarations: [
    AvatarComponent,
    FollowBtnComponent,
    TweetCardComponent,
    SidebarComponent

  ],

  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    FormsModule
  ],

  exports: [
    AvatarComponent,
    FollowBtnComponent,
    TweetCardComponent,
    SidebarComponent
  ]
})
export class SharedModule { }