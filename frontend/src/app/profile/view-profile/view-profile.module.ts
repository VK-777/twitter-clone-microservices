import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ViewProfileRoutingModule } from './view-profile-routing.module';
import { ViewProfileComponent } from './view-profile.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [ViewProfileComponent],

  imports: [
    CommonModule,
    RouterModule,
    ViewProfileRoutingModule,
    SharedModule
  ]
})
export class ViewProfileModule { }